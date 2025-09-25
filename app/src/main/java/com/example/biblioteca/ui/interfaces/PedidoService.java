package com.example.biblioteca.ui.interfaces;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import com.example.biblioteca.ui.model.Pedido;
import java.util.List;

public interface PedidoService {

    // 📌 Endpoints base de tu backend
    @GET("api/pedidos")
    Call<List<Pedido>> getPedidos();

    @GET("api/pedidos/{id}")
    Call<Pedido> getPedido(@Path("id") Long id);

    @POST("api/pedidos")
    Call<Pedido> createPedido(@Body Pedido pedido);

    @PUT("api/pedidos/{id}")
    Call<Pedido> updatePedido(@Path("id") Long id, @Body Pedido pedido);

    @DELETE("api/pedidos/{id}")
    Call<Void> deletePedido(@Path("id") Long id);

    // 📌 Extra de tus amigos (mantenerlo comentado si tu backend no lo soporta)
    // @GET("pedidos")
    // Call<List<Pedido>> getPedidosSimple();
    //
    // @GET("pedidos/{id}")
    // Call<Pedido> getPedidoSimple(@Path("id") String id);
    //
    // @PUT("pedidos/{id}/estado/{estado}")
    // Call<Pedido> cambiarEstado(@Path("id") String id, @Path("estado") String estado);
}
