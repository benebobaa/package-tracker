package com.beneboba.package_tracking.helper.dummy;

import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.entity.Package;
import com.beneboba.package_tracking.entity.Receiver;
import com.beneboba.package_tracking.entity.Sender;
import com.beneboba.package_tracking.model.request.CreatePackageRequest;
import com.beneboba.package_tracking.model.request.ReceiverRequest;
import com.beneboba.package_tracking.model.request.SenderRequest;

import static com.beneboba.package_tracking.helper.dummy.DummySender.SENDER_PHONE;

public class DummyPackage {

//    final public static String CODE_LOCATION ="gdng-0000";
    final public static float WEIGHT =  10;

    public static Package newPackage(
            Sender sender,
            Receiver receiver,
            Location location
    ){
        return new Package(
                1L,
                WEIGHT,
                sender,
                receiver,
                location
        );
    }

    public static Package newPackage(
    ){
        return new Package(
                1L,
                WEIGHT,
                DummySender.newSender(),
                DummyReceiver.newReceiver(),
                DummyLocation.newLocation()
        );
    }

    public static CreatePackageRequest newPackageRequest(
            SenderRequest senderRequest,
            ReceiverRequest receiverRequest,
            String codeLocation
    ){
        return CreatePackageRequest
                .builder()
                .weight(WEIGHT)
                .sender(senderRequest)
                .receiver(receiverRequest)
                .codeLocation(codeLocation)
                .build();
    }

    public static CreatePackageRequest newPackageRequest(){
        return CreatePackageRequest
                .builder()
                .weight(WEIGHT)
                .sender(DummySender.newSenderRequest())
                .receiver(DummyReceiver.newReceiverRequest())
                .codeLocation(DummyLocation.CODE_LOCATION)
                .build();
    }
}
