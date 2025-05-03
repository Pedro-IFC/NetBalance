package solver.methods;

import solver.MetodoLinear;
import java.util.Arrays;

public class MetodoGaussSeidel implements MetodoLinear {
    private final double tol = 1e-6;

    public void validar(double[][] A, double[] b) {
        int n = A.length;
        if (b.length != n) {
            throw new IllegalArgumentException("Dimensões incompatíveis entre A e b.");
        }
    }

    public double[] resolver(double[][] A, double[] b, int maxIter) {
        int n = A.length;
        double[] x = new double[n];
        for (int iter = 0; iter < maxIter; iter++) {
            double[] xOld = Arrays.copyOf(x, n);
            for (int i = 0; i < n; i++) {
                double soma = 0;
                for (int j = 0; j < n; j++) {
                    if (j != i) soma += A[i][j] * x[j];
                }
                x[i] = (b[i] - soma) / A[i][i];
            }
            double maxDiff = 0;
            for (int i = 0; i < n; i++) {
                maxDiff = Math.max(maxDiff, Math.abs(x[i] - xOld[i]));
            }
            if (maxDiff < tol) {
                return x;
            }
        }
        return x;
    }
}