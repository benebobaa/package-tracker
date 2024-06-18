package com.beneboba.package_tracking.website.home;


import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.entity.User;
import com.beneboba.package_tracking.service.LocationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/location")
@Slf4j
public class WebLocationController {

    @Autowired
    private LocationService locationService;

    @GetMapping
    public String locationPage(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String queryName
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();

        Page<Location> locationPage = locationService.getAll(queryName,page, size);

        List<Location> locationList = locationPage.getContent();

        int totalPages = locationPage.getTotalPages();
        int currentPage = locationPage.getPageable().getPageNumber();
        int pageSize = locationPage.getPageable().getPageSize();
        long totalElements = locationPage.getTotalElements();
        int startElement = currentPage * pageSize + 1;
        int endElement = (int) Math.min((long) (currentPage + 1) * pageSize, totalElements);

        model.addAttribute("locations", locationList);
        model.addAttribute("role", currentUser.getRole().name());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("startElement", startElement);
        model.addAttribute("endElement", endElement);
        model.addAttribute("queryName", queryName);

        return "home/location";
    }
}
