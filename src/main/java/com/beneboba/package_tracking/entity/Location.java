package com.beneboba.package_tracking.entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_location_name", columnList = "name"),
        }
)
public class Location {

    @Id
    @GeneratedValue
    private Long id;

    @Column(unique = true)
    private String codeloc;

    private String name;

    private String address;
}
