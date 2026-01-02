package com.saptarshi.store.controller;

import com.saptarshi.store.dto.ProductDto;
import com.saptarshi.store.entities.Product;
import com.saptarshi.store.mappers.ProductMapper;
import com.saptarshi.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
public class ProducrtController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    @GetMapping("")
    public List<ProductDto> getAllProducts(
            @RequestParam(required = false, defaultValue = "id", name = "sort")
            String sort) {
        if (!Set.of("price", "name", "id").contains(sort)) {
            sort = "id";
        }
        return productRepository.findAll(Sort.by(sort))
                .stream()
                .map(productMapper::toDto)
                .toList();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id){
        Product product = productRepository.findById(id).orElse(null);
        if(product!=null){
            return ResponseEntity.ok(productMapper.toDto(product));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }
}
