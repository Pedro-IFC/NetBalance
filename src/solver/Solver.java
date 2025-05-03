package solver;

import solver.methods.EliminacaoGauss;

public class Solver {

	static private MetodoLinear metodo = new EliminacaoGauss();
    
	static public double[] resolverEquacao(double[][] A, double[] b, int maxIter) {
        metodo.validar(A, b);
        return metodo.resolver(A, b, maxIter);
    }
}
