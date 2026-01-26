package com.effeta.BirthdayApp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "invitados")
public class Invitado {
    @Id
    private String id;
    private String nombre;
    private String telefono;
    private String fiestaId;
    private boolean confirmado;

    public Invitado() {
    }

    public Invitado(String id, String nombre, String telefono, String fiestaId, boolean confirmado) {
        this.id = id;
        this.nombre = nombre;
        this.telefono = telefono;
        this.fiestaId = fiestaId;
        this.confirmado = confirmado;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public String getFiestaId() {
        return fiestaId;
    }

    public void setFiestaId(String fiestaId) {
        this.fiestaId = fiestaId;
    }

    public boolean isConfirmado() {
        return confirmado;
    }

    public void setConfirmado(boolean confirmado) {
        this.confirmado = confirmado;
    }
}
