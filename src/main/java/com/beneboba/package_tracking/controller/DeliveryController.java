package com.beneboba.package_tracking.controller;

import com.beneboba.package_tracking.entity.Delivery;
import com.beneboba.package_tracking.model.BaseResponse;
import com.beneboba.package_tracking.model.request.CreateDeliveryPackageRequest;
import com.beneboba.package_tracking.model.request.PackageFilter;
import com.beneboba.package_tracking.model.request.UpdateCheckpointRequest;
import com.beneboba.package_tracking.service.DeliveryService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/api/deliveries")
@RequiredArgsConstructor
public class DeliveryController {

    private final DeliveryService deliveryService;

    @PostMapping
    public ResponseEntity<BaseResponse<Delivery>> createDeliveryWithPackage(@RequestBody CreateDeliveryPackageRequest request) {

        log.info("createDeliveryWithPackage -> " + request.toString());

        Delivery response = deliveryService.createWithPackage(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.<Delivery>builder()
                        .data(response)
                        .build());
    }

    @PatchMapping
    public ResponseEntity<BaseResponse<Delivery>> updateDelivery(@RequestBody UpdateCheckpointRequest request) {

        log.info("updateDelivery -> " + request);

        Delivery response = deliveryService.updateCheckpointLocation(request);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.<Delivery>builder()
                        .data(response)
                        .build());
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<Delivery>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        log.info("getAll -> page: " + page + " size: " + size);

        Page<Delivery> response = deliveryService.getAll(page, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.<Page<Delivery>>builder()
                        .data(response)
                        .build());
    }

    @GetMapping("/filter")
    public ResponseEntity<BaseResponse<Page<Delivery>>> getAllWithFilter(
            @RequestParam(value = "locationName", required = false) String locationName,
            @RequestParam(value = "isReceived", required = false) Boolean isReceived,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        log.info("getAll -> page: " + page + " size: " + size);

        PackageFilter filter = PackageFilter.builder()
                .isReceived(isReceived)
                .locationName(locationName)
                .build();

        log.info("getAll -> filter: " + filter);

        Page<Delivery> response = deliveryService.getAllWithFilter(filter,page, size);

        return ResponseEntity
                .status(HttpStatus.OK)
                .body(BaseResponse.<Page<Delivery>>builder()
                        .data(response)
                        .build());
    }
}
