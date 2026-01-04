package com.saptarshi.store.controller;

import com.saptarshi.store.dto.ProductDto;
import com.saptarshi.store.entities.Product;
import com.saptarshi.store.mappers.ProductMapper;
import com.saptarshi.store.repositories.CategoryRepository;
import com.saptarshi.store.repositories.ProductRepository;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Set;

@RestController
@RequestMapping("/products")
@AllArgsConstructor
@SuppressWarnings("unused")
public class ProducrtController {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final CategoryRepository categoryRepository;


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
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        Product product = productRepository.findById(id).orElse(null);
        if (product != null) {
            return ResponseEntity.ok(productMapper.toDto(product));
        }
        return new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }


    @PostMapping
    public ResponseEntity<ProductDto> createProduct(
            @RequestBody ProductDto productDto,
            UriComponentsBuilder uriBuilder) {
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        var product = productMapper.toEntity(productDto);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());

        var uri = uriBuilder.path("/products/{id}").buildAndExpand(productDto.getId()).toUri();

        return ResponseEntity.created(uri).body(productDto);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        var product = productRepository.findById(id).orElse(null);
        if (product == null)
            return ResponseEntity.badRequest().build();
        productRepository.delete(product);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductDto productDto) {
        var category = categoryRepository.findById(productDto.getCategoryId()).orElse(null);
        if (category == null) {
            return ResponseEntity.badRequest().build();
        }

        var product = productRepository.findById(id).orElse(null);
        if (product == null) {
            return ResponseEntity.notFound().build();
        }

        productMapper.update(productDto, product);
        product.setCategory(category);
        productRepository.save(product);
        productDto.setId(product.getId());

        return ResponseEntity.ok(productDto);
    }

}
