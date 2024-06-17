package com.beneboba.package_tracking.service.impl;

import com.beneboba.package_tracking.model.request.ServiceRequest;
import com.beneboba.package_tracking.repository.ServiceRepository;
import com.beneboba.package_tracking.service.ServiceService;
import com.beneboba.package_tracking.entity.Service;
import com.beneboba.package_tracking.util.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

@org.springframework.stereotype.Service
@Slf4j
@RequiredArgsConstructor
public class ServiceServiceImpl implements ServiceService {

    private final ServiceRepository serviceRepository;

    private final ValidationService validationService;

    @Override
    public Service create(ServiceRequest request) {
        log.info("create -> " + request.toString());

        validationService.validate(request);

        return serviceRepository.save(request.toEntity());
    }

    @Override
    public Page<Service> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return serviceRepository.findAll(pageable);
    }
}
