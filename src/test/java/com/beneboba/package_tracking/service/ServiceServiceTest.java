package com.beneboba.package_tracking.service;


import com.beneboba.package_tracking.entity.Service;
import com.beneboba.package_tracking.helper.dummy.DummyService;
import com.beneboba.package_tracking.model.request.ServiceRequest;
import com.beneboba.package_tracking.repository.ServiceRepository;
import com.beneboba.package_tracking.service.impl.ServiceServiceImpl;
import com.beneboba.package_tracking.util.ValidationService;
import jakarta.validation.Valid;
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
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class ServiceServiceTest {

    @Mock
    private ServiceRepository serviceRepository;

    @Mock
    private ValidationService validationService;

    @InjectMocks
    private ServiceServiceImpl serviceService ;


    @Test
    void testCreate() {
        ServiceRequest serviceRequest = DummyService.newServiceRequest();
        Service serviceEntity = serviceRequest.toEntity();

        when(serviceRepository.save(any(Service.class))).thenReturn(serviceEntity);

        Service service = serviceService.create(serviceRequest);

        verify(serviceRepository, times(1)).save(any(Service.class));
        assertEquals(service, serviceRequest.toEntity());
    }

    @Test
    void testFindAll(){
        Service serviceEntity = DummyService.newService();
        Page<Service> serviceList = new PageImpl<>(List.of(serviceEntity));

        when(serviceRepository.findAll(any(PageRequest.class))).thenReturn(serviceList);

        Page<Service> services = serviceService.getAll(null,0,10);

        verify(serviceRepository, times(1)).findAll(PageRequest.of(0,10));
        assertEquals(services.getTotalElements(), 1);
    }

    @Test
    void testFindAllWithQueryName(){
        Service serviceEntity = DummyService.newService();
        Page<Service> serviceList = new PageImpl<>(List.of(serviceEntity));

        when(serviceRepository.findByName(anyString(),any(PageRequest.class))).thenReturn(serviceList);

        Page<Service> services = serviceService.getAll("test",0,10);

        verify(serviceRepository, times(1)).findByName("test",PageRequest.of(0,10));
        verify(serviceRepository, times(0)).findAll(PageRequest.of(0,10));
        assertEquals(services.getTotalElements(), 1);
    }
}
