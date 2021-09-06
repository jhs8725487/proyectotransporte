package com.joel.proyectogrado.drive;

public class ConductorActivo {
    double Latitud;
    double Longitud;
    String Direccion;
    String Estado;

    public ConductorActivo(double latitud, double longitud) {
        this.Latitud = latitud;
        this.Longitud = longitud;
    }
    public double getLatitud() {
        return Latitud;
    }

    public void setLatitud(double latitud) {
        this.Latitud = latitud;
    }

    public double getLongitud() {
        return Longitud;
    }

    public void setLongitud(double longitud) {
        this.Longitud = longitud;
    }

    public String getDireccion() {
        return Direccion;
    }

    public void setDireccion(String direccion) {
        this.Direccion = direccion;
    }

    public String getEstado() {
        return Estado;
    }

    public void setEstado(String estado) {
        this.Estado = estado;
    }
}
