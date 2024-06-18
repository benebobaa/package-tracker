package com.beneboba.package_tracking.website.home;

import com.beneboba.package_tracking.entity.Location;
import com.beneboba.package_tracking.entity.Service;
import com.beneboba.package_tracking.entity.User;
import com.beneboba.package_tracking.service.ServiceService;
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
@RequestMapping("/service")
public class WebServiceController {

    @Autowired
    private ServiceService serviceService;

    @GetMapping
    public String servicePage(
            Model model,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String queryName
    ) {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        User currentUser = (User) authentication.getPrincipal();
        Page<Service> servicePage = serviceService.getAll(queryName,page, size);
        List<Service> serviceList = servicePage.getContent();

        int totalPages = servicePage.getTotalPages();
        int currentPage = servicePage.getPageable().getPageNumber();
        int pageSize = servicePage.getPageable().getPageSize();
        long totalElements = servicePage.getTotalElements();
        int startElement = currentPage * pageSize + 1;
        int endElement = (int) Math.min((long) (currentPage + 1) * pageSize, totalElements);

        model.addAttribute("services", serviceList);
        model.addAttribute("role", currentUser.getRole().name());
        model.addAttribute("totalPages", totalPages);
        model.addAttribute("currentPage", currentPage);
        model.addAttribute("pageSize", pageSize);
        model.addAttribute("totalElements", totalElements);
        model.addAttribute("startElement", startElement);
        model.addAttribute("endElement", endElement);
        model.addAttribute("queryName", queryName);

        return "home/service";
    }
}
