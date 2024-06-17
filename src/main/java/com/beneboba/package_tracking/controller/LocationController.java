package com.beneboba.package_tracking.controller;

import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.model.BaseResponse;
import com.beneboba.package_tracking.model.request.LocationRequest;
import com.beneboba.package_tracking.service.LocationService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/locations")
@Slf4j
@RequiredArgsConstructor
public class LocationController {


    private final LocationService locationService;

    @PostMapping
    public ResponseEntity<BaseResponse<Location>> create(@RequestBody LocationRequest request){

        log.info("create -> " + request.toString());

        Location response = locationService.create(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(BaseResponse.<Location>builder()
                        .data(response)
                        .build());
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<Location>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        Page<Location> response = locationService.getAll(page, size);

        return ResponseEntity.ok(BaseResponse
                .<Page<Location>>builder()
                .data(response)
                .build());
    }
}
