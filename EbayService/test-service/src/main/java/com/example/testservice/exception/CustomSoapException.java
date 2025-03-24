package com.example.testservice.exception;

import org.springframework.ws.soap.server.endpoint.annotation.FaultCode;
import org.springframework.ws.soap.server.endpoint.annotation.SoapFault;

@SoapFault(faultCode = FaultCode.SERVER)
public class CustomSoapException extends RuntimeException {
    public CustomSoapException(String message) {
        super(message);
    }
}
