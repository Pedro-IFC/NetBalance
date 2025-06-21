package main;

import java.util.Random;

import solver.Solver;
import solver.ag.AgSolver;
import solver.ag.Individual;

public class TesteMetodos {

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
        double[] b = {200, 75, 175, 90, 100};

		double[][] tester = new double[posicoesIniciais.length][posicoesIniciais.length];
        
        for (int i = 0; i < posicoesIniciais.length; i++) {
            for (int j = 0; j < posicoesIniciais.length; j++) {
            	double val = posicoesIniciais[i][j] * rand.nextDouble() * (maximo[i][j] - minimo[i][j]);
            	tester[i][j] = val;
            	tester[j][i] = val;
            }
        }

        System.out.println("Inicial: " + new Individual(posicoesIniciais, minimo, maximo,b));
        double[] sol = Solver.resolverEquacao(maximo, b, 2);
        System.out.println("Solução: \n ");
        for(double s : sol) {
            System.out.println("== "+s+" \n ");
        }
        
        System.out.println("Fitness: " + new Individual(posicoesIniciais, minimo, maximo,b).fitness(tester));
        
        Individual best = AgSolver.bestRealocate(posicoesIniciais, minimo, maximo, b);

	}

}
