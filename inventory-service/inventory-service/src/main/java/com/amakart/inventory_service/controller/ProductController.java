package com.amakart.inventory_service.controller;

import com.amakart.inventory_service.clients.OrdersFeignClient;
import com.amakart.inventory_service.dto.OrderRequestDto;
import com.amakart.inventory_service.dto.ProductDto;
import com.amakart.inventory_service.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestClient;

import java.util.List;

@RestController
@Slf4j
//@RequiredArgsConstructor
@RequestMapping("/products")
public class ProductController {
    private final ProductService productService;
    private final RestClient restClient;
    private final DiscoveryClient discoveryClient;
    private final OrdersFeignClient ordersFeignClient;

    public ProductController(ProductService productService, DiscoveryClient discoveryClient, RestClient restClient, OrdersFeignClient ordersFeignClient) {
        this.productService = productService;
        this.discoveryClient = discoveryClient;
        this.restClient = restClient;
        this.ordersFeignClient = ordersFeignClient;
    }

    @GetMapping("/fetchOrders")
    public String fetchFromOrdersService() {
//        log.info(httpServletRequest.getHeader("x-custom-header"));
//        ServiceInstance orderService = discoveryClient.getInstances("order-service").getFirst();
//
//        return restClient.get()
//                .uri(orderService.getUri()+"/orders/core/helloOrders")
//                .retrieve()
//                .body(String.class);

        return ordersFeignClient.helloOrders();
    }

    @GetMapping
    public ResponseEntity<List<ProductDto>> getAllInventory() {
        List<ProductDto> inventories = productService.getAllInventory();
        return ResponseEntity.ok(inventories);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getInventoryById(@PathVariable Long id) {
        ProductDto inventory = productService.getProductById(id);
        return ResponseEntity.ok(inventory);
    }

    @PutMapping("reduce-stocks")
    public ResponseEntity<Double> reduceStocks(@RequestBody OrderRequestDto orderRequestDto) {
        Double totalPrice = productService.reduceStocks(orderRequestDto);
        return ResponseEntity.ok(totalPrice);
    }
}
