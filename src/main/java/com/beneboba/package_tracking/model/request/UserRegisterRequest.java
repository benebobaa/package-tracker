package com.beneboba.package_tracking.model.request;

import com.beneboba.package_tracking.entity.User;
import com.beneboba.package_tracking.entity.UserRole;
import com.beneboba.package_tracking.util.constraint.CheckPassword;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@CheckPassword
@Builder
public class UserRegisterRequest {

    @NotBlank
    @Size(min = 1, max = 100)
    private String name;

    @NotBlank
    @Size(min = 6, max = 50)
    private String username;

    @NotBlank
    @Size(min = 6, max = 25)
    private String password;

    @NotBlank
    @Size(min = 6, max = 25)
    private String confirmPassword;

    @JsonIgnore
    private UserRole role;

    public User toUserEntity(){
        return new User(null, name, username, password, role);
    }
}
