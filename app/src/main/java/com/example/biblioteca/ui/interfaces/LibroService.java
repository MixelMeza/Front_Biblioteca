package com.example.biblioteca.ui.interfaces;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

import com.example.biblioteca.ui.model.Libro;

import java.util.List;

public interface LibroService {
    @GET("libros")
    Call<List<Libro>> getLibros();

    @GET("libros/{id}")
    Call<Libro> getLibro(@Path("id") Long id);

    @POST("libros")
    Call<Libro> createLibro(@Body Libro libro);

    @PUT("libros/{id}")
    Call<Libro> updateLibro(@Path("id") Long id, @Body Libro libro);

    @DELETE("libros/{id}")
    Call<Void> deleteLibro(@Path("id") Long id);

    @PUT("libros/{id}/{estado}")
    Call<Libro> cambiarEstado(@Path("id") Long id, @Path("estado") String estado);
}
