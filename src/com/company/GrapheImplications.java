package com.company;

/**
 * Construction du graphe des implications d'une formule 2-SAT.
 *
 * <p>Pour {@code n} variables, le graphe a {@code 2n} sommets : le litteral {@code x_k}
 * correspond au sommet {@code 2(k-1)} et {@code ¬x_k} au sommet {@code 2(k-1)+1}.
 * Une clause {@code (a ∨ b)} est equivalente a {@code (¬a ⇒ b) ∧ (¬b ⇒ a)} et produit
 * donc les deux arcs {@code ¬a -> b} et {@code ¬b -> a}.
 */
public final class GrapheImplications {

    private GrapheImplications() {
    }

    /** @return le sommet associe au litteral (non nul) {@code litteral} */
    public static int sommet(int litteral) {
        if (litteral == 0) {
            throw new IllegalArgumentException("0 n'est pas un litteral.");
        }
        int variable = Math.abs(litteral) - 1;
        return (litteral > 0) ? 2 * variable : 2 * variable + 1;
    }

    /** @return le sommet du litteral oppose ({@code x <-> ¬x}) */
    public static int oppose(int sommet) {
        return sommet ^ 1;
    }

    /** @return le graphe des implications de {@code formule}, chaque arc etant etiquete par le numero de sa clause */
    public static Graph<Integer> construire(LectureFormule formule) {
        Graph<Integer> graphe = new Graph<>(2 * formule.nbVariables());
        int numeroClause = 1;
        for (int[] clause : formule.clauses()) {
            int premier = sommet(clause[0]);
            int second = sommet(clause[1]);
            graphe.addArc(oppose(premier), second, numeroClause);
            graphe.addArc(oppose(second), premier, numeroClause);
            numeroClause++;
        }
        return graphe;
    }
}
