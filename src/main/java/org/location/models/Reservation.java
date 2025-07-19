package org.location.models;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Objects;

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
    private LocalDate startDate;

    @Column(name = "date_fin", nullable = false)
    private LocalDate endDate;

    @Column(name = "avec_chauffeur", nullable = false)
    private Boolean avecChauffeur;

    @Column(name = "statut", nullable = false)
    private String statut;

    @Column(name = "montantFacture", nullable = false)
    private Double montantFacture;

    public Reservation() {
        this.montantFacture = 0.0;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Client getClient() { return client; }
    public void setClient(Client client) { this.client = client; }
    public Vehicle getVehicle() { return vehicle; }
    public void setVehicle(Vehicle vehicle) { this.vehicle = vehicle; }
    public LocalDate getStartDate() { return startDate; }
    public void setStartDate(LocalDate startDate) { this.startDate = startDate; }
    public LocalDate getEndDate() { return endDate; }
    public void setEndDate(LocalDate endDate) { this.endDate = endDate; }
    public Boolean getAvecChauffeur() { return avecChauffeur; }
    public void setAvecChauffeur(Boolean avecChauffeur) { this.avecChauffeur = avecChauffeur; }
    public String getStatut() { return statut; }
    public void setStatut(String statut) { this.statut = statut; }
    public Double getMontantFacture() { return montantFacture; }
    public void setMontantFacture(Double montantFacture) { this.montantFacture = montantFacture; }

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

    @Override
    public String toString() {
        return "Reservation{" +
                "id=" + id +
                ", client=" + (client != null ? client.getId() : null) +
                ", vehicle=" + (vehicle != null ? vehicle.getId() : null) +
                ", startDate=" + startDate +
                ", endDate=" + endDate +
                ", avecChauffeur=" + avecChauffeur +
                ", statut='" + statut + '\'' +
                ", montantFacture=" + montantFacture +
                '}';
    }
}