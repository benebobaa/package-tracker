package com.beneboba.package_tracking.repository;

import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.entity.Package;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface PackageRepository extends JpaRepository<Package, Long> {

}
