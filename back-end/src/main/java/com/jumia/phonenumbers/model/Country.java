package com.jumia.phonenumbers.model;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Table(name = "country")
public class Country {
    @Id
    private Long id;
    private String name;
    private Integer code;
    private String regex;
}
