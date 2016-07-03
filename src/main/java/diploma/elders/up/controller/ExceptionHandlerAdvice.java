package diploma.elders.up.controller;

import diploma.elders.up.NotFoundException;
import diploma.elders.up.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * Created by Simonas on 7/3/2016.
 */
@ControllerAdvice
public class ExceptionHandlerAdvice {

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(value = NotFoundException.class)
    public String exception(NotFoundException exception, Model model) {
        model.addAttribute("errorMessage", exception.getMessage());
        model.addAttribute("errorCode", "Not found");
        return "error";
    }

    @ResponseStatus(HttpStatus.NOT_ACCEPTABLE)
    @ExceptionHandler(value = ValidationException.class)
    public String exception(ValidationException exception, Model model) {
        model.addAttribute("errorMessage", exception.getMessage());
        model.addAttribute("errorCode", "Not acceptable");
        return "error";
    }
}
