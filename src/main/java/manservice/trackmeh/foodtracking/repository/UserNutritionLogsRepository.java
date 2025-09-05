package manservice.trackmeh.foodtracking.repository;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import manservice.trackmeh.foodtracking.entity.UserNutritionLogs;

@Repository
public interface UserNutritionLogsRepository extends JpaRepository<UserNutritionLogs, String> {

    @Query(value = """
               select unl.user_id  ,
            	SUM(unl.proteins) AS total_proteins,
            	SUM(unl.carbohydrates ) AS total_carbohydrates,
            	SUM(unl.fats ) AS total_fats,
                SUM(unl.calories ) AS total_calories,
                ubl.weight_kg AS weight
            from project.user_nutrition_logs unl
            inner join project.user_body_logs ubl on unl.user_id = ubl.user_id
            where unl.user_id = ?1 and unl.log_date::date = ?2 and ubl.measured_at::date = ?2
            group by unl.user_id,unl.log_date::date,ubl.weight_kg""", nativeQuery = true)
    UserNutritionGroupByUserAndLogDate getDailySummary(String userId, LocalDate date);

    interface UserNutritionGroupByUserAndLogDate {
        BigDecimal getTotalCalories();

        BigDecimal getTotalCarbohydrates();

        BigDecimal getTotalFats();

        BigDecimal getTotalProteins();

        String getUserId();

        BigDecimal getWeight();
    }
}
