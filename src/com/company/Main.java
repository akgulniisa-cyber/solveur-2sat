package com.company;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.util.List;
import java.util.StringJoiner;

/**
 * Point d'entree : resout chaque formule passee en argument
 * (ou les formules d'exemple si aucun argument n'est fourni).
 */
public class Main {

    private static final List<String> FICHIERS_PAR_DEFAUT = List.of(
            "formule-2-sat.txt",
            "formule-3-6.txt",
            "formule-40-80.txt"
    );

    private static final String SEPARATEUR = "\n========================================\n";

    public static void main(String[] args) {
        List<String> fichiers = (args.length > 0) ? List.of(args) : FICHIERS_PAR_DEFAUT;

        for (int numero = 0; numero < fichiers.size(); numero++) {
            if (numero > 0) {
                System.out.println(SEPARATEUR);
            }
            traiter(fichiers.get(numero));
        }
    }

    private static void traiter(String fichier) {
        System.out.println("Fichier             : " + fichier);
        try {
            executer(fichier);
        } catch (NoSuchFileException e) {
            System.out.println("Lecture impossible  : fichier introuvable.");
        } catch (IOException e) {
            System.out.println("Lecture impossible  : " + e.getMessage());
        } catch (RuntimeException e) {
            System.out.println("Traitement interrompu : " + e.getMessage());
        }
    }

    private static void executer(String fichier) throws IOException {
        LectureFormule formule = new LectureFormule(fichier);
        System.out.println("Nombre de variables : " + formule.nbVariables());
        System.out.println("Nombre de clauses   : " + formule.clauses().size());

        DeuxSat probleme = new DeuxSat(formule);
        Graph<Integer> graphe = probleme.graphe();

        System.out.println();
        System.out.println("Graphe des implications : "
                + graphe.order() + " sommets, " + graphe.size() + " arcs");
        System.out.println("Composantes fortement connexes : " + probleme.composantes().nbComposantes());

        System.out.println();
        probleme.affectation().ifPresentOrElse(
                valeurs -> {
                    System.out.println("Resultat : la formule est SATISFIABLE.");
                    System.out.println("Affectation : " + formater(valeurs));
                    System.out.println("Verification : " + (probleme.verifie(valeurs) ? "OK" : "ECHEC"));
                },
                () -> System.out.println("Resultat : la formule est INSATISFIABLE."));
    }

    private static String formater(boolean[] valeurs) {
        StringJoiner texte = new StringJoiner(", ");
        for (int variable = 1; variable < valeurs.length; variable++) {
            texte.add("x" + variable + "=" + (valeurs[variable] ? "V" : "F"));
        }
        return texte.toString();
    }
}
