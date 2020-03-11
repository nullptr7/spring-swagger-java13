package com.github.nullptr7.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.github.nullptr7.models.Employee;

import java.util.List;

@Repository
public interface EmployeeRepository extends CrudRepository<Employee, Long> {

    @Query("select m from Employee m where m.firstName like %?1%")
    List<Employee> findByName(String name);
}
