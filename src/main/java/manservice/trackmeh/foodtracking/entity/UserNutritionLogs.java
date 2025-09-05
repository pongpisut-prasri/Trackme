package manservice.trackmeh.foodtracking.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.NotFound;
import org.hibernate.annotations.NotFoundAction;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "user_nutrition_logs")
public class UserNutritionLogs extends BaseEntity {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    private String id;

    @Column(name = "food_name")
    private String foodName;

    @Column(name = "proteins", precision = 6, scale = 2)
    private BigDecimal proteins;

    @Column(name = "carbohydrates", precision = 6, scale = 2)
    private BigDecimal carbohydrates;

    @Column(name = "fats", precision = 6, scale = 2)
    private BigDecimal fats;

    @Column(name = "calories", precision = 7, scale = 2)
    private BigDecimal calories;

    @Column(name = "log_date", nullable = false)
    private LocalDate logDate;

    @Column(name = "user_id", nullable = false)
    private String userId;

    // @Column(name = "nutrition_plan_id", nullable = false)
    // private String nutritionPlanId;

    @ManyToOne
    @NotFound(action = NotFoundAction.IGNORE)
    @JoinColumn(name = "user_id", nullable = false, insertable = false, updatable = false)
    private UserModel userModel;

    // @ManyToOne
    // @NotFound(action = NotFoundAction.IGNORE)
    // @JoinColumn(name = "nutrition_plan_id", nullable = false, insertable = false, updatable = false)
    // private UserNutritionPlan userNutritionPlan;
}
