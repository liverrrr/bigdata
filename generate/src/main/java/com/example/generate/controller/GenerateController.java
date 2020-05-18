package com.example.generate.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

@RestController
public class GenerateController {

    private static final Logger logger = LoggerFactory.getLogger(GenerateController.class);

    @PostMapping("/generate")
    public void generateLog(@RequestBody String content){
        logger.info(content);
    }

}
