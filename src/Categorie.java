import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Categorie {

    private String nom; // le nom de la catégorie p.ex : sport, politique,...
    private ArrayList<PaireChaineEntier> lexique = new ArrayList<PaireChaineEntier>(); //le lexique de la catégorie

    // constructeur
    public Categorie(String nom) {
        this.nom = nom;
    }


    public String getNom() {
        return nom;
    }


    public  ArrayList<PaireChaineEntier> getLexique() {
        return lexique;
    }


    // initialisation du lexique de la catégorie à partir du contenu d'un fichier texte
    public void initLexique(String nomFichier) {
        try{
            // lecture du fichier d'entrée
            FileInputStream input = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(input).useDelimiter(":");

            // Ajout du lexique dans l'arraylist
            while (scanner.hasNextLine()) {
                String mot = scanner.next();
                int poids = Integer.parseInt(scanner.nextLine().substring(1));

                this.lexique.add(new PaireChaineEntier(mot, poids));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //calcul du score d'une dépêche pour la catégorie
    public int score(Depeche d) {
        // Déclaration des variables
        int score = 0;

        for (int i = 0; i < d.getMots().size(); i++) {
            score += UtilitairePaireChaineEntier.entierPourChaine(this.lexique, d.getMots().get(i));
        }

        // Retour du résultat
        return score;
    }


}
