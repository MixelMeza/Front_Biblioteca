package com.example.biblioteca.ui.interfaces;

import retrofit2.Call;
import retrofit2.http.*;
import com.example.biblioteca.ui.model.Pedido;
import java.util.List;

public interface PedidoService {
    @GET("api/pedidos") // Agregué "api/" para que coincida con tu endpoint
    Call<List<Pedido>> getPedidos();

    @GET("api/pedidos/{id}")
    Call<Pedido> getPedido(@Path("id") Long id);

    @POST("api/pedidos")
    Call<Pedido> createPedido(@Body Pedido pedido);

    @PUT("api/pedidos/{id}")
    Call<Pedido> updatePedido(@Path("id") Long id, @Body Pedido pedido);

    @DELETE("api/pedidos/{id}")
    Call<Void> deletePedido(@Path("id") Long id);
}