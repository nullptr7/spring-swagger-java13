package com.github.nullptr7.controller;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import com.github.nullptr7.models.Employee;
import com.github.nullptr7.repo.AddressRepository;
import com.github.nullptr7.repo.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.lang3.StringUtils.isNotEmpty;
import static org.springframework.http.HttpStatus.CREATED;
import static org.springframework.http.HttpStatus.OK;
import static org.springframework.http.ResponseEntity.status;

@Slf4j
@RestController
@RequestMapping("/v1/employees")
@EnableJpaRepositories(basePackages = "com.github.nullptr7.repo")
public class EmployeeController {

    private final EmployeeRepository employeeRepository;
    private final AddressRepository addressRepository;

    public EmployeeController(EmployeeRepository employeeRepository, AddressRepository addressRepository) {
        this.employeeRepository = employeeRepository;
        this.addressRepository = addressRepository;
    }

    @GetMapping("/all")
    @ApiOperation("Retrieves all the Employees")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "SuccessFul Retrieval of Employees")
            }
    )
    public List<Employee> getEmployees() {
        List<Employee> employees = new ArrayList<>();
        employeeRepository
                .findAll()
                .forEach(employees::add);
        return employees;
    }

    @GetMapping("/{id}")
    @ApiOperation("Retrieve an Employee using the Employee id.")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Returns the Employee for the id."),
                    @ApiResponse(code = 404, message = "No Employee found for the id that's passed.")
            }
    )
    public ResponseEntity<?> getEmployee(@PathVariable Long id) {
        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            log.info("Response is {}.", employeeOptional.get());
            return status(OK)
                    .body(employeeOptional.get());
        } else {
            log.info("No Employee available with the given Employee Id - {}", id);
            throw ExceptionHandler.idNotFound.apply(id);
        }
    }

    @PostMapping("/employee")
    @ApiOperation("Adds a new Employee.")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 201, message = "Added in H2 Database.")
            }
    )
    public ResponseEntity<?> addEmployee(@RequestBody Employee employee) {

        log.info("Received the request to add a new Employee in the service {} ", employee);
        try {
            addressRepository.save(employee.getAddress());
            Employee addedEmployee = employeeRepository.save(employee);
            log.info("Employee SuccessFully added to the DB. New Employee Details are {} .", employee);
            return status(CREATED)
                    .body(addedEmployee);
        } catch (Exception ex) {
            log.error("Error while adding employee {} reason {}", employee.toString(), ex.getMessage());
            throw ExceptionHandler.internalServerError.get();
        }

    }

    @GetMapping("/employeeName")
    @ApiOperation("Returns the Employees using the employee name passed as part of the request.")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Returns the Employees using the FirstName or LastName of the employee."),
                    @ApiResponse(code = 404, message = "No Employee found for the name that's passed."),
            }
    )
    public ResponseEntity<?> employeeByName(@RequestParam("employee_name") String name) {

        log.info("Received the request to search by Employee name - {}", name);

        List<Employee> emp = employeeRepository.findByName(name);
        if (isEmpty(emp)) {
            log.info("No Employee available for the given Employee name - {}.", name);
            throw ExceptionHandler.nameNotFound.apply(name);
        } else {
            log.info("Response is : {}", emp);
            return status(OK)
                    .body(emp);
        }
    }

    @PutMapping("/employee/{id}")
    @ApiOperation("Updates the Employee details.")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Employee details are successfully updated to the DB."),
                    @ApiResponse(code = 404, message = "No Employee found for the id that's passed."),
            }
    )
    public ResponseEntity<?> updateEmployee(@PathVariable Long id, @RequestBody Employee updateEmployee) {
        log.info("Received the request to update the employee. Employee Id is {} and the updated Employee Details are {} ", id, updateEmployee);

        Optional<Employee> employeeOptional = employeeRepository.findById(id);
        if (employeeOptional.isPresent()) {
            Employee employeeToUpdate = employeeOptional.get();
            createEmployeeEntity(employeeToUpdate, updateEmployee);
            employeeRepository.save(employeeToUpdate);
            return status(OK)
                    .body(employeeToUpdate);

        } else {
            log.info("No Employee available for the given employee Id - {}.", id);
            throw ExceptionHandler.idNotFound.apply(id);

        }
    }

    @DeleteMapping("/employee/{id}")
    @ApiOperation("Removes the Employee details.")
    @ApiResponses(
            value = {
                    @ApiResponse(code = 200, message = "Employee details are successfully deleted from the DB."),
                    @ApiResponse(code = 404, message = "No Employee found for the year that's passed."),
            }
    )
    public ResponseEntity<?> deleteEmployee(@PathVariable Long id) {
        log.info("Received request to delete employee: {}", id);

        if (employeeRepository.findById(id).isPresent()) {
            employeeRepository.deleteById(id);
            log.info("Employee {} deleted!", id);
            return status(OK)
                    .body("DELETED!");
        } else {
            log.error("No Employee with id {} found", id);
            throw ExceptionHandler.idNotFound.apply(id);
        }
    }

    /**
     * Method will perform update operation
     *
     * @param employeeToUpdate given original {@link Employee} object
     * @param updatedEmp       updated {@link Employee} object
     */
    public void createEmployeeEntity(Employee employeeToUpdate, Employee updatedEmp) {
        if (isNotEmpty(updatedEmp.getFirstName()) && !updatedEmp.getFirstName().equals(employeeToUpdate.getFirstName())) {
            employeeToUpdate.setFirstName(updatedEmp.getFirstName());
        }
        if (isNotEmpty(updatedEmp.getLastName()) && !updatedEmp.getLastName().equals(employeeToUpdate.getLastName())) {
            employeeToUpdate.setLastName(updatedEmp.getLastName());
        }
        if (updatedEmp.getAge().equals(employeeToUpdate.getAge())) {
            employeeToUpdate.setAge(updatedEmp.getAge());
        }
        if (isNotEmpty(updatedEmp.getGender()) && !updatedEmp.getGender().equals(employeeToUpdate.getGender())) {
            employeeToUpdate.setGender(updatedEmp.getGender());
        }
        if (isNotEmpty(updatedEmp.getRole()) && !updatedEmp.getRole().equals(employeeToUpdate.getRole())) {
            employeeToUpdate.setRole(updatedEmp.getRole());
        }
    }
}
