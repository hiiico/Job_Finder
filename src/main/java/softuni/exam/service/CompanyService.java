package softuni.exam.service;


import softuni.exam.models.entity.Company;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.util.Optional;

public interface CompanyService {

    boolean areImported();

    String readCompaniesFromFile() throws IOException;

    String importCompanies() throws IOException, JAXBException;

    Company getCompanyById(Long companyId);

    Optional<Company> getCompanyId(Long companyId);

}
