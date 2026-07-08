package dev.yesserm.demosb4.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ProblemDetail;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(InvalidCredentialsException.class)
    ProblemDetail invalidCredentials(InvalidCredentialsException ex) {
        return problem(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler({TokenExpiredException.class, UnauthorizedException.class})
    ProblemDetail unauthorized(RuntimeException ex) {
        return problem(HttpStatus.UNAUTHORIZED, ex.getMessage());
    }

    @ExceptionHandler(ForbiddenException.class)
    ProblemDetail forbidden(ForbiddenException ex) {
        return problem(HttpStatus.FORBIDDEN, ex.getMessage());
    }

    @ExceptionHandler(ExternalApiException.class)
    ProblemDetail externalApi(ExternalApiException ex) {
        return problem(HttpStatus.BAD_GATEWAY, ex.getMessage());
    }

    @ExceptionHandler(IllegalArgumentException.class)
    ProblemDetail badRequest(IllegalArgumentException ex) {
        return problem(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({InvalidPasswordException.class, EmailAlreadyExistsException.class})
    ProblemDetail invalidUserRequest(RuntimeException ex) {
        return problem(HttpStatus.BAD_REQUEST, ex.getMessage());
    }

    @ExceptionHandler({UserNotFoundException.class, RoleNotFoundException.class})
    ProblemDetail notFound(RuntimeException ex) {
        return problem(HttpStatus.NOT_FOUND, ex.getMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ProblemDetail validation(MethodArgumentNotValidException ex) {
        return problem(HttpStatus.BAD_REQUEST, "Request validation failed");
    }

    private ProblemDetail problem(HttpStatus status, String detail) {
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setDetail(detail);
        return problem;
    }
}
