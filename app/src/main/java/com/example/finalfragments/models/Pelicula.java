package com.example.finalfragments.models;

public class Pelicula {
    private int id;
    private String titulo;
    private int duracion;
    private String genero;
    private String lanzamiento;

    public Pelicula(int id, String titulo, int duracion, String genero, String lanzamiento) {
        this.id = id;
        this.titulo = titulo;
        this.duracion = duracion;
        this.genero = genero;
        this.lanzamiento = lanzamiento;
    }

    public int getId() { return id; }
    public String getTitulo() { return titulo; }
    public int getDuracion() { return duracion; }
    public String getGenero() { return genero; }
    public String getLanzamiento() { return lanzamiento; }
}
