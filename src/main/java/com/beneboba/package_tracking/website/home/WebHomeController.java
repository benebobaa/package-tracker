package com.beneboba.package_tracking.website.home;

import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.entity.Service;
import com.beneboba.package_tracking.entity.User;
import com.beneboba.package_tracking.service.LocationService;
import com.beneboba.package_tracking.service.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/home")
@Slf4j
public class WebHomeController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public String homePage(
            Model model
    ) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        log.info("homePage -> currentUser :: {}", currentUser);

        model.addAttribute("user", currentUser);

        return "home/home";
    }


    @GetMapping("/delivery")
    public String deliveryContent(Model model) {
        model.addAttribute("delivery", "Hello Delivery");

        return "home/component/delivery :: delivery";
    }
}
