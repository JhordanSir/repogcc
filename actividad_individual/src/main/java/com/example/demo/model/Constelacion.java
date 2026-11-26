package com.example.demo.model;

import jakarta.persistence.*;

@Entity
@Table(name = "constelaciones")
public class Constelacion {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(nullable = false, length = 10)
    private String hemisferio;

    @Column(name = "estrella_principal", nullable = false, length = 100)
    private String estrellaPrincipal;

    protected Constelacion() {}

    public Constelacion(String nombre, String hemisferio, String estrellaPrincipal) {
        this.nombre = nombre;
        this.hemisferio = hemisferio;
        this.estrellaPrincipal = estrellaPrincipal;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getHemisferio() { return hemisferio; }
    public void setHemisferio(String hemisferio) { this.hemisferio = hemisferio; }
    public String getEstrellaPrincipal() { return estrellaPrincipal; }
    public void setEstrellaPrincipal(String estrellaPrincipal) { this.estrellaPrincipal = estrellaPrincipal; }
}
