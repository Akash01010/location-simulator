package com.simulator.location.repository;

import com.simulator.location.model.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.LinkedList;
import java.util.List;


@RepositoryRestResource(collectionResourceRel = "users", path = "users")
public interface UserRepository extends PagingAndSortingRepository<User, Long> {
    LinkedList
    List<User> findByName(@Param("name") String name);
}
