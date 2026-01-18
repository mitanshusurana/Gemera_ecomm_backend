package com.jewelry.backend.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class StoreResponse {
    private UUID id;
    private String name;
    private String address;
    private Coordinates coordinates;

    @Data
    public static class Coordinates {
        private Double lat;
        private Double lng;

        public Coordinates(Double lat, Double lng) {
            this.lat = lat;
            this.lng = lng;
        }
    }
}
