package com.beneboba.package_tracking.repository;

import com.beneboba.package_tracking.entity.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("SELECT d FROM Delivery d " +
            "LEFT JOIN d.checkpointDelivery loc " +
            "WHERE (:locationName IS NULL OR LOWER(loc.name) = LOWER(CAST(:locationName AS string))) " +
            "AND (:isReceived IS NULL OR d.isReceived = :isReceived)")
    Page<Delivery> findDeliveriesByLocationNameAndIsReceived(
            @Param("locationName") String locationName,
            @Param("isReceived") Boolean isReceived,
            Pageable pageable);

}
