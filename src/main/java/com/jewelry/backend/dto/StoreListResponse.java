package com.jewelry.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import java.util.List;

@Data
@AllArgsConstructor
public class StoreListResponse {
    private List<StoreResponse> stores;
}
