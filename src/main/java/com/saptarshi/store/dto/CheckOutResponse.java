package com.saptarshi.store.dto;

import lombok.Data;

@Data
public class CheckOutResponse {
    private Long orderID;

    public CheckOutResponse(Long id) {
    }
}
