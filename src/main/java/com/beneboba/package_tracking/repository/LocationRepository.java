package com.beneboba.package_tracking.repository;

import com.beneboba.package_tracking.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Location findFirstByOrderByIdDesc();

    Optional<Location> findFirstByCodeLocation(String codeLocation);

    @Query("SELECT l FROM Location l WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Location> findByName(@Param("name") String name, Pageable pageable);
}
