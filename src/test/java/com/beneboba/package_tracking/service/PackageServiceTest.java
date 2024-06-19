package com.beneboba.package_tracking.service;

import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.entity.Package;
import com.beneboba.package_tracking.helper.dummy.DummyLocation;
import com.beneboba.package_tracking.helper.dummy.DummyPackage;
import com.beneboba.package_tracking.helper.dummy.DummyReceiver;
import com.beneboba.package_tracking.helper.dummy.DummySender;
import com.beneboba.package_tracking.model.request.CreatePackageRequest;
import com.beneboba.package_tracking.model.request.ReceiverRequest;
import com.beneboba.package_tracking.model.request.SenderRequest;
import com.beneboba.package_tracking.repository.LocationRepository;
import com.beneboba.package_tracking.repository.PackageRepository;
import com.beneboba.package_tracking.repository.ReceiverRepository;
import com.beneboba.package_tracking.repository.SenderRepository;
import com.beneboba.package_tracking.service.impl.PackageServiceImpl;
import com.beneboba.package_tracking.util.ValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
public class PackageServiceTest {

    @Mock
    private PackageRepository packageRepository;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private SenderRepository senderRepository;

    @Mock
    private ReceiverRepository receiverRepository;

    @InjectMocks
    private PackageServiceImpl packageService;

    @Mock
    private ValidationService validationService;

    @Test
    void testCreate() {
        SenderRequest senderRequest = DummySender.newSenderRequest();
        ReceiverRequest receiverRequest = DummyReceiver.newReceiverRequest();
        CreatePackageRequest packageRequest = DummyPackage.newPackageRequest(
                senderRequest, receiverRequest, DummyLocation.CODE_LOCATION);

        Location expectedLocation = DummyLocation.newLocation();
        Package expectedPackage = packageRequest.toEntity(senderRequest.toEntity(), receiverRequest.toEntity(), expectedLocation);

        when(locationRepository.findFirstByCodeloc(DummyLocation.CODE_LOCATION))
                .thenReturn(Optional.of(expectedLocation));
        when(packageRepository.save(any(Package.class))).thenReturn(expectedPackage);


        Optional<Location> actualLocation = locationRepository.findFirstByCodeloc(DummyLocation.CODE_LOCATION);
        Package actualPackage = packageService.create(packageRequest);

        assertEquals(expectedLocation, actualLocation.get());
        assertEquals(expectedPackage, actualPackage);
    }

    @Test
    void testCreateWhenLocationIsEmpty() {
        CreatePackageRequest packageRequest = DummyPackage.newPackageRequest();

        when(locationRepository.findFirstByCodeloc(DummyLocation.CODE_LOCATION))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> packageService.create(packageRequest)
        );
    }

    @Test
    void testFindAll(){
        Package package1 = DummyPackage.newPackage();
        Package package2 = DummyPackage.newPackage();

        Page<Package> packageList = new PageImpl<>(List.of(package1, package2));

        when(packageRepository.findAll(any(PageRequest.class))).thenReturn(packageList);

        Page<Package> actual = packageService.getAll(0,10);

        verify(packageRepository, times(1)).findAll(any(PageRequest.class));
        assertEquals(2, actual.getTotalElements());
        assertEquals(packageList, actual);
    }
}