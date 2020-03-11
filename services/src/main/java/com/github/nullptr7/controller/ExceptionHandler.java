package com.github.nullptr7.controller;

import lombok.experimental.UtilityClass;
import org.springframework.web.server.ResponseStatusException;

import java.util.function.Function;
import java.util.function.Supplier;

import static org.springframework.http.HttpStatus.INTERNAL_SERVER_ERROR;
import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 *
 * @author Ishan Shah
 *
 * Singleton class to handle the exception based on exceptionType as invoked.
 */
@UtilityClass
public class ExceptionHandler {

    final Function<Long, ResponseStatusException> idNotFound = (id) -> {
        throw new ResponseStatusException(NOT_FOUND, "No Employee Available with the given Id - " + id);
    };

    final Function<String, ResponseStatusException> nameNotFound = (name) -> {
        throw new ResponseStatusException(NOT_FOUND, "No Employee Available with the given name - " + name);
    };

    final Supplier<ResponseStatusException> internalServerError = () -> {
        throw new ResponseStatusException(INTERNAL_SERVER_ERROR, "RunTimeException from Employee Service");
    };
}
