package com.beneboba.package_tracking.model.response;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserRegisterResponse {
    private Long id;
    private String name;
    private String username;
}
