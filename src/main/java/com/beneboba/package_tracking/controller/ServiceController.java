package com.beneboba.package_tracking.controller;

import com.beneboba.package_tracking.entity.Service;
import com.beneboba.package_tracking.model.BaseResponse;
import com.beneboba.package_tracking.model.request.ServiceRequest;
import com.beneboba.package_tracking.service.ServiceService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;



@RestController
@RequestMapping("/api/services")
@Slf4j
@RequiredArgsConstructor
public class ServiceController {

    private final ServiceService serviceService;

    @PostMapping
    public ResponseEntity<BaseResponse<Service>> create(@RequestBody ServiceRequest request){
        log.info(this.getClass().getSimpleName() + " create -> " + request.toString());

        Service response = serviceService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.<Service>builder()
                        .data(response)
                        .build());
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<Service>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {

        Page<Service> response = serviceService.getAll(page, size);

        log.info("getAll -> " + response.toString());

        return ResponseEntity.ok(BaseResponse
                .<Page<Service>>builder()
                .data(response)
                .build());
    }
}
