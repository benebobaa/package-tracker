package com.beneboba.package_tracking.helper.dummy;

import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.model.request.LocationRequest;

public class DummyLocation {

    public final static Long LOCATION_ID = 1L;
    public final static String CODE_LOCATION = "gdng-000";
    public final static String NAME_LOCATION = "Menteng Dalam";
    public final static String ADDRESS_LOCATION = "Jalan Kaki NO 88";

    public static Location newLocation(){
        return new Location(
                LOCATION_ID,
                CODE_LOCATION,
                NAME_LOCATION,
                ADDRESS_LOCATION
        );
    }

    public static Location newLocation(
            Long id,
            String codeLocation,
            String name
            ){
        return new Location(
                id,
                codeLocation,
                name,
                ADDRESS_LOCATION
        );
    }

    public static LocationRequest newLocationRequest(){
        return LocationRequest
                .builder()
                .name(NAME_LOCATION)
                .address(ADDRESS_LOCATION)
                .build();
    }
}
