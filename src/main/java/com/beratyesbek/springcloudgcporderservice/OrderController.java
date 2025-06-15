package com.beratyesbek.springcloudgcporderservice;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.cloud.spring.pubsub.core.PubSubTemplate;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final PubSubTemplate pubSubTemplate;

    private final ObjectMapper objectMapper;

    @PostMapping
    public ResponseEntity<Order> create(@RequestBody Order order) {
        try {
            order.setId(UUID.randomUUID().toString());
            String orderJson = objectMapper.writeValueAsString(order);
            pubSubTemplate.publish("order-notifcation", orderJson);
            return ResponseEntity.ok(order);
        }catch (Exception e) {
            return ResponseEntity.badRequest().build();
        }
    }
}
