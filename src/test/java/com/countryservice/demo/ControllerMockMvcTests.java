package com.countryservice.demo;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.countryservice.demo.beans.Country;
import com.countryservice.demo.controllers.CountryController;
import com.countryservice.demo.services.CountryService;
import com.fasterxml.jackson.databind.ObjectMapper;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class ControllerMockMvcTests {

	MockMvc mockMvc;

	@Mock
	CountryService countryService;

	@InjectMocks
	CountryController countryController;

	public List<Country> mycountries;
	Country country;

	@BeforeEach
	public void setUp() {
		mockMvc = MockMvcBuilders.standaloneSetup(countryController).build();
	}

	@Test
	@Order(1)
	public void test_getAllCountries() throws Exception {
		mycountries = new ArrayList<Country>();
		mycountries.add(new Country(1, "India", "Delhi"));
		mycountries.add(new Country(2, "USA", "Washington"));

		when(countryService.getAllCountries()).thenReturn(mycountries);

		this.mockMvc.perform(get("/getcountries"))
			.andDo(print())
			.andExpect(status().isFound());
	}

	@Test
	@Order(2)
	public void test_getCountryByID() throws Exception {
		country = new Country(1, "India", "Delhi");
		int countryID = 1;
		when(countryService.getCountryById(countryID)).thenReturn(country);

		this.mockMvc.perform(get("/getcountries/{id}", countryID))
			.andDo(print())
			.andExpect(status().isFound())
			.andExpect(jsonPath("$.idCountry").value(1))
			.andExpect(jsonPath("$.name").value("India"))
			.andExpect(jsonPath("$.capital").value("Delhi"));
	}

	@Test
	@Order(3)
	public void test_getCountryByName() throws Exception {
		country = new Country(1, "India", "Delhi");
		String countryName = "India";
		when(countryService.getCountryByName(countryName)).thenReturn(country);

		this.mockMvc.perform(get("/getcountries/countryname").param("name", "India"))
			.andDo(print())
			.andExpect(status().isFound())
			.andExpect(jsonPath("$.idCountry").value(1))
			.andExpect(jsonPath("$.name").value("India"))
			.andExpect(jsonPath("$.capital").value("Delhi"));
	}

	@Test
	@Order(4)
	public void test_addCountry() throws Exception {
		country = new Country(3, "Germany", "Berlin");

		when(countryService.addCountry(any(Country.class))).thenReturn(country);

		ObjectMapper mapper = new ObjectMapper();
		String jsonbody = mapper.writeValueAsString(country);

		this.mockMvc.perform(post("/addcountry")
				.content(jsonbody)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isCreated());
	}

	@Test
	@Order(5)
	public void test_updateCountry() throws Exception {
		country = new Country(3, "Japan", "Tokyo");
		int countryID = 3;

		when(countryService.getCountryById(countryID)).thenReturn(country);
		when(countryService.updateCountry(any(Country.class))).thenReturn(country);

		ObjectMapper mapper = new ObjectMapper();
		String jsonbody = mapper.writeValueAsString(country);

		this.mockMvc.perform(put("/updatecountry/{id}", countryID)
				.content(jsonbody)
				.contentType(MediaType.APPLICATION_JSON))
			.andDo(print())
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.name").value("Japan"))
			.andExpect(jsonPath("$.capital").value("Tokyo"));
	}

	@Test
	@Order(6)
	public void test_deleteCountry() throws Exception {
		country = new Country(3, "Japan", "Tokyo");
		int countryID = 3;

		when(countryService.getCountryById(countryID)).thenReturn(country);

		this.mockMvc.perform(delete("/deletecountry/{id}", countryID))
			.andDo(print())
			.andExpect(status().isOk());
	}

}