package sj.noveling.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class DataExistsException extends RuntimeException {

    public DataExistsException(String message) {
        super(message);
    }
}
