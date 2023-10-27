package ru.practicum.shareit.errorhandling;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingRequestHeaderException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import ru.practicum.shareit.booking.exception.BookingBadRequestException;
import ru.practicum.shareit.booking.exception.BookingNotFoundException;
import ru.practicum.shareit.item.exception.ItemNotAvailableException;
import ru.practicum.shareit.item.exception.ItemNotFoundException;
import ru.practicum.shareit.item.exception.WrongOwnerException;
import ru.practicum.shareit.request.exception.RequestNotFoundException;
import ru.practicum.shareit.user.exception.UserAlreadyExistsException;
import ru.practicum.shareit.user.exception.UserNotFoundException;

import javax.validation.ConstraintViolationException;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Обработчик всех исключений.
 */
@RestControllerAdvice
@Slf4j
public class ErrorHandler {
    /**
     * Обработчик исключения {@link UserNotFoundException}.
     * Возникает когда искомый пользователь не найден
     *
     * @param e Исключение {@link UserNotFoundException}
     * @return Объект {@link ErrorResponse} с информацией об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleUserNotFoundException(final UserNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обработчик исключения {@link UserAlreadyExistsException}.
     * Возникает когда пользователь уже существует
     *
     * @param e Исключение {@link UserAlreadyExistsException}
     * @return Объект {@link ErrorResponse} с информацией об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.CONFLICT)
    public ErrorResponse handleUserAlreadyExistException(final UserAlreadyExistsException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обработчик исключения {@link WrongOwnerException}.
     * Возникает, когда пользователь обращается не к своему предмету
     *
     * @param e Исключение {@link WrongOwnerException}
     * @return Объект {@link ErrorResponse} с информацией об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public ErrorResponse handleWrongOwnerException(final WrongOwnerException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обработчик исключения {@link ItemNotFoundException}.
     * Возникает когда искомый предмет не найден
     *
     * @param e Исключение {@link ItemNotFoundException}
     * @return Объект {@link ErrorResponse} с информацией об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleItemNotFoundException(final ItemNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }


    /**
     * Обработчик исключения {@link ItemNotAvailableException}.
     * Возникает когда искомый предмет не доступен
     *
     * @param e Исключение {@link ItemNotAvailableException}
     * @return Объект {@link ErrorResponse} с информацией об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleItemNotAvailableException(final ItemNotAvailableException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обработчик исключения {@link RequestNotFoundException}.
     * Возникает когда искомый запрос не найден
     *
     * @param e Исключение {@link RequestNotFoundException}
     * @return Объект {@link ErrorResponse} с информацией об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleRequestNotFoundException(final RequestNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обработчик исключения {@link BookingNotFoundException}.
     * Возникает когда искомая бронь не найдена
     *
     * @param e Исключение {@link BookingNotFoundException}
     * @return Объект {@link ErrorResponse} с информацией об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.NOT_FOUND)
    public ErrorResponse handleBookingNotFoundException(final BookingNotFoundException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обработчик исключения {@link BookingBadRequestException}.
     * Возникает когда искомая бронь неверна
     *
     * @param e Исключение {@link BookingBadRequestException}
     * @return Объект {@link ErrorResponse} с информацией об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleBookingBadRequestException(final BookingBadRequestException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обработчик исключения {@link ConstraintViolationException}.
     * Возникает, когда действие нарушает ограничение на структуру модели
     *
     * @param e Исключение {@link ConstraintViolationException}
     * @return Список всех нарушений {@link Violation}
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<Violation> onConstraintValidationException(ConstraintViolationException e) {
        List<Violation> collect = e.getConstraintViolations().stream().map(violation -> new Violation(violation.getPropertyPath().toString(), violation.getMessage())).collect(Collectors.toList());
        log.warn(collect.toString());
        return collect;
    }

    /**
     * Обработчик исключения {@link MethodArgumentNotValidException}.
     * Возникает когда проверка аргумента с аннотацией @Valid не удалась
     *
     * @param e Исключение {@link MethodArgumentNotValidException}
     * @return Список всех нарушений {@link Violation}
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public List<Violation> onMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<Violation> collect = e.getBindingResult().getFieldErrors().stream().map(violation -> new Violation(violation.getField(), violation.getDefaultMessage())).collect(Collectors.toList());
        log.error(collect.toString());
        return collect;
    }

    /**
     * Обработчик исключения {@link HttpRequestMethodNotSupportedException}.
     * Возникает когда обработчик запросов не поддерживает определенный метод запроса
     *
     * @param e Исключение {@link HttpRequestMethodNotSupportedException}
     * @return Объект {@link ErrorResponse} с информацией об ошибке
     */
    @ExceptionHandler
    @ResponseStatus(HttpStatus.METHOD_NOT_ALLOWED)
    public ErrorResponse handleHttpRequestMethodNotSupportedException(final HttpRequestMethodNotSupportedException e) {
        return new ErrorResponse(e.getMessage());
    }

    @ExceptionHandler
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorResponse handleMissingRequestHeaderException(final MissingRequestHeaderException e) {
        return new ErrorResponse(e.getMessage());
    }

    /**
     * Обработчик всевозможных исключений во время работы программы.
     *
     * @param e Исключение {@link Throwable}
     * @return Объект {@link ErrorResponse} с информацией об ошибке
     */
    @ExceptionHandler(Throwable.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ErrorResponse handleThrowable(final Throwable e) {
        log.error(e.getMessage());
        e.printStackTrace();
        return new ErrorResponse("Произошла непредвиденная ошибка.");
    }
}