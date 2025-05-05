package main;

import solver.Solver;
import solver.ag.AgSolver;
import solver.ag.Individual;

public class TesteMetodos {

	public static void main(String[] args) {
        double[][] A = {
            {400, 100, 100, 0, 0},
            {100, 400, 0, 0, 0},
            {100, 0, 400, 50, 50},
            {0, 0, 50, 400, 0},
            {0, 0, 50, 0, 400}
        };
        double[] b = {500, 300, 700, 350, 400};

        System.out.println("Inicial: " + Solver.resolverEquacao(A,b, 2)[0]);
        
        Individual best = AgSolver.bestRealocate(A, b);
        
        System.out.println("Ag: " + Solver.resolverEquacao(best.getGrafo(), best.getB(), 2)[0]);
	}

}
