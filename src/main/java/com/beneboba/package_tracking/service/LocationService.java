package com.beneboba.package_tracking.service;

import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.model.request.LocationRequest;
import org.springframework.data.domain.Page;

import java.util.List;

public interface LocationService {

    Location create(LocationRequest locationRequest);

    Page<Location> getAll(int page, int size);
}
