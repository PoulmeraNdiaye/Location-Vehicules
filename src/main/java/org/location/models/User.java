package org.location.models;

import javax.persistence.*;
import java.util.Objects;

@Entity
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "nom", nullable = false)
    private String nom;

    @Column(name = "login", nullable = false, unique = true)
    private String login;

    @Column(name = "motDePasse", nullable = false)
    private String motDePasse;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private Role role;

    @OneToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "client_id", referencedColumnName = "id")
    private Client client;

    public enum Role {
        ADMIN, EMPLOYEE, CLIENT
    }

    public User() {}

    public User(String nom, String login, String motDePasse, Role role) {
        this.nom = nom;
        this.login = login;
        this.motDePasse = motDePasse;
        this.role = role;
    }

    public User(String nom, String login, String motDePasse, Role role, Client client) {
        this.nom = nom;
        this.login = login;
        this.motDePasse = motDePasse;
        this.role = role;
        this.client = client;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getMotDePasse() {
        return motDePasse;
    }

    public void setMotDePasse(String motDePasse) {
        this.motDePasse = motDePasse;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", nom='" + nom + '\'' +
                ", login='" + login + '\'' +
                ", role=" + role +
                ", client=" + (client != null ? client.getId() : null) +
                '}';
    }
}