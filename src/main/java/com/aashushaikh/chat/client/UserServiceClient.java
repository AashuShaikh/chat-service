package com.aashushaikh.chat.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user", path = "/users/internal")
public interface UserServiceClient {

    @GetMapping("/{id}/exists")
    void checkUserExists(@PathVariable String id);

    @GetMapping("/{id}/profile")
    UserProfileDto getUserProfile(@PathVariable String id);
}
