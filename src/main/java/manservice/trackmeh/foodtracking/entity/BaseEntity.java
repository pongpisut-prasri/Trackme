package manservice.trackmeh.foodtracking.entity;

import java.io.Serializable;
import java.time.LocalDateTime;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public class BaseEntity implements Serializable {
    @Column(name = "create_by")
    private String createBy;
    @Column(name = "create_date")
    private LocalDateTime createDate;
    @Column(name = "update_by")
    private String updateBy;
    @Column(name = "update_date")
    private LocalDateTime updateDate;
}
