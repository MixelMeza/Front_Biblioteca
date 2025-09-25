package com.example.biblioteca;

public class Pedido {
    public String numero;
    public String estado;
    public String usuario;
    public String fecha;
    public String cantidad;
    public String direccion;
    public String total;

    public Pedido(String numero, String estado, String usuario, String fecha, String cantidad, String direccion, String total) {
        this.numero = numero;
        this.estado = estado;
        this.usuario = usuario;
        this.fecha = fecha;
        this.cantidad = cantidad;
        this.direccion = direccion;
        this.total = total;
    }
}
