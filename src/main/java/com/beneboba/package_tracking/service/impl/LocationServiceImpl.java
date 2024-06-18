package com.beneboba.package_tracking.service.impl;

import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.model.request.LocationRequest;
import com.beneboba.package_tracking.repository.LocationRepository;
import com.beneboba.package_tracking.service.LocationService;
import com.beneboba.package_tracking.util.Generator;
import com.beneboba.package_tracking.util.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {

    private final LocationRepository locationRepository;


    private final ValidationService validationService;

    @Override
    public Location create(LocationRequest locationRequest) {
        log.info(this.getClass().getSimpleName() + " create -> " + locationRequest.toString());
        validationService.validate(locationRequest);

        Long id = 1L;

        Location lastRecorded = locationRepository.findFirstByOrderByIdDesc();

        if (lastRecorded != null){
            id = lastRecorded.getId() + 1;
        }

        return locationRepository.save(locationRequest.toEntity(Generator.sequenceCodeLocation(id)));
    }

    @Override
    public Page<Location> getAll(String queryName, int page, int size) {

        log.info("getAll -> queryName :: " + queryName + " page :: " + page + " size :: " + size);

        Pageable pageable = PageRequest.of(page, size);

        if (queryName == null || queryName.trim().isEmpty()){
            return locationRepository.findAll(pageable);
        }

        return locationRepository.findByName(queryName, pageable);
    }
}
