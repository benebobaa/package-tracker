package com.beneboba.package_tracking.repository;

import com.beneboba.package_tracking.entity.Location;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Location findFirstByOrderByIdDesc();

    Optional<Location> findFirstByCodeLocation(String codeLocation);
}
