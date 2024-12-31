package controller;

import view.StatistiqueView;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class StatistiqueController {
    private StatistiqueView view = new StatistiqueView();

    public StatistiqueController() {
        // Mettre à jour les statistiques
        afficherStatistiques();

        // Ajouter un écouteur au bouton "Fermer"
        view.closeButtonListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                fermerFenetre();
            }
        });
    }

    /**
     * Met à jour les statistiques dans la vue.
     */
    private void afficherStatistiques() {
        String livreLePlusEmprunte = "N/A";
        String utilisateurLePlusActif = "N/A";
        String nombreUtilisateurs = "0";
        String nombreLivres = "0";
        

        // Utilisation des ressources avec try-with-resources pour garantir la fermeture des flux
        try (
            BufferedReader livreReader = new BufferedReader(new FileReader("C:\\Users\\DELL\\Desktop\\livres.csv"));
            BufferedReader utilisateurReader = new BufferedReader(new FileReader("C:\\Users\\DELL\\Desktop\\utilisateurs.csv"));
            BufferedReader empruntsReader = new BufferedReader(new FileReader("C:\\Users\\DELL\\Desktop\\emprunts.csv"))
        ) {
            // Calculer le nombre total de livres en utilisant l'ID
            Set<Integer> livres = new HashSet<>();
            String line = livreReader.readLine(); // Lire l'en-tête
            while ((line = livreReader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 2) continue; // Ignorer les lignes mal formatées
                try {
                    int id = Integer.parseInt(values[0].trim()); // Lire l'ID du livre
                    livres.add(id); // Ajouter l'ID au Set (les doublons seront automatiquement ignorés)
                } catch (NumberFormatException e) {
                    System.out.println("Erreur de conversion d'ID pour la ligne: " + line);
                }
            }
            nombreLivres = String.valueOf(livres.size()); // Nombre de livres uniques par ID


            // Calculer le nombre total d'utilisateurs par ID
            Set<Integer> utilisateursSet = new HashSet<>();
            line = utilisateurReader.readLine(); // Lire l'en-tête
            while ((line = utilisateurReader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 2) continue; // Ignorer les lignes mal formatées

                try {
                    int utilisateurId = Integer.parseInt(values[0].trim()); // ID de l'utilisateur (entier)
                    utilisateursSet.add(utilisateurId); // Ajouter l'ID au Set des utilisateurs
                } catch (NumberFormatException e) {
                    System.out.println("Erreur de conversion d'ID utilisateur pour la ligne: " + line);
                }
            }

            // Nombre d'utilisateurs uniques par ID
            nombreUtilisateurs = String.valueOf(utilisateursSet.size()); // Nombre d'utilisateurs uniques


            // Lire les emprunts pour calculer les statistiques
            Map<Integer, Integer> empruntsParLivre = new HashMap<>();
            Map<Integer, Integer> empruntsParUtilisateur = new HashMap<>();
            line = empruntsReader.readLine(); // Lire l'en-tête
            while ((line = empruntsReader.readLine()) != null) {
                String[] values = line.split(",");
                if (values.length < 3) continue; // Ignorer les lignes mal formatées

                try {
                    int livreId = Integer.parseInt(values[1].trim()); // ID du livre
                    int utilisateurId = Integer.parseInt(values[2].trim()); // ID de l'utilisateur

                    // Compter les emprunts par livre (utiliser l'ID du livre)
                    empruntsParLivre.put(livreId, empruntsParLivre.getOrDefault(livreId, 0) + 1);

                    // Compter les emprunts par utilisateur (utiliser l'ID de l'utilisateur)
                    empruntsParUtilisateur.put(utilisateurId, empruntsParUtilisateur.getOrDefault(utilisateurId, 0) + 1);
                } catch (NumberFormatException e) {
                    System.out.println("Erreur de conversion pour les ID dans la ligne: " + line);
                }
            }

            // Trouver l'utilisateur le plus actif (par ID) - celui avec le plus grand nombre d'emprunts
            int maxEmpruntsUtilisateur = 0;
            int utilisateurLePlusActifIdInt = -1;
            for (Map.Entry<Integer, Integer> entry : empruntsParUtilisateur.entrySet()) {
                if (entry.getValue() > maxEmpruntsUtilisateur) {
                    maxEmpruntsUtilisateur = entry.getValue();
                    utilisateurLePlusActifIdInt = entry.getKey(); // ID de l'utilisateur le plus actif
                }
            }

            // Trouver le livre le plus emprunté (par ID) - celui avec le plus grand nombre d'emprunts
            int maxEmpruntsLivre = 0;
            int livreLePlusEmprunteIdInt = -1;
            for (Map.Entry<Integer, Integer> entry : empruntsParLivre.entrySet()) {
                if (entry.getValue() > maxEmpruntsLivre) {
                    maxEmpruntsLivre = entry.getValue();
                    livreLePlusEmprunteIdInt = entry.getKey(); // ID du livre le plus emprunté
                }
            }

            // Affecter les résultats à afficher
            livreLePlusEmprunte = String.valueOf(livreLePlusEmprunteIdInt);
            utilisateurLePlusActif = String.valueOf(utilisateurLePlusActifIdInt);

        } catch (IOException e) {
            e.printStackTrace(); // Gérer les erreurs
        }

        // Mettre à jour les labels dans la vue
        view.getLivrelesplusempruntesField().setText(livreLePlusEmprunte);
        view.getUtilisateurplusactifsField().setText(utilisateurLePlusActif);
        view.getNombredutilisateursField().setText(nombreUtilisateurs);
        view.getNombredelivresField().setText(nombreLivres);
    }

    /**
     * Ferme la fenêtre de statistiques.
     */
    private void fermerFenetre() {
        view.dispose();
    }

    public StatistiqueView getView() {
        return view;
    }
}
