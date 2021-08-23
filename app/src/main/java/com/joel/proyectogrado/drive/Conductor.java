package com.joel.proyectogrado.drive;

import android.os.health.TimerStat;

import java.util.Date;

public class Conductor {


    String Categoria;
    String Expedido;
    String FechaNacimiento;

    public Conductor(String Categoria, String Expedido ,String FechaNacimiento) {
        this.Categoria = Categoria;
        this.Expedido = Expedido;
        this.FechaNacimiento = FechaNacimiento;
    }
    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getExpedido() {
        return Expedido;
    }

    public void setExpedido(String expedido) {
        Expedido = expedido;
    }

    public String getFechaNacimiento() {

        return FechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        FechaNacimiento = fechaNacimiento;
    }

}
