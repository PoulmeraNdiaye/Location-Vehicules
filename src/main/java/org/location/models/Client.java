package org.location.models;

//import  javax.persistence.*;
import jakarta.persistence.*;
@Entity
//@DiscriminatorValue("CLIENT")
@Table(name = "clients")
@PrimaryKeyJoinColumn(name = "id")
public class Client extends User {


    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private Integer pointsFidelite = 0;

    public Client() {}

    public Client(String nom, String email, String login, String motDePasse) {
        setNom(nom);
        setEmail(email);
        setLogin(login);
        setMotDePasse(motDePasse);
        setRole(Role.CLIENT);
        this.pointsFidelite = 0;
    }


    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getPointsFidelite() { return pointsFidelite; }
    public void setPointsFidelite(Integer pointsFidelite) { this.pointsFidelite = pointsFidelite; }
}
