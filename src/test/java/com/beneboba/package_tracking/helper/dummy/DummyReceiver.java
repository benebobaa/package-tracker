package com.beneboba.package_tracking.helper.dummy;

import com.beneboba.package_tracking.entity.Receiver;
import com.beneboba.package_tracking.entity.Sender;
import com.beneboba.package_tracking.model.request.ReceiverRequest;
import com.beneboba.package_tracking.model.request.SenderRequest;

public class DummyReceiver {

    final public static String RECEIVER_NAME = "bono";

    final public static String RECEIVER_PHONE = "0857111111";

    public static Receiver newReceiver(){
        return new Receiver(
                1L,
                RECEIVER_NAME,
                RECEIVER_PHONE
        );
    }

    public static ReceiverRequest newReceiverRequest(){
        return ReceiverRequest
                .builder()
                .name(RECEIVER_NAME)
                .phone(RECEIVER_PHONE)
                .build();
    }

}
