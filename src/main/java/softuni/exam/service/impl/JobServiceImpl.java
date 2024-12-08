package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.JobSeedDto;
import softuni.exam.models.dto.JobsSeedDto;
import softuni.exam.models.entity.Company;
import softuni.exam.models.entity.Job;
import softuni.exam.repository.JobRepository;
import softuni.exam.service.CompanyService;
import softuni.exam.service.JobService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.validation.Valid;
import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class JobServiceImpl implements JobService {

    private final static String XML_IMPORT_PATH = "/src/main/resources/files/xml/jobs.xml";
    private final JobRepository jobRepository;
    private final CompanyService companyService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    public JobServiceImpl(JobRepository jobRepository, CompanyService companyService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.jobRepository = jobRepository;
        this.companyService = companyService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }


    @Override
    public boolean areImported() {
        return this.jobRepository.count() > 0;
    }

    @Override
    public String readJobsFileContent() throws IOException {
        return Files.readString(Path.of(XML_IMPORT_PATH));
    }

    @Override
    public String importJobs() throws IOException, JAXBException {

        StringBuilder sb = new StringBuilder();
        JobsSeedDto jobsSeedDtos = this.xmlParser.fromFile(XML_IMPORT_PATH, JobsSeedDto.class);

        for (JobSeedDto jobSeedDto : jobsSeedDtos.getJobSeedDtos()) {
            if (this.companyService.getCompanyId(jobSeedDto.getCompanyId()).isEmpty() ||
                    !this.validationUtil.isValid(jobSeedDto)) {
                sb.append("Invalid job")
                        .append(System.lineSeparator());
                continue;
            }
            Job job = this.modelMapper.map(jobSeedDto,Job.class);
            Company company = this.companyService.getCompanyById(jobSeedDto.getCompanyId());
            job.setCompany(company);
            job.getCompanies().add(company);
            company.getJobs().add(job);
            company.getJobsCompany().add(job);

            this.jobRepository.save(job);
            sb.append(String.format("Successfully imported job %s",
                    job.getTitle()))
                    .append(System.lineSeparator());
        }
        return sb.toString().trim();
    }

    @Override
    public String getBestJobs() {

        StringBuilder sb = new StringBuilder();

        this.jobRepository.findAllJobBySalaryGreaterThanEqualAndHoursAWeekLessThanEqualOrderBySalaryDesc(5000.00, 30.00)
                .forEach(job -> sb.append(String.format(
                                "Job title %s\n" +
                                "-Salary: %.2f$\n" +
                                "--Hours a week: %.2fh.",
                        job.getTitle(),
                        job.getSalary(),
                        job.getHoursAWeek()
                )).append(System.lineSeparator()));

        return sb.toString().trim();
    }
}
