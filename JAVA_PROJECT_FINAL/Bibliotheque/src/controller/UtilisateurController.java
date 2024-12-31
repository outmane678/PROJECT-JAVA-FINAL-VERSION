package controller;

import exceptions.UtilisateurExisteException;
import exceptions.UtilisateurNonTrouveException;
import model.Utilisateur;
import model.UtilisateurModel;
import view.UtilisateurView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.util.List;

public class UtilisateurController {
    private UtilisateurModel model;
    private UtilisateurView view;

    public UtilisateurController() {
        this.model = new UtilisateurModel("C:\\Users\\DELL\\Desktop\\utilisateurs.csv");
        this.view = new UtilisateurView();

        // Ajouter les listeners aux boutons
        view.getAjouterButton().addActionListener(e -> ajouterUtilisateur());
        view.getModifierButton().addActionListener(e -> modifierUtilisateur());
        view.getSupprimerButton().addActionListener(e -> supprimerUtilisateur());
        view.getRetourButton().addActionListener(e -> retourMainView());

        // Ajouter un DocumentListener au champ de recherche
        view.getRechercheTextField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                rechercherUtilisateurs();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                rechercherUtilisateurs();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                rechercherUtilisateurs();
            }
        });

        // Charger les utilisateurs dans la vue
        chargerUtilisateurs();
    }

    private void retourMainView() {
        view.dispose(); // Ferme la fenêtre LivreView
    }

    private void chargerUtilisateurs() {
        List<Utilisateur> utilisateurs = model.getUtilisateurs();
        DefaultTableModel tableModel = (DefaultTableModel) view.getUtilisateursTable().getModel();
        tableModel.setRowCount(0); // Effacer les lignes actuelles
        for (Utilisateur utilisateur : utilisateurs) {
            tableModel.addRow(new Object[]{utilisateur.getId(), utilisateur.getNom(), utilisateur.getRole()});
        }
    }
    // ajouter utilisateur
    private void ajouterUtilisateur() {
        try {
            // Récupérer les valeurs des champs
            String idText = view.getIdTextField().getText();
            String nom = view.getNomTextField().getText();
            String role = view.getRoleTextField().getText().toLowerCase(); // Convertir en minuscule pour éviter les erreurs de casse
        
            // Vérifier si les champs sont vides
            if (idText.isEmpty() || nom.isEmpty() || role.isEmpty()) {
                JOptionPane.showMessageDialog(view, "Veuillez remplir toutes les informations.");
                return; // Arrêter l'exécution de la méthode si les champs sont vides
            }
        
            // Vérifier si l'ID est un nombre entier
            int id = Integer.parseInt(idText);
    
            // Validation du rôle
            if (!role.equals("membre") && !role.equals("bibliothécaire") && !role.equals("administrateur")) {
                JOptionPane.showMessageDialog(view, "Erreur : Le rôle doit être 'membre', 'bibliothécaire' ou 'administrateur'.");
                return;
            }
        
            // Ajouter l'utilisateur
            Utilisateur utilisateur = new Utilisateur(id, nom, role);
            model.ajouterUtilisateur(utilisateur);
            JOptionPane.showMessageDialog(view, "Utilisateur ajouté avec succès !");
            chargerUtilisateurs();
        
            // Réinitialisation des champs
            view.getIdTextField().setText("");
            view.getNomTextField().setText("");
            view.getRoleTextField().setText("");
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "Erreur de saisie : ID doit être un entier.");
        } catch (UtilisateurExisteException e) {
            JOptionPane.showMessageDialog(view, e.getMessage());
        }
    }
    
    

    private void modifierUtilisateur() {
        int selectedRow = view.getUtilisateursTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Veuillez sélectionner un utilisateur.");
            return;
        }
    
        // Vérifie si l'utilisateur est en train de modifier ou enregistrer
        if (view.getModifierButton().getText().equals("Modifier")) {
            // Remplit les champs avec les données de l'utilisateur sélectionné
            int idSelectionne = (int) view.getUtilisateursTable().getValueAt(selectedRow, 0);
            String nom = (String) view.getUtilisateursTable().getValueAt(selectedRow, 1);
            String role = (String) view.getUtilisateursTable().getValueAt(selectedRow, 2);
    
            view.getIdTextField().setText(String.valueOf(idSelectionne));
            view.getNomTextField().setText(nom);
            view.getRoleTextField().setText(role);
    
            // Empêche la modification de l'ID
            view.getIdTextField().setEditable(false);
    
            // Change le texte du bouton pour indiquer la validation
            view.getModifierButton().setText("Enregistrer");
        } else {
            // Valide les modifications
            try {
                String nom = view.getNomTextField().getText();
                String role = view.getRoleTextField().getText().toLowerCase(); // Convertir en minuscule pour validation
                int idSelectionne = Integer.parseInt(view.getIdTextField().getText());
    
                // Vérifie si les champs sont remplis
                if (nom.isEmpty() || role.isEmpty()) {
                    JOptionPane.showMessageDialog(view, "Veuillez remplir tous les champs avant d'enregistrer.");
                    return;
                }
    
                // Validation du rôle
                if (!role.equals("membre") && !role.equals("bibliothécaire") && !role.equals("administrateur")) {
                    JOptionPane.showMessageDialog(view, "Erreur : Le rôle doit être 'membre', 'bibliothécaire' ou 'administrateur'.");
                    return;
                }
    
                // Modification de l'utilisateur
                model.modifierUtilisateur(idSelectionne, nom, role);
                JOptionPane.showMessageDialog(view, "Utilisateur modifié avec succès !");
    
                // Rafraîchit la table et réinitialise les champs
                chargerUtilisateurs();
                réinitialiserChamps();
    
                // Réinitialise le bouton
                view.getModifierButton().setText("Modifier");
                view.getIdTextField().setEditable(true);
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(view, "Erreur de saisie : ID doit être un entier.");
            } catch (UtilisateurNonTrouveException e) {
                JOptionPane.showMessageDialog(view, e.getMessage());
            }
        }
    }
    
    

    private void supprimerUtilisateur() {
        int selectedRow = view.getUtilisateursTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Veuillez sélectionner un utilisateur.");
            return;
        }

        int id = (int) view.getUtilisateursTable().getValueAt(selectedRow, 0);
        try {
            model.supprimerUtilisateur(id);
            JOptionPane.showMessageDialog(view, "Utilisateur supprimé avec succès !");
            chargerUtilisateurs();
        } catch (UtilisateurNonTrouveException e) {
            JOptionPane.showMessageDialog(view, e.getMessage());
        }
    }

    // Méthode de recherche des utilisateurs
    private void rechercherUtilisateurs() {
        String query = view.getRechercheTextField().getText().trim(); // Trim any extra spaces
        List<Utilisateur> resultats = model.rechercherUtilisateurs(query); // Filter based on query
    
        DefaultTableModel tableModel = (DefaultTableModel) view.getUtilisateursTable().getModel();
        tableModel.setRowCount(0);  // Clear previous results
    
        // Add the filtered results back to the table
        for (Utilisateur utilisateur : resultats) {
            tableModel.addRow(new Object[]{
                utilisateur.getId(),
                utilisateur.getNom(),
                utilisateur.getRole()
            });
        }
    }

    // Ajout de la méthode getView()
    public UtilisateurView getView() {
        return this.view;
    }


    private void réinitialiserChamps() {
        // Réinitialise les champs de texte
        view.getIdTextField().setText("");
        view.getNomTextField().setText("");
        view.getRoleTextField().setText("");
    }
}
