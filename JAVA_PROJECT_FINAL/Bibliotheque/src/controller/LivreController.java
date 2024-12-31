package controller;

import model.Livre;
import model.LivreModel;
import exceptions.LivreDejaExisteException;
import exceptions.LivreNonTrouveException;
import view.LivreView;
import view.MainView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class LivreController {
    private final LivreModel model;
    private final LivreView view;

    public LivreController() {
        this.model = new LivreModel("C:\\Users\\DELL\\Desktop\\livres.csv");
        this.view = new LivreView();

        // Ajouter les listeners aux boutons
        view.getAjouterButton().addActionListener(e -> ajouterLivre());
        view.getModifierButton().addActionListener(e -> modifierLivre());
        view.getSupprimerButton().addActionListener(e -> supprimerLivre());
        view.getRetourButton().addActionListener(e -> retourMainView());

        // Ajouter un DocumentListener au champ de recherche
        view.getRechercheTextField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                rechercherLivres();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                rechercherLivres();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                rechercherLivres();
            }
        });

        // Charger les livres dans la vue
        chargerLivres();
    }

    private void retourMainView() {
        view.dispose(); // Ferme la fenêtre LivreView
    }

    public LivreView getView() {
        return view;
    }

    private void chargerLivres() {
        List<Livre> livres = model.getLivres();
        DefaultTableModel tableModel = (DefaultTableModel) view.getLivresTable().getModel();
        tableModel.setRowCount(0); // Effacer les lignes actuelles
        for (Livre livre : livres) {
            tableModel.addRow(new Object[]{
                livre.getId(),
                livre.getTitre(),
                livre.getAuteur(),
                livre.getAnneePublication(),
                livre.getGenre()
            });
        }
    }

    private void ajouterLivre() {
        // Récupérer les valeurs des champs
        String idText = view.getIdTextField().getText();
        String titre = view.getTitreTextField().getText();
        String auteur = view.getAuteurTextField().getText();
        String anneeText = view.getAnneeTextField().getText();
        String genre = view.getGenreTextField().getText();
    
        // Vérifier si l'un des champs est vide
        if (idText.isEmpty() || titre.isEmpty() || auteur.isEmpty() || anneeText.isEmpty() || genre.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Veuillez remplir toutes les informations avant d'enregistrer.");
            return; // Arrêter l'exécution si un champ est vide
        }
    
        try {
            // Convertir les champs nécessaires en entiers
            int id = Integer.parseInt(idText);
            int annee = Integer.parseInt(anneeText);
    
            // Créer l'objet Livre
            Livre livre = new Livre(id, titre, auteur, annee, genre);
    
            // Ajouter le livre
            model.ajouterLivre(livre); // Peut lancer LivreDejaExisteException
            JOptionPane.showMessageDialog(view, "Livre ajouté avec succès !");
            chargerLivres();
    
            // Réinitialisation des champs
            réinitialiserChamps();
        } catch (LivreDejaExisteException e) {
            JOptionPane.showMessageDialog(view, "Erreur : Un livre avec cet ID existe déjà !");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Veuillez entrer un ID et une année valides.");
        }
    }
    

    private void modifierLivre() {
        int selectedRow = view.getLivresTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Veuillez sélectionner un livre.");
            return;
        }
    
        // Vérifie si l'utilisateur est en train de modifier
        if (view.getModifierButton().getText().equals("Modifier")) {
            // Remplit les champs avec les données du livre sélectionné
            int idSelectionne = (int) view.getLivresTable().getValueAt(selectedRow, 0);
            String titre = (String) view.getLivresTable().getValueAt(selectedRow, 1);
            String auteur = (String) view.getLivresTable().getValueAt(selectedRow, 2);
            int annee = (int) view.getLivresTable().getValueAt(selectedRow, 3);
            String genre = (String) view.getLivresTable().getValueAt(selectedRow, 4);
    
            view.getIdTextField().setText(String.valueOf(idSelectionne));
            view.getTitreTextField().setText(titre);
            view.getAuteurTextField().setText(auteur);
            view.getAnneeTextField().setText(String.valueOf(annee));
            view.getGenreTextField().setText(genre);
    
            // Empêche la modification de l'ID
            view.getIdTextField().setEditable(false);
    
            // Change le texte du bouton pour indiquer la validation
            view.getModifierButton().setText("Enregistrer");
        } else {
            // Valide les modifications
            try {
                int idSelectionne = Integer.parseInt(view.getIdTextField().getText());
                String titre = view.getTitreTextField().getText();
                String auteur = view.getAuteurTextField().getText();
                int annee = Integer.parseInt(view.getAnneeTextField().getText());
                String genre = view.getGenreTextField().getText();
    
                model.modifierLivre(idSelectionne, titre, auteur, annee, genre);
                JOptionPane.showMessageDialog(view, "Livre modifié avec succès !");
                chargerLivres();
                réinitialiserChamps();
    
                // Réinitialise le bouton
                view.getModifierButton().setText("Modifier");
                view.getIdTextField().setEditable(true);
            } catch (LivreNonTrouveException e) {
                JOptionPane.showMessageDialog(view, "Erreur : Livre non trouvé.");
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view, "Erreur de saisie : ID et Année doivent être des entiers.");
            }
        }
    }
    

    private void supprimerLivre() {
        int selectedRow = view.getLivresTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Veuillez sélectionner un livre à supprimer.");
            return;
        }

        try {
            int id = (int) view.getLivresTable().getValueAt(selectedRow, 0);
            model.supprimerLivre(id);
            JOptionPane.showMessageDialog(view, "Livre supprimé avec succès !");
            chargerLivres();
        } catch (LivreNonTrouveException e) {
            JOptionPane.showMessageDialog(view, "Erreur : Livre non trouvé.");
        }
    }

    // The current method is fine, but let's ensure it updates the table properly.
    private void rechercherLivres() {
        String query = view.getRechercheTextField().getText().trim(); // Trim any extra spaces
        List<Livre> resultats = model.rechercherLivres(query); // Filter based on query
    
        DefaultTableModel tableModel = (DefaultTableModel) view.getLivresTable().getModel();
        tableModel.setRowCount(0);  // Clear previous results
    
        // Debug: Afficher les résultats dans la console pour voir si la recherche fonctionne
        System.out.println("Livres trouvés : " + resultats.size());
    
        // Add the filtered results back to the table
        for (Livre livre : resultats) {
            tableModel.addRow(new Object[]{
                livre.getId(),
                livre.getTitre(),
                livre.getAuteur(),
                livre.getAnneePublication(),
                livre.getGenre()
            });
        }
    }
    
    private void réinitialiserChamps() {
        view.getIdTextField().setText("");
        view.getTitreTextField().setText("");
        view.getAuteurTextField().setText("");
        view.getAnneeTextField().setText("");
        view.getGenreTextField().setText("");
    }
}
