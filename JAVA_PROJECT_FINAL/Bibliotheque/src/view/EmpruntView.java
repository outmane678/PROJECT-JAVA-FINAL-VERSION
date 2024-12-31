package view;

import javax.swing.*;
import java.awt.*;
import javax.swing.table.DefaultTableModel;

public class EmpruntView extends JFrame {
    private JTextField idTextField;
    private JTextField livreIdTextField;
    private JTextField utilisateurIdTextField;
    private JTextField dateEmpruntTextField;
    private JTextField dateRetourPrevueTextField;
    private JTextField dateRetourEffectiveTextField; // Nouveau champ pour la date de retour effective
    private JTextField penaliteTextField; // Nouveau champ pour la pénalité
    private JTable empruntsTable;
    private JButton ajouterButton;
    private JButton modifierButton;
    private JButton enregistrerRetourButton; // Nouveau bouton
    private JTextField utilisateurRechercheField; // Champ de recherche par utilisateur
    private JButton rechercherButton; // Bouton de recherche
    private JTextField rechercheGeneraleField; // Recherche générale
    private JButton retourButton;

    public EmpruntView() {
        setTitle("Gestion des Emprunts");
        setSize(900, 600); // Ajustement pour un espace supplémentaire
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        
        // Panel principal
        JPanel panel = new JPanel();
        panel.setLayout(new BorderLayout());
        
        // Table des emprunts
        empruntsTable = new JTable();
        DefaultTableModel tableModel = new DefaultTableModel(
                new Object[][] {},
                new String[] {"ID Emprunt", "ID Livre", "ID Utilisateur", "Date Emprunt", "Date Retour Prevue", "Date Retour Effective", "Pénalité"}
        );
        empruntsTable.setModel(tableModel);
        JScrollPane scrollPane = new JScrollPane(empruntsTable);
        panel.add(scrollPane, BorderLayout.CENTER);
        
        // Formulaire d'ajout
        JPanel formulairePanel = new JPanel();
        formulairePanel.setLayout(new GridLayout(9, 2)); // 9 lignes pour inclure les nouveaux champs
        
        formulairePanel.add(new JLabel("ID Emprunt :"));
        idTextField = new JTextField();
        formulairePanel.add(idTextField);
        
        formulairePanel.add(new JLabel("ID Livre :"));
        livreIdTextField = new JTextField();
        formulairePanel.add(livreIdTextField);
        
        formulairePanel.add(new JLabel("ID Utilisateur :"));
        utilisateurIdTextField = new JTextField();
        formulairePanel.add(utilisateurIdTextField);
        
        formulairePanel.add(new JLabel("Date Emprunt (yyyy-MM-dd) :"));
        dateEmpruntTextField = new JTextField();
        formulairePanel.add(dateEmpruntTextField);
        
        formulairePanel.add(new JLabel("Date Retour Prevue (yyyy-MM-dd) :"));
        dateRetourPrevueTextField = new JTextField();
        formulairePanel.add(dateRetourPrevueTextField);
        
        formulairePanel.add(new JLabel(""));
        dateRetourEffectiveTextField = new JTextField();
        dateRetourEffectiveTextField.setEditable(false);
        formulairePanel.add(dateRetourEffectiveTextField);
        
        formulairePanel.add(new JLabel(""));
        penaliteTextField = new JTextField();
        penaliteTextField.setEditable(false); // Champ non éditable
        formulairePanel.add(penaliteTextField);
        
        // Recherche des emprunts par utilisateur
        formulairePanel.add(new JLabel("Consulter l'historique par ID Utilisateur :"));
        utilisateurRechercheField = new JTextField();
        formulairePanel.add(utilisateurRechercheField);

        formulairePanel.add(new JLabel("Rechercher:"));
        rechercheGeneraleField = new JTextField(20);
        formulairePanel.add(rechercheGeneraleField);
        
        // Boutons
        ajouterButton = new JButton("Ajouter Emprunt");
        modifierButton = new JButton("Modifier Emprunt");
        enregistrerRetourButton = new JButton("Enregistrer Retour"); // Nouveau bouton
        retourButton = new JButton("Retour");
        
        JPanel buttonPanel = new JPanel();
        buttonPanel.add(ajouterButton);
        buttonPanel.add(modifierButton);
        buttonPanel.add(enregistrerRetourButton); // Ajouter le bouton
        rechercherButton = new JButton("Consulter l'historique");
        buttonPanel.add(rechercherButton);
        buttonPanel.add(retourButton);  // Ajouter le bouton de retour
        
        // Ajouter les panels à la fenêtre
        panel.add(formulairePanel, BorderLayout.NORTH);
        panel.add(buttonPanel, BorderLayout.SOUTH);
        
        add(panel);
    }
    
    // Getters pour les champs de texte
    public JTextField getIdTextField() {
        return idTextField;
    }
    
    public JTextField getLivreIdTextField() {
        return livreIdTextField;
    }
    
    public JTextField getUtilisateurIdTextField() {
        return utilisateurIdTextField;
    }
    
    public JTextField getDateEmpruntTextField() {
        return dateEmpruntTextField;
    }
    
    public JTextField getDateRetourPrevueTextField() {
        return dateRetourPrevueTextField;
    }

    public JTextField getDateRetourEffectiveTextField() {
        return dateRetourEffectiveTextField;
    }

    public JTextField getPenaliteTextField() {
        return penaliteTextField;
    }

    // Getter pour le tableau des emprunts
    public JTable getEmpruntsTable() {
        return empruntsTable;
    }
    
    // Getters pour les boutons
    public JButton getAjouterButton() {
        return ajouterButton;
    }
    
    public JButton getModifierButton() {
        return modifierButton;
    }
    
    public JButton getEnregistrerRetourButton() {
        return enregistrerRetourButton; // Getter pour le bouton
    }

    // Getter pour le champ de recherche
    public JTextField getUtilisateurRechercheField() {
        return utilisateurRechercheField;
    }

    // Getter pour le bouton de recherche
    public JButton getRechercherButton() {
        return rechercherButton;
    }

    // Getter pour le champ de recherche générale
    public JTextField getRechercheGeneraleField() {
        return rechercheGeneraleField;
    }

    public JButton getRetourButton() {  // Méthode pour obtenir le bouton Retour
        return retourButton;
    }
}
