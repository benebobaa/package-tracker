package com.beneboba.package_tracking.model.request;

import com.beneboba.package_tracking.entity.Location;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class LocationRequest {

    @NotBlank
    @Size(min = 1, max = 150)
    private String name;

    @NotBlank
    @Size(min = 1, max = 255)
    private String address;

    public Location toEntity(String codeLocation){
        return new Location(null,codeLocation, name, address);
    }
}
