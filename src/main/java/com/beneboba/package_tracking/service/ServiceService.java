package com.beneboba.package_tracking.service;

import com.beneboba.package_tracking.entity.Service;
import com.beneboba.package_tracking.model.request.ServiceRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ServiceService{

    Service create(ServiceRequest serviceRequest);

    Page<Service> getAll(String queryName,int page, int size);

}
