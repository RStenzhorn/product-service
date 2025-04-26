package de.rjst.ps.api;

import de.rjst.ps.api.model.ErrorResponse;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.MessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.HandlerMethodValidationException;

import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.stream.Collectors;

@RestControllerAdvice
public class RestExceptionHandler {

    private static final String DOUBLE_POINT = ": ";
    private static final String DELIMITER = ", ";
    private static final String DELIMITER_NEW_LINE = ",\n";


    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ErrorResponse handleMethodArgumentNotValidException(final HttpServletRequest servletWebRequest, final MethodArgumentNotValidException ex) {
        final var bindingResult = ex.getBindingResult();
        final var fieldErrors = bindingResult.getFieldErrors();
        final var errorMessage = fieldErrors.stream()
                .map(error -> error.getField() + DOUBLE_POINT + error.getDefaultMessage())
                .collect(Collectors.joining(DELIMITER_NEW_LINE));

        return getErrorResponse(servletWebRequest, errorMessage);
    }

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(HandlerMethodValidationException.class)
    public ErrorResponse handleHandlerMethodValidationException(final HttpServletRequest servletWebRequest, final HandlerMethodValidationException ex) {
        final var errors = ex.getParameterValidationResults();
        final var errorMessage = errors.stream()
                .map(error ->
                        error.getMethodParameter().getParameterName() + DOUBLE_POINT +
                                error.getResolvableErrors()
                                        .stream()
                                        .map(MessageSourceResolvable::getDefaultMessage)
                                        .collect(Collectors.joining(DELIMITER)))
                .collect(Collectors.joining(DELIMITER_NEW_LINE));
        return getErrorResponse(servletWebRequest, errorMessage);
    }


    private static ErrorResponse getErrorResponse(final HttpServletRequest servletWebRequest, final String reason) {
        final var errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(ZonedDateTime.now(ZoneOffset.UTC));
        errorResponse.setStatus(HttpStatus.BAD_REQUEST.value());
        errorResponse.setError("Bad Request");
        errorResponse.setMessage(reason);
        errorResponse.setPath(servletWebRequest.getRequestURI());
        return errorResponse;
    }

}
