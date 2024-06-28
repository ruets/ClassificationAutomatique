public class PaireChaineEntier {
    // Attributs
    private String chaine;
    private int entier;

    // Constructeur
    public PaireChaineEntier(String chaine, int entier) {
        this.chaine = chaine;
        this.entier = entier;
    }

    // Getters
    public String getChaine() {
        return this.chaine;
    }

    public int getEntier() {
        return this.entier;
    }

    public void setChaine(String chaine) {
        this.chaine = chaine;
    }
    
    public void setEntier(int entier) {
        this.entier = entier;
    }

    // Méthode toString() pour les tests
    @Override
    public String toString() {
        return chaine + ":" + entier;
    }
}
