package manservice.trackmeh.foodtracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manservice.trackmeh.foodtracking.entity.UserModel;

@Repository
public interface UserModelRepository extends JpaRepository<UserModel, String> {

}
