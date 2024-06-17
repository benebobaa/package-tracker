package com.beneboba.package_tracking.repository;

import com.beneboba.package_tracking.entity.Service;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServiceRepository extends JpaRepository<Service, Long> {
}
