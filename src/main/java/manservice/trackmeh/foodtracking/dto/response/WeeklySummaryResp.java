package manservice.trackmeh.foodtracking.dto.response;

import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import manservice.trackmeh.foodtracking.repository.UserNutritionLogsRepository.UserWeeklySummary;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class WeeklySummaryResp {
    private LocalDate startDate;
    private LocalDate endDate;
    private UserWeeklySummary weeklySummary;
}
