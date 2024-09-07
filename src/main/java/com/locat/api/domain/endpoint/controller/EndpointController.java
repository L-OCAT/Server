package com.locat.api.domain.endpoint.controller;

import com.locat.api.domain.endpoint.dto.EndpointRegistrationRequest;
import com.locat.api.domain.endpoint.service.PlatformEndpointService;
import com.locat.api.domain.user.entity.User;
import com.locat.api.domain.user.entity.UserEndpoint;
import com.locat.api.domain.user.service.UserEndpointService;
import com.locat.api.domain.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/endpoint")
public class EndpointController {

    private final PlatformEndpointService platformEndpointService;
    private final UserEndpointService userEndpointService;
    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<Void> register(
            @RequestBody final EndpointRegistrationRequest request) {
        List<UserEndpoint> endpoints = this.userEndpointService.findUserEndpointsByUserId(request.userId());
        boolean endpointExists = isEndpointExists(request, endpoints);

        if (!endpointExists) {
            String endpointArn = this.platformEndpointService.createPlatformEndpoint(
                    request.deviceToken(), request.platform());
            String subscribeArn = this.platformEndpointService.subscribeEndpointToTopic(endpointArn);

            User user = this.userService.findById(request.userId());
            this.userEndpointService.saveUserEndpoint(
                    user, request.deviceToken(), request.platform(), endpointArn);
        }
        return ResponseEntity.noContent().build();
    }

    private static boolean isEndpointExists(EndpointRegistrationRequest request, List<UserEndpoint> endpoints) {
        return endpoints.stream()
                .anyMatch(e -> e.getDeviceToken().equals(request.deviceToken())
                        && e.getPlatformType().getValue().equals(request.platform()));
    }
}
