package com.beneboba.package_tracking.controller;

import com.beneboba.package_tracking.entity.Package;
import com.beneboba.package_tracking.helper.JsonReader;
import com.beneboba.package_tracking.helper.dummy.DummyPackage;
import com.beneboba.package_tracking.service.PackageService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
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
import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PackageControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private PackageService packageService;

    @Test
    @WithMockUser
    void testGetAll() throws Exception {
        Package package1 = DummyPackage.newPackage();
        package1.setId(1L);

        Package package2 = DummyPackage.newPackage();
        package2.setId(2L);

        List<Package> packageList = List.of(package1, package2);

        Page<Package> mockPage = new PageImpl<>(packageList,
                PageRequest.of(0, 10), 2);

        when(packageService.getAll(anyInt(), anyInt())).thenReturn(mockPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/packages")
                        .param("page", "0")
                        .param("size", "10")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(result -> {

                    String expectedJson = JsonReader.read("json/package_getall_response.json");
                    String actualJson = result.getResponse().getContentAsString();
                    JSONAssert.assertEquals(expectedJson, actualJson, true);

                });

        verify(packageService, times(1)).getAll(0,10);
    }
}
