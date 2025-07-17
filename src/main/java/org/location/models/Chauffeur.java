package org.location.models;

import  javax.persistence.*;
@Entity
@Table(name = "chauffeurs")
public class Chauffeur {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(nullable = false)
    private String nom;

    @Column(nullable = false)
    private Boolean dispo = true;

    public Chauffeur() {}

    public Chauffeur(String nom, Boolean dispo) {
        this.nom = nom;
        this.dispo = dispo;
    }

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }

    public Boolean getDispo() { return dispo; }
    public void setDispo(Boolean dispo) { this.dispo = dispo; }

    @Override
    public String toString() {
        return "Chauffeur{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", dispo=" + dispo +
                '}';
    }
}
