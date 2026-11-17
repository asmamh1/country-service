package com.countryservice.demo.services;

import java.util.List;

import org.springframework.stereotype.Service;

import com.countryservice.demo.beans.Country;
import com.countryservice.demo.repositories.CountryRepository;

@Service
public class CountryService {

	private final CountryRepository countryRep;

	public CountryService(CountryRepository countryRep) {
		this.countryRep = countryRep;
	}

	public List<Country> getAllCountries() {
		return countryRep.findAll();
	}

	public Country getCountryById(int id) {
		List<Country> countries = countryRep.findAll();
		Country country = null;

		for (Country con : countries) {
			if (con.getIdCountry() == id)
				country = con;
		}

		return country;
	}

	public Country getCountryByName(String name) {
		List<Country> countries = countryRep.findAll();
		Country country = null;

		for (Country con : countries) {
			if (con.getName().equalsIgnoreCase(name))
				country = con;
		}

		return country;
	}

	public Country addCountry(Country country) {
		return countryRep.save(country);
	}

	public Country updateCountry(Country country) {
		return countryRep.save(country);
	}

	public void deleteCountry(Country country) {
		countryRep.delete(country);
	}

}
