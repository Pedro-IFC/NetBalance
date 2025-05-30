package main;

import java.util.Random;

import solver.Solver;
import solver.ag.AgSolver;
import solver.ag.Individual;

public class TesteMetodoIlhas {

    public static void main(String[] args) { 
        Random rand = new Random();     
        double[][] posicoesIniciais = {
            {1, 1, 0, 0, 0},
            {1, 1, 1, 0, 0},
            {0, 1, 1, 1, 0},
            {0, 0, 1, 1, 1},
            {0, 0, 0, 1, 1}
        };    
        double[][] minimo = {
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0},
            {0, 0, 0, 0, 0}
        };    
        double[][] maximo = {
            {100, 80, 0, 0, 0},
            {80, 100, 80, 0, 0},
            {0, 80, 100, 80, 0},
            {0, 0, 80, 100, 80},
            {0, 0, 0, 80, 100}
        };
        double[] b = {125, 75, 175, 87.5, 100};

        double[][] tester = new double[posicoesIniciais.length][posicoesIniciais.length];
        
        for (int i = 0; i < posicoesIniciais.length; i++) {
            for (int j = 0; j < posicoesIniciais.length; j++) {
                double val = posicoesIniciais[i][j] * rand.nextDouble() * (maximo[i][j] - minimo[i][j]);
                tester[i][j] = val;
                tester[j][i] = val;
            }
        }

        System.out.println("Inicial: " + new Individual(posicoesIniciais, minimo, maximo, b));
        double[] sol = Solver.resolverEquacao(maximo, b, 2);
        System.out.println("Solução: \n ");
        for(double s : sol) {
            System.out.println("== "+s+" \n ");
        }
        
        System.out.println("Fitness: " + new Individual(posicoesIniciais, minimo, maximo, b).fitness(tester));
        
        int numIslands = 4;
        int migrationInterval = 5; 
        int migrationSize = 2; 
        
        Individual bestIsland = AgSolver.bestRealocateIsland(
            posicoesIniciais, 
            minimo, 
            maximo, 
            b,
            numIslands,
            migrationInterval,
            migrationSize
        );

        System.out.println("\nFinal (AG com Ilhas): " + bestIsland);
        System.out.println("Solução (AG com Ilhas): \n");
        sol = Solver.resolverEquacao(bestIsland.getGrafo(), bestIsland.getB(), 2);
        for(double s : sol) {
            System.out.println("== "+s+" \n ");
        }
        System.out.println("Fitness (AG com Ilhas): " + bestIsland.fitness(tester));
    }
}