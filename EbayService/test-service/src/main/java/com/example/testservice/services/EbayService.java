package com.example.testservice.services;
import com.example.testservice.adapter.EbayServiceAdapter;
import com.example.testservice.exception.AppException;
import com.example.testservice.soap.Product;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class EbayService {
    @Autowired
    EbayServiceAdapter ebayServiceAdapter;

    public List<Product> getProductsByManufacturer(Integer manufacturerId, int page, int size) throws AppException {
        if(size == 0 || page == 0){
            manufacturerId = 94;
            page = 1;
            size = 10;
        }
        String filters = "filter=owner%5Beq%5D%3D" + manufacturerId;
        return  ebayServiceAdapter.getProducts(filters, page, size);
    }

    public List<Product> getProductsByUnitOfMeasure(String unitOfMeasure, int page, int size) throws AppException {
        String filters = "filter=unitOfMeasure%5Beq%5D%3D" + unitOfMeasure;
        return ebayServiceAdapter.getProducts(filters, page, size);
    }
}
