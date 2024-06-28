import java.sql.ResultSetMetaData;
import java.util.ArrayList;

public class UtilitairePaireChaineEntier {


    public static int indicePourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        for (int i = 0; i < listePaires.size(); i++) {
            if (listePaires.get(i).getChaine().equals(chaine)) {
                // Retour de l'indice de la chaîne si elle est trouvée dans la liste
                return i;
            }
        }

        // Retour de -1 si la chaîne n'est pas trouvée
        return -1;
        }

    public static int entierPourChaine(ArrayList<PaireChaineEntier> listePaires, String chaine) {
        for (int i = 0; i < listePaires.size(); i++) {
            if (chaine.equalsIgnoreCase(listePaires.get(i).getChaine())) {
                // Retour de l'entier associé à la chaîne si elle est trouvée dans la liste
                return i;   
            }
        }
        // Retour de 0 si la chaîne n'est pas trouvée
        return 0;
    }

    public static String chaineMax(ArrayList<PaireChaineEntier> listePaires) {
        // Déclaration des variables
        int result = 0;

        for (int i = 0; i < listePaires.size(); i++) {
            if (listePaires.get(i).getEntier() > listePaires.get(result).getEntier()) {
                // Si l'entier associé à la chaine est plus grand que celui associé à la chaîne d'indice result, on remplace result par la valeur de i
                result = i;
            }
        }

        // Retour du résultat
        return listePaires.get(result).getChaine();
    }


    public static float moyenne(ArrayList<PaireChaineEntier> listePaires) {
        float somme = 0;
        
        for (int i = 0; i < listePaires.size(); i++) {
            // On ajoute tous les entiers contenus dans les paires de la liste
            somme += listePaires.get(i).getEntier();
        }

        // Retour de la somme divisée par le nombre de paires dans la liste
        return somme / listePaires.size();
    }

}
