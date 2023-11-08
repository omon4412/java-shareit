package ru.practicum.shareit.errorhandling;

import org.hibernate.validator.internal.engine.path.PathImpl;
import org.junit.jupiter.api.Test;
import org.springframework.core.MethodParameter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import ru.practicum.shareit.booking.exception.BookingBadRequestException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.item.exception.ItemNotAvailableException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.WrongOwnerException;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.user.exception.UserAlreadyExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Path;
import javax.validation.metadata.ConstraintDescriptor;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.mock;

class ErrorHandlerTest {

    @Test
    void handleUserNotFoundException() {
        UserNotFoundException exception = new UserNotFoundException("User not found");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorResponse response = errorHandler.handleUserNotFoundException(exception);
        assertEquals("User not found", response.getError());
    }

    @Test
    void handleUserAlreadyExistException() {
        UserAlreadyExistsException exception = new UserAlreadyExistsException("User is exist");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorResponse response = errorHandler.handleUserAlreadyExistException(exception);
        assertEquals("User is exist", response.getError());
    }

    @Test
    void handleWrongOwnerException() {
        WrongOwnerException exception = new WrongOwnerException("Wrong owner");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorResponse response = errorHandler.handleWrongOwnerException(exception);
        assertEquals("Wrong owner", response.getError());
    }

    @Test
    void handleItemNotFoundException() {
        ItemNotFoundException exception = new ItemNotFoundException("Item not found");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorResponse response = errorHandler.handleItemNotFoundException(exception);
        assertEquals("Item not found", response.getError());
    }

    @Test
    void handleItemNotAvailableException() {
        ItemNotAvailableException exception = new ItemNotAvailableException("Item not available");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorResponse response = errorHandler.handleItemNotAvailableException(exception);
        assertEquals("Item not available", response.getError());
    }

    @Test
    void handleRequestNotFoundException() {
        RequestNotFoundException exception = new RequestNotFoundException("Request not found");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorResponse response = errorHandler.handleRequestNotFoundException(exception);
        assertEquals("Request not found", response.getError());
    }

    @Test
    void handleBookingNotFoundException() {
        BookingNotFoundException exception = new BookingNotFoundException("Booking not found");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorResponse response = errorHandler.handleBookingNotFoundException(exception);
        assertEquals("Booking not found", response.getError());
    }

    @Test
    void handleBookingBadRequestException() {
        BookingBadRequestException exception = new BookingBadRequestException("Booking bad request");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorResponse response = errorHandler.handleBookingBadRequestException(exception);
        assertEquals("Booking bad request", response.getError());
    }

    @Test
    void onConstraintValidationException() {
        ErrorHandler errorHandler = new ErrorHandler();

        Set<ConstraintViolation<?>> violations = new HashSet<>();
        ConstraintViolation<?> violation1 = createViolation("field1", "Message1");
        ConstraintViolation<?> violation2 = createViolation("field2", "Message2");
        violations.add(violation1);
        violations.add(violation2);

        ConstraintViolationException exception = new ConstraintViolationException("Validation failed", violations);

        List<Violation> response = errorHandler.onConstraintValidationException(exception);

        assertEquals(2, response.size());
    }

    @Test
    void onMethodArgumentNotValidException() {
        ErrorHandler errorHandler = new ErrorHandler();

        BindingResult bindingResult = mock(BindingResult.class);
        MethodParameter methodParameter = mock(MethodParameter.class);

        List<FieldError> fieldErrors = new ArrayList<>();
        fieldErrors.add(createFieldError("field1", "Message1"));
        fieldErrors.add(createFieldError("field2", "Message2"));
        bindingResult.addError(fieldErrors.get(0));
        bindingResult.addError(fieldErrors.get(1));

        MethodArgumentNotValidException exception = new MethodArgumentNotValidException(methodParameter, bindingResult);

        List<Violation> response = errorHandler.onMethodArgumentNotValidException(exception);

        assertEquals(0, response.size());
    }

    private FieldError createFieldError(String field, String message) {
        return new FieldError("objectName", field, message);
    }

    @Test
    void handleHttpRequestMethodNotSupportedException() {
        HttpRequestMethodNotSupportedException exception = new HttpRequestMethodNotSupportedException("POST");
        ErrorHandler errorHandler = new ErrorHandler();
        ErrorResponse response = errorHandler.handleHttpRequestMethodNotSupportedException(exception);
        assertEquals("Request method 'POST' not supported", response.getError());
    }

    @Test
    void handleMissingRequestHeaderException() {
        MissingRequestHeaderException exception = null;
        assertNull(exception);
    }

    // Вспомогательный метод для создания ConstraintViolation
    private ConstraintViolation<?> createViolation(String property, String message) {
        return new ConstraintViolation<>() {
            @Override
            public String getMessage() {
                return message;
            }

            @Override
            public String getMessageTemplate() {
                return null;
            }

            @Override
            public Object getRootBean() {
                return null;
            }

            @Override
            public Class<Object> getRootBeanClass() {
                return null;
            }

            @Override
            public Object getLeafBean() {
                return null;
            }

            @Override
            public Object[] getExecutableParameters() {
                return new Object[0];
            }

            @Override
            public Object getExecutableReturnValue() {
                return null;
            }

            @Override
            public Path getPropertyPath() {
                return PathImpl.createPathFromString(property);
            }

            @Override
            public Object getInvalidValue() {
                return null;
            }

            @Override
            public ConstraintDescriptor<?> getConstraintDescriptor() {
                return null;
            }

            @Override
            public <U> U unwrap(Class<U> type) {
                return null;
            }
        };
    }
}