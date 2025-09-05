package manservice.trackmeh.foodtracking.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import manservice.trackmeh.foodtracking.entity.UserNutritionPlan;

@Repository
public interface UserNutritionPlanRepository extends JpaRepository<UserNutritionPlan,String>{
    
    UserNutritionPlan findByUserId(String userId);
}
