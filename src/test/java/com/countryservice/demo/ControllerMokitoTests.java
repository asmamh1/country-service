package com.countryservice.demo;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.countryservice.demo.beans.Country;
import com.countryservice.demo.controllers.CountryController;
import com.countryservice.demo.dto.CountryDTO;
import com.countryservice.demo.services.CountryService;

@ExtendWith(MockitoExtension.class)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ControllerMokitoTests {

	@Mock
	CountryService countryService;

	@InjectMocks
	CountryController countryController;

	List<Country> mycountries;
	Country country;

	@Test
	@Order(1)
	void test_getAllCountries() {
		mycountries = new ArrayList<Country>();
		mycountries.add(new Country(1,"India","Delhi"));
		mycountries.add(new Country(2,"USA","Washington"));

		when(countryService.getAllCountries()).thenReturn(mycountries);//Mocking
		ResponseEntity<List<Country>> res = countryController.getCountries();

		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(2, res.getBody().size());

	}

	@Test
	@Order(2)
	void test_getCountryByID() {
		country = new Country(1,"India","Delhi");

		int countryID=1;
		when(countryService.getCountryById(countryID)).thenReturn(country);//Mocking
		ResponseEntity<Country> res = countryController.getCountryById(countryID);

		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(1, res.getBody().getIdCountry());

	}

	@Test
	@Order(3)
	void test_getCountryByName() {
		country = new Country(1,"India","Delhi");

		String countryName="India";
		when(countryService.getCountryByName(countryName)).thenReturn(country);//Mocking
		ResponseEntity<Country> res = countryController.getCountryByName(countryName);

		assertEquals(HttpStatus.FOUND, res.getStatusCode());
		assertEquals(countryName, res.getBody().getName());

	}

	@Test
	@Order(4)
	void test_addCountry() {
		country = new Country(3,"Germany","Berlin");

		when(countryService.addCountry(any(Country.class))).thenReturn(country);//Mocking
		ResponseEntity<Country> res = countryController.addCountry(new CountryDTO(3,"Germany","Berlin"));

		assertEquals(HttpStatus.CREATED, res.getStatusCode());
		assertEquals(country, res.getBody());

	}

	@Test
	@Order(5)
	void test_updateCountry() {
		country = new Country(3,"Japan","Tokyo");
		int countryID = 3;

		when(countryService.getCountryById(countryID)).thenReturn(country);//Mocking
		when(countryService.updateCountry(country)).thenReturn(country);//Mocking

		ResponseEntity<Country> res = countryController.updateCountry(countryID, new CountryDTO(3,"Japan","Tokyo"));

		assertEquals(HttpStatus.OK, res.getStatusCode());
		assertEquals("Japan", res.getBody().getName());
		assertEquals("Tokyo", res.getBody().getCapital());

	}

	@Test
	@Order(6)
	void test_updateCountry_notFound() {
		int countryID = 99;

		when(countryService.getCountryById(countryID)).thenReturn(null);//Mocking

		ResponseEntity<Country> res = countryController.updateCountry(countryID, new CountryDTO(99,"Narnia","Cair Paravel"));

		assertEquals(HttpStatus.NOT_FOUND, res.getStatusCode());

	}

	@Test
	@Order(7)
	void test_deleteCountry() {
		country = new Country(3,"Japan","Tokyo");
		int countryID = 3;

		when(countryService.getCountryById(countryID)).thenReturn(country);//Mocking

		ResponseEntity<Country> res = countryController.deleteCountry(countryID);

		assertEquals(HttpStatus.OK, res.getStatusCode());

	}

}