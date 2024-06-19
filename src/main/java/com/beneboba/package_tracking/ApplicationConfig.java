package com.beneboba.package_tracking;

import com.beneboba.package_tracking.entity.UserRole;
import com.beneboba.package_tracking.model.request.UserRegisterRequest;
import com.beneboba.package_tracking.repository.UserRepository;
import com.beneboba.package_tracking.service.AuthService;
import com.beneboba.package_tracking.service.DeliveryService;
import com.beneboba.package_tracking.service.LocationService;
import com.beneboba.package_tracking.service.ServiceService;
import com.beneboba.package_tracking.util.Generator;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class ApplicationConfig {

    @Bean
    UserDetailsService userDetailsService(
            UserRepository userRepository
    ) {
        return username -> userRepository.findUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
    }

    @Bean
    BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    AuthenticationProvider authenticationProvider(
            UserRepository userRepository
    ) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();

        authProvider.setUserDetailsService(userDetailsService(userRepository));
        authProvider.setPasswordEncoder(passwordEncoder());

        return authProvider;
    }

//    @Bean
//	public CommandLineRunner commandLineRunner(
//            LocationService locationService,
//            ServiceService serviceService,
//            DeliveryService deliveryService,
//            AuthService authService,
//            UserRepository userRepository
//    ) {
//		return args -> {
//			// Generate Location Data
//			Generator.generateLocation(locationService, 50, true);
//
//			// Generate Service Data
//			Generator.generateService(serviceService, 15, true);
//
//			// Generate Delivery Data
//			Generator.generateDelivery(deliveryService, 30, true);
//
//			// Generate user admin
//            UserRegisterRequest userRequest1 = UserRegisterRequest
//                    .builder()
//                    .name("Phincon Admin")
//                    .username("admin123")
//                    .password("admin123")
//                    .role(UserRole.ADMIN)
//                    .build();
//
//			Generator.generateUser(authService, userRepository, userRequest1);
//
//            UserRegisterRequest userRequest2 = UserRegisterRequest
//                    .builder()
//                    .name("Bene Spring")
//                    .username("beneboba")
//                    .password("beneboba")
//                    .role(UserRole.USER)
//                    .build();
//
//            Generator.generateUser(authService, userRepository, userRequest2);
//        };
//	}
}
