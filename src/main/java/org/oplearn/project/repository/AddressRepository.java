package org.oplearn.project.repository;

import org.oplearn.project.entity.Users;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AddressRepository extends MongoRepository<Users, Integer> {
}
