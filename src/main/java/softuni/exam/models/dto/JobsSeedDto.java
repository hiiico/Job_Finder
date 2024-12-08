package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@XmlRootElement(name = "jobs")
@XmlAccessorType(XmlAccessType.FIELD)
public class JobsSeedDto {

    @XmlElement(name = "job")
    private List<JobSeedDto> jobSeedDtos;

    public JobsSeedDto() {
        this.jobSeedDtos = new ArrayList<>();
    }

    public List<JobSeedDto> getJobSeedDtos() {
        return jobSeedDtos;
    }

    public void setJobSeedDtos(List<JobSeedDto> jobSeedDtos) {
        this.jobSeedDtos = jobSeedDtos;
    }
}
