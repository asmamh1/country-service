package com.countryservice.demo.dto;

/**
 * Objet de transfert (DTO) utilisé pour les requêtes POST et PUT.
 * On n'expose plus directement l'entité JPA Country dans l'API (règle de sécurité Sonar).
 */
public record CountryDTO(int idCountry, String name, String capital) {
}
