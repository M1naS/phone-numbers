package com.jumia.phonenumbers.dto;

import lombok.Data;


@Data
public class CountryDTO {
    Long id;
    String country;
    String state;
    String code;
    String phone;

    public CountryDTO() {}

    public CountryDTO(
            Long id,
            String country,
            String state,
            String code,
            String phone
    ) {
        this.id = id;
        this.country = country;
        this.state = state;
        this.code = code;
        this.phone = phone;
    }
}
