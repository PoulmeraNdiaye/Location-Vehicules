package org.location.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "clients")
public class Client {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Column(name = "pointsFidelite", nullable = false)
    private Integer pointsFidelite;

    public Client() {
        this.pointsFidelite = 0; // Valeur par défaut
    }

    public Client(String nom, String email) {
        this.nom = nom;
        this.email = email;
        this.pointsFidelite = 0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNom() { return nom; }
    public void setNom(String nom) { this.nom = nom; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public Integer getPointsFidelite() { return pointsFidelite; }
    public void setPointsFidelite(Integer pointsFidelite) { this.pointsFidelite = pointsFidelite; }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Client client = (Client) o;
        return Objects.equals(id, client.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}