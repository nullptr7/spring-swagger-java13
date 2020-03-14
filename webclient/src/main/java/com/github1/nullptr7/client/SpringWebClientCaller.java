package com.github1.nullptr7.client;

import com.github.nullptr7.models.Employee;
import com.github1.nullptr7.client.retry.RetryLogic;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.function.UnaryOperator;

@Slf4j
public class SpringWebClientCaller {

    private WebClient webClient;

    public SpringWebClientCaller(WebClient webClient) {
        this.webClient = webClient;
    }

    /**
     * Returns List of {@link Employee} objects
     */
    public Supplier<List<Employee>> getEmployees = () ->
            webClient.get()
                     .uri("/all")
                     .retrieve()
                     .bodyToFlux(Employee.class)
                     .collectList()
                     .block();

    /**
     * Returns {@link Employee} by Id
     */
    public Function<Integer, Employee> getEmpById = id -> {
        try {
            return webClient.get()
                            .uri("/{id}", id)
                            .retrieve()
                            .bodyToMono(Employee.class)
                            .block();
        } catch (WebClientResponseException e) {
            log.error("Error response code is {} and body {}", e.getRawStatusCode(), e.getResponseBodyAsString());
            throw e;
        } catch (Exception e) {
            log.error("Exception in getEmpId - {}", e.getMessage());
            throw e;
        }
    };

    /**
     * Returns {@link Employee} by Id
     */
    public Function<Integer, Employee> getEmpByIdWithRetry = id ->
            webClient.get()
                     .uri("/{id}", id)
                     .retrieve()
                     .bodyToMono(Employee.class)
                     .retryWhen(RetryLogic.fixedRetry)
                     .block();

    /**
     * Returns {@link Employee} with error handling mechanism
     */
    public Function<Integer, Employee> getEmpByIdWithErrorHandling = id ->
            webClient.get()
                     .uri("/{id}", id)
                     .retrieve()
                     .onStatus(HttpStatus::is4xxClientError, RetryLogic::handle4xxErrorResponse)
                     .onStatus(HttpStatus::is5xxServerError, RetryLogic::handle5xxErrorResponse)
                     .bodyToMono(Employee.class)
                     .block();

    /**
     * Returns {@link Employee} object on successful insertion of employee
     */
    public UnaryOperator<Employee> addEmployee = employee -> {
        try {
            return webClient.post()
                            .uri("/employee")
                            .bodyValue(employee)
                            .retrieve()
                            .bodyToMono(Employee.class)
                            .block();
        } catch (WebClientResponseException ex) {
            log.error("Error Response Code is: {} with message {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
            throw ex;
        } catch (Exception ex) {
            log.error("Exception while adding employee - caused by {}", ex.getMessage());
            throw ex;
        }
    };

    /**
     * Returns List of {@link Employee} objects matching with given name
     */
    public Function<String, List<Employee>> getEmpByName = name -> {
        try {
            final String urlString = UriComponentsBuilder.fromUriString("/employeeName")
                                                         .queryParam("employee_name", name)
                                                         .build()
                                                         .toUriString();
            return webClient.get()
                            .uri(urlString)
                            .retrieve()
                            .bodyToFlux(Employee.class)
                            .collectList()
                            .block();
        } catch (WebClientResponseException ex) {
            log.error("Error in getEmpByName status {} and body {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
            throw ex;
        } catch (Exception ex) {
            log.error("Error while returning employee name {}", ex.getMessage());
            throw ex;
        }
    };

    public Function<Integer, UnaryOperator<Employee>> updateEmployee = id -> employee -> {
        try {
            return webClient.put()
                            .uri("/employee/{id}", id)
                            .bodyValue(employee)
                            .retrieve()
                            .bodyToMono(Employee.class)
                            .block();
        } catch (WebClientResponseException ex) {
            log.error("Error in updateEmployee status {} and body {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
            throw ex;
        } catch (Exception ex) {
            log.error("Error while updating employee {}", ex.getMessage());
            throw ex;
        }
    };

    public Function<Long, String> deleteEmployee = id -> {
        try {
            return webClient.delete()
                            .uri("/employee/{id}", id)
                            .retrieve()
                            .bodyToMono(String.class)
                            .block();
        } catch (WebClientResponseException ex) {
            log.error("Error in deleteEmployee status {} and body {}", ex.getRawStatusCode(), ex.getResponseBodyAsString());
            throw ex;
        } catch (Exception ex) {
            log.error("Error while deleting employee {}", ex.getMessage());
            throw ex;
        }
    };


}