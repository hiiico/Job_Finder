package softuni.exam.service;


import softuni.exam.models.entity.Country;

import java.io.IOException;
import java.util.Optional;

// TODO: Implement all methods
public interface CountryService {

    boolean areImported();

    String readCountriesFileContent() throws IOException;

    String importCountries() throws IOException;

    Optional<Country> getCountry(Long country);

    Country getCountryById(Long countryId);

}
