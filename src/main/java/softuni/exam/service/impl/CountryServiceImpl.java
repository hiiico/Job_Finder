package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountrySeedDto;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class CountryServiceImpl implements CountryService {

    private final static String JSON_IMPORT_PATH = "/src/main/resources/files/json/countries.json";
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFileContent() throws IOException {
        return Files.readString(Path.of(JSON_IMPORT_PATH));
    }

    @Override
    public String importCountries() throws IOException {

        StringBuilder sb = new StringBuilder();
        CountrySeedDto[] countrySeedDtos = this.gson.fromJson(readCountriesFileContent(),CountrySeedDto[].class);

        for (CountrySeedDto countrySeedDto : countrySeedDtos) {
            if(!this.validationUtil.isValid(countrySeedDto)) {
                sb.append("Invalid country")
                        .append(System.lineSeparator());
                continue;
            }
            this.countryRepository.save(this.modelMapper.map(countrySeedDto, Country.class));
            sb.append(String.format("Successfully imported country %s - %s",
                            countrySeedDto.getName(), countrySeedDto.getCountryCode()))
                    .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    @Override
    public Optional<Country> getCountry(Long country) {
        return this.countryRepository.findById(country);
    }

    @Override
    public Country getCountryById(Long countryId) {
        return this.countryRepository.findById(countryId).orElse(null);
    }
}
