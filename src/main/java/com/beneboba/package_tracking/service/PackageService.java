package com.beneboba.package_tracking.service;

import com.beneboba.package_tracking.entity.Package;
import com.beneboba.package_tracking.model.request.CreatePackageRequest;
import org.springframework.data.domain.Page;



public interface PackageService {

    Package create(CreatePackageRequest createPackageRequest);

    Page<Package> getAll(int page, int size);

}
