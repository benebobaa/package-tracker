package com.beneboba.package_tracking.service.impl;

import com.beneboba.package_tracking.entity.*;
import com.beneboba.package_tracking.entity.Package;
import com.beneboba.package_tracking.model.request.CreateDeliveryPackageRequest;
import com.beneboba.package_tracking.model.request.PackageFilter;
import com.beneboba.package_tracking.model.request.UpdateCheckpointRequest;
import com.beneboba.package_tracking.repository.*;
import com.beneboba.package_tracking.service.DeliveryService;
import com.beneboba.package_tracking.util.ValidationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@org.springframework.stereotype.Service
@Slf4j
@RequiredArgsConstructor
public class DeliveryServiceImpl implements DeliveryService {

    private final DeliveryRepository deliveryRepository;

    private final PackageRepository packageRepository;

    private final ServiceRepository serviceRepository;

    private final ValidationService validationService;

    private final LocationRepository locationRepository;

    private final SenderRepository senderRepository;

    private final ReceiverRepository receiverRepository;

    @Override
    @Transactional
    public Delivery createWithPackage(CreateDeliveryPackageRequest request) {
        validationService.validate(request);

        Optional<Location> location = locationRepository.findFirstByCodeLocation(request.getPackageRequest().getCodeLocation());

        if (location.isEmpty()) {
            log.error(this.getClass().getSimpleName() + " create -> Location not found");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location not found");
        }

        Optional<Service> service = serviceRepository.findById(request.getServiceId());

        if (service.isEmpty()){
            log.error("createWithPackage -> Service not found");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Service not found");
        }

        Sender sender = senderRepository.save(
                request.getPackageRequest()
                        .getSender()
                        .toEntity()
        );

        Receiver receiver = receiverRepository.save(
                request.getPackageRequest()
                        .getReceiver()
                        .toEntity()
        );

        Package packageResult = packageRepository.save(
                request.getPackageRequest()
                        .toEntity(sender, receiver, location.get())
        );

        float totalPrice = packageResult.getWeight() * service.get().getPriceKg();

        return deliveryRepository.save(
                request.toEntity(
                        service.get(),
                        packageResult,
                        totalPrice,
                        false
                ));
    }

    @Override
    @Transactional
    public Delivery updateCheckpointLocation(UpdateCheckpointRequest request) {
        log.info("updateCheckpointLocation -> " + request);

        validationService.validate(request);

        Optional<Delivery> optionalDelivery = deliveryRepository.findById(request.getDeliveryId());

        if (optionalDelivery.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Delivery not found");
        }

        Optional<Location> location = locationRepository.findFirstByCodeLocation(request.getCodeLocation());

        if (location.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Location not found");
        }

        Delivery delivery = optionalDelivery.get();
        List<Location> checkpointDelivery = delivery.getCheckpointDelivery();

        log.info("checkpointDelivery -> " + checkpointDelivery);

        checkpointDelivery.add(location.get());
        delivery.setCheckpointDelivery(checkpointDelivery);

        log.info("after add :: checkpointDelivery -> " + checkpointDelivery);

        log.info("request codeLocation -> {}", request.getCodeLocation());
        log.info("delivery destination codeLocation -> {}", delivery.getPackageItem().getDestination().getCodeLocation());
        if (Objects.equals(request.getCodeLocation(), delivery.getPackageItem().getDestination().getCodeLocation())){
            delivery.setIsReceived(true);
        }

        return deliveryRepository.save(delivery);
    }

    @Override
    public Page<Delivery> getAll(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        return deliveryRepository.findAll(pageable);
    }

    @Override
    public Page<Delivery> getAllWithFilter(PackageFilter filter, int page, int size) {

        Pageable pageable = PageRequest.of(page, size);

        return deliveryRepository.findDeliveriesByLocationNameAndIsReceived(
                filter.getLocationName(),
                filter.getIsReceived(),
                pageable
        );
    }
}
