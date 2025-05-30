package solver;

import solver.methods.EliminacaoGauss;

public class Solver {

	static private MetodoLinear metodo = new EliminacaoGauss();
	static public double[] resolverEquacao(double[][] A, double[] b, int maxIter) {
	    double[][] M = new double[A.length][A[0].length];
	    for (int i = 0; i < A.length; i++) {
	        M[i] = A[i].clone();
	    }
	    double[] y = b.clone();
	    metodo.validar(M, y);
	    return metodo.resolver(M, y, maxIter);
	}

}
