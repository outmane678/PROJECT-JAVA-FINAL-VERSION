package model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public class Emprunt {
    private int id;
    private Livre livre;
    private Utilisateur utilisateur;
    private LocalDate dateEmprunt;
    private LocalDate dateRetourPrevue; // Date prévue pour le retour
    private LocalDate dateRetourEffective; // Date effective du retour
    private double penalite;

    // constructeur par défaut
    public Emprunt() {}

    // constructeur par initialisation
    public Emprunt(int id, Livre livre, Utilisateur utilisateur, LocalDate dateEmprunt, LocalDate dateRetourPrevue) {
        this.id = id;
        this.livre = livre;
        this.utilisateur = utilisateur;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = null; // Pas encore retourné
        this.penalite = 0.0;  // Initialement, aucune pénalité
    }

    // Constructeur avec tous les paramètres
    public Emprunt(int id, Livre livre, Utilisateur utilisateur, LocalDate dateEmprunt, LocalDate dateRetourPrevue,
                   LocalDate dateRetourEffective, double penalite) {
        this.id = id;
        this.livre = livre;
        this.utilisateur = utilisateur;
        this.dateEmprunt = dateEmprunt;
        this.dateRetourPrevue = dateRetourPrevue;
        this.dateRetourEffective = dateRetourEffective;
        this.penalite = penalite;
    }

    // getters et setters
    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Livre getLivre() {
        return this.livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public Utilisateur getUtilisateur() {
        return this.utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public LocalDate getDateEmprunt() {
        return this.dateEmprunt;
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public LocalDate getDateRetourPrevue() {
        return this.dateRetourPrevue;
    }

    public void setDateRetourPrevue(LocalDate dateRetourPrevue) {
        this.dateRetourPrevue = dateRetourPrevue;
    }

    public LocalDate getDateRetourEffective() {
        return dateRetourEffective;
    }

    public void setDateRetourEffective(LocalDate dateRetourEffective) {
        this.dateRetourEffective = dateRetourEffective;
    }

    public double getPenalite() {
        return penalite;
    }

    public void setPenalite(double penalite) {
        this.penalite = penalite;
    }

    // Calculer la pénalité en fonction des jours de retard
    public double calculerPenalite() {
        if (dateRetourEffective != null && dateRetourEffective.isAfter(dateRetourPrevue)) {
            long joursDeRetard = ChronoUnit.DAYS.between(dateRetourPrevue, dateRetourEffective);
            return joursDeRetard * 5; // Exemple : 5 unités par jour de retard
        }
        return 0;
    }

    // méthode toString
@Override
    public String toString(){
        return "Emprunt ID: " + id + ", Livre: " + livre.getTitre() + ", Utilisateur: " + utilisateur.getNom() +
               ", Date Emprunt: " + dateEmprunt + ", Date Retour Prevue: " + dateRetourPrevue
               + ", Date Retour Effective: "+ (dateRetourEffective != null ? dateRetourEffective : "Pas encore retourné") +
               ", Pénalité: " + calculerPenalite();
    }
}
