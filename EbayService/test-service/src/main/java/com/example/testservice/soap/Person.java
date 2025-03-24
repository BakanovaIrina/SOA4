package com.example.testservice.soap;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.time.ZonedDateTime;

@XmlRootElement(name = "Person")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Person {
    @NotNull(message = "ID не может быть null")
    @Min(value = 1, message = "ID должен быть больше 0")
    @XmlElement(name="id")
    private Integer id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @XmlElement(name="birthday")
    private ZonedDateTime birthday; // Может быть null

    @XmlElement(name="height")
    @Min(value = 1, message = "Рост должен быть больше 0")
    private long height;

    @XmlElement(name="weight")
    @Min(value = 1, message = "Вес должен быть больше 0")
    private long weight;

    @XmlElement(name="location")
    private Location location; // Может быть null
}

