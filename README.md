# Solveur 2-SAT en temps linéaire

Implémentation en Java d'un solveur pour le problème **2-SAT**, fondée sur le graphe des implications et le calcul des composantes fortement connexes par l'algorithme de **Kosaraju**. Pour une formule à `n` variables et `m` clauses, la résolution s'exécute en `O(n + m)`.

Le programme indique si la formule est satisfiable et, si c'est le cas, donne une affectation des variables qui la satisfait, puis la vérifie.

## Principe

1. **Graphe des implications.** Chaque littéral `x` ou `¬x` est un sommet. Une clause `(a ∨ b)` équivaut à `(¬a ⇒ b) ∧ (¬b ⇒ a)` et ajoute les arcs `¬a → b` et `¬b → a`.
2. **Composantes fortement connexes.** L'algorithme de Kosaraju fait deux parcours en profondeur, l'un sur le graphe et l'autre sur son transposé. Les composantes obtenues sont numérotées dans un ordre topologique.
3. **Satisfiabilité.** La formule est insatisfiable si et seulement si une variable `x` et sa négation `¬x` appartiennent à la même composante.
4. **Affectation.** Dans le cas contraire, on pose `x = vrai` lorsque la composante de `x` vient après celle de `¬x` dans l'ordre topologique (Aspvall, Plass & Tarjan, 1979).

## Structure

| Fichier | Rôle |
|---|---|
| [`Graph.java`](src/com/company/Graph.java) | Graphe orienté étiqueté (listes d'incidence), transposition |
| [`LectureFormule.java`](src/com/company/LectureFormule.java) | Lecture et validation d'un fichier DIMACS CNF |
| [`GrapheImplications.java`](src/com/company/GrapheImplications.java) | Codage littéral ↔ sommet et construction du graphe des implications |
| [`Kosaraju.java`](src/com/company/Kosaraju.java) | Composantes fortement connexes |
| [`DeuxSat.java`](src/com/company/DeuxSat.java) | Test de satisfiabilité, affectation et vérification |
| [`Main.java`](src/com/company/Main.java) | Point d'entrée en ligne de commande |

## Compilation et exécution

Le projet nécessite Java 17 ou une version plus récente.

```bash
javac -d out src/com/company/*.java
java -cp out com.company.Main                      # formules d'exemple
java -cp out com.company.Main ma-formule.txt ...   # vos propres fichiers
```

## Format d'entrée (DIMACS CNF)

```
c commentaire
p cnf 3 4
1 -2 0
-1 3 0
1 3 0
-2 -3 0
```

La ligne `p cnf <variables> <clauses>` sert d'en-tête. Chaque clause contient exactement deux littéraux et se termine par `0`. Le littéral `k` désigne la variable `x_k` et `-k` sa négation.

## Exemple de sortie

```
Fichier             : formule-2-sat.txt
Nombre de variables : 3
Nombre de clauses   : 4

Graphe des implications : 6 sommets, 8 arcs
Composantes fortement connexes : 6

Resultat : la formule est SATISFIABLE.
Affectation : x1=V, x2=F, x3=V
Verification : OK
```

Le dépôt fournit trois formules d'exemple : `formule-2-sat.txt` (satisfiable), `formule-3-6.txt` et `formule-40-80.txt` (insatisfiables).
