package Activity.client;

public class Usuario {

    String Nombre;
    String Apellidos;
    String Genero;
    String Telefono;
    String Correo;
    String Contra;

    public Usuario(String nombre, String apellidos, String genero, String telefono, String correo, String contra) {

        this.Nombre = nombre;
        this.Apellidos = apellidos;
        this.Genero = genero;
        this.Telefono = telefono;
        this.Correo = correo;
        this.Contra = contra;
    }
    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getApellidos() {
        return Apellidos;
    }

    public void setApellidos(String apellidos) {
        Apellidos = apellidos;
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

}
