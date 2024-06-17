package com.beneboba.package_tracking.model.request;

import com.beneboba.package_tracking.entity.Sender;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SenderRequest {

    @NotBlank
    @Size(min = 1, max = 255)
    private String name;

    @NotBlank
    @Pattern(regexp = "^(\\+62|0)\\d{7,14}$", message = "Phone number must start with +62 or 0 and followed by 7 to 14 digits")
    private String phone;

    public Sender toEntity() {
        return new Sender(null,name, phone);
    }
}
