package com.example.testservice.controllers;

import com.example.testservice.services.SoapService;
import com.example.testservice.soap.ManufacturerRequest;
import com.example.testservice.soap.UnitRequest;
import com.example.testservice.utils.ResponseUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import org.springframework.ws.client.core.WebServiceTemplate;

@RestController
@RequestMapping("/api/v1/ebay/filter")
public class EbayController {

    @Autowired
    private WebServiceTemplate webServiceTemplate;

    @Autowired
    private SoapService soapService;

    @Autowired
    private ResponseUtils responseUtils;

    @PostMapping("/manufacturer/{manufacturer-id}")
    public ResponseEntity<?> getProductsByManufacturer(
            @PathVariable("manufacturer-id") Integer manufacturerId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        try {
            if(page < 1 || size < 1) {
                return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, "Page number is out of bounds!");
            }
            if (manufacturerId <= 0) {
                return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, "Id is under 0!");
            }
            ManufacturerRequest request = new ManufacturerRequest(manufacturerId, page, size);
            return ResponseEntity.ok(soapService.getProductsByManufacturer(request));

        } catch (Exception e) {
            return responseUtils.buildResponseWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    @PostMapping("/unit-of-measure/{unit-of-measure}")
    public ResponseEntity<?> getProductsByUnitOfMeasure(
            @PathVariable("unit-of-measure") String unitOfMeasure,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        try {
            if(page < 1 || size < 1) {
                return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, "Page number is out of bounds!");
            }

            UnitRequest request = new UnitRequest(unitOfMeasure, page, size);

            return ResponseEntity.ok(soapService.getProductsByUnitOfMeasure(request));
        } catch (IllegalArgumentException e) {
            return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, "Invalid query param value");
        } catch (Exception e) {
            return responseUtils.buildResponseWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }
}



/*
@RestController
@RequestMapping("/api/v1/ebay/filter")
@Controller
public class EbayController {

    @Autowired
    private EbayService productService;

    @Autowired
    private ResponseUtils responseUtils;


    @GetMapping("/manufacturer/{manufacturer-id}")
    public ResponseEntity<?> getProductsByManufacturer(
            @PathVariable("manufacturer-id") Integer manufacturerId,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        try {
            if(page < 1 || size < 1) {
                return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, "Page number is out of bounds!");
            }
            if (manufacturerId <= 0) {
                return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, "Id is under 0!");
            }

            List<Product> products = productService.getProductsByManufacturer(manufacturerId, page, size);
            return ResponseEntity.ok(products);
        } catch (AppRuntimeException e){
            return responseUtils.buildResponseByException(e);
        }
        catch (Exception e) {
            return responseUtils.buildResponseWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }

    @GetMapping("/unit-of-measure/{unit-of-measure}")
    public ResponseEntity<?> getProductsByUnitOfMeasure(
            @PathVariable("unit-of-measure") String unitOfMeasure,
            @RequestParam(required = false, defaultValue = "1") int page,
            @RequestParam(required = false, defaultValue = "10") int size) {

        try {
            if(page < 1 || size < 1) {
                return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, "Page number is out of bounds!");
            }
            List<Product> products = productService.getProductsByUnitOfMeasure(unitOfMeasure, page, size);
            return ResponseEntity.ok(products);
        } catch (AppRuntimeException e){
            return responseUtils.buildResponseByException(e);
        } catch (IllegalArgumentException e) {
            return responseUtils.buildResponseWithMessage(HttpStatus.BAD_REQUEST, "Invalid query param value");
        } catch (Exception e) {
            return responseUtils.buildResponseWithMessage(HttpStatus.INTERNAL_SERVER_ERROR, "Internal server error");
        }
    }
}

 */

