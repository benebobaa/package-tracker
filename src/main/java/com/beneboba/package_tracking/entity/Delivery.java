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
public class Delivery {

    @Id
    @GeneratedValue
    private Long id;

    private float priceDelivery;

    @ManyToOne
    @JoinColumn(name = "service_id")
    private Service service;

    @ManyToOne
    @JoinColumn(name = "package_id")
    private Package packageItem;

    @ManyToMany
    @JoinTable(
            name = "delivery_location",
            joinColumns = @JoinColumn(name = "delivery_id"),
            inverseJoinColumns = @JoinColumn(name = "location_id")
    )

    private List<Location> checkpointDelivery;

    private Boolean isReceived;
}
