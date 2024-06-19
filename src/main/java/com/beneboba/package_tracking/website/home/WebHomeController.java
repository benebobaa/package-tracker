package com.beneboba.package_tracking.website.home;

import com.beneboba.package_tracking.entity.Delivery;
import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.entity.Service;
import com.beneboba.package_tracking.entity.User;
import com.beneboba.package_tracking.model.request.PackageFilter;
import com.beneboba.package_tracking.service.DeliveryService;
import com.beneboba.package_tracking.service.LocationService;
import com.beneboba.package_tracking.service.ServiceService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/home")
@Slf4j
public class WebHomeController {

    @Autowired
    private LocationService locationService;

    @Autowired
    private ServiceService serviceService;

    @Autowired
    private DeliveryService deliveryService;

    @GetMapping
    public String homePage(
            Model model,
            @RequestParam(value = "locationName", required = false) String locationName,
            @RequestParam(value = "isReceived", required = false) Boolean isReceived,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size
    ) {
        log.info("locationName -> {}", locationName);
        log.info("isReceived -> {}", isReceived);
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        log.info("getAll -> page: " + page + " size: " + size);

        PackageFilter filter = PackageFilter.builder()
                .isReceived(isReceived)
                .locationName(locationName)
                .build();

        log.info("getAll -> filter: " + filter);

        Page<Delivery> deliveryPage = deliveryService.getAllWithFilter(filter,page, size);
        List<Delivery> deliveryList = deliveryPage.getContent();

        int totalPages = deliveryPage.getTotalPages();
        int currentPage = deliveryPage.getPageable().getPageNumber();
        int pageSize = deliveryPage.getPageable().getPageSize();
        long totalElements = deliveryPage.getTotalElements();
        int startElement = currentPage * pageSize + 1;
        int endElement = (int) Math.min((long) (currentPage + 1) * pageSize, totalElements);

        model.addAttribute("user", currentUser);
        model.addAttribute("deliveries", deliveryList);
        model.addAttribute("role", currentUser.getRole().name());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("startElement", startElement);
        model.addAttribute("endElement", endElement);

        return "home/home";
    }

    @GetMapping("/delivery/{id}")
    public String deliveryContent(
            Model model,
            @PathVariable Long id
    ) {
        Delivery delivery = deliveryService.findById(id);
        log.info("delivery -> {}", delivery);


        model.addAttribute("delivery", delivery);
        model.addAttribute("checkpoints", delivery.getCheckpointDelivery());


        log.info("checkpoints -> {}", delivery.getCheckpointDelivery());
        return "home/detail_delivery";
    }
}