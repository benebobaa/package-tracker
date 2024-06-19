package com.beneboba.package_tracking.util;

import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.entity.Service;
import com.beneboba.package_tracking.entity.User;
import com.beneboba.package_tracking.entity.UserRole;
import com.beneboba.package_tracking.model.request.*;
import com.beneboba.package_tracking.model.response.LoginResponse;
import com.beneboba.package_tracking.model.response.UserRegisterResponse;
import com.beneboba.package_tracking.repository.UserRepository;
import com.beneboba.package_tracking.service.AuthService;
import com.beneboba.package_tracking.service.DeliveryService;
import com.beneboba.package_tracking.service.LocationService;
import com.beneboba.package_tracking.service.ServiceService;
import lombok.extern.slf4j.Slf4j;

import java.util.Optional;

@Slf4j
public class Generator {

    private static Long serviceMinId;
    private static Long serviceMaxId;

    private static Long locationMinId;
    private static Long locationMaxId;

    public static String sequenceCodeLocation(Long id) {
        return String.format("gdng-%05d", id);
    }

    public static String randomCodeLocation(Long min, Long max){
        return sequenceCodeLocation(randomId(min, max));
    }

    public static String randomServiceName() {
        String[] services = {"JNE", "JNT", "POS", "TIKI", "SICEPAT"};
        return services[(int) (Math.random() * services.length)];
    }

    public static String randomName(){
        String[] names = {"Ariel", "Niko", "Qowi", "Ahmad", "Bene", "Boba", "Didi", "Agung"};
        return names[(int) (Math.random() * names.length)];
    }

    public static String randomPhone(){
        String[] phoneFormat = {"+62", "08"};
        return phoneFormat[(int) (Math.random() * phoneFormat.length)] + (int) (Math.random() * 1000000000L);
    }

    public static Long randomId(Long min, Long max){
        return (long) (Math.random() * (max - min) + min);
    }

    public static float randomPrice(){
        return (float) (Math.random() * 10000);
    }

    public static float randomWeight(){
        return (float) ((Math.random() * 200 ) + 1);
    }

    public static String randomLocationName(){
        String[] names = {"Jakarta", "Bandung", "Surabaya", "Semarang", "Yogyakarta"};
        return names[(int) (Math.random() * names.length)];
    }

    public static String randomAddress(){
        String[] addresses = {
                "Jl. Sudirman", "Jl. Asia Afrika",
                "Jl. Pahlawan", "Jl. Merdeka", "Jl. Diponegoro"};
        return addresses[(int) (Math.random() * addresses.length)];
    }

    // Service Generator
    public static void generateService(ServiceService service, Integer loop){
        generateService(service, loop, false);
    }

    public static void generateService(ServiceService service, Integer loop, Boolean showLog){

        log.info("Generate Service -> Start generating {} services", loop);

        for (int i = 0; i < loop; i++) {
            ServiceRequest serviceRequest = ServiceRequest
                    .builder()
                    .name(randomServiceName())
                    .priceKg(randomPrice())
                    .build();

            if (i == 0) {
                serviceMinId = service.create(serviceRequest).getId();
            } else if (i == loop - 1) {
                serviceMaxId = service.create(serviceRequest).getId();
            } else {
                service.create(serviceRequest);
            }

            if (showLog){
                log.info("Generate Service -> {}", serviceRequest);
            }
        }

        log.info("Generate Service -> Success generating {} services", loop);
    }

    // Location Generator
    public static void generateLocation(LocationService service, Integer loop){
        generateLocation(service, loop, false);
    }

    public static void generateLocation(LocationService service, Integer loop, Boolean showLog){

        log.info("Generate Location -> Start generating {} locations", loop);

        for (int i = 0; i < loop; i++) {
            LocationRequest locationRequest = LocationRequest.builder()
                    .name(randomLocationName())
                    .address(randomAddress())
                    .build();
            service.create(locationRequest);

            if (i == 0) {
                locationMinId = service.create(locationRequest).getId();
            } else if (i == loop - 1) {
                locationMaxId = service.create(locationRequest).getId();
            } else {
                service.create(locationRequest);
            }

            if (showLog){
                log.info("Generate Location -> {}", locationRequest);
            }
        }

        log.info("Generate Location -> Success generating {} locations", loop);
    }

    // Package Delivery Generator
    public static void generateDelivery(DeliveryService service, Integer loop){
        generateDelivery(service, loop);
    }

    public static void generateDelivery(DeliveryService service, Integer loop, Boolean showLog){

        log.info("Generate Delivery -> Start generating {} delivery", loop);


        for (int i = 0; i < loop; i++) {
            ReceiverRequest receiverRequest = ReceiverRequest
                    .builder()
                    .name(randomName())
                    .phone(randomPhone())
                    .build();

            SenderRequest senderRequest = SenderRequest
                    .builder()
                    .name(randomName())
                    .phone(randomPhone())
                    .build();


            CreatePackageRequest packageRequest = CreatePackageRequest
                    .builder()
                    .weight(randomWeight())
                    .sender(senderRequest)
                    .receiver(receiverRequest)
                    .codeLocation(randomCodeLocation(locationMinId,locationMaxId))
                    .build();

            CreateDeliveryPackageRequest deliveryPackageRequest = CreateDeliveryPackageRequest
                    .builder()
                    .packageRequest(packageRequest)
                    .serviceId(randomId(serviceMinId, serviceMaxId))
                    .build();

            service.createWithPackage(deliveryPackageRequest);

            if (showLog){
                log.info("Generate Delivery -> {}", deliveryPackageRequest);
            }
        }

        log.info("Generate Delivery -> Success generating {} delivery", loop);
    }

    // User Admin Generator
    public static void generateUser(
            AuthService service,
            UserRepository userRepository,
            UserRegisterRequest request
    ){
        Optional<User> user = userRepository.findUserByUsername(request.getUsername());

        if (user.isPresent()){
            log.info("CmdLineRunner -> User exists -> {}", user.get());
            return;
        }

        request.setConfirmPassword(request.getPassword());

        UserRegisterResponse createdUser = service.register(request);

        log.info("CmdLineRunner -> Generated User -> {}", createdUser);
    }
}
