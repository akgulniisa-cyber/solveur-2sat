package com.company;

import java.util.Optional;

/**
 * Resolution d'une formule 2-SAT en temps lineaire (Aspvall, Plass &amp; Tarjan, 1979).
 *
 * <p>La formule est insatisfiable si et seulement si une variable {@code x} et sa negation
 * {@code ¬x} appartiennent a la meme composante fortement connexe du graphe des implications.
 *
 * <p>Sinon, une affectation satisfaisante s'obtient en donnant a {@code x} la valeur {@code vrai}
 * lorsque la composante de {@code x} vient apres celle de {@code ¬x} dans l'ordre topologique.
 */
public class DeuxSat {

    private final LectureFormule formule;
    private final Graph<Integer> graphe;
    private final Kosaraju composantes;
    private final boolean[] affectation;

    public DeuxSat(LectureFormule formule) {
        this.formule = formule;
        this.graphe = GrapheImplications.construire(formule);
        this.composantes = new Kosaraju(graphe);
        this.affectation = resoudre();
    }

    /** @return une affectation satisfaisante (indice {@code k} pour {@code x_k}), ou {@code null} si aucune n'existe */
    private boolean[] resoudre() {
        boolean[] valeurs = new boolean[formule.nbVariables() + 1];
        for (int variable = 1; variable <= formule.nbVariables(); variable++) {
            int positif = composantes.composante(GrapheImplications.sommet(variable));
            int negatif = composantes.composante(GrapheImplications.sommet(-variable));
            if (positif == negatif) {
                return null;
            }
            valeurs[variable] = positif > negatif;
        }
        return valeurs;
    }

    public boolean estSatisfiable() {
        return affectation != null;
    }

    /**
     * @return une affectation satisfaisante si la formule en admet une : la case {@code k}
     *         contient la valeur de {@code x_k} (la case {@code 0} est inutilisee)
     */
    public Optional<boolean[]> affectation() {
        return Optional.ofNullable(affectation).map(boolean[]::clone);
    }

    /** @return {@code true} si {@code valeurs} satisfait toutes les clauses de la formule */
    public boolean verifie(boolean[] valeurs) {
        for (int[] clause : formule.clauses()) {
            if (!valeur(clause[0], valeurs) && !valeur(clause[1], valeurs)) {
                return false;
            }
        }
        return true;
    }

    private static boolean valeur(int litteral, boolean[] valeurs) {
        boolean variable = valeurs[Math.abs(litteral)];
        return (litteral > 0) == variable;
    }

    public Graph<Integer> graphe() {
        return graphe;
    }

    public Kosaraju composantes() {
        return composantes;
    }
}
