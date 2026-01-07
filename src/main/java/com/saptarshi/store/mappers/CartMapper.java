package com.saptarshi.store.mappers;

import com.saptarshi.store.dto.CartDto;
import com.saptarshi.store.entities.Cart;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface CartMapper {
    CartDto toDto(Cart cart);
}
