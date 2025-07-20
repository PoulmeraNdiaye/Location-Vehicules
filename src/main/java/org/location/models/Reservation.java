package org.location.models;

//import  javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;
import jakarta.persistence.*;
@Entity
@Table(name = "reservations")
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vehicle_id", nullable = false)
    private Vehicle vehicle;

    @Column(name = "date_debut", nullable = false)
    private LocalDate dateDebut;

    @Column(name = "date_fin", nullable = false)
    private LocalDate dateFin;

    @Column(name = "statut", nullable = false)
    private String statut;

    @Column(name = "avec_chauffeur", nullable = false)
    private Boolean avecChauffeur = false;
    private Double coutTotal;

    // Constructeurs
    public Reservation() {
    }

    public Reservation(Client client, Vehicle vehicle, String statut) {
        this.client = client;
        this.vehicle = vehicle;
        this.statut = statut;
        this.avecChauffeur = false;
    }

    public Reservation(Client client, Vehicle vehicle, LocalDate dateDebut, LocalDate dateFin, String statut, Boolean avecChauffeur) {
        this.client = client;
        this.vehicle = vehicle;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.statut = statut;
        this.avecChauffeur = avecChauffeur;
    }

    // Getters et Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Vehicle getVehicle() {
        return vehicle;
    }

    public void setVehicle(Vehicle vehicle) {
        this.vehicle = vehicle;
    }

    public LocalDate getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(LocalDate dateDebut) {
        this.dateDebut = dateDebut;
    }

    public LocalDate getDateFin() {
        return dateFin;
    }

    public void setDateFin(LocalDate dateFin) {
        this.dateFin = dateFin;
    }

    public String getStatut() {
        return statut;
    }

    public void setStatut(String statut) {
        this.statut = statut;
    }

    public Boolean getAvecChauffeur() {
        return avecChauffeur;
    }

    public void setAvecChauffeur(Boolean avecChauffeur) {
        this.avecChauffeur = avecChauffeur;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Reservation that = (Reservation) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
    public Double getCoutTotal() {
        return coutTotal;
    }

    public void setCoutTotal(Double coutTotal) {
        this.coutTotal = coutTotal;
    }


    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", client=" + (client != null ? client.getId() : null) +
                ", vehicle=" + (vehicle != null ? vehicle.getId() : null) +
                ", dateDebut=" + dateDebut +
                ", dateFin=" + dateFin +
                ", statut='" + statut + '\'' +
                ", avecChauffeur=" + avecChauffeur +
                '}';
    }
}