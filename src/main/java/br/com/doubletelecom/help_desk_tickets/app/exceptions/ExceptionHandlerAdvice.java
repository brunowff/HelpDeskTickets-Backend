/**
 * ExceptionHandlerAdvice is a controller advice class that handles exceptions thrown by the application.
 * It provides methods to handle specific exceptions and return appropriate HTTP responses.
 * 
 * <p>This class handles the following exceptions:
 * <ul>
 *   <li>{@link MethodArgumentNotValidException} - Handles validation errors for method arguments.</li>
 *   <li>{@link BaseRuntimeException} - Handles custom runtime exceptions.</li>
 *   <li>{@link Throwable} - Handles all other exceptions.</li>
 * </ul>
 * 
 * <p>Each handler method logs the exception and returns a {@link ResponseEntity} containing an {@link ApiErrorDto}.
 * The {@link ApiErrorDto} includes details such as the timestamp, HTTP status, and a set of {@link ErrorDto} objects.
 * 
 * <p>Dependencies:
 * <ul>
 *   <li>{@link MessageSource} - Used for internationalization of error messages.</li>
 * </ul>
 * 
 * <p>Methods:
 * <ul>
 *   <li>{@link #handlerMethodArgumentNotValid(MethodArgumentNotValidException)} - Handles validation errors.</li>
 *   <li>{@link #handlerBaseException(Throwable)} - Handles custom runtime exceptions.</li>
 *   <li>{@link #handlerMethodThrowable(Throwable)} - Handles all other exceptions.</li>
 *   <li>{@link #buildError(String, String)} - Builds an {@link ErrorDto} object.</li>
 *   <li>{@link #baseErrorBuilder(HttpStatus, Set)} - Builds an {@link ApiErrorDto} object.</li>
 *   <li>{@link #bindExceptionKeywords(Map, String)} - Binds exception keywords to a message template.</li>
 *   <li>{@link #getResponseStatus(Throwable)} - Retrieves the HTTP status from the exception's {@link ResponseStatus} annotation.</li>
 * </ul>
 * 
 * <p>Logging:
 * <ul>
 *   <li>Logs the exception class name and message for each handled exception.</li>
 * </ul>
 * 
 * @see MethodArgumentNotValidException
 * @see BaseRuntimeException
 * @see Throwable
 * @see ApiErrorDto
 * @see ErrorDto
 * @see MessageSource
 * @see ResponseStatus
 * 
 * @author 
 * @version
 */
package br.com.doubletelecom.help_desk_tickets.app.exceptions;

import java.util.Date;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import br.com.doubletelecom.help_desk_tickets.app.exceptions.contract.MessageException;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.dtos.ApiErrorDto;
import br.com.doubletelecom.help_desk_tickets.app.exceptions.dtos.ErrorDto;

@ControllerAdvice
public class ExceptionHandlerAdvice {
    private static final String UNKNOWN_ERROR_KEY = "unknown.error";

    private static final Logger logger = LoggerFactory.getLogger(ExceptionHandlerAdvice.class);
    private final MessageSource messageSource;

    public ExceptionHandlerAdvice(MessageSource messageSource) {
        this.messageSource = messageSource;
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiErrorDto> handlerMethodArgumentNotValid(
            MethodArgumentNotValidException exception
    ) {
        logger.error("Exception {}, Message: {}", exception.getClass().getName(), exception.getMessage());
        Set<ErrorDto> errors = exception.getBindingResult()
                .getFieldErrors()
                .stream()
                .map(error -> buildError(error.getCode(), error.getDefaultMessage()))
                .collect(Collectors.toSet());

        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(baseErrorBuilder(HttpStatus.BAD_REQUEST, errors));
    }

    @ExceptionHandler(BaseRuntimeException.class)
    public ResponseEntity<ApiErrorDto> handlerBaseException(Throwable exception) {
        logger.error("Exception {}", exception.getClass().getName());
        MessageException messageException = (MessageException) exception;
        ErrorDto error = buildError(messageException.getExceptionKey(),
                bindExceptionKeywords(messageException.getMapDetails(),messageException.getExceptionKey()));

        Set<ErrorDto> errors = Set.of(error);
        ApiErrorDto apiErrorDto = baseErrorBuilder(getResponseStatus(exception), errors);

        return ResponseEntity
                .status(getResponseStatus(exception))
                .body(apiErrorDto);
    }

    @ExceptionHandler(Throwable.class)
    public ResponseEntity<ApiErrorDto> handlerMethodThrowable(Throwable exception) {
        logger.error("Exception {}, Message: {}", exception.getClass().getName(), exception.getMessage());
        Set<ErrorDto> errors = Set.of(buildError(UNKNOWN_ERROR_KEY, exception.getMessage()));
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(baseErrorBuilder(HttpStatus.INTERNAL_SERVER_ERROR, errors));
    }

    private ErrorDto buildError(String code, String message) {
        return new ErrorDto(code, message);
    }

    private ApiErrorDto baseErrorBuilder(HttpStatus httpStatus, Set<ErrorDto> errorList) {
        return new ApiErrorDto(
                new Date(),
                httpStatus.value(),
                httpStatus.name(),
                errorList);
    }

    private String bindExceptionKeywords(Map<String, Object> keywords, String exceptionKey) {
        String message = messageSource.getMessage(exceptionKey, null, LocaleContextHolder.getLocale());
        return Objects.nonNull(keywords) ? new StringSubstitutor(keywords).replace(message) : message;
    }

    private HttpStatus getResponseStatus(Throwable exception) {
        ResponseStatus responseStatus = exception.getClass().getAnnotation(ResponseStatus.class);
        if (exception.getClass().getAnnotation(ResponseStatus.class) == null) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
        return responseStatus.value();
    }
}
