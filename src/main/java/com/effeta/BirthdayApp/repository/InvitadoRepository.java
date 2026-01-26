package com.effeta.BirthdayApp.repository;

import com.effeta.BirthdayApp.model.Invitado;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InvitadoRepository extends MongoRepository<Invitado, String> {
    List<Invitado> findByFiestaId(String fiestaId);
}
