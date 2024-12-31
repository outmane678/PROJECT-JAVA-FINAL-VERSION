package model;

import exceptions.EmpruntExisteException;
import exceptions.EmpruntNonTrouveException;
import java.io.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class EmpruntModel implements EmpruntModelInterface {
    private List<Emprunt> emprunts;
    private String filePath;
    private List<Utilisateur> utilisateurs;
    private List<Livre> livres;

    // Constructeur de la classe EmpruntModel
    public EmpruntModel(String filePath, List<Utilisateur> utilisateurs, List<Livre> livres) {
        this.filePath = filePath;
        this.utilisateurs = utilisateurs;
        this.livres = livres;
        this.emprunts = new ArrayList<>();
        chargerEmprunts();  // Charger les emprunts depuis le fichier au démarrage
    }

    // Ajouter un emprunt
    @Override
    public void ajouterEmprunt(Emprunt emprunt) throws EmpruntExisteException {
        // Vérification si l'ID de l'emprunt existe déjà
        boolean empruntExiste = emprunts.stream()
                                        .anyMatch(e -> e.getId() == emprunt.getId());

        if (empruntExiste) {
            throw new EmpruntExisteException("Un emprunt avec cet ID existe déjà. Veuillez utiliser un autre ID.");
        } else {
            // Ajouter l'emprunt uniquement si l'ID est unique
            emprunts.add(emprunt);
            System.out.println("Emprunt ajouté : " + emprunt);
            sauvegarderEmprunts();  // Sauvegarder après ajout
        }
    }

    // Afficher les emprunts
    @Override
    public void afficherEmprunts() {
        if (emprunts.isEmpty()) {
            System.out.println("Aucun emprunt.");
        } else {
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("dd MMMM yyyy");
            for (Emprunt emprunt : emprunts) {
                System.out.println("Emprunt ID: " + emprunt.getId() +
                                   ", Livre ID: " + emprunt.getLivre().getId() +
                                   ", Utilisateur ID: " + emprunt.getUtilisateur().getId() +
                                   ", Date Emprunt: " + emprunt.getDateEmprunt().format(dateFormat) +
                                   ", Date Retour: " + emprunt.getDateRetourPrevue().format(dateFormat));
            }
        }
    }

    // Modifier un emprunt (par exemple, modification de la date de retour)
    @Override
    public void modifierEmprunt(int id, LocalDate nouvelleDateRetourPrevue) throws EmpruntNonTrouveException {
        for (Emprunt emprunt : emprunts) {
            if (emprunt.getId() == id) {
                emprunt.setDateRetourPrevue(nouvelleDateRetourPrevue); // Modification de la date de retour Prevue
                System.out.println("Emprunt modifié : " + emprunt);
                sauvegarderEmprunts(); // Sauvegarder après modification
                return;
            }
        }
        throw new EmpruntNonTrouveException("Emprunt avec ID " + id + " non trouvé.");
    }

    public void sauvegarderEmprunts() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(filePath))) {
            writer.write("id_Emprunt,id_livre,id_utilisateur,date_emprunt,date_retour_prevue,date_retour_effective,penalite");
            writer.newLine();
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            for (Emprunt emprunt : emprunts) {
                writer.write(emprunt.getId() + "," +
                             emprunt.getLivre().getId() + "," +
                             emprunt.getUtilisateur().getId() + "," +
                             emprunt.getDateEmprunt().format(dateFormat) + "," +
                             emprunt.getDateRetourPrevue().format(dateFormat) + "," +
                             (emprunt.getDateRetourEffective() != null ? emprunt.getDateRetourEffective().format(dateFormat) : "") + "," +
                             emprunt.getPenalite());
                writer.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erreur lors de la sauvegarde des emprunts : " + e.getMessage());
        }
    }
    

    // Charger les emprunts depuis un fichier CSV
    public void chargerEmprunts() {
    File file = new File(filePath);
    if (file.exists()) {
        try (BufferedReader reader = new BufferedReader(new FileReader(filePath))) {
            String line;
            DateTimeFormatter dateFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");

            // Lire et ignorer la première ligne (en-têtes)
            reader.readLine();

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(",");
                if (parts.length == 7) { // Maintenant il y a 7 colonnes
                    try {
                        int id = Integer.parseInt(parts[0]);
                        int idLivre = Integer.parseInt(parts[1]);
                        int idUtilisateur = Integer.parseInt(parts[2]);
                        LocalDate dateEmprunt = LocalDate.parse(parts[3], dateFormat);
                        LocalDate dateRetourPrevue = LocalDate.parse(parts[4], dateFormat);
                        LocalDate dateRetourEffective = parts[5].isEmpty() ? null : LocalDate.parse(parts[5], dateFormat); // Vérification de la date de retour effective
                        double penalite = Double.parseDouble(parts[6]); // La pénalité

                        Livre livreEmprunt = findLivreById(idLivre);
                        Utilisateur utilisateurEmprunt = findUtilisateurById(idUtilisateur);

                        if (livreEmprunt != null && utilisateurEmprunt != null) {
                            emprunts.add(new Emprunt(id, livreEmprunt, utilisateurEmprunt, dateEmprunt, dateRetourPrevue, dateRetourEffective, penalite));
                            System.out.println("Emprunt chargé: " + id);
                        } else {
                            System.out.println("Erreur : Livre ou Utilisateur introuvable pour l'ID " + id);
                        }
                    } catch (NumberFormatException | DateTimeParseException e) {
                        System.out.println("Erreur de format dans la ligne : " + line);
                    }
                } else {
                    System.out.println("Ligne mal formée dans le fichier CSV : " + line);
                }
            }
        } catch (IOException e) {
            System.out.println("Erreur lors du chargement des emprunts : " + e.getMessage());
        }
    }
}

    // Recherche d'un emprunt par son ID
    public Emprunt findEmpruntById(int id) throws EmpruntNonTrouveException {
        return emprunts.stream()
                       .filter(e -> e.getId() == id)
                       .findFirst()
                       .orElseThrow(() -> new EmpruntNonTrouveException("Emprunt non trouvé avec ID : " + id));
    }

    // Recherche d'un livre par son ID
    private Livre findLivreById(int id) {
        return livres.stream()
                     .filter(livre -> livre.getId() == id)
                     .findFirst()
                     .orElse(null);
    }

    // Recherche d'un utilisateur par son ID
    private Utilisateur findUtilisateurById(int id) {
        return utilisateurs.stream()
                           .filter(utilisateur -> utilisateur.getId() == id)
                           .findFirst()
                           .orElse(null);
    }

    // Méthode pour rechercher les emprunts d'un utilisateur par son ID
    public List<Emprunt> getEmpruntsParUtilisateur(Integer utilisateurId) {
        List<Emprunt> empruntsUtilisateur = new ArrayList<>();

        // Si l'ID de l'utilisateur est 0 ou -1, retourner tous les emprunts
        if (utilisateurId==null || utilisateurId <= 0) {
            return new ArrayList<>(emprunts);  // Retourner tous les emprunts
        }

        // Sinon, filtrer les emprunts par utilisateur
        for (Emprunt emprunt : emprunts) {
            if (emprunt.getUtilisateur().getId() == utilisateurId) {
                empruntsUtilisateur.add(emprunt);
            }
        }

        return empruntsUtilisateur;
    }

    // enregistrer le retour d'un livre
public void enregistrerRetour(int idEmprunt, LocalDate dateRetourEffective) {
    for (Emprunt emprunt : emprunts) {
        if (emprunt.getId() == idEmprunt) {
            emprunt.setDateRetourEffective(dateRetourEffective);
            double penalite = emprunt.calculerPenalite(); // calculer la pénalité
            emprunt.setPenalite(penalite);  // mettre à jour la pénalité
            System.out.println("Retour enregistré. Pénalité : " + penalite);
            sauvegarderEmprunts();  // Sauvegarder après enregistrement du retour
            return;
        }
    }
    System.out.println("Emprunt non trouvé.");
}
    @Override
    public List<Emprunt> getEmprunts() {
        return emprunts;
    }
    // Method to add penalty for a specific loan
public void ajouterPenalite(int empruntId, double penalite) throws EmpruntNonTrouveException {
    Emprunt emprunt = findEmpruntById(empruntId);
    if (emprunt != null) {
        emprunt.setPenalite(penalite); // Set the penalty for the loan
        sauvegarderEmprunts(); // Save after updating the penalty
        System.out.println("Pénalité ajoutée pour l'emprunt ID: " + empruntId);
    }
}
// Calculer la pénalité pour un emprunt spécifique
public double calculerPenalite(int id) {
    for (Emprunt emprunt : emprunts) {
        if (emprunt.getId() == id) {
            return emprunt.calculerPenalite(); // Appel de la méthode calculerPenalite() sur l'objet Emprunt
        }
    }
    return 0; // Si emprunt non trouvé
}


  // Méthode pour rechercher des emprunts
public List<Emprunt> rechercherEmprunts(String query) {
    if (query == null || query.trim().isEmpty()) {
        return emprunts; // Retourner tous les emprunts si la requête est vide
    }

    // Filtrer la liste des emprunts en fonction de plusieurs critères
    return emprunts.stream()
            .filter(emprunt -> 
                String.valueOf(emprunt.getId()).contains(query) ||  // Recherche par ID de l'emprunt
                String.valueOf(emprunt.getLivre().getId()).contains(query) || // Recherche par ID du livre
                String.valueOf(emprunt.getUtilisateur().getId()).contains(query) || // Recherche par ID de l'utilisateur
                emprunt.getDateEmprunt().toString().contains(query) ||  // Recherche par date d'emprunt
                emprunt.getDateRetourPrevue().toString().contains(query) || // Recherche par date de retour prévue
                (emprunt.getDateRetourEffective() != null && emprunt.getDateRetourEffective().toString().contains(query)) || // Recherche par date de retour effective
                ( String.valueOf(emprunt.getPenalite()).contains(query)) // Recherche par pénalité
            )
            .collect(Collectors.toList());
}





}
