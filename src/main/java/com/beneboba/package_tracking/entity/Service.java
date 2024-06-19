package com.beneboba.package_tracking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(
        indexes = {
                @Index(name = "idx_service_name", columnList = "name"),
        }
)
public class Service {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private float priceKg;
}
