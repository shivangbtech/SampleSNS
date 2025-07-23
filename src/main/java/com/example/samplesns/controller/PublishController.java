package com.example.samplesns.controller;

import com.example.samplesns.service.SnsPublisherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
public class PublishController {

  @Autowired
  private SnsPublisherService snsPublisherService;

  @PostMapping("/publish")
  public ResponseEntity<String> publish(@RequestBody String message) {
    snsPublisherService.publishMessage(message);
    return ResponseEntity.ok("Message Published");
  }
}

