package solver.methods;

import solver.MetodoLinear;

public class EliminacaoGauss implements MetodoLinear {
    public void validar(double[][] A, double[] b) {
        int n = A.length;
        if (b.length != n) {
            throw new IllegalArgumentException("Dimensões incompatíveis entre A e b.");
        }
    }

    public double[] resolver(double[][] A, double[] b, int maxIter) {
        int n = A.length;
        for (int i = 0; i < n; i++) {
            int maxIndex = i;
            for (int k = i + 1; k < n; k++) {
                if (Math.abs(A[k][i]) > Math.abs(A[maxIndex][i])) {
                    maxIndex = k;
                }
            }
            if (A[maxIndex][i] == 0) {
                throw new IllegalArgumentException(
                    "Matriz singular, não é possível resolver o sistema no passo " + i);
            }
            if (maxIndex != i) {
                double[] tmp = A[i]; A[i] = A[maxIndex]; A[maxIndex] = tmp;
                double t = b[i]; b[i] = b[maxIndex]; b[maxIndex] = t;
            }
            for (int k = i + 1; k < n; k++) {
                double fator = A[k][i] / A[i][i];
                for (int j = i; j < n; j++) {
                    A[k][j] -= fator * A[i][j];
                }
                b[k] -= fator * b[i];
            }
        }
        double[] x = new double[n];
        for (int i = n - 1; i >= 0; i--) {
            double soma = 0;
            for (int j = i + 1; j < n; j++) {
                soma += A[i][j] * x[j];
            }
            x[i] = (b[i] - soma) / A[i][i];
        }
        for (int iter = 0; iter < maxIter; iter++) {
            double[] r = new double[n];
            for (int i = 0; i < n; i++) {
                double soma = 0;
                for (int j = 0; j < n; j++) {
                    soma += A[i][j] * x[j];
                }
                r[i] = b[i] - soma;
            }
            double[] delta = new double[n];
            for (int i = n - 1; i >= 0; i--) {
                double soma = 0;
                for (int j = i + 1; j < n; j++) {
                    soma += A[i][j] * delta[j];
                }
                delta[i] = (r[i] - soma) / A[i][i];
            }
            for (int i = 0; i < n; i++) {
                x[i] += delta[i];
            }
        }
        return x;
    }
}
