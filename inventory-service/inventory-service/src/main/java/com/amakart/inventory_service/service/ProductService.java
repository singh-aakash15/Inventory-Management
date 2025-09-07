package com.amakart.inventory_service.service;

import com.amakart.inventory_service.dto.OrderRequestDto;
import com.amakart.inventory_service.dto.OrderRequestItemDto;
import com.amakart.inventory_service.dto.ProductDto;
import com.amakart.inventory_service.entity.Product;
import com.amakart.inventory_service.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
//@RequiredArgsConstructor
public class ProductService {

    public ProductService(ProductRepository productRepository, ModelMapper modelMapper) {
        this.productRepository = productRepository;
        this.modelMapper = modelMapper;
    }

    private final ProductRepository productRepository;
    private final ModelMapper modelMapper;

    public List<ProductDto> getAllInventory() {
//        log.info("Fetching all inventory items");
        List<Product> inventories = productRepository.findAll();
        return inventories.stream()
                .map(product -> modelMapper.map(product, ProductDto.class))
                .toList();
    }

    public ProductDto getProductById(Long id) {
//        log.info("Fetching Product with ID: {}", id);
        Optional<Product> inventory = productRepository.findById(id);
        return inventory.map(item -> modelMapper.map(item, ProductDto.class))
                .orElseThrow(() -> new RuntimeException("Inventory not found"));
    }

    @Transactional
    public Double reduceStocks(OrderRequestDto orderRequestDto) {
//        log.info("Reducing the stocks");
        Double totalPrice = 0.0;
        for(OrderRequestItemDto orderRequestItemDto: orderRequestDto.getItems()) {
            Long productId = orderRequestItemDto.getProductId();
            Integer quantity = orderRequestItemDto.getQuantity();

            Product product = productRepository.findById(productId).orElseThrow(() ->
                    new RuntimeException("Product not found with id: "+productId));

            if(product.getStock() < quantity) {
                throw new RuntimeException("Product cannot be fulfilled for given quantity");
            }

            product.setStock(product.getStock()-quantity);
            productRepository.save(product);
            totalPrice += quantity*product.getPrice();
        }
        return totalPrice;
    }
}