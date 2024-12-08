package softuni.exam.models.dto;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

@XmlRootElement(name = "companies")
@XmlAccessorType(XmlAccessType.FIELD)
public class CompaniesSeedDto {

    @XmlElement(name = "company")
    private List<CompanySeedDto> companySeedDtos;

    public CompaniesSeedDto() {
        this.companySeedDtos = new ArrayList<>();
    }

    public List<CompanySeedDto> getCompanySeedDtos() {
        return companySeedDtos;
    }

    public void setCompanySeedDtos(List<CompanySeedDto> companySeedDtos) {
        this.companySeedDtos = companySeedDtos;
    }
}
