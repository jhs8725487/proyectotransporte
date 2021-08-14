package com.joel.proyectogrado.drive;

public class Conductor {


    String categoria;
    String cedula;
    String Contra;

    public Conductor(String categoria, String cedula, String contra) {
        this.categoria = categoria;
        this.cedula = cedula;
        this.Contra = contra;
    }
    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getcedula() {
        return cedula;
    }

    public void setcedula(String cedula) {
        this.cedula = cedula;
    }

    public String getContra() {
        return Contra;
    }

    public void setContra(String contra) {
        this.Contra = contra;
    }

}
