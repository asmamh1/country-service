package com.countryservice.demo.controllers;

import java.util.List;
import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.countryservice.demo.beans.Country;
import com.countryservice.demo.dto.CountryDTO;
import com.countryservice.demo.services.CountryService;

@RestController
public class CountryController {

	private final CountryService countryservice;

	public CountryController(CountryService countryservice) {
		this.countryservice = countryservice;
	}

	@GetMapping("/getcountries")
	public ResponseEntity<List<Country>> getCountries() {
		try {
			List<Country> countries = countryservice.getAllCountries();
			return new ResponseEntity<>(countries, HttpStatus.FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/getcountries/{id}")
	public ResponseEntity<Country> getCountryById(@PathVariable(value = "id") int id) {
		try {
			Country country = countryservice.getCountryById(id);
			return new ResponseEntity<>(country, HttpStatus.FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/getcountries/countryname")
	public ResponseEntity<Country> getCountryByName(@RequestParam(value = "name") String name) {
		try {
			Country country = countryservice.getCountryByName(name);
			return new ResponseEntity<>(country, HttpStatus.FOUND);
		}
		catch (Exception e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/addcountry")
	public ResponseEntity<Country> addCountry(@RequestBody CountryDTO countryDto) {
		try {
			Country country = new Country(countryDto.idCountry(), countryDto.name(), countryDto.capital());
			country = countryservice.addCountry(country);
			return new ResponseEntity<>(country, HttpStatus.CREATED);
		}
		catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.CONFLICT);
		}
	}

	@PutMapping("/updatecountry/{id}")
	public ResponseEntity<Country> updateCountry(@PathVariable(value = "id") int id, @RequestBody CountryDTO countryDto) {
		Country existCountry = countryservice.getCountryById(id);
		if (existCountry == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		existCountry.setName(countryDto.name());
		existCountry.setCapital(countryDto.capital());

		Country updatedCountry = countryservice.updateCountry(existCountry);
		return new ResponseEntity<>(updatedCountry, HttpStatus.OK);
	}

	@DeleteMapping("/deletecountry/{id}")
	public ResponseEntity<Country> deleteCountry(@PathVariable("id") int id) {
		Country country = null;
		try {
			country = countryservice.getCountryById(id);
			countryservice.deleteCountry(country);
		}
		catch (NoSuchElementException e) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		return new ResponseEntity<>(country, HttpStatus.OK);
	}

}
