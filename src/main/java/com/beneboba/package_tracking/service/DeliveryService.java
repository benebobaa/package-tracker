package com.beneboba.package_tracking.service;

import com.beneboba.package_tracking.entity.Delivery;
import com.beneboba.package_tracking.model.request.CreateDeliveryPackageRequest;
import com.beneboba.package_tracking.model.request.PackageFilter;
import com.beneboba.package_tracking.model.request.UpdateCheckpointRequest;
import org.springframework.data.domain.Page;

public interface DeliveryService {

    Delivery createWithPackage(CreateDeliveryPackageRequest createDeliveryPackageRequest);

    Delivery updateCheckpointLocation(UpdateCheckpointRequest updateCheckpointRequest);

    Page<Delivery> getAll(int page, int size);

    Page<Delivery> getAllWithFilter(PackageFilter filter, int page, int size);

    Delivery findById(Long id);
}
