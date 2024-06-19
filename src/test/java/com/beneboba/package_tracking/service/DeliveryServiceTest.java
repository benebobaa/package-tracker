package com.beneboba.package_tracking.service;

import com.beneboba.package_tracking.entity.*;
import com.beneboba.package_tracking.entity.Package;
import com.beneboba.package_tracking.helper.dummy.*;
import com.beneboba.package_tracking.model.request.*;
import com.beneboba.package_tracking.repository.*;
import com.beneboba.package_tracking.service.impl.DeliveryServiceImpl;
import com.beneboba.package_tracking.util.ValidationService;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Slf4j
public class DeliveryServiceTest {

    @Mock
    private DeliveryRepository deliveryRepository;

    @Mock
    private PackageRepository packageRepository;

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ValidationService validationService;

    @Mock
    private LocationRepository locationRepository;

    @Mock
    private SenderRepository senderRepository;

    @Mock
    private ReceiverRepository receiverRepository;

    @InjectMocks
    private DeliveryServiceImpl deliveryService;

    @Test
    void testCreateWithPackageSuccess() {
        Location location = DummyLocation.newLocation();
        Sender sender = DummySender.newSender();
        Receiver receiver = DummyReceiver.newReceiver();

        Service service = DummyService.newService();

        ReceiverRequest receiverRequest = DummyReceiver.newReceiverRequest();
        SenderRequest senderRequest = DummySender.newSenderRequest();
        CreatePackageRequest packageRequest = DummyPackage.newPackageRequest(
                senderRequest,
                receiverRequest,
                DummyLocation.CODE_LOCATION
        );

        Package pkg = packageRequest.toEntity(sender, receiver, location);

        CreateDeliveryPackageRequest deliveryRequest = DummyDelivery.newDeliveryRequest(
                packageRequest,
                DummyService.SERVICE_ID
        );

        Delivery expectedDelivery = deliveryRequest.toEntity(
                service, pkg, DummyDelivery.DELIVERY_PRICE,
                DummyDelivery.DELIVERY_IS_RECEIVED);


        when(locationRepository.findFirstByCodeloc(anyString())).thenReturn(Optional.of(location));
        when(senderRepository.save(any(Sender.class))).thenReturn(sender);
        when(receiverRepository.save(any(Receiver.class))).thenReturn(receiver);
        when(packageRepository.save(any(Package.class))).thenReturn(pkg);
        when(serviceRepository.findById(anyLong())).thenReturn(Optional.of(service));
        when(deliveryRepository.save(any(Delivery.class))).thenReturn(expectedDelivery);

        Delivery actualDelivery = deliveryService.createWithPackage(deliveryRequest);

        verify(locationRepository, times(1)).findFirstByCodeloc(anyString());
        verify(senderRepository, times(1)).save(any(Sender.class));
        verify(receiverRepository, times(1)).save(any(Receiver.class));
        verify(packageRepository, times(1)).save(any(Package.class));
        verify(serviceRepository, times(1)).findById(anyLong());
        verify(deliveryRepository, times(1)).save(any(Delivery.class));

        assertEquals(expectedDelivery, actualDelivery);
    }

    @Test
    void testCreateWithPackageFailedLocationEmpty() {
        CreatePackageRequest packageRequest = DummyPackage.newPackageRequest();
        CreateDeliveryPackageRequest request = DummyDelivery.newDeliveryRequest(
                packageRequest, DummyService.SERVICE_ID);

        when(locationRepository.findFirstByCodeloc(anyString())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            deliveryService.createWithPackage(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Location not found", exception.getReason());

        verify(locationRepository, times(1)).findFirstByCodeloc(anyString());
        verify(senderRepository, times(0)).save(any(Sender.class));
        verify(receiverRepository, times(0)).save(any(Receiver.class));
        verify(packageRepository, times(0)).save(any(Package.class));
        verify(serviceRepository, times(0)).findById(anyLong());
        verify(deliveryRepository, times(0)).save(any(Delivery.class));
    }

    @Test
    void testCreateWithPackageServiceNotFound() {
        CreatePackageRequest packageRequest = DummyPackage.newPackageRequest();
        CreateDeliveryPackageRequest request = DummyDelivery.newDeliveryRequest(
                packageRequest, DummyService.SERVICE_ID);

        when(locationRepository.findFirstByCodeloc(anyString())).thenReturn(Optional.of(new Location()));
        when(serviceRepository.findById(anyLong())).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            deliveryService.createWithPackage(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Service not found", exception.getReason());

        verify(locationRepository, times(1)).findFirstByCodeloc(anyString());
        verify(senderRepository, times(0)).save(any(Sender.class));
        verify(receiverRepository, times(0)).save(any(Receiver.class));
        verify(packageRepository, times(0)).save(any(Package.class));
        verify(serviceRepository, times(1)).findById(anyLong());
        verify(deliveryRepository, times(0)).save(any(Delivery.class));
    }

    @Test
    void testUpdateCheckpointLocationSuccess() {
        Delivery deliveryEntity = DummyDelivery.newDelivery();

        Location locationEntity = DummyLocation.newLocation();

        UpdateCheckpointRequest request = new UpdateCheckpointRequest();
        request.setDeliveryId(DummyDelivery.DELIVERY_ID);
        request.setCodeLocation(DummyLocation.CODE_LOCATION);

        when(deliveryRepository.findById(DummyDelivery.DELIVERY_ID)).thenReturn(Optional.of(deliveryEntity));
        when(locationRepository.findFirstByCodeloc(DummyLocation.CODE_LOCATION)).thenReturn(Optional.of(locationEntity));
        when(deliveryRepository.save(any(Delivery.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Delivery updatedDelivery = deliveryService.updateCheckpointLocation(request);

        verify(deliveryRepository, times(1)).findById(DummyDelivery.DELIVERY_ID);
        verify(locationRepository, times(1)).findFirstByCodeloc(DummyLocation.CODE_LOCATION);
        verify(deliveryRepository, times(1)).save(any(Delivery.class));

        assertNotNull(updatedDelivery);
        assertEquals(1, updatedDelivery.getCheckpointDelivery().size());
        assertTrue(updatedDelivery.getIsReceived());
        assertEquals(locationEntity, updatedDelivery.getCheckpointDelivery().get(0));
    }

    @Test
    void testUpdateCheckpointLocationDeliveryNotFound() {
        UpdateCheckpointRequest request = new UpdateCheckpointRequest();
        request.setDeliveryId(DummyDelivery.DELIVERY_ID);
        request.setCodeLocation(DummyLocation.CODE_LOCATION);

        when(deliveryRepository.findById(DummyDelivery.DELIVERY_ID)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            deliveryService.updateCheckpointLocation(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Delivery not found", exception.getReason());

        verify(deliveryRepository, times(1)).findById(DummyDelivery.DELIVERY_ID);
        verify(locationRepository, never()).findFirstByCodeloc(anyString());
        verify(deliveryRepository, never()).save(any(Delivery.class));
    }

    @Test
    void testUpdateCheckpointLocationLocationNotFound() {
        Delivery deliveryEntity = new Delivery();
        deliveryEntity.setId(DummyDelivery.DELIVERY_ID);

        UpdateCheckpointRequest request = new UpdateCheckpointRequest();
        request.setDeliveryId(DummyDelivery.DELIVERY_ID);
        request.setCodeLocation(DummyLocation.CODE_LOCATION);

        when(deliveryRepository.findById(DummyDelivery.DELIVERY_ID)).thenReturn(Optional.of(deliveryEntity));
        when(locationRepository.findFirstByCodeloc(DummyLocation.CODE_LOCATION)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            deliveryService.updateCheckpointLocation(request);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Location not found", exception.getReason());

        verify(deliveryRepository, times(1)).findById(DummyDelivery.DELIVERY_ID);
        verify(locationRepository, times(1)).findFirstByCodeloc(DummyLocation.CODE_LOCATION);
        verify(deliveryRepository, never()).save(any(Delivery.class));
    }


    @Test
    void testGetAllDeliveries() {

        Delivery delivery1 = DummyDelivery.newDelivery();
        Delivery delivery2 = DummyDelivery.newDelivery();

        List<Delivery> deliveries = List.of(delivery1, delivery2);
        Page<Delivery> deliveryPage = new PageImpl<>(deliveries);

        when(deliveryRepository.findAll(any(Pageable.class))).thenReturn(deliveryPage);

        Page<Delivery> result = deliveryService.getAll(0, 10);

        verify(deliveryRepository, times(1)).findAll(any(Pageable.class));

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(1, result.getTotalPages());
        assertEquals(deliveries, result.getContent());
    }

    @Test
    public void testGetAllWithFilter() {
        PackageFilter filter = PackageFilter
                .builder()
                .locationName(DummyLocation.NAME_LOCATION)
                .isReceived(DummyDelivery.DELIVERY_IS_RECEIVED)
                .build();
        Pageable pageable = PageRequest.of(0, 10);

        Delivery delivery1 = DummyDelivery.newDelivery();
        Delivery delivery2 = DummyDelivery.newDelivery();
        List<Delivery> deliveries = List.of(delivery1, delivery2);

        Page<Delivery> deliveryPage = new PageImpl<>(deliveries);
        when(deliveryRepository.findDeliveriesByLocationNameAndIsReceived(filter.getLocationName(), filter.getIsReceived(), pageable))
                .thenReturn(deliveryPage);

        Page<Delivery> result = deliveryService.getAllWithFilter(filter, 0, 10);

         assertEquals(deliveries.size(), result.getContent().size());
         verify(deliveryRepository, times(1))
                 .findDeliveriesByLocationNameAndIsReceived(
                         filter.getLocationName(),
                         filter.getIsReceived(),
                         pageable);
    }

    @Test
    void testFindById() {
        Long deliveryId = DummyDelivery.DELIVERY_ID;;
        Delivery delivery = DummyDelivery.newDelivery();
        delivery.setId(deliveryId);

        when(deliveryRepository.findById(deliveryId)).thenReturn(Optional.of(delivery));

        Delivery result = deliveryService.findById(deliveryId);

        verify(deliveryRepository, times(1)).findById(deliveryId);

        assertNotNull(result);
        assertEquals(delivery, result);
    }

    @Test
    void testFindByIdNotFound() {
        Long deliveryId = DummyDelivery.DELIVERY_ID;

        when(deliveryRepository.findById(deliveryId)).thenReturn(Optional.empty());

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            deliveryService.findById(deliveryId);
        });

        verify(deliveryRepository, times(1)).findById(deliveryId);

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        assertEquals("Delivery not found", exception.getReason());
    }

}

