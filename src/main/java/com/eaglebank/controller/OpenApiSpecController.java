package com.eaglebank.controller;

import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OpenApiSpecController {

    private final Resource openApiSpec;

    public OpenApiSpecController(ResourceLoader resourceLoader) {
        this.openApiSpec = resourceLoader.getResource("classpath:openapi.yaml");
    }

    @GetMapping(value = "/openapi.yaml", produces = "application/yaml")
    public ResponseEntity<Resource> openApiSpec() {
        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType("application/yaml"))
                .body(openApiSpec);
    }
}
