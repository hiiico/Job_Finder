package softuni.exam.repository;

import jdk.dynalink.linker.LinkerServices;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import softuni.exam.models.entity.Job;

import java.util.List;

@Repository
public interface JobRepository extends JpaRepository<Job, Long> {

List<Job> findAllJobBySalaryGreaterThanEqualAndHoursAWeekLessThanEqualOrderBySalaryDesc(double salary, double hours);
}
