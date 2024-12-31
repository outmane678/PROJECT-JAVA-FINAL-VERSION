package test;

import controller.StatistiqueController;
import view.StatistiqueView;

public class TestStatistique {
    public static void main(String[] args) {
        // Créer une instance de StatistiqueController
        StatistiqueController controller = new StatistiqueController();

        // Rendre la vue visible en utilisant getView()
        StatistiqueView view = controller.getView(); // Accéder à la vue du contrôleur via getView()
        view.setVisible(true);
    }
}
