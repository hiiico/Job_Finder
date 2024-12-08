package softuni.exam.models.dto;

import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.DecimalMin;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "job")
@XmlAccessorType(XmlAccessType.FIELD)
public class JobSeedDto {

    @XmlElement
    @Length(min = 2, max = 40)
    private String jobTitle;

    @XmlElement
    @DecimalMin(value = "10.00")
    private double hoursAWeek;

    @XmlElement
    @DecimalMin(value = "300.00")
    private double salary;

    @XmlElement
    @Length(min = 5)
    private String description;

    @XmlElement
    private Long companyId;

    public JobSeedDto() {
    }

    public String getTitle() {
        return jobTitle;
    }

    public void setTitle(String jobTitle) {
        this.jobTitle = jobTitle;
    }

    public double getHoursAWeek() {
        return hoursAWeek;
    }

    public void setHoursAWeek(double hoursAWeek) {
        this.hoursAWeek = hoursAWeek;
    }

    public double getSalary() {
        return salary;
    }

    public void setSalary(double salary) {
        this.salary = salary;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Long getCompanyId() {
        return companyId;
    }

    public void setCompanyId(Long companyId) {
        this.companyId = companyId;
    }
}
