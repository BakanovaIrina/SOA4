package com.soa.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import java.time.ZonedDateTime;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "persons")
public class PersonEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter
    private Long id;

    @NotNull
    @NotBlank
    @Column(nullable = false)
    @Getter
    @Setter
    private String name;

    @Getter
    @Setter
    private ZonedDateTime birthday;

    @Min(1)
    @Getter
    @Setter
    @Column(nullable = false)
    private long height;

    @Min(1)
    @Getter
    @Setter
    @Column(nullable = false)
    private long weight;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "location_id")
    @Getter
    @Setter
    private LocationEntity location;

}
