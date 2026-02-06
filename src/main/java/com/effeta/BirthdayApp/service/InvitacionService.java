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
        // Lista de invitados para la fiesta sorpresa de Juanma
        crearInvitado(new Invitado("1", "Jaisa Rincon", "+573155088491", "fiesta1", false));
        crearInvitado(new Invitado("2", "Vale Campanelli", "+573046646595", "fiesta1", false));
        crearInvitado(new Invitado("3", "Nayito", "+573183174268", "fiesta1", false));
        crearInvitado(new Invitado("4", "Juan Villada", "+573108013248", "fiesta1", false));
        crearInvitado(new Invitado("5", "Karol", "+573177022109", "fiesta1", false));
        crearInvitado(new Invitado("6", "Gallito", "+573152155523", "fiesta1", false));
        crearInvitado(new Invitado("7", "Jose Lineros", "+573203574957", "fiesta1", false));
        crearInvitado(new Invitado("8", "Camila Rodriguez", "+573022991078", "fiesta1", false));
        crearInvitado(new Invitado("9", "Mapis Peñuela", "+573213880189", "fiesta1", false));
        crearInvitado(new Invitado("10", "Brayan Jimenez", "+573144560694", "fiesta1", false));
        crearInvitado(new Invitado("11", "Julian Parra", "+573006400368", "fiesta1", false));
        crearInvitado(new Invitado("12", "Samu medina", "+573212527852", "fiesta1", false));
        crearInvitado(new Invitado("13", "Nelson Monroy", "+573107220130", "fiesta1", false));
        crearInvitado(new Invitado("14", "Richard Rodriguez", "+573177970213", "fiesta1", false));
        crearInvitado(new Invitado("15", "Vanesa Rincon", "+573193927132", "fiesta1", false));
        crearInvitado(new Invitado("16", "Cristian Rodriguez", "+573192713167", "fiesta1", false));
        crearInvitado(new Invitado("17", "David Parra", "+573127701620", "fiesta1", false));
        crearInvitado(new Invitado("18", "Sebastián Aguilar", "+573115967530", "fiesta1", false));
        crearInvitado(new Invitado("19", "Manuel Aguilar", "+573115970103", "fiesta1", false));
        crearInvitado(new Invitado("20", "Manuela Robayo", "+573164938000", "fiesta1", false));
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

    public String generarNombreArchivoInvitacion(String nombreInvitado) {
        // Convertir a minúsculas, reemplazar ñ por n y remover acentos
        String nombreArchivo = nombreInvitado.toLowerCase()
                .replace("ñ", "n")
                .replace("á", "a")
                .replace("é", "e")
                .replace("í", "i")
                .replace("ó", "o")
                .replace("ú", "u")
                .replace(" ", "_");
        return "Invitacion_" + nombreArchivo + ".jpeg";
    }
}

