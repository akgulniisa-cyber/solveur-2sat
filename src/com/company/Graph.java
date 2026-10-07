package com.company;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Graphe oriente represente par listes d'incidence.
 * Les sommets sont numerotes de {@code 0} a {@code order() - 1} et chaque arc porte une etiquette.
 *
 * @param <Label> type des etiquettes portees par les arcs
 */
public class Graph<Label> {

    private record Edge<Label>(int source, int destination, Label label) {
    }

    private final int cardinal;
    private final List<LinkedList<Edge<Label>>> incidency;

    /**
     * Cree un graphe sans arc.
     *
     * @param size nombre de sommets
     */
    public Graph(int size) {
        if (size < 0) {
            throw new IllegalArgumentException("Le nombre de sommets doit etre positif : " + size);
        }
        cardinal = size;
        incidency = new ArrayList<>(size);
        for (int i = 0; i < cardinal; i++) {
            incidency.add(new LinkedList<>());
        }
    }

    /** @return le nombre de sommets */
    public int order() {
        return cardinal;
    }

    /** @return le nombre d'arcs */
    public int size() {
        int total = 0;
        for (LinkedList<Edge<Label>> arcs : incidency) {
            total += arcs.size();
        }
        return total;
    }

    /** Ajoute l'arc {@code source -> dest} portant l'etiquette {@code label}. */
    public void addArc(int source, int dest, Label label) {
        verifierSommet(source);
        verifierSommet(dest);
        incidency.get(source).addLast(new Edge<>(source, dest, label));
    }

    /** @return les successeurs de {@code sommet}, dans l'ordre d'insertion des arcs */
    public List<Integer> voisins(int sommet) {
        verifierSommet(sommet);
        List<Integer> resultat = new ArrayList<>(incidency.get(sommet).size());
        for (Edge<Label> e : incidency.get(sommet)) {
            resultat.add(e.destination());
        }
        return resultat;
    }

    /** @return un nouveau graphe ou chaque arc est inverse */
    public Graph<Label> transpose() {
        Graph<Label> resultat = new Graph<>(cardinal);
        for (LinkedList<Edge<Label>> arcs : incidency) {
            for (Edge<Label> e : arcs) {
                resultat.addArc(e.destination(), e.source(), e.label());
            }
        }
        return resultat;
    }

    private void verifierSommet(int sommet) {
        if (sommet < 0 || sommet >= cardinal) {
            throw new IndexOutOfBoundsException(
                    "Sommet " + sommet + " hors de [0, " + cardinal + "[");
        }
    }

    @Override
    public String toString() {
        StringBuilder resultat = new StringBuilder();
        resultat.append(cardinal).append('\n');
        for (LinkedList<Edge<Label>> arcs : incidency) {
            for (Edge<Label> e : arcs) {
                resultat.append(e.source()).append(' ')
                        .append(e.destination()).append(' ')
                        .append(e.label()).append('\n');
            }
        }
        return resultat.toString();
    }
}
