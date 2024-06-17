package com.beneboba.package_tracking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Package {

    @Id
    @GeneratedValue
    private Long id;

    private float weight;

    @ManyToOne
    @JoinColumn(name = "sender_id")
    private Sender sender;

    @ManyToOne
    @JoinColumn(name = "receiver_id")
    private Receiver receiver;

    @ManyToOne
    @JoinColumn(name = "location_id")
    private Location destination;
}
