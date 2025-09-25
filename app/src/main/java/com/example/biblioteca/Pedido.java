package com.example.biblioteca;

public class Pedido {
    public Long idPedido;
    public String fecha;
    public String descripcion;
    public String estado;
    public String usuario;
    public Libro libro;

    public static class Libro {
        public Long idLibro;
        public String titulo;
        public String autor;
        public String estado;
        public String categoria;
        public String codigo;
        public Integer stock;
        public String descripcion;
    }
}
