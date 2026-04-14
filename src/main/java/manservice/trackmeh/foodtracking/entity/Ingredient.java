package manservice.trackmeh.foodtracking.entity;

import java.math.BigDecimal;

import org.hibernate.annotations.GenericGenerator;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;  
import lombok.Setter;

@Entity
@Table(name = "ingredient", indexes = {
        @Index(name = "ingredient_name_idx", columnList = "ingredient_name"),
        @Index(name = "ingredient_name_en_idx", columnList = "ingredient_name_en")
})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ingredient extends BaseEntity {
    @Id
    @GenericGenerator(name = "uuid2", strategy = "uuid2")
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "uuid2")
    private String id;
    @Column(name = "ingredient_name", nullable = false)
    private String ingredientName;
    @Column(name = "ingredient_name_en", nullable = false)
    private String ingredientNameEn;
    @Column(name = "protein", nullable = false)
    private BigDecimal protein;
    @Column(name = "carbohydrate", nullable = false)
    private BigDecimal carbohydrate;
    @Column(name = "fat", nullable = false)
    private BigDecimal fat;
}
