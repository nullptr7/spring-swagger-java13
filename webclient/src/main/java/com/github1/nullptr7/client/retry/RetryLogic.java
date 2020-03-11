package com.github1.nullptr7.client.retry;

import com.github1.nullptr7.client.exception.EmployeeServiceException;
import lombok.experimental.UtilityClass;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.retry.Retry;

import java.time.Duration;

/**
 *
 * @author Ishan Shah
 *
 * Singleton class created for handling error faults received from the RESTful call to
 * employee service.
 *
 * All 4xx flavours of {@link org.springframework.http.HttpStatus} converge to one method only &
 * should be treated as {@link RuntimeException}
 *
 * All 5xx flavours of {@link org.springframework.http.HttpStatus} codes should be treated
 * as application based exceptions
 */
@Slf4j
@UtilityClass
public class RetryLogic {

    public Retry<?> fixedRetry = Retry
            .anyOf(WebClientResponseException.class)
            .fixedBackoff(Duration.ofSeconds(2))
            .retryMax(3)
            .doOnRetry((ex) -> log.info("The Exception is: {}", ex));


    public Mono<RuntimeException> handle4xxErrorResponse(ClientResponse response) {
        Mono<String> errorResponse = response.bodyToMono(String.class);
        return errorResponse.flatMap(message -> {
            log.error("Error response {} with message {}", response.rawStatusCode(), message);
            return Mono.error(new RuntimeException(message));
        });
    }

    public Mono<EmployeeServiceException> handle5xxErrorResponse(ClientResponse response) {
        Mono<String> errorResponse = response.bodyToMono(String.class);
        return errorResponse.flatMap(message -> {
            log.error("Service error {} with message {}", response.rawStatusCode(), message);
            return Mono.error(new EmployeeServiceException(message));
        });
    }

}