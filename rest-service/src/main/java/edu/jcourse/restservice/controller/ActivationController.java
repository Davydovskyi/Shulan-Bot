package edu.jcourse.restservice.controller;

import edu.jcourse.restservice.service.UserActivationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static edu.jcourse.restservice.util.MessageUtil.REGISTRATION_SUCCESS;

@RestController
@RequestMapping("users")
@RequiredArgsConstructor
public class ActivationController {
    private final UserActivationService userActivationService;

    @GetMapping("/activation/{id}")
    public ResponseEntity<String> activation(@PathVariable String id) {
        return userActivationService.activate(id) ?
                ResponseEntity.ok().body(REGISTRATION_SUCCESS) :
                ResponseEntity.internalServerError().build();
    }
}