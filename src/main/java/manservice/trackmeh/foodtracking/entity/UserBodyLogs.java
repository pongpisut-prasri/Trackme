package manservice.trackmeh.foodtracking.entity;

import java.math.BigDecimal;
import java.time.LocalDate;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "user_body_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserBodyLogs extends BaseEntity {

    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    private String id;

    @Column(name = "user_id", nullable = false)
    private String userId;
    @Column(name = "measured_at", nullable = false)
    private LocalDate measuredAt;

    // // ===== ค่าเมตริกหลัก =====

    // /** ส่วนสูง (เซนติเมตร) */
    // @DecimalMin(value = "0.0", inclusive = false)
    // @Digits(integer = 6, fraction = 2)
    // @Column(name = "height_cm", precision = 6, scale = 2)
    // private BigDecimal heightCm;

    /** น้ำหนัก (กิโลกรัม) */
    @DecimalMin(value = "0.0", inclusive = false)
    @Digits(integer = 6, fraction = 2)
    @Column(name = "weight_kg", precision = 6, scale = 2)
    private BigDecimal weightKg;

    /** ไขมันในร่างกาย (%) 0–100 */
    @DecimalMin("0.0")
    @DecimalMax("100.0")
    @Digits(integer = 5, fraction = 2)
    @Column(name = "body_fat_percent", precision = 5, scale = 2)
    private BigDecimal bodyFatPercent;

    // // ===== รอบสัดส่วน (เซนติเมตร) =====
    // @Digits(integer = 6, fraction = 2)
    // @Column(name = "neck_cm", precision = 6, scale = 2)
    // private BigDecimal neckCm;

    // @Digits(integer = 6, fraction = 2)
    // @Column(name = "shoulder_cm", precision = 6, scale = 2)
    // private BigDecimal shoulderCm;

    // @Digits(integer = 6, fraction = 2)
    // @Column(name = "chest_cm", precision = 6, scale = 2)
    // private BigDecimal chestCm;

    // @Digits(integer = 6, fraction = 2)
    // @Column(name = "underbust_cm", precision = 6, scale = 2)
    // private BigDecimal underbustCm;

    // @Digits(integer = 6, fraction = 2)
    // @Column(name = "waist_cm", precision = 6, scale = 2)
    // private BigDecimal waistCm;

    // @Digits(integer = 6, fraction = 2)
    // @Column(name = "abdomen_cm", precision = 6, scale = 2)
    // private BigDecimal abdomenCm;

    // @Digits(integer = 6, fraction = 2)
    // @Column(name = "hip_cm", precision = 6, scale = 2)
    // private BigDecimal hipCm;

    // // แขน
    // @Digits(integer = 6, fraction = 2)
    // @Column(name = "bicep_left_cm", precision = 6, scale = 2)
    // private BigDecimal bicepLeftCm;

    // @Digits(integer = 6, fraction = 2)
    // @Column(name = "bicep_right_cm", precision = 6, scale = 2)
    // private BigDecimal bicepRightCm;

    // @Digits(integer = 6, fraction = 2)
    // @Column(name = "forearm_left_cm", precision = 6, scale = 2)
    // private BigDecimal forearmLeftCm;

    // @Digits(integer = 6, fraction = 2)
    // @Column(name = "forearm_right_cm", precision = 6, scale = 2)
    // private BigDecimal forearmRightCm;

    // // ขา
    // @Digits(integer = 6, fraction = 2)
    // @Column(name = "thigh_left_cm", precision = 6, scale = 2)
    // private BigDecimal thighLeftCm;

    // @Digits(integer = 6, fraction = 2)
    // @Column(name = "thigh_right_cm", precision = 6, scale = 2)
    // private BigDecimal thighRightCm;

    // @Digits(integer = 6, fraction = 2)
    // @Column(name = "calf_left_cm", precision = 6, scale = 2)
    // private BigDecimal calfLeftCm;

    // @Digits(integer = 6, fraction = 2)
    // @Column(name = "calf_right_cm", precision = 6, scale = 2)
    // private BigDecimal calfRightCm;

    // // อื่นๆ
    // @Min(20)
    // @Max(250)
    // @Column(name = "resting_hr_bpm")
    // private Integer restingHeartRateBpm;
}
