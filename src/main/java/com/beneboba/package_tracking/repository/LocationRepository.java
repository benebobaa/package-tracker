package com.beneboba.package_tracking.repository;

import com.beneboba.package_tracking.entity.Location;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface LocationRepository extends JpaRepository<Location, Long> {

    Location findFirstByOrderByIdDesc();

    Optional<Location> findFirstByCodeloc(String codeloc);

    @Query("SELECT l FROM Location l WHERE LOWER(l.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Location> findByName(@Param("name") String name, Pageable pageable);

    @Query(value = "SELECT " +
            "    dl.location_id AS id, " +
            "    (SELECT l.codeloc FROM location l WHERE l.id = dl.location_id) AS codeloc, " +
            "    (SELECT l.name FROM location l WHERE l.id = dl.location_id) AS name, " +
            "    (SELECT l.address FROM location l WHERE l.id = dl.location_id) AS address " +
            "FROM " +
            "    delivery_location dl " +
            "WHERE " +
            "    dl.delivery_id = :deliveryId", nativeQuery = true)
    List<Location> findCheckpointDeliveryByDeliveryId(@Param("deliveryId") Long deliveryId);
}
