package com.jewelry.backend.service;

import com.jewelry.backend.dto.StoreListResponse;
import com.jewelry.backend.dto.StoreResponse;
import com.jewelry.backend.entity.Store;
import com.jewelry.backend.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class StoreService {
    @Autowired
    StoreRepository storeRepository;

    public StoreListResponse getAllStores() {
        List<Store> stores = storeRepository.findAll();
        List<StoreResponse> responses = stores.stream().map(this::mapToResponse).collect(Collectors.toList());
        return new StoreListResponse(responses);
    }

    private StoreResponse mapToResponse(Store store) {
        StoreResponse response = new StoreResponse();
        response.setId(store.getId());
        response.setName(store.getName());
        response.setAddress(store.getAddress());
        response.setCoordinates(new StoreResponse.Coordinates(store.getLatitude(), store.getLongitude()));
        return response;
    }
}
