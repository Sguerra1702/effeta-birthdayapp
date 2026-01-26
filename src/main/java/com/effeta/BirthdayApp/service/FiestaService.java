package com.effeta.BirthdayApp.service;

import com.effeta.BirthdayApp.model.Fiesta;
import com.effeta.BirthdayApp.repository.FiestaRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class FiestaService {
    
    @Autowired
    private FiestaRepository fiestaRepository;

    public FiestaService() {
    }

    public void crearFiestaDefault() {
        // Solo crear fiesta por defecto si no hay fiestas
        if (fiestaRepository.count() == 0) {
            String id = generarIdUnico();
            Fiesta fiesta = new Fiesta(
                id,
                "Mi Cumpleaños 2026",
                "Sábado 15 de Febrero, 2026",
                "6:00 PM",
                "Salón de Fiestas \"La Alegría\"",
                "Casual elegante",
                "Admin"
            );
            fiestaRepository.save(fiesta);
            System.out.println("🎉 Fiesta por defecto creada: " + fiesta.getNombre() + " - ID: " + id);
        }
    }

    public Fiesta crearFiesta(String nombre, String fecha, String hora, String lugar, 
                              String codigoVestimenta, String anfitrion) {
        String id = generarIdUnico();
        Fiesta fiesta = new Fiesta(id, nombre, fecha, hora, lugar, codigoVestimenta, anfitrion);
        fiesta = fiestaRepository.save(fiesta);
        System.out.println("🎉 Fiesta creada: " + nombre + " - ID: " + id);
        return fiesta;
    }

    public Fiesta obtenerFiesta(String id) {
        Optional<Fiesta> fiesta = fiestaRepository.findById(id);
        return fiesta.orElse(null);
    }

    public List<Fiesta> obtenerTodasFiestas() {
        return fiestaRepository.findAll();
    }

    public Fiesta obtenerFiestaDefault() {
        List<Fiesta> fiestas = fiestaRepository.findAll();
        return fiestas.isEmpty() ? null : fiestas.get(0);
    }

    public boolean eliminarFiesta(String id) {
        if (fiestaRepository.existsById(id)) {
            fiestaRepository.deleteById(id);
            return true;
        }
        return false;
    }

    private String generarIdUnico() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }
}
