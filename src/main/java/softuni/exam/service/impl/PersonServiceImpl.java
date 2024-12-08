package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.PersonSeedDto;
import softuni.exam.models.entity.Country;
import softuni.exam.models.entity.Person;
import softuni.exam.repository.PersonRepository;
import softuni.exam.service.CountryService;
import softuni.exam.service.PersonService;
import softuni.exam.util.ValidationUtil;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class PersonServiceImpl implements PersonService {

    private final static String JSON_IMPORT_PATH = "D:\\IdeaProjects\\SpringData\\examPreparation\\Job Finder_Skeleton\\skeleton\\src\\main\\resources\\files\\json\\people.json";

    private final PersonRepository personRepository;
    private final CountryService countryService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public PersonServiceImpl(PersonRepository personRepository, CountryService countryService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.personRepository = personRepository;
        this.countryService = countryService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.personRepository.count() > 0;
    }

    @Override
    public String readPeopleFromFile() throws IOException {
        return Files.readString(Path.of(JSON_IMPORT_PATH));
    }

    @Override
    public String importPeople() throws IOException, JAXBException {

        StringBuilder sb = new StringBuilder();
        PersonSeedDto[] personSeedDtos = this.gson.fromJson(readPeopleFromFile(), PersonSeedDto[].class);

        for (PersonSeedDto personSeedDto: personSeedDtos) {
            if (this.personRepository.findByFirstNameOrEmailOrPhone(
                    personSeedDto.getFirstName(),
                    personSeedDto.getEmail(),
                    personSeedDto.getPhone()).isPresent() ||
            !this.validationUtil.isValid(personSeedDto)) {

                sb.append("Invalid person")
                        .append(System.lineSeparator());
                continue;
            }

            Person person = this.modelMapper.map(personSeedDto, Person.class);
            Country country = this.countryService.getCountry(personSeedDto.getCountry()).get();
            country.getPeople().add(person);
            person.setCountry(country);

            this.personRepository.save(person);
            sb.append(String.format("Successfully imported person %s %s",
                    personSeedDto.getFirstName(), personSeedDto.getLastName()))
                    .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }
}
