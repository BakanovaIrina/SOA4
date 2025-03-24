package com.example.testservice.soap;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "Location")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @NotNull(message = "Координата X не может быть null")
    @XmlElement(name="x")
    private Double x;

    @NotNull(message = "Координата Y не может быть null")
    @XmlElement(name="y")
    private Double y;

    @NotBlank(message = "Название не может быть пустым")
    @Size(max = 940, message = "Название не может быть длиннее 940 символов")
    private String name;
}

