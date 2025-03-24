package com.soa.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Location {

    @NotNull(message = "Координата X не может быть null")
    private Double x;

    @NotNull(message = "Координата Y не может быть null")
    private Double y;

    @NotBlank(message = "Название не может быть пустым")
    @Size(max = 940, message = "Название не может быть длиннее 940 символов")
    private String name;
}

