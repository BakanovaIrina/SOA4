package com.soa.model;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.Generated;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
public class Person {
    @NotNull(message = "ID не может быть null")
    @Min(value = 1, message = "ID должен быть больше 0")
    @Generated
    private Long id;

    @NotBlank(message = "Имя не может быть пустым")
    private String name;

    private ZonedDateTime birthday; // Может быть null

    @Min(value = 1, message = "Рост должен быть больше 0")
    private long height;

    @Min(value = 1, message = "Вес должен быть больше 0")
    private long weight;

    private Location location; // Может быть null

    public Person(Long id, String name, ZonedDateTime birthday, long height, long weight, Location location) {
        this.id = id;
        this.name = name;
        this.birthday = birthday;
        this.height = height;
        this.weight = weight;
        this.location = location;
    }
}

