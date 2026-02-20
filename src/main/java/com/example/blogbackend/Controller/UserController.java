package com.example.blogbackend.Controller;

import com.example.blogbackend.Const.Consts;
import com.example.blogbackend.Dto.UserProfileRequestDTO;
import com.example.blogbackend.Dto.UserProfileResponseDTO;
import com.example.blogbackend.Service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(Consts.API_BASE + Consts.API_VERSION + "/users")
@RequiredArgsConstructor

public class UserController {

    private final UserService userService;


    @GetMapping("/profile")
    public UserProfileResponseDTO getMyProfile(@AuthenticationPrincipal Jwt jwt) {
        return userService.getMyProfile(jwt);
    }


    @PutMapping("/profile/complete")
    public UserProfileResponseDTO completeProfile(
            @Valid
            @AuthenticationPrincipal Jwt jwt,
            @Valid @RequestBody UserProfileRequestDTO dto
    ) {
        return userService.completeProfile(jwt, dto);
    }
}