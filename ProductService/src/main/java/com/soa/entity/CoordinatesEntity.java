package com.soa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "coordinates")
public class CoordinatesEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @NotNull
    @Column(nullable = false)
    @Getter
    @Setter
    private Double x;

    @NotNull
    @Max(189)
    @Column(nullable = false)
    @Getter
    @Setter
    private Float y;
}

