package com.soa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "locations")
public class LocationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @Column(nullable = false)
    @Getter
    @Setter
    private double x;

    @Column(nullable = false)
    @Getter
    @Setter
    private double y;

    @NotNull
    @Size(max = 940)
    @Column(nullable = false, length = 940)
    @Getter
    @Setter
    private String name;
}

