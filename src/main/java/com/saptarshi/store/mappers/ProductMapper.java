package com.saptarshi.store.mappers;

import com.saptarshi.store.dto.ProductDto;
import com.saptarshi.store.entities.Product;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    @Mapping(source = "category.id", target = "categoryId")
    ProductDto toDto(Product product);
}
