package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Lecture d'une formule 2-SAT au format DIMACS CNF.
 *
 * <pre>
 * c commentaire
 * p cnf &lt;nbVariables&gt; &lt;nbClauses&gt;
 * 1 -2 0
 * -1 3 0
 * </pre>
 *
 * Chaque clause contient exactement deux litteraux et se termine par {@code 0}.
 * Le litteral {@code k} designe la variable {@code x_k} et {@code -k} sa negation.
 */
public class LectureFormule {

    private int nbVariables = -1;
    private final List<int[]> clauses = new ArrayList<>();

    public LectureFormule(String cheminFichier) throws IOException {
        List<Integer> litteraux = new ArrayList<>();

        try (BufferedReader lecteur = Files.newBufferedReader(Path.of(cheminFichier))) {
            String ligne;
            int numeroLigne = 0;
            while ((ligne = lecteur.readLine()) != null) {
                numeroLigne++;
                ligne = ligne.trim();

                if (ligne.isEmpty() || ligne.startsWith("c")) {
                    continue;
                }

                if (ligne.startsWith("p")) {
                    lireEntete(ligne);
                    continue;
                }

                if (nbVariables < 0) {
                    throw new IllegalArgumentException(
                            "Ligne " + numeroLigne + " : clause trouvee avant la ligne d'entete 'p cnf'.");
                }

                for (String partie : ligne.split("\\s+")) {
                    int valeur = lireLitteral(partie, numeroLigne);
                    if (valeur == 0) {
                        ajouterClause(litteraux);
                        litteraux.clear();
                    } else {
                        litteraux.add(valeur);
                    }
                }
            }
        }

        if (nbVariables < 0) {
            throw new IllegalArgumentException("Ligne d'entete 'p cnf' absente.");
        }
        if (!litteraux.isEmpty()) {
            throw new IllegalArgumentException("La derniere clause ne se termine pas par 0.");
        }
    }

    private void lireEntete(String ligne) {
        String[] parties = ligne.split("\\s+");
        if (parties.length < 4 || !parties[1].equals("cnf")) {
            throw new IllegalArgumentException("Ligne d'entete incorrecte : " + ligne);
        }
        nbVariables = Integer.parseInt(parties[2]);
    }

    private int lireLitteral(String texte, int numeroLigne) {
        int valeur;
        try {
            valeur = Integer.parseInt(texte);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(
                    "Ligne " + numeroLigne + " : litteral invalide '" + texte + "'.");
        }
        if (Math.abs(valeur) > nbVariables) {
            throw new IllegalArgumentException("Ligne " + numeroLigne + " : la variable "
                    + Math.abs(valeur) + " depasse le nombre de variables (" + nbVariables + ").");
        }
        return valeur;
    }

    private void ajouterClause(List<Integer> litteraux) {
        if (litteraux.size() != 2) {
            throw new IllegalArgumentException("Une clause 2-SAT doit avoir exactement "
                    + "2 litteraux, clause numero " + (clauses.size() + 1)
                    + " en contient " + litteraux.size() + ".");
        }
        clauses.add(new int[]{litteraux.get(0), litteraux.get(1)});
    }

    /** @return le nombre de variables declare dans l'entete */
    public int nbVariables() {
        return nbVariables;
    }

    /** @return les clauses lues, chacune sous la forme {@code {l1, l2}} */
    public List<int[]> clauses() {
        return Collections.unmodifiableList(clauses);
    }
}
