package solver;

public class Metodos {
    private MetodoLinear metodo;
    
    public Metodos(MetodoLinear m) {
        this.metodo = m;
    }

    public double[] resolverEquacao(double[][] A, double[] b, int maxIter) {
        metodo.validar(A, b);
        return metodo.resolver(A, b, maxIter);
    }
}