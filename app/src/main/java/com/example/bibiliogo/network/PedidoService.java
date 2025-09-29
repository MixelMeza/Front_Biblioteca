package com.example.bibiliogo.network;

import com.example.bibiliogo.model.Pedido;
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface PedidoService {
    @GET("pedidos")
    Call<List<Pedido>> getPedidos();

    @GET("pedidos/{id}")
    Call<Pedido> getPedido(@Path("id") Long id);

    @POST("pedidos")
    Call<Pedido> createPedido(@Body Pedido pedido);

    @PUT("pedidos/{id}")
    Call<Pedido> updatePedido(@Path("id") Long id, @Body Pedido pedido);

    @DELETE("pedidos/{id}")
    Call<Void> deletePedido(@Path("id") Long id);

    @PUT("pedidos/{id}/{estado}")
    Call<Pedido> cambiarEstado(@Path("id") Long id, @Path("estado") String estado);
}
