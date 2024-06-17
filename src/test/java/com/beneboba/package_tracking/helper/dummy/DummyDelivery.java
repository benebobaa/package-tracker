package com.beneboba.package_tracking.helper.dummy;

import com.beneboba.package_tracking.entity.*;
import com.beneboba.package_tracking.entity.Package;
import com.beneboba.package_tracking.model.request.CreateDeliveryPackageRequest;
import com.beneboba.package_tracking.model.request.CreatePackageRequest;

import java.util.ArrayList;
import java.util.List;

public class DummyDelivery {

    public static Long DELIVERY_ID = 1L;
    public static float DELIVERY_PRICE = 20000;
    public static Boolean DELIVERY_IS_RECEIVED = false;
    public static List<Location> DELIVERY_CHECKPOINTS = new ArrayList<>();

    public static Delivery newDelivery(
            float priceDelivery,
            Service service,
            Package packageItem,
            List<Location> checkpointDelivery,
            Boolean isReceived
    ){
        return new Delivery(
                1L,
                priceDelivery,
                service,
                packageItem,
                checkpointDelivery,
                isReceived
        );
    }

    public static Delivery newDelivery(
    ){
        return new Delivery(
                1L,
                DELIVERY_PRICE,
                DummyService.newService(),
                DummyPackage.newPackage(),
                DELIVERY_CHECKPOINTS,
                DELIVERY_IS_RECEIVED
        );
    }

    public static Delivery newDelivery(
            List<Location> checkpointDelivery
    ){
        return new Delivery(
                1L,
                DELIVERY_PRICE,
                DummyService.newService(),
                DummyPackage.newPackage(),
                checkpointDelivery,
                DELIVERY_IS_RECEIVED
        );
    }

    public static CreateDeliveryPackageRequest newDeliveryRequest(
            CreatePackageRequest packageRequest,
            Long serviceId
    ){
        return CreateDeliveryPackageRequest
                .builder()
                .packageRequest(packageRequest)
                .serviceId(serviceId)
                .build();
    }
}
