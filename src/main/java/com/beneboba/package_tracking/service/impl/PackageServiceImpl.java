package com.beneboba.package_tracking.service.impl;

import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.entity.Package;
import com.beneboba.package_tracking.entity.Receiver;
import com.beneboba.package_tracking.entity.Sender;
import com.beneboba.package_tracking.model.request.CreatePackageRequest;
import com.beneboba.package_tracking.model.request.PackageFilter;
import com.beneboba.package_tracking.repository.*;
import com.beneboba.package_tracking.service.PackageService;
import com.beneboba.package_tracking.util.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class PackageServiceImpl implements PackageService {


    private final PackageRepository packageRepository;


    private final SenderRepository senderRepository;


    private final ReceiverRepository receiverRepository;


    private final LocationRepository locationRepository;


    private final ValidationService validationService;


    @Override
    @Transactional
    public Package create(CreatePackageRequest createPackageRequest) {
        validationService.validate(createPackageRequest);

        Optional<Location> location = locationRepository.findFirstByCodeloc(createPackageRequest.getCodeLocation());

        if (location.isEmpty()) {
            log.error(this.getClass().getSimpleName() + " create -> Location not found");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location not found");
        }

        Sender sender = senderRepository.save(createPackageRequest.getSender().toEntity());

        Receiver receiver = receiverRepository.save(createPackageRequest.getReceiver().toEntity());

        return packageRepository.save(createPackageRequest.toEntity(sender, receiver, location.get()));
    }

    @Override
    public Page<Package> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        return packageRepository.findAll(pageable);
    }
}
