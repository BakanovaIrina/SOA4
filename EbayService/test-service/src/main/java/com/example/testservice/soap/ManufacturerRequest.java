package com.example.testservice.soap;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@XmlRootElement(name = "manufacturerRequest", namespace = "http://example.com/soap")
@XmlAccessorType(XmlAccessType.FIELD)
public class ManufacturerRequest {
    @XmlElement(name = "manufacturerId", required = true)
    private int manufacturerId;

    @XmlElement(name = "page", required = true)
    private int page;

    @XmlElement(name = "size", required = true)
    private int size;
}
