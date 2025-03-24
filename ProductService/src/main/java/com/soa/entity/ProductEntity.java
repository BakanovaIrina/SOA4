package com.soa.entity;

import com.soa.model.UnitOfMeasure;
import jakarta.persistence.*;
import lombok.Getter;

import jakarta.validation.constraints.*;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Table(name = "products")
public class ProductEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Integer id;

    @NotNull
    @NotBlank
    @Column(nullable = false, unique = true)
    @Getter
    @Setter
    private String name;

    @NotNull
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "coordinates_id", nullable = false)
    @Getter
    @Setter
    private CoordinatesEntity coordinates;

    @NotNull
    @Column(nullable = false, updatable = false)
    @Getter
    @Setter
    private LocalDateTime creationDate;

    @Min(1)
    @Column(nullable = false)
    @Getter
    @Setter
    private double price;

    @Getter
    @Setter
    private int manufactureCost;

    @Enumerated(EnumType.STRING)
    @Getter
    @Setter
    private UnitOfMeasure unitOfMeasure;

    @NotNull
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "owner_id", nullable = false)
    @Getter
    @Setter
    private PersonEntity owner;

    @PrePersist
    protected void onCreate() {
        this.creationDate = LocalDateTime.now();
    }
}

