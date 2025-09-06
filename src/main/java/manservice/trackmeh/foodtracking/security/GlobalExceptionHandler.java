package manservice.trackmeh.foodtracking.security;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.MethodParameter;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.RequestBodyAdvice;

import lombok.SneakyThrows;
import manservice.trackmeh.foodtracking.dto.response.BaseResponse;
import manservice.trackmeh.foodtracking.security.jwt.JwtUtils;

@RestControllerAdvice
public class GlobalExceptionHandler implements RequestBodyAdvice {

    @Autowired
    JwtUtils jwtUtils;

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ResponseEntity<?> handleValidationExceptions(MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error -> {
            errors.put(error.getField(), error.getDefaultMessage());
        });
        // BaseStatusResp status = new BaseStatusResp();
        // status.setCode(ApiReturn.BAD_REQUEST.code());
        // status.setDescription(ApiReturn.BAD_REQUEST.description());
        // BaseResponse res = new BaseResponse(errors);
        // res.setStatus(status);
        return ResponseEntity.badRequest().body(new BaseResponse("400",errors));
    }

    @Override
    public HttpInputMessage beforeBodyRead(HttpInputMessage inputMessage, MethodParameter parameter,
            Type targetType, Class<? extends HttpMessageConverter<?>> converterType)
            throws IOException {

        return inputMessage;
    }

    @Override
    public boolean supports(MethodParameter methodParameter,
            Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return true;
    }

    @Override
    @SneakyThrows
    public Object afterBodyRead(Object body, HttpInputMessage inputMessage,
            MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {

        return body;
    }

    @Override
    public Object handleEmptyBody(Object body, HttpInputMessage inputMessage,
            MethodParameter parameter, Type targetType, Class<? extends HttpMessageConverter<?>> converterType) {
        return body;
    }

}