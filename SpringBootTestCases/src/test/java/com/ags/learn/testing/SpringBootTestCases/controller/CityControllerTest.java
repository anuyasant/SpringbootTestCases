package com.ags.learn.testing.SpringBootTestCases.controller;

import com.ags.learn.testing.SpringBootTestCases.AbstractTestClass;
import com.ags.learn.testing.SpringBootTestCases.entity.City;
import com.ags.learn.testing.SpringBootTestCases.service.CityService;
import org.fluttercode.datafactory.impl.DataFactory;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CityControllerTest extends AbstractTestClass{

    @MockBean
    CityService cityService;

    @InjectMocks
    CityController cityController;

    private List<City> cityList;

    private DataFactory dataFactory;

    @Autowired
    JdbcTemplate jdbcTemplate;

    @Before
    public void initTests(){
        dataFactory = new DataFactory();
        cityList = new ArrayList<>();
        cityList.add(new City(1L, "Pune", 100000L));
        cityList.add(new City(2L, "Mumbai", 2000000L));
        cityList.add(new City(3L, "Delhi", 120000L));

        jdbcTemplate.execute("delete from City;");
    }


    @Test
    public void whenGetAllThenReturnListOfCities() throws Exception {
        String uri = "/city";
        given(cityService.getAll()).willReturn(cityList);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
        String content = mvcResult.getResponse().getContentAsString();
    }

    @Test
    public void whenGetCityByIdThenFound() throws Exception {
        Long id = 1L;
        String uri = "/city/"+ id;
        City city = getCity(id);

        given(cityService.get(id)).willReturn(city);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name", is(city.getName())));
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void whenGetCityByIdThenNotFound() throws Exception {
        Long id = 20L;
        String uri = "/city/"+ id;
        City city = new City();
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE)).andReturn();

        mockMvc.perform(MockMvcRequestBuilders.get(uri)
                .accept(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isNotFound());
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);

    }

    @Test
    public void whenAddCityThenSuccess() throws Exception {
        Long id = 1L;
        String uri = "/city";
        City city = getCity(id);
        String inputJson = super.mapToJson(city);
        given(cityService.add(city)).willReturn(city);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void whenUpdateCityThenSuccess() throws Exception {
        Long id = 20L;
        String uri = "/city/"+ id;
        City city = getCity(id);
        City city1 = getCity(id);
        given(cityService.get(id)).willReturn(city);
        given(cityService.update(city1)).willReturn(city1);
        String inputJson = super.mapToJson(city);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void whenUpdateCityThenNoCityFoundException() throws Exception {
        Long id = 1L;
        String uri = "/city/"+ id;
        City city = getCity(id);

        String inputJson = super.mapToJson(city);
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.put(uri)
                .contentType(MediaType.APPLICATION_JSON_VALUE).content(inputJson)).andReturn();

        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }

    @Test
    @Sql(scripts = "/addCityForDelete.sql", executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    public void whenDeleteCityThenSuccess() throws Exception {
        Long id = 21L;
        String uri = "/city/"+ id;
        City city = getCity(id);
        given(cityService.get(id)).willReturn(city);
        given(cityService.delete(city)).willReturn("Success");
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(200, status);
    }

    @Test
    public void whenDeleteCityThenNoCityFoundException() throws Exception {
        Long id = 2L;
        String uri = "/city/"+ id;
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.delete(uri)).andReturn();
        int status = mvcResult.getResponse().getStatus();
        assertEquals(404, status);
    }


    private City getCity(Long id){
        return new City(id,
                dataFactory.getCity(),
                new Long(dataFactory.getNumberBetween(0, 10000000)));
    }
}