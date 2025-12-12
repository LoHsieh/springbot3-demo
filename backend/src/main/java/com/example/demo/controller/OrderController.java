package com.example.demo.controller;

import com.example.demo.dto.CheckoutRequest;
import com.example.demo.entity.Order;
import com.example.demo.service.OrderService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@PreAuthorize("hasRole('BUYER')")
@SecurityRequirement(name = "Bearer Authentication")
@Tag(name = "Orders", description = "Order management endpoints (Buyer only)")
public class OrderController {
    
    private final OrderService orderService;
    
    @PostMapping("/checkout")
    @Operation(summary = "Checkout and create order")
    public ResponseEntity<Order> checkout(
            @RequestBody CheckoutRequest request,
            HttpServletRequest httpRequest) {
        Long userId = (Long) httpRequest.getAttribute("userId");
        return ResponseEntity.ok(orderService.checkout(userId, request));
    }
    
    @GetMapping
    @Operation(summary = "Get order history")
    public ResponseEntity<List<Order>> getOrderHistory(HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(orderService.getOrderHistory(userId));
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get order details")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id, HttpServletRequest request) {
        Long userId = (Long) request.getAttribute("userId");
        return ResponseEntity.ok(orderService.getOrderById(id, userId));
    }
}
