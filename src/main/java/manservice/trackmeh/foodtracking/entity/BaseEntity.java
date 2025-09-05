package manservice.trackmeh.foodtracking.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@MappedSuperclass
public abstract class BaseEntity implements Serializable {
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "update_by")
    private String updateBy;
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
