package com.effeta.BirthdayApp.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "fiestas")
public class Fiesta {
    @Id
    private String id;
    private String nombre;
    private String fecha;
    private String hora;
    private String lugar;
    private String codigoVestimenta;
    private String anfitrion;

    public Fiesta() {
    }

    public Fiesta(String id, String nombre, String fecha, String hora, String lugar, 
                  String codigoVestimenta, String anfitrion) {
        this.id = id;
        this.nombre = nombre;
        this.fecha = fecha;
        this.hora = hora;
        this.lugar = lugar;
        this.codigoVestimenta = codigoVestimenta;
        this.anfitrion = anfitrion;
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

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getLugar() {
        return lugar;
    }

    public void setLugar(String lugar) {
        this.lugar = lugar;
    }

    public String getCodigoVestimenta() {
        return codigoVestimenta;
    }

    public void setCodigoVestimenta(String codigoVestimenta) {
        this.codigoVestimenta = codigoVestimenta;
    }

    public String getAnfitrion() {
        return anfitrion;
    }

    public void setAnfitrion(String anfitrion) {
        this.anfitrion = anfitrion;
    }
}
