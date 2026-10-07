package com.company;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.List;

/**
 * Calcul des composantes fortement connexes par l'algorithme de Kosaraju, en {@code O(V + E)}.
 *
 * <ol>
 *   <li>Un premier parcours en profondeur empile les sommets par date de fin croissante.</li>
 *   <li>Un second parcours, sur le graphe transpose et dans l'ordre de depilement,
 *       marque chaque composante.</li>
 * </ol>
 *
 * Les composantes sont numerotees dans un ordre topologique du graphe quotient :
 * s'il existe un chemin de la composante {@code i} vers la composante {@code j != i}, alors {@code i < j}.
 */
public class Kosaraju {

    private static final int NON_MARQUE = -1;

    private final int[] composante;
    private final int nbComposantes;
    private final Deque<Integer> pileFin;

    public Kosaraju(Graph<?> graphe) {
        int ordre = graphe.order();
        composante = new int[ordre];
        Arrays.fill(composante, NON_MARQUE);
        pileFin = new ArrayDeque<>(ordre);

        premierParcours(listesAdjacence(graphe));
        nbComposantes = secondParcours(listesAdjacence(graphe.transpose()));
    }

    private static List<List<Integer>> listesAdjacence(Graph<?> graphe) {
        List<List<Integer>> adjacence = new ArrayList<>(graphe.order());
        for (int sommet = 0; sommet < graphe.order(); sommet++) {
            adjacence.add(graphe.voisins(sommet));
        }
        return adjacence;
    }

    private void premierParcours(List<List<Integer>> adjacence) {
        boolean[] visite = new boolean[adjacence.size()];
        for (int sommet = 0; sommet < adjacence.size(); sommet++) {
            if (!visite[sommet]) {
                explorer(adjacence, sommet, visite);
            }
        }
    }

    private void explorer(List<List<Integer>> adjacence, int sommet, boolean[] visite) {
        visite[sommet] = true;
        for (int voisin : adjacence.get(sommet)) {
            if (!visite[voisin]) {
                explorer(adjacence, voisin, visite);
            }
        }
        pileFin.push(sommet);
    }

    private int secondParcours(List<List<Integer>> adjacenceTransposee) {
        int numero = 0;
        while (!pileFin.isEmpty()) {
            int depart = pileFin.pop();
            if (composante[depart] == NON_MARQUE) {
                marquer(adjacenceTransposee, depart, numero);
                numero++;
            }
        }
        return numero;
    }

    private void marquer(List<List<Integer>> adjacenceTransposee, int sommet, int numero) {
        composante[sommet] = numero;
        for (int voisin : adjacenceTransposee.get(sommet)) {
            if (composante[voisin] == NON_MARQUE) {
                marquer(adjacenceTransposee, voisin, numero);
            }
        }
    }

    /** @return le numero de la composante fortement connexe contenant {@code sommet} */
    public int composante(int sommet) {
        return composante[sommet];
    }

    /** @return le nombre de composantes fortement connexes */
    public int nbComposantes() {
        return nbComposantes;
    }
}
