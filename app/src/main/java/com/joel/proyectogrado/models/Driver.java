package com.joel.proyectogrado.models;

public class Driver {

    String id;
    String Nombre;
    String ApellidoPaterno;
    String ApellidoMaterno;
    String Genero;
    String Telefono;
    String Correo;
    String Contra;
    String Categoria;
    String Cedula;
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidoPaterno() {
        return ApellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        ApellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return ApellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        ApellidoMaterno = apellidoMaterno;
    }

    public String getGenero() {
        return Genero;
    }

    public void setGenero(String genero) {
        Genero = genero;
    }

    public String getTelefono() {
        return Telefono;
    }

    public void setTelefono(String telefono) {
        Telefono = telefono;
    }

    public String getCategoria() {
        return Categoria;
    }

    public void setCategoria(String categoria) {
        Categoria = categoria;
    }

    public String getCedula() {
        return Cedula;
    }

    public void setCedula(String cedula) {
        Cedula = cedula;
    }

    public String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public String getContra() {
        return Contra;
    }

    public void setContra(String contra) {
        Contra = contra;
    }

    public Driver(String id, String nombre, String apellidoPaterno, String apellidoMaterno, String genero, String telefono, String categoria, String cedula, String correo, String contra) {
        this.id = id;
        this.Nombre = nombre;
        this.ApellidoPaterno = apellidoPaterno;
        this.ApellidoMaterno = apellidoMaterno;
        this.Genero = genero;
        this.Telefono = telefono;
        this.Categoria = categoria;
        this.Cedula = cedula;
        this.Correo = correo;
        this.Contra = contra;
    }


    public Driver(String id,String nombre, String apellidoPaterno, String apellidoMaterno, String genero, String telefono, String categoria, String cedula, String correo) {
        this.id = id;
        this.Nombre = nombre;
        this.ApellidoPaterno = apellidoPaterno;
        this.ApellidoMaterno = apellidoMaterno;
        this.Genero = genero;
        this.Telefono = telefono;
        this.Categoria = categoria;
        this.Cedula = cedula;
        this.Correo = correo;
    }

    public Driver(String nombre, String apellidoPaterno, String apellidoMaterno, String genero, String telefono, String correo, String contra) {
        this.Nombre = nombre;
        this.ApellidoPaterno = apellidoPaterno;
        this.ApellidoMaterno = apellidoMaterno;
        this.Genero = genero;
        this.Telefono = telefono;
        this.Correo = correo;
        this.Contra = contra;
    }


}
