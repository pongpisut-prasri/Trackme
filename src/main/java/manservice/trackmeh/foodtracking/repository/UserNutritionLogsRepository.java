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

    @Query(value = """
            with body_logs as (
                select ubl.measured_at,ubl.weight_kg as weight from project.user_body_logs ubl
                where ubl.user_id =?1
                order by ubl.measured_at desc
            ),
            logs_data as (
                select
                    TO_CHAR(unl.log_date, 'Day') AS day,
                    log_date,
                    sum(unl.calories) as total_calories,
                    sum(unl.proteins) as total_protein ,
                    sum(unl.carbohydrates) as total_carbohydrate,
                    sum(unl.fats) as total_fat from project.user_nutrition_logs unl
                where unl.user_id =?1       and
                ( (cast(?2 as date) is null or cast(?3 as date) is null)
                    or unl.log_date between ?2 and ?3 )
                group by unl.log_date,unl.user_id
                order by unl.log_date
            )
            select * from logs_data as ld
            left join body_logs bl on bl.measured_at = ld.log_date
                                """, nativeQuery = true)
    Page<UserDailySummaryPagination> getSummaryPagination(String userId, LocalDate startDate, LocalDate endDate,
            Pageable pageable);

    @Query(value = """
            with body_logs as (
            	select avg(weight_kg) as weight,user_id from project.user_body_logs ubl
            	where ubl.measured_at between ?2 and ?3 and ubl.user_id = ?1
            	group by user_id
            ),
            log_data as (
            	select
            	sum(unl.calories) as calories,
            	sum(unl.proteins) as proteins,
            	sum(unl.carbohydrates ) as carbohydrates,
            	sum(unl.fats) as fats,
            	user_id
            	from project.user_nutrition_logs unl
            --	inner join body_logs ubl on unl.user_id = ubl.user_id
            	where unl.user_id =?1 and unl.log_date between ?2 and ?3
                            	group by unl.user_id, unl.log_date
            )
            select
            	avg(ld.fats) as average_fat,
            		avg(ld.calories) as average_calories,
            	avg(ld.proteins) as average_protein,
            	avg(ld.carbohydrates ) as average_carbohydrate,
            	avg(bl.weight) as average_weight
            	from log_data ld
            	inner join body_logs bl on ld.user_id = bl.user_id
                                """, nativeQuery = true)
    UserWeeklySummary getUserSummary(String userId, LocalDate startOfWeek, LocalDate endOfWeek);

    public static interface UserWeeklySummary {

        BigDecimal getAverageCalories();

        BigDecimal getAverageCarbohydrate();

        BigDecimal getAverageFat();

        BigDecimal getAverageProtein();

        BigDecimal getAverageWeight();
    }

    interface UserDailySummaryPagination {

        String getDay();

        LocalDate getLogDate();

        BigDecimal getTotalCalories();

        BigDecimal getTotalCarbohydrate();

        BigDecimal getTotalFat();

        BigDecimal getTotalProtein();

        BigDecimal getWeight();
    }

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
