package com.jumia.phonenumbers.service;

import com.jumia.phonenumbers.db.function.REGEXP;
import com.jumia.phonenumbers.dto.CountryDTO;
import com.jumia.phonenumbers.util.ControllerUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DataSourceUtils;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.sqlite.Function;
import org.sqlite.SQLiteConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Objects;

@Service
public class CountryService {
    @Autowired
    JdbcTemplate jdbcTemplate;

    @Transactional(readOnly = true)
    public Page<CountryDTO> getCountryDataDTO(String search, Pageable pageable) throws SQLException {
        Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource())).unwrap(SQLiteConnection.class);
        Function.create(
                connection,
                "REGEXP",
                new REGEXP()
        );

        if (pageable.isUnpaged()) {
            pageable = ControllerUtils.createSortOnlyPageable("id", Sort.Direction.ASC);
        }
        Sort.Order order = (pageable.getSort().isEmpty())? Sort.Order.by("id"):  pageable.getSort().toList().get(0);

        String sql = "SELECT customer.id AS id, " +
                "country.name AS country, " +
                "IIF(REGEXP(country.regex, customer.phone), 'Valid', 'Not Valid') AS state, " +
                "'+' || country.code AS code, " +
                "customer.phone AS phone " +
                "FROM customer JOIN country " +
                "ON SUBSTR(customer.phone, INSTR(customer.phone, '(') + 1, INSTR(customer.phone, ')') - INSTR(customer.phone, '(') - 1) = country.code " +
                "WHERE (country.name LIKE '" + search + "%' OR " +
                "IIF(REGEXP(country.regex, customer.phone), 'Valid', 'Not Valid') LIKE '" + search + "%' OR " +
                "country.code LIKE '" + search + "%' OR " +
                "customer.phone LIKE '" + search + "%' ) " +
                "ORDER BY " + order.getProperty() + " " + order.getDirection().name() + " " +
                "LIMIT " + pageable.getPageSize() + " OFFSET " + pageable.getOffset();

        List<CountryDTO> users =
                new JdbcTemplate(new SingleConnectionDataSource(connection, false))
                        .query(sql, (resultSet, rowNum) ->
                                new CountryDTO(
                                        resultSet.getLong("id"),
                                        resultSet.getString("country"),
                                        resultSet.getString("state"),
                                        resultSet.getString("code"),
                                        resultSet.getString("phone")
                                )
                        );

        return new PageImpl<>(users, pageable, getCountryDataDTOCount(search));
    }

    @Transactional(readOnly = true)
    public Integer getCountryDataDTOCount(String search) throws SQLException {
        Connection connection = DataSourceUtils.getConnection(Objects.requireNonNull(jdbcTemplate.getDataSource())).unwrap(SQLiteConnection.class);
        Function.create(
                connection,
                "REGEXP",
                new REGEXP()
        );

        String sql = "SELECT count(*) " +
                "FROM customer JOIN country " +
                "ON SUBSTR(customer.phone, INSTR(customer.phone, '(') + 1, INSTR(customer.phone, ')') - INSTR(customer.phone, '(') - 1) = country.code " +
                "WHERE (country.name LIKE '" + search + "%' OR " +
                "IIF(REGEXP(country.regex, customer.phone), 'Valid', 'Not Valid') LIKE '" + search + "%' OR " +
                "country.code LIKE '" + search + "%' OR " +
                "customer.phone LIKE '" + search + "%' )";

        return new JdbcTemplate(new SingleConnectionDataSource(connection, false))
                .queryForObject(sql, Integer.class);
    }
}
