package com.github1.nullptr7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringWebClientDemo {

    public static void main(String[] args) {

        SpringApplication.run(SpringWebClientDemo.class, args);
        /*WebClient webClient = WebClient.create("http://localhost:8081/services/v1/employees");
        SpringWebClientCaller caller = new SpringWebClientCaller(webClient);
        final List<Employee> employees = caller.getAllEmployees();
        assertNotNull(employees);
        assertNotEquals(0, employees.size());*/

    }
}
