package az.tourmate.exceptions;


import az.tourmate.exceptions.address.CountryNotFoundException;
import az.tourmate.exceptions.branch.BranchAlreadyScoredException;
import az.tourmate.exceptions.branch.BranchIsNotFoundException;
import az.tourmate.exceptions.comments.CommentIsNotFoundException;
import az.tourmate.exceptions.file.ProfileIsNotFoundException;
import az.tourmate.exceptions.files.ImageIsWrongException;
import az.tourmate.exceptions.files.SomethingWentWrongInIOException;
import az.tourmate.exceptions.hotel.ManagementIsNotFoundException;
import az.tourmate.exceptions.rooms.RoomIsNotAvailableException;
import az.tourmate.exceptions.rooms.RoomIsNotFoundException;
import az.tourmate.exceptions.users.PasswordIsWrongException;
import az.tourmate.exceptions.users.UserAlreadyHasManagementException;
import az.tourmate.exceptions.users.UserHasNotAccessException;
import az.tourmate.exceptions.users.UserIsNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.lang.NonNull;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler extends ResponseEntityExceptionHandler {


    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  @NonNull HttpHeaders headers,
                                                                  @NonNull HttpStatusCode status,
                                                                  @NonNull WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            errors.put(fieldName, errorMessage);
        });
        logger.info(String.format("Api validation error: %s",errors));
        return new ResponseEntity<>(errors, HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(PasswordIsWrongException.class)
    public ResponseEntity<?> handle(PasswordIsWrongException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ImageIsWrongException.class)
    public ResponseEntity<?> handle(ImageIsWrongException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(BranchIsNotFoundException.class)
    public ResponseEntity<?> handle(BranchIsNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ManagementIsNotFoundException.class)
    public ResponseEntity<?> handle(ManagementIsNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserHasNotAccessException.class)
    public ResponseEntity<?> handle(UserHasNotAccessException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CountryNotFoundException.class)
    public ResponseEntity<?> handle(CountryNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(SomethingWentWrongInIOException.class)
    public ResponseEntity<?> handle(SomethingWentWrongInIOException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(BranchAlreadyScoredException.class)
    public ResponseEntity<?> handle(BranchAlreadyScoredException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserIsNotFoundException.class)
    public ResponseEntity<?> handle(UserIsNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(UserAlreadyHasManagementException.class)
    public ResponseEntity<?> handle(UserAlreadyHasManagementException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(ProfileIsNotFoundException.class)
    public ResponseEntity<?> handle(ProfileIsNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(RoomIsNotFoundException.class)
    public ResponseEntity<?> handle(RoomIsNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }


    @ExceptionHandler(RoomIsNotAvailableException.class)
    public ResponseEntity<?> handle(RoomIsNotAvailableException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CommentIsNotFoundException.class)
    public ResponseEntity<?> handle(CommentIsNotFoundException exception){
        return new ResponseEntity<>(exception.getMessage(),HttpStatus.BAD_REQUEST);
    }
}
