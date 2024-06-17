package com.beneboba.package_tracking.model.request;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PackageFilter {

    private Boolean isReceived;

    private String locationName;
}
