package com.beneboba.package_tracking.repository;

import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.entity.Service;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface ServiceRepository extends JpaRepository<Service, Long> {

    @Query("SELECT s FROM Service s WHERE LOWER(s.name) LIKE LOWER(CONCAT('%', :name, '%'))")
    Page<Service> findByName(@Param("name") String name, Pageable pageable);
}
