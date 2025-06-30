package org.location.models;

import jakarta.persistence.*;

@Entity
@Table(name = "vehicules")
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String marque;

    @Column(nullable = false)
    private String modele;

    @Column(nullable = false)
    private Double tarif;

    @Column(nullable = false)
    private Boolean disponible = true;

    @Column(nullable = false, unique = true)
    private String immatriculation;

    // Commenté temporairement pour tester
    // @OneToMany(mappedBy = "vehicule", cascade = CascadeType.ALL)
    // private List<Reservation> reservations;

    public Vehicle() {}

    public Vehicle(String marque, String modele, Double tarif, String immatriculation) {
        this.marque = marque;
        this.modele = modele;
        this.tarif = tarif;
        this.immatriculation = immatriculation;
        this.disponible = true;
    }

    // Getters et setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }
    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }
    public Double getTarif() { return tarif; }
    public void setTarif(Double tarif) { this.tarif = tarif; }
    public Boolean getDisponible() { return disponible; }
    public void setDisponible(Boolean disponible) { this.disponible = disponible; }
    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }
    // public List<Reservation> getReservations() { return reservations; }
    // public void setReservations(List<Reservation> reservations) { this.reservations = reservations; }
}