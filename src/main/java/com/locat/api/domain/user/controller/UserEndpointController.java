package com.locat.api.domain.user.controller;

import com.locat.api.domain.core.BaseResponse;
import com.locat.api.domain.user.dto.EndpointRegistrationRequest;
import com.locat.api.domain.user.service.UserEndpointService;
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
public class UserEndpointController {

    private final UserEndpointService userEndpointService;

    @PostMapping
    public ResponseEntity<BaseResponse<Void>> register(
            @AuthenticationPrincipal LocatUserDetails userDetails,
            @RequestBody @Valid final EndpointRegistrationRequest request) {
        this.userEndpointService.register(request, userDetails);

        return ResponseEntity.ok(BaseResponse.ofEmpty());
    }
}
