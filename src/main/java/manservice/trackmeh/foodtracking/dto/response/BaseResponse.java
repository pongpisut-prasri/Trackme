package manservice.trackmeh.foodtracking.dto.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
// @NoArgsConstructor
// @AllArgsConstructor
@SuperBuilder
public class BaseResponse implements Serializable {
    private String httpStatusCode;
    private String description;
    private Object content;

    public BaseResponse() {
        this.httpStatusCode = "200";
        this.description = "success";
    }

    public BaseResponse(Object object) {
        this.httpStatusCode = "200";
        this.content = object;
        this.description = "success";
    }
}
