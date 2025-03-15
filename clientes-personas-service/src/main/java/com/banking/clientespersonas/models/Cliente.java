package com.banking.clientespersonas.models;

import jakarta.persistence.*;

@Entity
@Table(name = "clientes")
@PrimaryKeyJoinColumn(name = "persona_id")
public class Cliente extends Persona {

    @Column(unique = true, nullable = false)
    private String identidad;

    @Column(nullable = false)
    private String contrasena;

    private boolean estado;

 
    public Cliente() {
    }

  
    public Cliente(String identidad, String contrasena, boolean estado) {
        this.identidad = identidad;
        this.contrasena = contrasena;
        this.estado = estado;
    }


    public String getIdentidad() {
        return identidad;
    }

    public void setIdentidad(String identidad) {
        this.identidad = identidad;
    }

    public String getContrasena() {
        return contrasena;
    }

    public void setContrasena(String contrasena) {
        this.contrasena = contrasena;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }

 
    @Override
    public String toString() {
        return "Cliente{" +
                "identidad='" + identidad + '\'' +
                ", contrasena='" + contrasena + '\'' +
                ", estado=" + estado +
                '}';
    }
}
