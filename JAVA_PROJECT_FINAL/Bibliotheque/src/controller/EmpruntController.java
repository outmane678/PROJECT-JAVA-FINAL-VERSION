package controller;

import exceptions.EmpruntExisteException;
import exceptions.EmpruntNonTrouveException;
import model.Emprunt;
import model.EmpruntModel;
import model.Livre;
import model.LivreModel;
import model.Utilisateur;
import model.UtilisateurModel;
import view.EmpruntView;

import javax.swing.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;

public class EmpruntController {
    private EmpruntModel model;
    private EmpruntView view;
    private LivreModel livreModel;
    private UtilisateurModel utilisateurModel;

    public EmpruntController() {
        // Initialisation des modèles
        this.livreModel = new LivreModel("C:\\Users\\DELL\\Desktop\\livres.csv");
        this.utilisateurModel = new UtilisateurModel("C:\\Users\\DELL\\Desktop\\utilisateurs.csv");
        this.model = new EmpruntModel("C:\\Users\\DELL\\Desktop\\emprunts.csv", utilisateurModel.getUtilisateurs(), livreModel.getLivres());
        this.view = new EmpruntView();

        // Ajouter les listeners aux boutons
        view.getAjouterButton().addActionListener(e -> ajouterEmprunt());
        view.getModifierButton().addActionListener(e -> modifierEmprunt());
        view.getEnregistrerRetourButton().addActionListener(e -> enregistrerRetour());
        view.getRechercherButton().addActionListener(e -> rechercherEmpruntsParUtilisateur());
        view.getRetourButton().addActionListener(e -> retourMainView());

        // Ajouter un DocumentListener pour la recherche par texte
        view.getRechercheGeneraleField().getDocument().addDocumentListener(new DocumentListener() {
            @Override
            public void insertUpdate(DocumentEvent e) {
                rechercherEmprunts();
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                rechercherEmprunts();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                rechercherEmprunts();
            }
        });

        // Charger les emprunts dans la vue
        chargerEmprunts();

        // Afficher la vue
        view.setVisible(true);
    }

    private void retourMainView() {
        view.dispose(); // Ferme la fenêtre LivreView
    }

    private void chargerEmprunts() {
        List<Emprunt> emprunts = model.getEmprunts();
        DefaultTableModel tableModel = (DefaultTableModel) view.getEmpruntsTable().getModel();
        tableModel.setRowCount(0); // Effacer les lignes actuelles
        for (Emprunt emprunt : emprunts) {
            tableModel.addRow(new Object[] {
                    emprunt.getId(),
                    emprunt.getLivre().getId(),
                    emprunt.getUtilisateur().getId(),
                    emprunt.getDateEmprunt(),
                    emprunt.getDateRetourPrevue(),
                    emprunt.getDateRetourEffective(),
                    emprunt.getPenalite()
            });
        }
    }

    private void ajouterEmprunt() {
        // Récupérer les valeurs des champs
        String idText = view.getIdTextField().getText();
        String livreIdText = view.getLivreIdTextField().getText();
        String utilisateurIdText = view.getUtilisateurIdTextField().getText();
        String dateEmpruntText = view.getDateEmpruntTextField().getText();
        String dateRetourPrevueText = view.getDateRetourPrevueTextField().getText();
    
        // Vérifier si l'un des champs est vide
        if (idText.isEmpty() || livreIdText.isEmpty() || utilisateurIdText.isEmpty() || dateEmpruntText.isEmpty() || dateRetourPrevueText.isEmpty()) {
            JOptionPane.showMessageDialog(view, "Veuillez remplir toutes les informations avant d'enregistrer.");
            return; // Arrêter l'exécution si un champ est vide
        }
    
        try {
            // Convertir les champs nécessaires en entiers et dates
            int id = Integer.parseInt(idText);
            int livreId = Integer.parseInt(livreIdText);
            int utilisateurId = Integer.parseInt(utilisateurIdText);
            LocalDate dateEmprunt = LocalDate.parse(dateEmpruntText);
            LocalDate dateRetourPrevue = LocalDate.parse(dateRetourPrevueText);
    
            LocalDate today = LocalDate.now();
    
            // Vérifications
            if (!dateEmprunt.equals(today)) {
                JOptionPane.showMessageDialog(view, "La date d'emprunt doit être la date d'aujourd'hui : " + today);
                return;
            }
            if (!dateRetourPrevue.isAfter(today)) {
                JOptionPane.showMessageDialog(view, "La date de retour prévue doit être après aujourd'hui : " + today);
                return;
            }
            if (model.getEmprunts().stream().anyMatch(e -> e.getId() == id)) {
                throw new EmpruntExisteException("Un emprunt avec cet ID existe déjà !");
            }
    
            Livre livre = livreModel.getLivres().stream()
                    .filter(l -> l.getId() == livreId)
                    .findFirst()
                    .orElseThrow(() -> new EmpruntNonTrouveException("Livre introuvable avec cet ID !"));
    
            Utilisateur utilisateur = utilisateurModel.getUtilisateurs().stream()
                    .filter(u -> u.getId() == utilisateurId)
                    .findFirst()
                    .orElseThrow(() -> new EmpruntNonTrouveException("Utilisateur introuvable avec cet ID !"));
    
            Emprunt emprunt = new Emprunt(id, livre, utilisateur, dateEmprunt, dateRetourPrevue);
            model.ajouterEmprunt(emprunt);
            JOptionPane.showMessageDialog(view, "Emprunt ajouté avec succès !");
            chargerEmprunts();
            reinitialiserChamps();
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "ID, Livre ID et Utilisateur ID doivent être des entiers.");
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(view, "Format de date invalide. Utilisez yyyy-MM-dd.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, e.getMessage());
        }
    }
    

    private void modifierEmprunt() {
        int selectedRow = view.getEmpruntsTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Veuillez sélectionner un emprunt à modifier.");
            return;
        }
    
        try {
            int id = (int) view.getEmpruntsTable().getValueAt(selectedRow, 0);
            String newDateRetour = JOptionPane.showInputDialog(view, "Nouvelle date de retour prevue (yyyy-MM-dd) :");
    
            // Vérifier si l'utilisateur a annulé la saisie (newDateRetour sera null)
            if (newDateRetour == null) {
                return; // Arrêter l'exécution si l'utilisateur a annulé
            }
    
            LocalDate dateRetour = LocalDate.parse(newDateRetour);
            if (!dateRetour.isAfter(LocalDate.now())) {
                JOptionPane.showMessageDialog(view, "La date de retour doit être après aujourd'hui.");
                return;
            }
    
            model.modifierEmprunt(id, dateRetour);
            JOptionPane.showMessageDialog(view, "Date de retour prevue modifiée avec succès !");
            chargerEmprunts();
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(view, "Format de date invalide. Utilisez yyyy-MM-dd.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, e.getMessage());
        }
    }
    

    private void enregistrerRetour() {
        int selectedRow = view.getEmpruntsTable().getSelectedRow();
        if (selectedRow == -1) {
            JOptionPane.showMessageDialog(view, "Veuillez sélectionner un emprunt pour enregistrer le retour.");
            return;
        }
    
        try {
            int id = (int) view.getEmpruntsTable().getValueAt(selectedRow, 0);
            Emprunt emprunt = model.findEmpruntById(id);
    
            // Vérifier si le retour a déjà été enregistré
            if (emprunt.getDateRetourEffective() != null) {
                JOptionPane.showMessageDialog(view, "Ce livre a déjà été retourné.");
                return;
            }
    
            // Demander à l'utilisateur de saisir la date de retour effective
            String newDateRetour = JOptionPane.showInputDialog(view, "Entrez la date de retour effective (yyyy-MM-dd) :");
    
            // Vérification du format de la date
            LocalDate dateRetourEffective = LocalDate.parse(newDateRetour);
    
            // Vérifier que la date de retour effective est après la date d'emprunt
            if (!dateRetourEffective.isAfter(emprunt.getDateEmprunt())) {
                JOptionPane.showMessageDialog(view, "La date de retour effective ne peut pas être avant la date d'emprunt.");
                return;
            }
    
            // Enregistrer le retour
            model.enregistrerRetour(id, dateRetourEffective);
    
            // Calcul de la pénalité si la date de retour est après la date prévue
            LocalDate dateRetourPrevue = emprunt.getDateRetourPrevue();
            if (dateRetourEffective.isAfter(dateRetourPrevue)) {
                double penalite = emprunt.calculerPenalite(); // Calcul de la pénalité
                model.ajouterPenalite(id, penalite);
                JOptionPane.showMessageDialog(view, "Retour enregistré avec succès ! Pénalité : " + penalite + " EUR.");
            } else {
                JOptionPane.showMessageDialog(view, "Retour enregistré avec succès !");
            }
    
            // Recharger les emprunts dans la vue
            chargerEmprunts();
    
        } catch (DateTimeParseException e) {
            JOptionPane.showMessageDialog(view, "Format de date invalide. Utilisez yyyy-MM-dd.");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(view, e.getMessage());
        }
    }

    private void rechercherEmprunts() {
        String query = view.getRechercheGeneraleField().getText().trim(); // Récupérer et nettoyer la requête
        List<Emprunt> resultats = model.rechercherEmprunts(query); // Filtrer les emprunts
    
        DefaultTableModel tableModel = (DefaultTableModel) view.getEmpruntsTable().getModel();
        tableModel.setRowCount(0); // Effacer les résultats précédents
    
        // Debug : Afficher le nombre de résultats dans la console
        System.out.println("Emprunts trouvés : " + resultats.size());
    
        // Ajouter les résultats filtrés dans le tableau
        for (Emprunt emprunt : resultats) {
            tableModel.addRow(new Object[]{
                emprunt.getId(), // ID de l'emprunt
                emprunt.getLivre().getId(), // Titre du livre
                emprunt.getUtilisateur().getId(), // Nom de l'utilisateur
                emprunt.getDateEmprunt(), // Date de l'emprunt
                emprunt.getDateRetourPrevue(), // Date de retour prévue
                emprunt.getDateRetourEffective(), // Date de retour effective (peut être null)
                emprunt.getPenalite() // Pénalité éventuelle
            });
        }
    }
    

    private void reinitialiserChamps() {
        view.getIdTextField().setText("");
        view.getLivreIdTextField().setText("");
        view.getUtilisateurIdTextField().setText("");
        view.getDateEmpruntTextField().setText("");
        view.getDateRetourPrevueTextField().setText("");
    }


    private void rechercherEmpruntsParUtilisateur() {
        try {
            String utilisateurIdText = view.getUtilisateurRechercheField().getText();
    
            DefaultTableModel tableModel = (DefaultTableModel) view.getEmpruntsTable().getModel();
            tableModel.setRowCount(0);
    
            List<Emprunt> emprunts;
    
            if (utilisateurIdText.isEmpty()) {
                // If the input is empty, retrieve all emprunts
                emprunts = model.getEmprunts();
            } else {
                // Otherwise, retrieve emprunts for a specific user
                int utilisateurId = Integer.parseInt(utilisateurIdText);
                emprunts = model.getEmpruntsParUtilisateur(utilisateurId);
            }
    
            for (Emprunt emprunt : emprunts) {
                tableModel.addRow(new Object[] {
                        emprunt.getId(),
                        emprunt.getLivre().getId(),
                        emprunt.getUtilisateur().getId(),
                        emprunt.getDateEmprunt(),
                        emprunt.getDateRetourPrevue(),
                        emprunt.getDateRetourEffective(),
                        emprunt.getPenalite()
                });
            }
        } catch (NumberFormatException e) {
            JOptionPane.showMessageDialog(view, "L'ID utilisateur doit être un entier.");
        }
    }
    

    public EmpruntView getView() {
        return view;
    }
}
