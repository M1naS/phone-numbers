package com.jumia.phonenumbers;

import com.jumia.phonenumbers.dto.CountryDTO;
import com.jumia.phonenumbers.service.CountryService;
import com.jumia.phonenumbers.service.CustomerService;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.sql.SQLException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class PhoneNumbersApplicationTests {

    @Autowired
    CustomerService customerService;

    @Autowired
    CountryService countryService;

    Integer totalCount;

    @BeforeAll
    @DisplayName("@Autowired is Working")
    void checkCountryService() {
        assertInstanceOf(CustomerService.class, customerService);
        assertInstanceOf(CountryService.class, countryService);

        totalCount = customerService.getCountOfAllCustomers();
    }

    @Test
    @DisplayName("Get all Country DTO Data")
    void getCountryDTOData() throws SQLException {
        assertEquals(countryService.getCountryDataDTO("", Pageable.unpaged()).getContent().size(), totalCount);
    }

    @Test
    @DisplayName("Get all Country DTO Data with Pagination")
    void getCountryDTODataPaginated() throws SQLException {
        assertTrue(totalCount >= 5);
        assertEquals(countryService.getCountryDataDTO("", PageRequest.of(0, 5)).getContent().size(), 5);
    }

    @Test
    @DisplayName("Get all Country DTO Count")
    void getCountryDTOCount() throws SQLException {
        assertEquals(countryService.getCountryDataDTOCount(""), totalCount);
    }

    @Test
    @DisplayName("Search in Country DTO by state")
    void searchInCountryDTOByState() throws SQLException {
        assertEquals(
                totalCount,
                (countryService.getCountryDataDTOCount("Valid") + countryService.getCountryDataDTOCount("Not Valid"))
        );
    }

    @Test
    @DisplayName("Check properties returned from Get Country DTO Data")
    void checkPropertiesFromGetCountryDTOData() throws SQLException {
        assertTrue(totalCount >= 1);

        List<CountryDTO> countryDTOList = countryService.getCountryDataDTO("", PageRequest.of(0, 1, Sort.by(Sort.Direction.ASC, "id"))).getContent();

        assertEquals(countryDTOList.size(), 1);
        assertAll("Country DTO Properties",
                  () -> assertNotNull(countryDTOList.get(0).getCountry()),
                  () -> assertNotNull(countryDTOList.get(0).getCode()),
                  () -> assertNotNull(countryDTOList.get(0).getPhone()),
                  () -> assertNotNull(countryDTOList.get(0).getState())
        );
    }
}
