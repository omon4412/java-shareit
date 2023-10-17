package ru.practicum.shareit.errorhandling;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ErrorResponseTest {
    @Test
    void testGetError() {
        String errorMessage = "Текст ошибки";
        ErrorResponse errorResponse = new ErrorResponse(errorMessage);

        assertEquals(errorMessage, errorResponse.getError());
    }

}