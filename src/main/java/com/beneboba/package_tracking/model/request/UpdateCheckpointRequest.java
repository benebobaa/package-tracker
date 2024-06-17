package com.beneboba.package_tracking.model.request;

import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
public class UpdateCheckpointRequest {

    @NotNull
    private Long deliveryId;

    @NotNull
    private String codeLocation;
}
