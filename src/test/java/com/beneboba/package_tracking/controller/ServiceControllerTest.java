package com.beneboba.package_tracking.controller;

import com.beneboba.package_tracking.entity.Service;
import com.beneboba.package_tracking.helper.JsonReader;
import com.beneboba.package_tracking.helper.dummy.DummyService;
import com.beneboba.package_tracking.model.BaseResponse;
import com.beneboba.package_tracking.model.request.ServiceRequest;
import com.beneboba.package_tracking.service.ServiceService;
import com.beneboba.package_tracking.util.JwtService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestTemplate;
import org.mockito.ArgumentMatchers;
import org.skyscreamer.jsonassert.JSONAssert;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.util.StreamUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class ServiceControllerTest {

    @Autowired
    private ServiceController serviceController;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    ObjectMapper objectMapper;

    @MockBean
    private ServiceService serviceService;

    @Test
    @WithMockUser
    void testGetAll() throws Exception {
        Service service1 = DummyService.newService();

        Service service2 = DummyService.newService();

        Page<Service> services = new PageImpl<>(Arrays.asList(service1, service2));

        when(serviceService.getAll(anyString(),anyInt(),anyInt())).thenReturn(services);

        mockMvc.perform(
                        MockMvcRequestBuilders.get("/api/services")
                                .param("queryName", "")
                                .accept(MediaType.APPLICATION_JSON)
                                .contentType(MediaType.APPLICATION_JSON)
                )
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON));

        verify(serviceService, times(1)).getAll("",0, 10);
    }

    @Test
    @WithMockUser(roles = {"ADMIN"})
    public void testCreateService() throws Exception {
        String requestJson = JsonReader.read("json/service_request.json");

        ServiceRequest serviceRequest = objectMapper.readValue(requestJson, ServiceRequest.class);
        Service createdService = serviceRequest.toEntity();
        createdService.setId(1L);

        when(serviceService.create(ArgumentMatchers.any(ServiceRequest.class)))
                .thenReturn(createdService);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/services")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {

                    String expectedJson = JsonReader.read("json/service_response.json");
                    String actualJson = result.getResponse().getContentAsString();

                    JSONAssert.assertEquals(expectedJson, actualJson, true);
                });

        verify(serviceService, times(1)).create(serviceRequest);
    }

    @Test
    @WithMockUser
    public void testGetAllServices() throws Exception {
        Service service1 = DummyService.newService(1L, "JNT", 1);
        Service service2 = DummyService.newService(2L, "SICEPAT", 2);
        Service service3 = DummyService.newService(3L, "PhinExpress", 3);

        List<Service> serviceList = List.of(service1, service2, service3);

        Page<Service> page = new PageImpl<>(serviceList, PageRequest.of(0, 10), 3);

        when(serviceService.getAll(anyString(),anyInt(), anyInt()))
                .thenReturn(page);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/services")
                        .param("page", "0")
                        .param("size", "10")
                        .param("queryName", "")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {

                    String expectedJson = JsonReader.read("json/service_getall_response.json");
                    String actualJson = result.getResponse().getContentAsString();

                    JSONAssert.assertEquals(expectedJson, actualJson, true);
                });

        verify(serviceService, times(1)).getAll("",0, 10);
    }
}
