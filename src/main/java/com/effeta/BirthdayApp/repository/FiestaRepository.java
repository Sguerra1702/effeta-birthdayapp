package com.effeta.BirthdayApp.repository;

import com.effeta.BirthdayApp.model.Fiesta;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FiestaRepository extends MongoRepository<Fiesta, String> {
}
