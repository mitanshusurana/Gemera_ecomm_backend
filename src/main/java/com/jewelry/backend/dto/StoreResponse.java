package com.jewelry.backend.dto;

import com.jewelry.backend.entity.Store;
import lombok.Data;
import lombok.AllArgsConstructor;
import java.util.List;

@Data
@AllArgsConstructor
public class StoreResponse {
    private List<Store> stores;
}
