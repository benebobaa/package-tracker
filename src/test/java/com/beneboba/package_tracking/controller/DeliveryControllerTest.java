package com.beneboba.package_tracking.controller;

import com.beneboba.package_tracking.entity.*;
import com.beneboba.package_tracking.entity.Package;
import com.beneboba.package_tracking.helper.dummy.*;
import com.beneboba.package_tracking.model.request.*;
import com.beneboba.package_tracking.service.DeliveryService;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static com.beneboba.package_tracking.helper.dummy.DummyService.SERVICE_ID;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Slf4j
public class DeliveryControllerTest {

    @Autowired
    private DeliveryController deliveryController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private DeliveryService deliveryService;

    @Test
    @WithMockUser
    void testCreateDeliveryWithPackageSuccess() throws Exception {
        // Mock data
        Location locationEntity = DummyLocation.newLocation();
        Service serviceEntity = DummyService.newService();

        SenderRequest senderRequest = DummySender.newSenderRequest();

        ReceiverRequest receiverRequest = DummyReceiver.newReceiverRequest();

        CreatePackageRequest packageRequest = DummyPackage.newPackageRequest(
                senderRequest,
                receiverRequest,
                DummyLocation.CODE_LOCATION
        );

        CreateDeliveryPackageRequest request = DummyDelivery.newDeliveryRequest(
                packageRequest,
                SERVICE_ID
        );

        Sender senderEntity = senderRequest.toEntity();

        Receiver receiverEntity = receiverRequest.toEntity();

        Package packageEntity = packageRequest.toEntity(senderEntity, receiverEntity, locationEntity);

        Delivery response = request.toEntity(
                serviceEntity,
                packageEntity,
                DummyDelivery.DELIVERY_PRICE,
                DummyDelivery.DELIVERY_IS_RECEIVED
        );

        // Mocking DeliveryService behavior
        when(deliveryService.createWithPackage(any(CreateDeliveryPackageRequest.class))).thenReturn(response);

        mockMvc.perform(
                        MockMvcRequestBuilders.post("/api/deliveries")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(response.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.packageItem").value(response.getPackageItem()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.priceDelivery").value(response.getPriceDelivery()));

        verify(deliveryService, times(1)).createWithPackage(any(CreateDeliveryPackageRequest.class));
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testUpdateDeliveryCheckpointSuccess() throws Exception {
        // Mock data
        UpdateCheckpointRequest request = new UpdateCheckpointRequest();
        request.setDeliveryId(DummyDelivery.DELIVERY_ID);
        request.setCodeLocation(DummyLocation.CODE_LOCATION);

        Location location = DummyLocation.newLocation();

        Delivery response = DummyDelivery.newDelivery(List.of(location));

        log.info("checkpointDelivery -> {}", response.getCheckpointDelivery());

        // Mocking DeliveryService behavior
        when(deliveryService.updateCheckpointLocation(any(UpdateCheckpointRequest.class))).thenReturn(response);

        mockMvc.perform(
                        MockMvcRequestBuilders.patch("/api/deliveries")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(request))
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.id").value(response.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.checkpointDelivery[0].codeloc").value(location.getCodeloc()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.priceDelivery").value(response.getPriceDelivery()));

        verify(deliveryService, times(1)).updateCheckpointLocation(any(UpdateCheckpointRequest.class));
    }

    @Test
    @WithMockUser
    public void testGetAll() throws Exception {
        // Mock data
        List<Delivery> deliveries = new ArrayList<>();
        deliveries.add(DummyDelivery.newDelivery());
        deliveries.add(DummyDelivery.newDelivery());
        Page<Delivery> page = new PageImpl<>(deliveries);

        // Mock service method
        when(deliveryService.getAll(anyInt(), anyInt())).thenReturn(page);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/deliveries")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        verify(deliveryService, times(1)).getAll(anyInt(), anyInt());
    }

    @Test
    @WithMockUser
    public void testGetAllWithFilter() throws Exception {
        // Mock data
        List<Delivery> deliveries = new ArrayList<>();
        deliveries.add(DummyDelivery.newDelivery());
        deliveries.add(DummyDelivery.newDelivery());
        Page<Delivery> page = new PageImpl<>(deliveries);

        // Mock service method
        when(deliveryService.getAllWithFilter(
                        ArgumentMatchers.any(PackageFilter.class),
                        ArgumentMatchers.anyInt(),
                        ArgumentMatchers.anyInt()))
                .thenReturn(page);

        // Perform GET request
        mockMvc.perform(MockMvcRequestBuilders.get("/api/deliveries/filter")
                        .param("locationName", "Bandung")
                        .param("isReceived", "true")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        verify(deliveryService, times(1)).getAllWithFilter(any(PackageFilter.class), anyInt(), anyInt());
    }
}
