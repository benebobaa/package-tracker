package com.beneboba.package_tracking.service;


import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.entity.Service;
import com.beneboba.package_tracking.helper.dummy.DummyLocation;
import com.beneboba.package_tracking.model.request.LocationRequest;
import com.beneboba.package_tracking.model.request.ServiceRequest;
import com.beneboba.package_tracking.repository.LocationRepository;
import com.beneboba.package_tracking.service.impl.LocationServiceImpl;
import com.beneboba.package_tracking.util.Generator;
import com.beneboba.package_tracking.util.ValidationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LocationServiceTest {

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private LocationServiceImpl locationService;


    @Test
    void testCreate() {
        LocationRequest locationRequest = DummyLocation.newLocationRequest();

        Location lastRecorded = DummyLocation.newLocation();

        Location expectedLocation = locationRequest.toEntity(Generator.sequenceCodeLocation(lastRecorded.getId() + 1));

        when(locationRepository.findFirstByOrderByIdDesc()).thenReturn(lastRecorded);
        when(locationRepository.save(any(Location.class))).thenReturn(expectedLocation);

        Location actualLocation = locationService.create(locationRequest);

        verify(locationRepository, times(1)).findFirstByOrderByIdDesc();
        verify(locationRepository, times(1)).save(any(Location.class));
        assertEquals(expectedLocation, actualLocation);
    }

    @Test
    void testFindAll(){
        Location location1 = DummyLocation.newLocation();
        Location location2 = DummyLocation.newLocation();

        Page<Location> locationList = new PageImpl<>(List.of(location1, location2));

        when(locationRepository.findAll(any(PageRequest.class))).thenReturn(locationList);

        Page<Location> actual = locationService.getAll(0,10);

        verify(locationRepository, times(1)).findAll(any(PageRequest.class));
        assertEquals(2, actual.getTotalElements());
        assertEquals(locationList, actual);
    }
}
