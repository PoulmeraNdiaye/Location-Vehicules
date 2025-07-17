package org.location.models;

import  javax.persistence.*;
@Entity
//@DiscriminatorValue("ADMIN")
@Table(name = "admins")
@PrimaryKeyJoinColumn(name = "id")
public class Admin extends User {

    public Admin() {}

    public Admin(String login, String motDePasse,String nom) {
        setLogin(login);
        setMotDePasse(motDePasse);
        setRole(Role.ADMIN);
        setNom(nom);
    }


}
