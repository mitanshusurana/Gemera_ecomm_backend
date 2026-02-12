package com.jewelry.backend.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class AddressDTO {
    private UUID id;
    private String firstName;
    private String lastName;
    private String street;
    private String city;
    private String state;
    private String zipCode;
    private String country;
    private String phone;
    private boolean isDefault;
}
