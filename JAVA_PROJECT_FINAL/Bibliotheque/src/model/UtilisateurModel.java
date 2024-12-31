package model;

import exceptions.UtilisateurExisteException;
import exceptions.UtilisateurNonTrouveException;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UtilisateurModel {
    private List<Utilisateur> utilisateurs;
    private String fichierCSV;

    public UtilisateurModel(String fichierCSV) {
        this.fichierCSV = fichierCSV;
        utilisateurs = new ArrayList<>();
        chargerUtilisateursDepuisCSV();
    }

    public void ajouterUtilisateur(Utilisateur utilisateur) throws UtilisateurExisteException {
        for (Utilisateur u : utilisateurs) {
            if (u.getId() == utilisateur.getId()) {
                throw new UtilisateurExisteException("Erreur : Un utilisateur avec cet ID existe déjà !");
            }
        }
        utilisateurs.add(utilisateur);
        sauvegarderUtilisateursDansCSV();
    }

    public void modifierUtilisateur(int id, String nouveauNom, String nouveauRole) throws UtilisateurNonTrouveException {
        boolean utilisateurTrouve = false;
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getId() == id) {
                utilisateur.setNom(nouveauNom);
                utilisateur.setRole(nouveauRole);
                utilisateurTrouve = true;
                break;
            }
        }
        if (!utilisateurTrouve) {
            throw new UtilisateurNonTrouveException("Erreur : Utilisateur non trouvé pour la modification !");
        }
        sauvegarderUtilisateursDansCSV();
    }

    public void supprimerUtilisateur(int id) throws UtilisateurNonTrouveException {
        boolean utilisateurSupprime = false;
        for (int i = 0; i < utilisateurs.size(); i++) {
            if (utilisateurs.get(i).getId() == id) {
                utilisateurs.remove(i);
                utilisateurSupprime = true;
                break;
            }
        }
        if (!utilisateurSupprime) {
            throw new UtilisateurNonTrouveException("Erreur : Utilisateur non trouvé pour la suppression !");
        }
        sauvegarderUtilisateursDansCSV();
    }

    public List<Utilisateur> getUtilisateurs() {
        return utilisateurs;
    }

    private void chargerUtilisateursDepuisCSV() {
        try (BufferedReader reader = new BufferedReader(new FileReader(fichierCSV))) {
            String ligne;
            while ((ligne = reader.readLine()) != null) {
                // Ignore empty lines
                if (ligne.trim().isEmpty()) {
                    continue;
                }

                String[] donnees = ligne.split(",");
                
                // Ensure the CSV line has at least 3 columns: ID, Name, Role
                if (donnees.length < 3) {
                    System.out.println("Skipping invalid row (too few columns): " + ligne);
                    continue;  // Skip invalid rows
                }

                try {
                    int id = Integer.parseInt(donnees[0].trim());  // Parse ID
                    String nom = donnees[1].trim();  // Name
                    String role = donnees[2].trim();  // Role

                    // Add valid user to list
                    utilisateurs.add(new Utilisateur(id, nom, role));
                } catch (NumberFormatException e) {
                    // Log the error and skip invalid rows
                    System.out.println("Skipping row with invalid ID: " + ligne);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void sauvegarderUtilisateursDansCSV() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fichierCSV))) {
            // Écrire l'en-tête
            writer.write("id,nom,role");
            writer.newLine();
            for (Utilisateur utilisateur : utilisateurs) {
                writer.write(utilisateur.getId() + "," + utilisateur.getNom() + "," + utilisateur.getRole());
                writer.newLine();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

     // Méthode pour récupérer un utilisateur par son ID
     public Utilisateur getUtilisateurById(int id) {
        for (Utilisateur utilisateur : utilisateurs) {
            if (utilisateur.getId() == id) {
                return utilisateur; // Retourner l'utilisateur si l'ID correspond
            }
        }
        return null; // Retourner null si aucun utilisateur avec cet ID n'a été trouvé
    }

    // Méthode pour rechercher des utilisateurs
    public List<Utilisateur> rechercherUtilisateurs(String query) {
        if (query == null || query.trim().isEmpty()) {
            return utilisateurs; // Return all users if the query is empty
        }
    
        // Filter the list based on multiple fields (ID, name, role)
        return utilisateurs.stream()
                .filter(utilisateur -> 
                    String.valueOf(utilisateur.getId()).contains(query) ||  // Search by ID
                    utilisateur.getNom().toLowerCase().contains(query.toLowerCase()) || // Search by name
                    utilisateur.getRole().toLowerCase().contains(query.toLowerCase())) // Search by role
                .collect(Collectors.toList());
    }
}
