package com.example.biblioteca.ui.model;

public class Pedido {
    private Long idPedido;
    private String fecha; // LocalDate se convierte a String en JSON
    private String descripcion;
    private String estado;
    private String usuario; // Es String, no Long
    private Libro libro;   // Objeto Libro completo - ¡EXCELENTE!

    // Getters y Setters
    public Long getIdPedido() { return idPedido; }
    public void setIdPedido(Long idPedido) { this.idPedido = idPedido; }

    public String getFecha() { return fecha; }
    public void setFecha(String fecha) { this.fecha = fecha; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getUsuario() { return usuario; }
    public void setUsuario(String usuario) { this.usuario = usuario; }

    public Libro getLibro() { return libro; }
    public void setLibro(Libro libro) { this.libro = libro; }
}