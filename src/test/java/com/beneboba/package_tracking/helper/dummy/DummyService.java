package com.beneboba.package_tracking.helper.dummy;

import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.entity.Service;
import com.beneboba.package_tracking.model.request.ServiceRequest;

public class DummyService {
    final public static Long SERVICE_ID = 1L;
    final public static String SERVICE_NAME = "Express Lemot";
    final public static float SERVICE_PRICE_KG = 2000;

    public static Service newService(){
        return new Service(
                SERVICE_ID,
                SERVICE_NAME,
                SERVICE_PRICE_KG
        );
    }

    public static Service newService(
          Long id,
          String name,
          float price
    ){
        return new Service(
                id,
                name,
                price
        );
    }

    public static ServiceRequest newServiceRequest(){
        return ServiceRequest
                .builder()
                .name(SERVICE_NAME)
                .priceKg(SERVICE_PRICE_KG)
                .build();
    }
}
