package com.saptarshi.store.repositories;

import com.saptarshi.store.entities.User;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<User, Long> {
}
