package com.locat.api.domain.endpoint.controller;

import com.locat.api.domain.core.BaseResponse;
import com.locat.api.domain.endpoint.dto.EndpointRegistrationRequest;
import com.locat.api.domain.endpoint.service.PlatformEndpointService;
import com.locat.api.global.auth.LocatUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("v1/endpoints")
public class EndpointController {

    private final PlatformEndpointService platformEndpointService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> register(
            @AuthenticationPrincipal LocatUserDetails userDetails,
            @RequestBody @Valid final EndpointRegistrationRequest request) {
        this.platformEndpointService.register(request, userDetails);

        return ResponseEntity.ok(BaseResponse.ofEmpty());
    }
}
