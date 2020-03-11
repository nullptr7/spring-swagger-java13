package com.github.nullptr7.repo;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import com.github.nullptr7.models.Address;

import java.util.Optional;

@Repository
public interface AddressRepository extends CrudRepository<Address, Long> {

    @Query("select addr from Address addr where addr.id = ?1")
    Optional<Address> findAddressById(Long id);
}
