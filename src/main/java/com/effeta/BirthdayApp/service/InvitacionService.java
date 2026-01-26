package com.effeta.BirthdayApp.service;

import com.effeta.BirthdayApp.model.Invitado;
import com.effeta.BirthdayApp.repository.InvitadoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InvitacionService {
    
    @Autowired
    private InvitadoRepository invitadoRepository;
    
    @Autowired
    private FiestaService fiestaService;

    public InvitacionService() {
    }

    public void inicializarDatosPrueba(String fiestaId) {
        // Solo crear datos de prueba si no hay invitados
        if (invitadoRepository.count() == 0) {
            crearInvitado("Ana García", "573001234567", fiestaId);
            crearInvitado("Carlos Pérez", "573007654321", fiestaId);
            crearInvitado("María López", "573009876543", fiestaId);
            crearInvitado("Juan Rodríguez", "573005554444", fiestaId);
        }
    }

    /**
     * Crea un nuevo invitado con un ID único
     */
    public Invitado crearInvitado(String nombre, String telefono, String fiestaId) {
        String id = generarIdUnico();
        Invitado invitado = new Invitado(id, nombre, telefono, fiestaId, false);
        invitado = invitadoRepository.save(invitado);
        System.out.println("✅ Invitado creado: " + nombre + " - Teléfono: " + telefono + " - ID: " + id + " - Fiesta: " + fiestaId);
        System.out.println("   Link: http://localhost:8080/invitacion.html?id=" + id);
        return invitado;
    }

    /**
     * Obtiene un invitado por su ID
     */
    public Invitado obtenerInvitado(String id) {
        Optional<Invitado> invitado = invitadoRepository.findById(id);
        return invitado.orElse(null);
    }

    /**
     * Confirma la asistencia de un invitado
     */
    public boolean confirmarAsistencia(String id) {
        Optional<Invitado> optionalInvitado = invitadoRepository.findById(id);
        if (optionalInvitado.isPresent()) {
            Invitado invitado = optionalInvitado.get();
            invitado.setConfirmado(true);
            invitadoRepository.save(invitado);
            return true;
        }
        return false;
    }

    /**
     * Obtiene todos los invitados (para administración)
     */
    public List<Invitado> obtenerTodosInvitados() {
        return invitadoRepository.findAll();
    }

    /**
     * Obtiene invitados por fiesta
     */
    public List<Invitado> obtenerInvitadosPorFiesta(String fiestaId) {
        return invitadoRepository.findByFiestaId(fiestaId);
    }

    /**
     * Genera un ID único de 6 caracteres
     */
    private String generarIdUnico() {
        return UUID.randomUUID().toString().replace("-", "").substring(0, 6);
    }
}
