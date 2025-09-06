package manservice.trackmeh.foodtracking.dto.response;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import manservice.trackmeh.utils.Constant.HTTP_RESPONSE;

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
        this.httpStatusCode = HTTP_RESPONSE.SUCCESS.code();
        this.description = HTTP_RESPONSE.SUCCESS.description();
    }

    public BaseResponse(Object object) {
        this.httpStatusCode = HTTP_RESPONSE.SUCCESS.code();
        this.content = object;
        this.description = HTTP_RESPONSE.SUCCESS.description();
    }

    public BaseResponse(String code, Object object) {
        this.httpStatusCode = code;
        this.description = HTTP_RESPONSE.SUCCESS.description();
        if (code.equals(HTTP_RESPONSE.BAD_REQUEST.code())) {
            this.description = HTTP_RESPONSE.BAD_REQUEST.description();
        }
        this.content = object;
    }
}
