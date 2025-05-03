package solver;

public interface MetodoLinear {
    void validar(double[][] A, double[] b);

    double[] resolver(double[][] A, double[] b, int maxIter);
}
