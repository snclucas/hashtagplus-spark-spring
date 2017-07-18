package com.hashtagplus.rest;

import com.hashtagplus.model.User;
import com.hashtagplus.model.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class ResourceController {

    @Autowired
    private UserRepository userRepository;

    @Secured({"ROLE_ADMIN"})
    @RequestMapping("/resource")
    public Map<String, Object> home(@AuthenticationPrincipal UserDetails userDetails) {
        User user = userRepository.findOne(userDetails.getUsername());
        Map<String, Object> model = new HashMap<>();
        model.put("roles", user.getRoles());
        return model;
    }
}