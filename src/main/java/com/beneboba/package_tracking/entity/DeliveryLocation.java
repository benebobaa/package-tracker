//package com.beneboba.package_tracking.entity;
//
//import com.beneboba.package_tracking.entity.Delivery;
//import com.beneboba.package_tracking.entity.Location;
//import jakarta.persistence.*;
//import lombok.AllArgsConstructor;
//import lombok.Data;
//import lombok.NoArgsConstructor;
//
//import java.time.LocalDateTime;
//
//@Entity
//@Data
//@AllArgsConstructor
//@NoArgsConstructor
//public class DeliveryLocation {
//
//    @Id
//    @GeneratedValue
//    private Long id;
//
//    @ManyToOne
//    @JoinColumn(name = "delivery_id")
//    private Delivery delivery;
//
//    @ManyToOne
//    @JoinColumn(name = "location_id")
//    private Location location;
//
//    @Column(name = "created_at")
//    private LocalDateTime createdAt;
//
//    @PrePersist
//    protected void onCreate() {
//        this.createdAt = LocalDateTime.now();
//    }
//}