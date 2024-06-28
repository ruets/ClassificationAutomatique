import java.io.File;
import java.io.FileInputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

public class Classification {

    public static void main(String[] args) {
        // Début du calcul du temps d'exécution
        long startTime = System.currentTimeMillis();
        
        // Chargement des dépêches en mémoire
        ArrayList<Depeche> depeches = lectureDepeches("./depeches.txt");
        ArrayList<Depeche> test = lectureDepeches("./test.txt");

        /*
        for (int i = 0; i < depeches.size(); i++) {
            depeches.get(i).afficher();
        }
        */

        // Génération auto des lexiques
            generationLexique(depeches, "ENVIRONNEMENT-SCIENCES", "./motsauto/SCIENCES.txt");
            generationLexique(depeches, "CULTURE", "./motsauto/CULTURE.txt");
            generationLexique(depeches, "ECONOMIE", "./motsauto/ECONOMIE.txt");
            generationLexique(depeches, "POLITIQUE", "./motsauto/POLITIQUE.txt");
            generationLexique(depeches, "SPORTS", "./motsauto/SPORTS.txt");
        


        // Création des catégories contenant les lexiques
        ArrayList<Categorie> categories = new ArrayList<Categorie>();

        Categorie SCIENCES = new Categorie("ENVIRONNEMENT-SCIENCES");
        SCIENCES.initLexique("./motsauto/SCIENCES.txt");
        categories.add(SCIENCES);

        Categorie CULTURE = new Categorie("CULTURE");
        CULTURE.initLexique("./motsauto/CULTURE.txt");
        categories.add(CULTURE);

        Categorie ECONOMIE = new Categorie("ECONOMIE");
        ECONOMIE.initLexique("./motsauto/ECONOMIE.txt");
        categories.add(ECONOMIE);

        Categorie POLITIQUE = new Categorie("POLITIQUE");
        POLITIQUE.initLexique("./motsauto/POLITIQUE.txt");
        categories.add(POLITIQUE);

        Categorie SPORTS = new Categorie("SPORTS");
        SPORTS.initLexique("./motsauto/SPORTS.txt");
        categories.add(SPORTS);


        // Lancement du tri
        classementDepeches(test, categories, "./output.txt");

        // Fin du calcul du temps d'exécution
        long endTime = System.currentTimeMillis();
        System.out.println("Le programme s'est exécuté en " + (endTime-startTime) + " ms");
    }

    private static ArrayList<Depeche> lectureDepeches(String nomFichier) {
        //creation d'un tableau de dépêches
        ArrayList<Depeche> depeches = new ArrayList<>();
        try {
            // lecture du fichier d'entrée
            FileInputStream file = new FileInputStream(nomFichier);
            Scanner scanner = new Scanner(file);

            while (scanner.hasNextLine()) {
                String ligne = scanner.nextLine();
                String id = ligne.substring(3);
                ligne = scanner.nextLine();
                String date = ligne.substring(3);
                ligne = scanner.nextLine();
                String categorie = ligne.substring(3);
                ligne = scanner.nextLine();
                String lignes = ligne.substring(3);
                while (scanner.hasNextLine() && !ligne.equals("")) {
                    ligne = scanner.nextLine();
                    if (!ligne.equals("")) {
                        lignes = lignes + '\n' + ligne;
                    }
                }
                Depeche uneDepeche = new Depeche(id, date, categorie, lignes);
                depeches.add(uneDepeche);
            }
            scanner.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return depeches;
    }

    
    public static void classementDepeches(ArrayList<Depeche> depeches, ArrayList<Categorie> categories, String nomFichier) {
        try {
            // Déclaration des variables
            ArrayList<PaireChaineEntier> classement = new ArrayList<PaireChaineEntier>();
            ArrayList<PaireChaineEntier> pourcentages = new ArrayList<PaireChaineEntier>();
            FileWriter output = new FileWriter(nomFichier);

            // Remplissage de la liste pourcentages
            pourcentages.add(new PaireChaineEntier("ENVIRONNEMENT-SCIENCES", 0));
            pourcentages.add(new PaireChaineEntier("CULTURE", 0));
            pourcentages.add(new PaireChaineEntier("ECONOMIE", 0));
            pourcentages.add(new PaireChaineEntier("POLITIQUE", 0));
            pourcentages.add(new PaireChaineEntier("SPORTS", 0));



            for (int i = 0; i < depeches.size(); i++) {
                for (int j = 0; j < categories.size(); j++) {
                    // Ajout des scores pour chaque catégorie dans le classement
                    classement.add(new PaireChaineEntier(categories.get(j).getNom(), categories.get(j).score(depeches.get(i))));
                }

                // Ecriture de la catégorie ayant le plus grand score dans le fichier output.txt
                output.write(depeches.get(i).getId() + ":" + UtilitairePaireChaineEntier.chaineMax(classement)+ "\n");

                if (UtilitairePaireChaineEntier.chaineMax(classement).equals(depeches.get(i).getCategorie())) {
                    // Incrémentation du pourcentage de la catégorie de la dépêche si celle trouvée correspond à cette dernière
                    pourcentages.get(UtilitairePaireChaineEntier.indicePourChaine(pourcentages, depeches.get(i).getCategorie())).setEntier(pourcentages.get(UtilitairePaireChaineEntier.indicePourChaine(pourcentages, depeches.get(i).getCategorie())).getEntier()+1);
                }

                // On vide classement afin de pouvoir la réutiliser pour la dépêche suivante
                classement.clear();
            }

            output.write("\n");

            // Calcul et ajout de la moyenne des pourcentages dans le fichier d'output
            int moyenne = 0;

            for (int i = 0; i < pourcentages.size(); i++) {
                output.write(pourcentages.get(i).getChaine() + " : " + pourcentages.get(i).getEntier() + "%\n");
                moyenne += pourcentages.get(i).getEntier();
            }

            output.write("MOYENNE : " + moyenne/5 + "%");
            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static ArrayList<PaireChaineEntier> initDico(ArrayList<Depeche> depeches, String categorie) {
        ArrayList<PaireChaineEntier> resultat = new ArrayList<>();
        
        for (int i = 0; i < depeches.size(); i++) {
            for (int j = 0; j < depeches.get(i).getMots().size(); j++) {
                if (depeches.get(i).getCategorie().equalsIgnoreCase(categorie) && !existeDans(resultat, depeches.get(i).getMots().get(j))) {
                    // Ajout des mots au dictionnaire, si ils n'y figurent pas déjà et si la catégorie de leur dépêche correspond à celle concernée par le dictionnaire
                    resultat.add(new PaireChaineEntier(depeches.get(i).getMots().get(j), 0));
                }
            }
        }

        // Retour du résultat
        return resultat;
    }

    public static boolean existeDans(ArrayList<PaireChaineEntier> liste, String chaine) {
        /*
        Méthode utilitaire pour initDico permettant de vérifier l'existence d'une chaîne dans une liste
        */

        for (int i = 0; i < liste.size(); i++) {
            if (chaine.equals(liste.get(i).getChaine())) {
                // Retour du résultat si la chaône existe dans la liste
                return true;
            }
        }

        // Retour du résultat
        return false;
    }

    public static void calculScores(ArrayList<Depeche> depeches, String categorie, ArrayList<PaireChaineEntier> dictionnaire) {
        for (int i = 0; i < depeches.size(); i++) {
            for (int j = 0; j < depeches.get(i).getMots().size(); j++) {
                if (UtilitairePaireChaineEntier.indicePourChaine(dictionnaire, depeches.get(i).getMots().get(j)) >= 0) {
                    if (depeches.get(i).getCategorie().equalsIgnoreCase(categorie)) {
                        // Si le mot est présent dans une dépêche de la même catégorie, on augmente le score du mot concerné de 1
                        dictionnaire.get(UtilitairePaireChaineEntier.indicePourChaine(dictionnaire, depeches.get(i).getMots().get(j))).setEntier(dictionnaire.get(UtilitairePaireChaineEntier.indicePourChaine(dictionnaire, depeches.get(i).getMots().get(j))).getEntier()+1);
                    } else {
                        // Si le mot est présent dans une dépêche d'une autre catégorie, on diminue le score du mot concerné de 1
                        dictionnaire.get(UtilitairePaireChaineEntier.indicePourChaine(dictionnaire, depeches.get(i).getMots().get(j))).setEntier(dictionnaire.get(UtilitairePaireChaineEntier.indicePourChaine(dictionnaire, depeches.get(i).getMots().get(j))).getEntier()-1);
                    }
                }
            }
        }
    }

    public static int poidsPourScore(int score) {
        if (score >= 5 && score <= 20) {
            // Le poids d'un mot dont le score est compris entre 5 et 20 sera de 3
            return 3;
        } else if (score >= 3 && score <= 4) {
            // Le poids d'un mot dont le score est compris entre 3 et 4 sera de 2
            return 2;
        } else if (score >= 1 && score <= 2) {
            // Le poids d'un mot dont le score est compris entre 1 et 2 sera de 1
            return 1;
        } else {
            // Le poids d'un mot dont le score est supérieur à 20 sera de 0
            return 0;
        }
    }

    public static void generationLexique(ArrayList<Depeche> depeches, String categorie, String nomFichier) {
        // Déclaration des variables
        ArrayList<PaireChaineEntier> dictionnaire = new ArrayList<PaireChaineEntier>();

        // Initialisation du dictionnaire et màj des scores
        dictionnaire = initDico(depeches, categorie);
        calculScores(depeches, categorie, dictionnaire);

        try {
            // Déclaration du chemin du fichier de sortie
            FileWriter output = new FileWriter(nomFichier);

            for (int i = 0; i < dictionnaire.size(); i++) {
                if (poidsPourScore(dictionnaire.get(i).getEntier()) > 0) {
                    // Si le mot a un poids non nul, il sera ajouté au fichier de sortie et sera utilisé pour le lexique. Sinon on le passe
                    output.write(dictionnaire.get(i).getChaine() + ":" + poidsPourScore(dictionnaire.get(i).getEntier()) + "\n");
                }
            }

            output.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}