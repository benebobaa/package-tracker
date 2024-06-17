package com.beneboba.package_tracking.controller;

import com.beneboba.package_tracking.entity.Package;
import com.beneboba.package_tracking.model.BaseResponse;
import com.beneboba.package_tracking.model.request.CreatePackageRequest;
import com.beneboba.package_tracking.model.request.PackageFilter;
import com.beneboba.package_tracking.service.PackageService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/packages")
@Slf4j
@RequiredArgsConstructor
public class PackageController {

    private final PackageService packageService;

//    @PostMapping
//    public ResponseEntity<BaseResponse<Package>> create(
//            @RequestBody CreatePackageRequest request
//    ){
//
//        log.info("create -> " + request.toString());
//
//        Package response = packageService.create(request);
//
//        return ResponseEntity
//                .status(HttpStatus.CREATED)
//                .body(BaseResponse.<Package>builder()
//                        .data(response)
//                        .build());
//    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<Package>>> getAll(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("getAll -> page: " + page + " size: " + size);

        Page<Package> response = packageService.getAll(page, size);

        log.info("getAll -> " + response.toString());

        return ResponseEntity.ok(BaseResponse
                .<Page<Package>>builder()
                .data(response)
                .build());
    }
}



