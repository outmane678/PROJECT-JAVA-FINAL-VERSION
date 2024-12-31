package view;

import javax.swing.*;
import java.awt.*;

public class MainView extends JFrame {
    public JButton btnGererEmprunt;
    public JButton btnGererLivre;
    public JButton btnGererUtilisateur;
    public JButton btnAfficherStatistiques;  // Nouveau bouton pour afficher les statistiques

    public MainView() {
        setTitle("Main View");
        setSize(400, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Title
        JLabel titleLabel = new JLabel("Tableau de Bord", JLabel.CENTER);
        titleLabel.setFont(new Font("Arial", Font.BOLD, 24));
        add(titleLabel, BorderLayout.NORTH);

        // Boutons pour naviguer vers les modules
        btnGererEmprunt = new JButton("Gérer Emprunts");
        btnGererLivre = new JButton("Gérer Livres");
        btnGererUtilisateur = new JButton("Gérer Utilisateurs");
        btnAfficherStatistiques = new JButton("Afficher Statistiques");  // Ajouter ce bouton

        // Panel pour ajouter les boutons
        JPanel panel = new JPanel();
        panel.add(btnGererLivre);
        panel.add(btnGererEmprunt);
        panel.add(btnGererUtilisateur);
        panel.add(btnAfficherStatistiques);  // Ajouter le bouton de statistiques au panel

        add(panel, BorderLayout.CENTER);
        setVisible(true);
    }
}
