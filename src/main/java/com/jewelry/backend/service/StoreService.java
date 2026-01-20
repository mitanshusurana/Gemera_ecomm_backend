package com.jewelry.backend.service;

import com.jewelry.backend.dto.StoreResponse;
import com.jewelry.backend.entity.Store;
import com.jewelry.backend.repository.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StoreService {

    @Autowired
    StoreRepository storeRepository;

    public StoreResponse getAllStores() {
        List<Store> stores = storeRepository.findAll();
        return new StoreResponse(stores);
    }
}
