package com.example.testservice.soap;

import lombok.*;

import javax.validation.constraints.Max;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Coordinates")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Coordinates {

    @NotNull(message = "Координата X не может быть null")
    @XmlElement(name="x")
    private Double x;

    @NotNull(message = "Координата Y не может быть null")
    @XmlElement(name="y")
    @Max(value = 189, message = "Координата Y не может быть больше 189")
    private Float y;
}

