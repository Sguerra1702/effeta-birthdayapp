package com.effeta.BirthdayApp.service;

import com.effeta.BirthdayApp.model.Invitado;
import org.springframework.stereotype.Service;
import jakarta.annotation.PostConstruct;

import java.util.*;

@Service
public class InvitacionService {
    private final Map<String, Invitado> invitados = new HashMap<>();

    @PostConstruct
    public void inicializarDatosPrueba() {
        // Datos de prueba
        crearInvitado(new Invitado("1", "Juan Pérez", "+573001234567", "fiesta1", false));
        crearInvitado(new Invitado("2", "María García", "+573007654321", "fiesta1", true));
        crearInvitado(new Invitado("3", "Carlos López", "+573009876543", "fiesta1", false));
    }

    public Invitado crearInvitado(Invitado invitado) {
        if (invitado.getId() == null || invitado.getId().isEmpty()) {
            invitado.setId(UUID.randomUUID().toString());
        }
        invitados.put(invitado.getId(), invitado);
        return invitado;
    }


    public List<Invitado> obtenerTodosInvitados() {
        return new ArrayList<>(invitados.values());
    }

    public Invitado obtenerInvitado(String id) {
        return invitados.get(id);
    }

    public boolean confirmarAsistencia(String id) {
        Invitado invitado = invitados.get(id);
        if (invitado != null) {
            invitado.setConfirmado(true);
            return true;
        }
        return false;
    }

    public void eliminarInvitado(String id) {
        invitados.remove(id);
    }

    public String generarEnlaceInvitacion(String invitadoId) {
        return "/invitacion.html?id=" + invitadoId;
    }

    public List<Invitado> obtenerInvitadosPorFiesta(String fiestaId) {
        List<Invitado> invitadosFiesta = new ArrayList<>();
        for (Invitado invitado : invitados.values()) {
            if (fiestaId.equals(invitado.getFiestaId())) {
                invitadosFiesta.add(invitado);
            }
        }
        return invitadosFiesta;
    }
}

