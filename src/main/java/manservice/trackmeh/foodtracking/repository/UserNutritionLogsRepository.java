package manservice.trackmeh.foodtracking.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
                  where unl.user_id = '1' and (
                  	 	select * from project.user_body_logs ubl
                        where ubl.measured_at::date <= Date '2025-09-08' and ubl.user_id ='1'
                        order by ubl.measured_at desc
                        limit 1
                  )
                  group by unl.user_id,unl.log_date::date,ubl.weight_kg""", nativeQuery = true)
    UserNutritionGroupByUserAndLogDate getDailySummarySubQuery(String userId, LocalDate date);

    @Query(value = """
            with body_Logs_latest as (
            	select * from project.user_body_logs ubl
            	where ubl.measured_at::date <= ?2 and ubl.user_id =?1
            	order by ubl.measured_at desc
            	limit 1
            )
             select unl.user_id  ,
                       	SUM(unl.proteins) AS total_proteins,
                       	SUM(unl.carbohydrates ) AS total_carbohydrates,
                       	SUM(unl.fats ) AS total_fats,
                           SUM(unl.calories ) AS total_calories,
                           ubl.weight_kg AS weight
                       from project.user_nutrition_logs unl
                       inner join body_Logs_latest ubl on unl.user_id = ubl.user_id
                       where unl.user_id = ?1 and unl.log_date::date = ?2
                       group by unl.user_id,unl.log_date::date,ubl.weight_kg""", nativeQuery = true)
    UserNutritionGroupByUserAndLogDate getDailySummary(String userId, LocalDate date);

    @Query(value = """
            select
                unl.id ,
                unl.food_name,
                unl.calories,
                unl.proteins,
                unl.carbohydrates,
                unl.fats,
                unl.log_date
            from project.user_nutrition_logs unl
            where unl.user_id =?1
            order by unl.log_date
            """, nativeQuery = true)
    Page<UserNutritionPaginationResp> getLogsPagination(String userId, Pageable pageable);

    interface UserNutritionGroupByUserAndLogDate {
        BigDecimal getTotalCalories();

        BigDecimal getTotalCarbohydrates();

        BigDecimal getTotalFats();

        BigDecimal getTotalProteins();

        String getUserId();

        BigDecimal getWeight();
    }

    interface UserNutritionPaginationResp {
        String getId();

        String getFoodName();

        BigDecimal getProteins();

        BigDecimal getCarbohydrates();

        BigDecimal getFats();

        BigDecimal getCalories();

        LocalDate getLogDate();
    }
}
