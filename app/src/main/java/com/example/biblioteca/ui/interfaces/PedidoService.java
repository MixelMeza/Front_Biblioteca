package com.example.biblioteca.ui.interfaces;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import com.example.biblioteca.Pedido;
import java.util.List;

public interface PedidoService {
    @GET("pedidos")
    Call<List<Pedido>> getPedidos();

    @GET("pedidos/{id}")
    Call<Pedido> getPedido(@Path("id") String id);

    @POST("pedidos")
    Call<Pedido> createPedido(@Body Pedido pedido);

    @PUT("pedidos/{id}")
    Call<Pedido> updatePedido(@Path("id") String id, @Body Pedido pedido);

    @DELETE("pedidos/{id}")
    Call<Void> deletePedido(@Path("id") String id);

    @PUT("pedidos/{id}/estado/{estado}")
    Call<Pedido> cambiarEstado(@Path("id") String id, @Path("estado") String estado);
}
