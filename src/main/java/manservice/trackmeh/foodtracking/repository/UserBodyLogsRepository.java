package manservice.trackmeh.foodtracking.repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import manservice.trackmeh.foodtracking.entity.UserBodyLogs;

@Repository
public interface UserBodyLogsRepository extends JpaRepository<UserBodyLogs, String> {

    @Query(value = """
            select ubl.weight_kg  as weight, ubl.measured_at as log_date ,TO_CHAR(ubl.measured_at, 'Day') AS day_name
            from project.user_body_logs ubl
            where ubl.user_id =:userId and ubl.measured_at between :startDate and  :endDate
            order by log_date
                        """, nativeQuery = true)
    List<WeightLogResp> getLogsInRangeDate(@Param("userId") String userId, @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate);

    @Query("SELECT u FROM UserBodyLogs u WHERE u.measuredAt =:measuredAt AND u.userId = :userId")
    java.util.Optional<UserBodyLogs> findByMeasuredAtAndUserId(@Param("measuredAt") LocalDate measuredAt,
            @Param("userId") String userId);

    interface WeightLogResp {
        LocalDate getLogDate();

        BigDecimal getWeight();

        String getDayName();
    }

}
