package com.example.testservice.services;

import com.example.testservice.exception.AppException;
import com.example.testservice.exception.AppRuntimeException;
import com.example.testservice.soap.Product;
import com.example.testservice.soap.ListResponse;
import com.example.testservice.soap.ManufacturerRequest;
import com.example.testservice.soap.UnitRequest;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;
import org.springframework.ws.soap.server.endpoint.annotation.SoapAction;

import javax.xml.namespace.QName;
import javax.xml.soap.*;
import java.util.List;
import java.util.concurrent.CompletionException;

@Endpoint
public class SoapService {

    private static final String NAMESPACE_URI = "http://example.com/soap";
    private static final Logger log = LoggerFactory.getLogger(SoapService.class);
    @Autowired
    private EbayService ebayService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "manufacturerRequest")
    @SoapAction("http://example.com/soap/manufacturer")
    @ResponsePayload
    public ListResponse getProductsByManufacturer(@RequestPayload ManufacturerRequest request) throws AppException, SOAPException {
        log.info("manufacturer request");
        try {
            List<Product> res = ebayService.getProductsByManufacturer(request.getManufacturerId(), request.getPage(), request.getSize());
            return new ListResponse(res);
        }
        catch (AppRuntimeException e ) {
            throwSoapFault("400", e.getMessage());
        } catch (CompletionException e) {
            throwSoapFault("400", e.getCause().getMessage());
        }
        catch (Exception e) {
            throwSoapFault("500", e.getMessage());
        }
        return null;
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "unitRequest")
    @SoapAction("http://example.com/soap/unit")
    @ResponsePayload
    public ListResponse getProductsByUnitOfMeasure(@RequestPayload UnitRequest request) throws AppException {
        log.info("unit request");
        try {
            List<Product> res = ebayService.getProductsByUnitOfMeasure(request.getUnit(), request.getPage(), request.getSize());
            return new ListResponse(res);
        }
        catch (Exception e) {
            return new ListResponse();
        }
    }

    public static void throwSoapFault(String faultCode, String message) throws SOAPException {
            try {
                JSONObject jsonObj = new JSONObject(message);
                String messageText = jsonObj.getString("message");
                String time = jsonObj.getString("time");

                SOAPFactory factory = SOAPFactory.newInstance();
                SOAPFault fault = factory.createFault();

                fault.setFaultCode(new QName(SOAPConstants.URI_NS_SOAP_ENVELOPE, faultCode));
                fault.setFaultString(messageText);

                Detail detail = fault.addDetail();
                detail.addChildElement("responseTime").addTextNode(time);
                detail.addChildElement("errorCode").addTextNode(faultCode);

                throw new SOAPException(fault.getFaultString());
        } catch (JSONException e) {
            throw new SOAPException("Error parsing JSON response", e);
        }
    }




/*
    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "decreaseDifficultyRequest")
    @SoapAction("http://example.com/soap/decreaseDifficulty")
    @ResponsePayload
    public StatusResponse decreaseDifficulty(@RequestPayload DecreaseDifficultyRequest request) throws AppException {
        log.info("decreaseDifficulty request");
        try {
            secondaryService.decreaseDifficulty(request.getLabworkId(), request.getStepsCount());
            StatusResponse response = new StatusResponse();
            response.setStatus("OK");
            response.setMessage("OK");
            return response;
        }catch (Exception e) {
            log.info(e.getMessage());
            return new StatusResponse("ERROR", "произошла ошибка: " + e.getMessage());
        }
    }

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "deleteLabworkRequest")
    @SoapAction("http://example.com/soap/deleteLabwork")
    @ResponsePayload
    public StatusResponse deleteLabwork(@RequestPayload DeleteLabworkRequestFromDiscipline request) throws AppException {
        log.info("delete request");
        try {
            secondaryService.deleteLabwork(request.getDisciplineId(), request.getLabworkId());
            StatusResponse response = new StatusResponse();
            response.setStatus("OK");
            response.setMessage("OK");
            return response;
        }catch (Exception e) {
            log.info(e.getMessage());
            return new StatusResponse("ERROR", "произошла ошибка: " + e.getMessage());
        }
    }

 */
}

