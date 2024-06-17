package com.beneboba.package_tracking.model.request;

import com.beneboba.package_tracking.entity.Delivery;
import com.beneboba.package_tracking.entity.Package;
import com.beneboba.package_tracking.entity.Service;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreateDeliveryPackageRequest {

    @Valid
    @NotNull
    private CreatePackageRequest packageRequest;

    @NotNull
    private Long serviceId;

    public Delivery toEntity(Service service, Package pacakge, float deliveryPrice, Boolean isReceived){
        return new Delivery(null, deliveryPrice, service, pacakge, null, isReceived);
    }
}
