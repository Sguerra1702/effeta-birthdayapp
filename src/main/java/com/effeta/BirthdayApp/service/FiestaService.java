package com.effeta.BirthdayApp.service;

import com.effeta.BirthdayApp.model.Fiesta;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.*;

@Service
public class FiestaService {
    private final Map<String, Fiesta> fiestas = new HashMap<>();

    @PostConstruct
    public void crearFiestaDefault() {
        Fiesta fiesta = new Fiesta(
            "fiesta1",
            "Mi Cumpleaños 2026",
            "Sábado 15 de Febrero, 2026",
            "6:00 PM",
            "Salón de Fiestas \"La Alegría\"",
            "Casual elegante",
            "Admin"
        );
        fiestas.put(fiesta.getId(), fiesta);
    }

    public Fiesta crearFiesta(Fiesta fiesta) {
        if (fiesta.getId() == null || fiesta.getId().isEmpty()) {
            fiesta.setId(UUID.randomUUID().toString());
        }
        fiestas.put(fiesta.getId(), fiesta);
        return fiesta;
    }

    public Fiesta obtenerFiesta(String id) {
        return fiestas.get(id);
    }

    public List<Fiesta> obtenerTodasFiestas() {
        return new ArrayList<>(fiestas.values());
    }

    public Fiesta obtenerFiestaDefault() {
        return fiestas.isEmpty() ? null : fiestas.values().iterator().next();
    }

    public boolean eliminarFiesta(String id) {
        if (fiestas.containsKey(id)) {
            fiestas.remove(id);
            return true;
        }
        return false;
    }
}
