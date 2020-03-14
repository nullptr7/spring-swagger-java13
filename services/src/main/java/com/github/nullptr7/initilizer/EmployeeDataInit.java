package com.github.nullptr7.initilizer;

import com.github.nullptr7.models.Address;
import com.github.nullptr7.models.Employee;
import com.github.nullptr7.repo.AddressRepository;
import com.github.nullptr7.repo.EmployeeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

@Slf4j
@Component
public class EmployeeDataInit implements CommandLineRunner {

    private final EmployeeRepository employeeRepository;
    private final AddressRepository addressRepository;

    public EmployeeDataInit(EmployeeRepository employeeRepository, AddressRepository addressRepository) {
        this.employeeRepository = employeeRepository;
        this.addressRepository = addressRepository;
    }

    @Override
    public void run(String... args) {


        Employee employee1 = new Employee(null, "Chris", "Evans", 50, "male", "Lead Engineer",
                addressRepository.save(Address.builder()
                                              .addressLine1("Kharadi")
                                              .pinCode(411014)
                                              .build()));
        Employee employee2 = new Employee(null, "Adam", "Sandler", 50, "male", "Senior Engineer",
                addressRepository.save(Address.builder()
                                              .addressLine1("Chandanagar")
                                              .pinCode(411014)
                                              .build()));
        Employee employee3 = new Employee(null, "Jenny", "Richards", 32, "female", "Senior Engineer",
                addressRepository.save(Address.builder()
                                              .addressLine1("Hinjwadi")
                                              .pinCode(411014)
                                              .build()));
        Employee employee4 = new Employee(null, "Amy", "Adams", 44, "female", "Manager",
                addressRepository.save(Address.builder()
                                              .addressLine1("Aundh")
                                              .pinCode(411014)
                                              .build()));
        List<Employee> employeeList = Arrays.asList(employee1, employee2, employee3, employee4);


        log.info("********* Employee RestFul Service Initial Data Starts *********");
        employeeRepository.saveAll(employeeList);
        employeeRepository.findAll().forEach((System.out::println));
        log.info("********* Employee RestFul Service Initial Data Ends *********");
    }
}
