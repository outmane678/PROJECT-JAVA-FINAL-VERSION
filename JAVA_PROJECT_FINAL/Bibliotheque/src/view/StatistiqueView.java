package view;

import javax.swing.*;
import java.awt.*;

public class StatistiqueView extends JFrame {
    private static final long serialVersionUID = 1L;

    // Labels
    private JLabel livrelesplusempruntesLabel = new JLabel("Le livre le plus emprunté");
    private JLabel utilisateurplusactifsLabel = new JLabel("Utilisateur le plus actif");
    private JLabel nombredutilisateursLabel = new JLabel("Nombre d'utilisateurs");
    private JLabel nombredelivresLabel = new JLabel("Nombre de livres");
    
    // Statistiques variables (initialiser avec des valeurs par défaut)
    private String livreleplusemprunte = "Non défini";
    private String utilisateurleplusactif = "Non défini";
    private String nombreutilisateur = "0";
    private String nombrelivres = "0";

    // Statistiques Fields
    private JLabel livrelesplusempruntesField = new JLabel(livreleplusemprunte);
    private JLabel utilisateurplusactifsField = new JLabel(utilisateurleplusactif);
    private JLabel nombredutilisateursField = new JLabel(nombreutilisateur);
    private JLabel nombredelivresField = new JLabel(nombrelivres);

    // Buttons
    private JButton closeButton = new JButton("Fermer");

    public StatistiqueView() {
        this.setTitle("Statistiques de la bibliotheque");
        this.setDefaultCloseOperation(JFrame.HIDE_ON_CLOSE);
        this.setSize(400, 200);
        this.setLocationRelativeTo(null);

        // Initialize components
        addComponents();

        this.setVisible(true);
    }
    
    private void addComponents() {
        // Panel for form fields
        JPanel formPanel = new JPanel(new GridLayout(4, 2, 5, 5));
        formPanel.setBorder(BorderFactory.createTitledBorder("Informations sur la bibliotheque"));
        formPanel.add(livrelesplusempruntesLabel);
        formPanel.add(getLivrelesplusempruntesField());
        formPanel.add(utilisateurplusactifsLabel);
        formPanel.add(getUtilisateurplusactifsField());
        formPanel.add(nombredutilisateursLabel);
        formPanel.add(getNombredutilisateursField());
        formPanel.add(nombredelivresLabel);
        formPanel.add(getNombredelivresField());

        // Panel for buttons
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        buttonPanel.add(closeButton);

        // Main layout
        this.setLayout(new BorderLayout(10, 10));
        this.add(formPanel, BorderLayout.NORTH);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    public void closeButtonListener(java.awt.event.ActionListener listenAjouterButton) {
        closeButton.addActionListener(listenAjouterButton);
    }

    public JButton getcloseButton() {
        return closeButton;
    }

    public JLabel getLivrelesplusempruntesField() {
        return livrelesplusempruntesField;
    }

    public void setLivrelesplusempruntesField(JLabel livrelesplusempruntesField) {
        this.livrelesplusempruntesField = livrelesplusempruntesField;
    }

    public JLabel getUtilisateurplusactifsField() {
        return utilisateurplusactifsField;
    }

    public void setUtilisateurplusactifsField(JLabel utilisateurplusactifsField) {
        this.utilisateurplusactifsField = utilisateurplusactifsField;
    }

    public JLabel getNombredutilisateursField() {
        return nombredutilisateursField;
    }

    public void setNombredutilisateursField(JLabel nombredutilisateursField) {
        this.nombredutilisateursField = nombredutilisateursField;
    }

    public JLabel getNombredelivresField() {
        return nombredelivresField;
    }

    public void setNombredelivresField(JLabel nombredelivresField) {
        this.nombredelivresField = nombredelivresField;
    }
}
