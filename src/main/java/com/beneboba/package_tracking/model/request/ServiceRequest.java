package com.beneboba.package_tracking.model.request;

import com.beneboba.package_tracking.entity.Service;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ServiceRequest {

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    @Min(1)
    private float priceKg;

    public Service toEntity(){
        return new Service(null, name, priceKg);
    }
}
