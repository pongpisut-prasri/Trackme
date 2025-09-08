package manservice.trackmeh.foodtracking.repository;

import org.springframework.data.domain.PageImpl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import manservice.trackmeh.foodtracking.entity.UserModel;

@Repository
public interface UserModelRepository extends JpaRepository<UserModel, String> {

    @Query(value = """
            select * from project.user_data
            where username = ?1
            """, nativeQuery = true)
    UserModel findByUserName(String username);

}
