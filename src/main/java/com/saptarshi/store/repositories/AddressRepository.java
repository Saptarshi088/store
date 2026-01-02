package com.saptarshi.store.repositories;

import com.saptarshi.store.entities.Address;
import org.springframework.data.repository.CrudRepository;

public interface AddressRepository extends CrudRepository<Address, Long> {
}