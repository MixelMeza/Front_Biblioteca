
package com.example.bibiliogo.model;

import java.io.Serializable;

public class Pedido implements Serializable {
    private Long idPedido;
    private String fecha; // Usar String para fecha simple, o java.time.LocalDate si lo prefieres
    private String descripcion;
    private String direccion;
    private String nombre;
    private String telefono;
    private String estado;
    private String usuario;
    private Libro libro;

    public Pedido() {
        this.estado = "PENDIENTE";
    }

    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getDireccion() { return direccion; }
    public void setDireccion(String direccion) { this.direccion = direccion; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public Libro getLibro() { return libro; }
    public void setLibro(Libro libro) { this.libro = libro; }

    // Compatibilidad para adaptador: getId()
    public Long getId() { return idPedido; }
}
