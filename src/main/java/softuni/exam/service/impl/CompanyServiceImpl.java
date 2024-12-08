package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CompaniesSeedDto;
import softuni.exam.models.dto.CompanySeedDto;
import softuni.exam.models.entity.Company;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CompanyRepository;
import softuni.exam.service.CompanyService;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Optional;

@Service
public class CompanyServiceImpl implements CompanyService {
    private final static String XML_IMPORT_PATH = "/src/main/resources/files/xml/companies.xml";
    private final CompanyRepository companyRepository;
    private final CountryService countryService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public CompanyServiceImpl(CompanyRepository companyRepository, CountryService countryService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.companyRepository = companyRepository;
        this.countryService = countryService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.companyRepository.count() > 0;
    }

    @Override
    public String readCompaniesFromFile() throws IOException {
        return Files.readString(Path.of(XML_IMPORT_PATH));
    }

    @Override
    public String importCompanies() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        CompaniesSeedDto companiesSeedDto = this.xmlParser.fromFile(XML_IMPORT_PATH, CompaniesSeedDto.class);

        for (CompanySeedDto companySeedDto: companiesSeedDto.getCompanySeedDtos()) {
            if(this.companyRepository.findByName(companySeedDto.getName()).isPresent() ||
                    !this.validationUtil.isValid(companySeedDto)) {

                sb.append("Invalid company")
                        .append(System.lineSeparator());
                continue;
            }

            Company company = this.modelMapper.map(companySeedDto, Company.class);
            Country country = this.countryService.getCountryById(companySeedDto.getCountryId());
            country.getCompanies().add(company);

            company.setCountry(country);
            this.companyRepository.save(company);
            sb.append(String.format("Successfully imported company %s - %d",
                            company.getName(), company.getCountry().getId()))
                    .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    @Override
    public Company getCompanyById(Long companyId) {
        return this.companyRepository.findById(companyId).orElse(null);
    }

    @Override
    public Optional<Company> getCompanyId(Long companyId) {
        return this.companyRepository.findById(companyId);
    }
}
