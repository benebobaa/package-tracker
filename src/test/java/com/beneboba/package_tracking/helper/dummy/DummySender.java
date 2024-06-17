package com.beneboba.package_tracking.helper.dummy;

import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.entity.Sender;
import com.beneboba.package_tracking.model.request.LocationRequest;
import com.beneboba.package_tracking.model.request.SenderRequest;

public class DummySender {

    final public static String SENDER_NAME = "bene";

    final public static String SENDER_PHONE = "08572983212";


    public static Sender newSender(){
        return new Sender(
                1L,
                SENDER_NAME,
                SENDER_PHONE
        );
    }

    public static SenderRequest newSenderRequest(){
        return SenderRequest
                .builder()
                .name(SENDER_NAME)
                .phone(SENDER_PHONE)
                .build();
    }
}
