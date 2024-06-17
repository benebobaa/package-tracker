package com.beneboba.package_tracking.website.home;

import com.beneboba.package_tracking.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/home")
@Slf4j
public class WebHomeController {

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

    @GetMapping("/location")
    public String locationContent(Model model) {
        model.addAttribute("location", "Hello Location");

        return "home/component/location :: location";
    }

    @GetMapping("/service")
    public String serviceContent(Model model) {
        model.addAttribute("service", "Hello Service");

        return "home/component/service :: service";
    }
}
