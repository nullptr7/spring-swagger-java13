package com.github1.nullptr7.client;

import com.github.nullptr7.ServiceEntryPoint;
import com.github.nullptr7.models.Address;
import com.github.nullptr7.models.Employee;
import com.github.nullptr7.models.security.AdminUser;
import com.github.nullptr7.models.security.AuthenticatedResponse;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.MethodOrderer;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.DEFINED_PORT;

@DisplayName("Employee CRUD operation test case")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@SpringBootTest(classes = ServiceEntryPoint.class, webEnvironment = DEFINED_PORT)
class SpringWebClientCallerTest {

    static SpringWebClientCaller caller;
    static String jwt;

    @BeforeEach
    public void init() {

        if (StringUtils.isEmpty(jwt)) {
            var webClient = WebClient.create("http://localhost:8081/services");
            var user = webClient.post()
                                .uri("/addAdmin")
                                .bodyValue(AdminUser.builder().username("foo").password("foo").build())
                                .accept(MediaType.APPLICATION_JSON)
                                .retrieve()
                                .bodyToMono(AdminUser.class)
                                .block();

            assert user != null;
            jwt = Objects.requireNonNull(webClient.post()
                                                  .uri("/authenticate")
                                                  .bodyValue(user)
                                                  .accept(MediaType.APPLICATION_JSON)
                                                  .retrieve()
                                                  .bodyToMono(AuthenticatedResponse.class)
                                                  .block()).getJwt();
            webClient = WebClient.builder()
                                 .baseUrl("http://localhost:8081/services/v1/employees")
                                 .defaultHeader("Authorization", "Bearer " + jwt)
                                 .build();
            caller = new SpringWebClientCaller(webClient);
        }

    }

    @Test
    @Order(2)
    @DisplayName("Can get all employees")
    void getAllEmployees() {
        final List<Employee> employees = caller.getEmployees.get();
        assertNotNull(employees);
        assertNotEquals(0, employees.size());
    }

    @Test
    @Order(3)
    @DisplayName("Can get employee by ID")
    void getEmployeeById() {
        final Employee employee = caller.getEmpById.apply(2);
        assertNotNull(employee);
        assertEquals(2L, employee.getId());
    }

    @Test
    @Order(3)
    @DisplayName("Error for invalid employee ID")
    void getEmployeeById_notFound() {
        assertThrows(WebClientResponseException.class, () -> caller.getEmpById.apply(-1));
    }

    @Test
    @Order(3)
    @DisplayName("Error for invalid employee ID with custom handler")
    void getEmployeeById_notFound_withCustomHandling() {
        assertThrows(RuntimeException.class, () -> caller.getEmpByIdWithErrorHandling.apply(-1));
    }

    @Test
    @Order(3)
    @DisplayName("Error for invalid employee ID with retry")
    void getEmployeeById_notFound_withRetry() {
        assertThrows(RuntimeException.class, () -> caller.getEmpByIdWithRetry.apply(-1));
    }

    @Test
    @Order(99)
    @DisplayName("Can add employee")
    void addEmployee() {

        final Employee addedEmployee = employeeSupplier.get();
        assertNotNull(addedEmployee);
        assertEquals("Chris", addedEmployee.getFirstName());
    }

    @Test
    @DisplayName("Error on adding invalid employee data")
    void addEmployee_error() {
        Employee employee = employeeSupplier.get();
        employee.setFirstName(null);

        assertThrows(WebClientResponseException.class, () -> caller.addEmployee.apply(employee));
    }

    @Test
    @DisplayName("Can get employee by name")
    void getEmployeesByName() {
        List<Employee> employees = caller.getEmpByName.apply("Adam");
        assertNotNull(employees);
        assertFalse(employees.isEmpty());
        final Employee employee = employees.get(0);
        assertEquals("Adam", employee.getFirstName());
    }

    @Test
    @DisplayName("Error on getting by invalid name")
    void getEmployeesByName_notFound() {
        assertThrows(WebClientResponseException.class, () -> caller.getEmpByName.apply("XXX"));
    }

    @Test
    @DisplayName("Can update employee")
    void updateEmployee() {

        Employee employee = caller.getEmpById.apply(2);
        employee.setLastName("Thunder");
        Employee updatedEmployee = caller.updateEmployee.apply(2).apply(employee);
        assertNotNull(updatedEmployee);
        assertEquals("Thunder", updatedEmployee.getLastName());

    }

    @Test
    @Order(99) // End test
    @DisplayName("Delete an employee")
    void deleteEmployee() {
        long idToDelete = employeeSupplier.get().getId();
        assertEquals("DELETED!", caller.deleteEmployee.apply(idToDelete));
        assertThrows(WebClientResponseException.class, () -> caller.getEmpById.apply(((int) idToDelete)));

    }

    @Test
    @DisplayName("Failed to delete an employee")
    void deleteEmployee_idNotFound() {
        long idToDelete = -1;
        assertThrows(WebClientResponseException.class, () -> caller.deleteEmployee.apply(idToDelete));

    }

    private final Supplier<Employee> employeeSupplier = () -> {
        Employee employee = Employee.builder()
                                    .firstName("Chris")
                                    .lastName("Evans")
                                    .gender("M")
                                    .age(23)
                                    .role("SSE")
                                    .address(Address.builder()
                                                    .pinCode(411014)
                                                    .addressLine1("EON")
                                                    .build())
                                    .build();
        return caller.addEmployee.apply(employee);
    };
}