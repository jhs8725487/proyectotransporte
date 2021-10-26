package com.joel.proyectogrado.adapters;

public class Historial {
    private String Origen;
    private String Destino;
    private String Telefono;
    private String Nombre;
    private String Foto;
    public Historial(){

    }

    public Historial(String origen, String destino, String telefono, String nombre, String foto) {
        Origen = origen;
        Destino = destino;
        Telefono = telefono;
        Nombre = nombre;
        Foto = foto;
    }

    public String getOrigen() {
        return Origen;
    }

    public void setOrigen(String origen) {
        Origen = origen;
    }

    public String getDestino() {
        return Destino;
    }

    public void setDestino(String destino) {
        Destino = destino;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getFoto() {
        return Foto;
    }

    public void setFoto(String foto) {
        Foto = foto;
    }
}
