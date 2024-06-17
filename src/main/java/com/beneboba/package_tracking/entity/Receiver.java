package com.beneboba.package_tracking.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Receiver {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    private String phone;
}
