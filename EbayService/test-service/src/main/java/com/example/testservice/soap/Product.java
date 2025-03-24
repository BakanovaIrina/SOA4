package com.example.testservice.soap;

import lombok.*;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.xml.bind.annotation.*;
import java.time.LocalDateTime;

@XmlRootElement(name = "Product")
@XmlAccessorType(XmlAccessType.FIELD)
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @NotNull(message = "ID не может быть null")
    @Min(value = 1, message = "ID должен быть больше 0")
    @XmlElement(name="id")
    private Integer id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    @NotNull(message = "Координаты не могут быть null")
    @XmlElement(name="coordinates")
    private Coordinates coordinates;

    @NotNull(message = "Дата создания не может быть null")
    @XmlElement(name="creationDate")
    private LocalDateTime creationDate = LocalDateTime.now(); // Генерируется автоматически

    @Min(value = 1, message = "Цена должна быть больше 0")
    @XmlElement(name="price")
    private double price;

    @XmlElement(name="manufactureCost")
    private int manufactureCost;

    @XmlElement(name="unitOfMeasure")
    private UnitOfMeasure unitOfMeasure;

    @NotNull(message = "Владелец не может быть null")
    @XmlElement(name="owner")
    private Person owner;
}

