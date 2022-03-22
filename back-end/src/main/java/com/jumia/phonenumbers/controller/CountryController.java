package com.jumia.phonenumbers.controller;

import com.jumia.phonenumbers.dto.CountryDTO;
import com.jumia.phonenumbers.http.response.Response;
import com.jumia.phonenumbers.service.CountryService;
import com.jumia.phonenumbers.util.ControllerUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;


@CrossOrigin(origins = "*", allowedHeaders = "*")
@RequestMapping("/api/country")
@RestController
@Slf4j
public class CountryController {
    @Autowired
    CountryService countryService;

    @GetMapping("/read/list")
    public ResponseEntity<Response<List<CountryDTO>>> getCountryData(
            @RequestParam Optional<Integer> page,
            @RequestParam Optional<Integer> size,
            @RequestParam Optional<String> search,
            @RequestParam Optional<String> sortBy,
            @RequestParam Optional<Boolean> sortDesc
    ) {
        Response<List<CountryDTO>> response = new Response<>();


        try {
            Page<CountryDTO> countryPage = countryService.getCountryDataDTO(
                    search.orElse(""),
                    (page.isPresent() && size.isPresent()) ? ControllerUtils.createPageable(
                            page.get(),
                            size.get(),
                            sortBy.filter(Predicate.not(String::isBlank)).orElse("id"),
                            (sortDesc.orElse(false)) ? Sort.Direction.DESC : Sort.Direction.ASC
                    ) : ControllerUtils.createSortOnlyPageable(
                            sortBy.filter(Predicate.not(String::isBlank)).orElse("id"),
                            (sortDesc.orElse(false)) ? Sort.Direction.DESC : Sort.Direction.ASC
                    )
            );

            response.setData(countryPage.getContent());

            response.addMetaData("totalCount", countryPage.getTotalElements());
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
            response.setData(new ArrayList<>());
            response.addMetaData("error", e.getMessage());
        }
        return ResponseEntity.ok(response);
    }
}
