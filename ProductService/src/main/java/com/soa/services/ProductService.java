package com.soa.services;

import com.soa.entity.CoordinatesEntity;
import com.soa.entity.LocationEntity;
import com.soa.entity.PersonEntity;
import com.soa.entity.ProductEntity;
import com.soa.model.*;
import com.soa.repository.CoordinateRepository;
import com.soa.repository.LocationRepository;
import com.soa.repository.OwnerRepository;
import com.soa.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CoordinateRepository coordinateRepository;

    @Autowired
    private LocationRepository locationRepository;

    @Autowired
    private OwnerRepository ownerRepository;

    public List<Product> getProducts(String sort, String filters, Integer page, Integer pageSize) {
        List<ProductEntity> entities = productRepository.findAll();

        if(filters != null){
            entities = applyFilters(entities, filters);
        }

        if(sort != null){
            entities = applySorting(entities, sort);
        }

        entities = applyPagination(entities, page, pageSize);

        List<Product> products = new ArrayList<>();
        for(ProductEntity entity: entities){
            products.add(mapToProduct(entity));
        };

        return products;
    }

    public Product getProductById(int id) {
        return mapToProduct(productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт с id " + id + " не найден")));
    }

    @Transactional
    public Product postProduct(NewProduct newProduct) {
        CoordinatesEntity coordinatesEntity = new CoordinatesEntity();
        coordinatesEntity.setX(newProduct.getCoordinates().getX());
        coordinatesEntity.setY(newProduct.getCoordinates().getY());

        LocationEntity locationEntity = new LocationEntity();
        locationEntity.setX(newProduct.getOwner().getLocation().getX());
        locationEntity.setY(newProduct.getOwner().getLocation().getY());
        locationEntity.setName(newProduct.getOwner().getLocation().getName());

        PersonEntity ownerEntity = new PersonEntity();
        ownerEntity.setName(newProduct.getOwner().getName());
        ownerEntity.setBirthday(newProduct.getOwner().getBirthday());
        ownerEntity.setHeight(newProduct.getOwner().getHeight());
        ownerEntity.setWeight(newProduct.getOwner().getWeight());
        ownerEntity.setLocation(locationEntity);

        ProductEntity productEntity = new ProductEntity();
        productEntity.setName(newProduct.getName());
        productEntity.setCoordinates(coordinatesEntity);
        productEntity.setPrice(newProduct.getPrice());
        productEntity.setManufactureCost(newProduct.getManufactureCost());
        productEntity.setUnitOfMeasure(newProduct.getUnitOfMeasure());
        productEntity.setOwner(ownerEntity);
        productEntity.setCreationDate(LocalDateTime.now());

        coordinateRepository.save(coordinatesEntity);
        coordinateRepository.flush();

        locationRepository.save(locationEntity);
        locationRepository.flush();

        ownerRepository.save(ownerEntity);
        ownerRepository.flush();

        productRepository.save(productEntity);
        productRepository.flush();

        return mapToProduct(productEntity);
    }

    @Transactional
    public Product updateProductById(int id, NewProduct newProduct) {
        ProductEntity existingProduct = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Продукт с id " + id + " не найден"));

        boolean locationChanged = false;
        boolean coordinatesChanged = false;

        LocationEntity newLocationEntity = existingProduct.getOwner().getLocation();
        if (newLocationEntity == null ||
                !(newLocationEntity.getX() == newProduct.getOwner().getLocation().getX()) ||
                !(newLocationEntity.getY() == (newProduct.getOwner().getLocation().getY())) ||
                !newLocationEntity.getName().equals(newProduct.getOwner().getLocation().getName())) {

            newLocationEntity = new LocationEntity();
            newLocationEntity.setX(newProduct.getOwner().getLocation().getX());
            newLocationEntity.setY(newProduct.getOwner().getLocation().getY());
            newLocationEntity.setName(newProduct.getOwner().getLocation().getName());
            locationChanged = true;
        }

        CoordinatesEntity newCoordinatesEntity = existingProduct.getCoordinates();
        if (newCoordinatesEntity == null ||
                !newCoordinatesEntity.getX().equals(newProduct.getCoordinates().getX()) ||
                !newCoordinatesEntity.getY().equals(newProduct.getCoordinates().getY())) {

            newCoordinatesEntity = new CoordinatesEntity();
            newCoordinatesEntity.setX(newProduct.getCoordinates().getX());
            newCoordinatesEntity.setY(newProduct.getCoordinates().getY());
            coordinatesChanged = true;
        }

        existingProduct.setName(newProduct.getName());
        existingProduct.setPrice(newProduct.getPrice());
        existingProduct.setManufactureCost(newProduct.getManufactureCost());
        existingProduct.setUnitOfMeasure(newProduct.getUnitOfMeasure());

        PersonEntity newOwnerEntity = existingProduct.getOwner();
        if (existingProduct.getOwner() == null || locationChanged) {
            newOwnerEntity = new PersonEntity();
            newOwnerEntity.setName(newProduct.getOwner().getName());
            newOwnerEntity.setBirthday(newProduct.getOwner().getBirthday());
            newOwnerEntity.setHeight(newProduct.getOwner().getHeight());
            newOwnerEntity.setWeight(newProduct.getOwner().getWeight());
            newOwnerEntity.setLocation(newLocationEntity);
        }

        existingProduct.setOwner(newOwnerEntity);
        if (coordinatesChanged) {
            existingProduct.setCoordinates(newCoordinatesEntity);
        }

        productRepository.save(existingProduct);
        return mapToProduct(existingProduct);
    }

    @Transactional
    public boolean deleteProductById(int id) {
        if (productRepository.existsById(id)) {
            productRepository.deleteById(id);
            return true;
        }
        return false;
    }

    @Transactional
    public void deleteAllProductsByUnitOfMeasure(String unitOfMeasure) {
        UnitOfMeasure unit= UnitOfMeasure.valueOf(unitOfMeasure);


        List<ProductEntity> products = productRepository.findByUnitOfMeasure(unit);
        if (!products.isEmpty()) {
            productRepository.deleteAll(products);
        }
    }

    public Product getProductWithMinPrice() {
        return mapToProduct(productRepository.findTopByOrderByPriceAsc()
                .orElseThrow(() -> new RuntimeException("Нет продуктов в базе")));
    }

    public int getCountOfProductsWithBiggerCost(int cost) {
        if (cost < 0){
            throw new RuntimeException();
        }
        return productRepository.countByManufactureCostGreaterThan(cost);
    }


    public Map<String, String> parseSorting(String sorting) {
        Map<String, String> sortingMap = new LinkedHashMap<>();

        if (sorting != null && !sorting.isBlank()) {
            String[] sortFields = sorting.split(",");

            for (String field : sortFields) {
                String direction = field.startsWith("-") ? "DESC" : "ASC";
                String fieldName = field.replace("-", "");
                sortingMap.put(fieldName, direction);
            }
        }

        return sortingMap;
    }

    public List<String> parseFilter(String filter) {
        List<String> filterList = new ArrayList<>();

        if(filter != null && !filter.isBlank()) {
            String[] filterFields = filter.split(",");

            for (String field : filterFields) {
                String regex = "(.+?)\\[(.+?)\\]=(.*)";
                Pattern pattern = Pattern.compile(regex);
                Matcher matcher = pattern.matcher(filter);

                if (matcher.matches()) {
                    filterList.add(matcher.group(1));
                    filterList.add(matcher.group(2));
                    filterList.add(matcher.group(3));
                } else {
                    throw new IllegalArgumentException("Bad filter expression: " + filter);
                }
            }
        }
        return filterList;
    }

    public List<ProductEntity> applyFilters(List<ProductEntity> products, String filters) {
        List<String> filterList = parseFilter(filters);

        for (int i = 0; i < filterList.size(); i += 3) {
            String field = filterList.get(i);
            String operator = filterList.get(i + 1);
            String value = filterList.get(i + 2);

            products = products.stream()
                    .filter(product -> applyFilterForProduct(product, field, operator, value))
                    .collect(Collectors.toList());
        }
        return products;
    }

    private boolean applyNumberOperator(double fieldValue, String operator, double filterValue) {
        return switch (operator) {
            case "eq" -> fieldValue == filterValue;
            case "neq" -> fieldValue != filterValue;
            case "gt" -> fieldValue > filterValue;
            case "lt" -> fieldValue < filterValue;
            case "gte" -> fieldValue >= filterValue;
            case "lte" -> fieldValue <= filterValue;
            default -> throw new IllegalArgumentException("Bad filter operator: " + operator);
        };
    }

    private boolean applyStringOperator(String fieldValue, String operator, String filterValue) {
        return switch (operator) {
            case "eq" -> fieldValue.equals(filterValue);
            case "neq" -> !fieldValue.equals(filterValue);
            case "like" -> fieldValue.contains(filterValue);
            default -> throw new IllegalArgumentException("Bad filter operator: " + operator);
        };
    }

    private boolean applyDateOperator(LocalDateTime fieldValue, String operator, LocalDateTime filterValue) {
        return switch (operator) {
            case "eq" -> fieldValue.isEqual(filterValue);
            case "neq" -> !fieldValue.isEqual(filterValue);
            case "gt" -> fieldValue.isAfter(filterValue);
            case "lt" -> fieldValue.isBefore(filterValue);
            case "gte" -> !fieldValue.isBefore(filterValue);
            case "lte" -> !fieldValue.isAfter(filterValue);
            default -> throw new IllegalArgumentException("Bad filter operator: " + operator);
        };
    }

    private boolean applyFilterForProduct(ProductEntity product, String field, String operator, String filterValue) {
        switch (field) {
            case "id":
                int idValue = product.getId();
                double filterId = Integer.parseInt(filterValue);
                return applyNumberOperator(idValue, operator, filterId);
            case "coordinates.x":
                double xValue = product.getCoordinates().getX();
                double filterX = Double.parseDouble(filterValue);
                return applyNumberOperator(xValue, operator, filterX);
            case "coordinates.y":
                double yValue = product.getCoordinates().getY();
                double filterY = Double.parseDouble(filterValue);
                return applyNumberOperator(yValue, operator, filterY);
            case "creationDate":
                LocalDateTime creationDate = product.getCreationDate();
                LocalDateTime filterDate = LocalDateTime.parse(filterValue);
                return applyDateOperator(creationDate, operator, filterDate);
            case "name":
                String nameValue = product.getName();
                return applyStringOperator(nameValue, operator, filterValue);
            case "price":
                double priceValue = product.getPrice();
                double filterPrice = Double.parseDouble(filterValue);
                return applyNumberOperator(priceValue, operator, filterPrice);
            case "manufactureCost":
                int manufactureCostValue = product.getManufactureCost();
                int filterManufactureCost = Integer.parseInt(filterValue);
                return applyNumberOperator(manufactureCostValue, operator, filterManufactureCost);
            case "owner":
                long ownerId = product.getOwner().getId();
                int filterOwnerId = Integer.parseInt(filterValue);
                return applyNumberOperator(ownerId, operator, filterOwnerId);
            case "owner.name":
                String ownerName = product.getOwner().getName();
                return applyStringOperator(ownerName, operator, filterValue);
            case "unitOfMeasure":
                UnitOfMeasure unitOfMeasureValue = product.getUnitOfMeasure();
                UnitOfMeasure filterUnitOfMeasure = UnitOfMeasure.valueOf(filterValue);
                return unitOfMeasureValue == filterUnitOfMeasure;
            default:
                throw new IllegalArgumentException("Unknown field: " + field);
        }
    }

    public List<ProductEntity> applySorting(List<ProductEntity> products, String sort) {
        if (sort == null || sort.isEmpty()) {
            return products;
        }

        String[] sortFields = sort.split(",");

        for (String sortField : sortFields) {
            boolean descending = sortField.startsWith("-");
            String fieldName = descending ? sortField.substring(1) : sortField;
            Comparator<ProductEntity> comparator = switch (fieldName) {
                case "id" ->
                        (p1, p2) -> descending ? Integer.compare(p2.getId(), p1.getId()) : Integer.compare(p1.getId(), p2.getId());
                case "name" ->
                        (p1, p2) -> descending ? p2.getName().compareTo(p1.getName()) : p1.getName().compareTo(p2.getName());
                case "price" ->
                        (p1, p2) -> descending ? Double.compare(p2.getPrice(), p1.getPrice()) : Double.compare(p1.getPrice(), p2.getPrice());
                case "creationDate" ->
                        (p1, p2) -> descending ? p2.getCreationDate().compareTo(p1.getCreationDate()) : p1.getCreationDate().compareTo(p2.getCreationDate());
                case "coordinates.x" ->
                        (p1, p2) -> descending ? Double.compare(p2.getCoordinates().getX(), p1.getCoordinates().getX()) : Double.compare(p1.getCoordinates().getX(), p2.getCoordinates().getX());
                case "coordinates.y" ->
                        (p1, p2) -> descending ? Double.compare(p2.getCoordinates().getY(), p1.getCoordinates().getY()) : Double.compare(p1.getCoordinates().getY(), p2.getCoordinates().getY());
                case "owner.name" ->
                        (p1, p2) -> descending ? p2.getOwner().getName().compareTo(p1.getOwner().getName()) : p1.getOwner().getName().compareTo(p2.getOwner().getName());
                case "manufactureCost" ->
                        (p1, p2) -> descending ? Integer.compare(p2.getManufactureCost(), p1.getManufactureCost()) : Integer.compare(p1.getManufactureCost(), p2.getManufactureCost());
                case "owner" ->
                        (p1, p2) -> descending ? Long.compare(p2.getOwner().getId(), p1.getOwner().getId()) : Long.compare(p1.getOwner().getId(), p2.getOwner().getId());
                default -> throw new IllegalArgumentException("Unsupported sorting field: " + fieldName);
            };
            products.sort(comparator);
        }

        return products;
    }

    private List<ProductEntity> applyPagination(List<ProductEntity> products, Integer page, Integer size) {
        if (page == null || size == null || page < 0) {
            size = 10;
            page = 1;
        }

        if(size <= 0){
            return new ArrayList<>();
        }

        int fromIndex = (page - 1) * size;
        if (fromIndex >= products.size()) {
            return products;
        }

        int toIndex = Math.min(fromIndex + size, products.size());
        return products.subList(fromIndex, toIndex);
    }

    private Product mapToProduct(ProductEntity entity) {
        return new Product(
                entity.getId(),
                entity.getName(),
                new Coordinates(entity.getCoordinates().getX(), entity.getCoordinates().getY()),
                entity.getCreationDate(),
                entity.getPrice(),
                entity.getManufactureCost(),
                entity.getUnitOfMeasure(),
                new Person(
                        entity.getOwner().getId(),
                        entity.getOwner().getName(),
                        entity.getOwner().getBirthday(),
                        entity.getOwner().getHeight(),
                        entity.getOwner().getWeight(),
                        new Location(
                                entity.getOwner().getLocation().getX(),
                                entity.getOwner().getLocation().getY(),
                                entity.getOwner().getLocation().getName()
                        )
                )
        );
    }
}

