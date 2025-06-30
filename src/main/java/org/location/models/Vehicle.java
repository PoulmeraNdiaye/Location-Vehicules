package org.location.models;

import javax.persistence.*;

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

    @Column(nullable = false, unique = true)
    private String immatriculation;

    public Vehicle() {}

    public Vehicle(String marque, String modele, Double tarif, String immatriculation) {
        this.marque = marque;
        this.modele = modele;
        this.tarif = tarif;
        this.immatriculation = immatriculation;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getMarque() { return marque; }
    public void setMarque(String marque) { this.marque = marque; }

    public String getModele() { return modele; }
    public void setModele(String modele) { this.modele = modele; }

    public Double getTarif() { return tarif; }
    public void setTarif(Double tarif) { this.tarif = tarif; }

    public String getImmatriculation() { return immatriculation; }
    public void setImmatriculation(String immatriculation) { this.immatriculation = immatriculation; }

    @Override
    public String toString() {
        return "Vehicle{" +
                "id=" + id +
                ", marque='" + marque + '\'' +
                ", modele='" + modele + '\'' +
                ", tarif=" + tarif +
                ", immatriculation='" + immatriculation + '\'' +
                '}';
    }
}
