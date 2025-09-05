package manservice.trackmeh.foodtracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manservice.trackmeh.foodtracking.entity.UserBodyLogs;

@Repository
public interface UserBodyLogsRepository extends JpaRepository<UserBodyLogs, String> {

}
