package by.vtu.vtuproject.advice;

import by.vtu.vtuproject.exception.UserNotFoundException;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice
public class BookmarkControllerAdvice {

    @ResponseBody
    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<EntityModel<String>> userNotFoundExceptionHandler(UserNotFoundException e) {
        EntityModel<String> model = EntityModel.of(e.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(model);
    }


}
