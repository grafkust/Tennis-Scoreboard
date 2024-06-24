package project.scoreboard.util;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class HandlerException {

    @ExceptionHandler
    private void handleExceptions(IllegalArgumentException e){

    }



}
