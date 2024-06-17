package com.beneboba.package_tracking.model.request;


import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.entity.Package;
import com.beneboba.package_tracking.entity.Receiver;
import com.beneboba.package_tracking.entity.Sender;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class CreatePackageRequest {

    @Valid
    @NotNull
    private SenderRequest sender;

    @Valid
    @NotNull
    private ReceiverRequest receiver;

    @NotBlank
    private String codeLocation;

    @Min(1)
    private float weight;

    public Package toEntity(Sender sender, Receiver receiver, Location location){
        return new Package(null, weight, sender, receiver, location);
    }
}
