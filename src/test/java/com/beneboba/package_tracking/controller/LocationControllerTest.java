package com.beneboba.package_tracking.controller;

import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.helper.JsonReader;
import com.beneboba.package_tracking.helper.dummy.DummyLocation;
import com.beneboba.package_tracking.model.request.LocationRequest;
import com.beneboba.package_tracking.model.request.ServiceRequest;
import com.beneboba.package_tracking.service.LocationService;
import com.beneboba.package_tracking.service.ServiceService;
import com.beneboba.package_tracking.util.Generator;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class LocationControllerTest {

    @Autowired
    private LocationController locationController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private LocationService locationService;

    @Test
    @WithMockUser(roles = {"ADMIN"})
    void testCreate() throws Exception {
        String requestJson = JsonReader.read("json/location_request.json");

        LocationRequest locationRequest = objectMapper.readValue(requestJson, LocationRequest.class);
        Location createdLocation = locationRequest.toEntity(Generator.sequenceCodeLocation(1L));
        createdLocation.setId(1L);

        when(locationService.create(any(LocationRequest.class))).thenReturn(createdLocation);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/locations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {

                    String expectedJson = JsonReader.read("json/location_response.json");
                    String actualJson = result.getResponse().getContentAsString();

                    JSONAssert.assertEquals(expectedJson, actualJson, true);
                });

        verify(locationService, times(1)).create(any(LocationRequest.class));
    }


    @Test
    @WithMockUser
    void testGetAll() throws Exception {
        Location location1 = DummyLocation.newLocation(1L, "gdng-001", "Jakarta 1");
        Location location2 = DummyLocation.newLocation(2L, "gdng-002", "Jakarta 2");

        List<Location> locationList = List.of(location1, location2);
        Page<Location> mockPage = new PageImpl<>(locationList, PageRequest.of(0, 10), 2);

        when(locationService.getAll(anyInt(), anyInt())).thenReturn(mockPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/locations")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {

                    String expectedJson = JsonReader.read("json/location_getall_response.json");
                    String actualJson = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(expectedJson, actualJson, true);

                });

        verify(locationService, times(1)).getAll(0,10);
    }
}
