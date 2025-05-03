package solver.ag;

import java.util.Arrays;
import java.util.Random;

import solver.Solver;

public class Individual {
    private double[][] grafo;
    private double[] b;
    private static final Random rand = new Random();

    public Individual(double[][] grafo, double[] b) {
        this.grafo = deepCopy(grafo);
        this.b = Arrays.copyOf(b, b.length);
    }
    public double fitness() {
    	double[] numeros = Solver.resolverEquacao(this.grafo, this.b, 2);
        if (numeros == null || numeros.length == 0) {
            throw new IllegalArgumentException("O array não pode ser nulo ou vazio.");
        }

        double soma = 0.0;
        for (double num : numeros) {
            soma += num;
        }

        return 1/(soma / numeros.length);
    }

    public Individual mutate() {
        int n = b.length;
        int i = rand.nextInt(n);
        int j = rand.nextInt(n);
        while (j == i) {
            j = rand.nextInt(n);
        }
        double[][] mutG = deepCopy(this.grafo);
        double[] mutB = Arrays.copyOf(this.b, n);
        double[] tmpRow = mutG[i];
        mutG[i] = mutG[j];
        mutG[j] = tmpRow;
        for (int k = 0; k < n; k++) {
            double tmp = mutG[k][i];
            mutG[k][i] = mutG[k][j];
            mutG[k][j] = tmp;
        }
        double tmpB = mutB[i];
        mutB[i] = mutB[j];
        mutB[j] = tmpB;

        return new Individual(mutG, mutB);
    }

    public Individual randomize() {
        int n = b.length;
        int[] perm = new int[n];
        for (int i = 0; i < n; i++) perm[i] = i;
        for (int i = n - 1; i > 0; i--) {
            int j = rand.nextInt(i + 1);
            int tmp = perm[i]; perm[i] = perm[j]; perm[j] = tmp;
        }
        double[][] newG = new double[n][n];
        double[] newB = new double[n];
        for (int i = 0; i < n; i++) {
            newB[i] = this.b[perm[i]];
            for (int j = 0; j < n; j++) {
                newG[i][j] = this.grafo[perm[i]][perm[j]];
            }
        }
        return new Individual(newG, newB);
    }


    public Individual cross(Individual par) {
        int n = b.length;
        double[][] newG = new double[n][n];
        double[] newB = new double[n];

        for (int i = 0; i < n; i++) {
            newB[i] = rand.nextBoolean() ? this.b[i] : par.b[i];
            for (int j = 0; j < n; j++) {
                newG[i][j] = rand.nextBoolean()
                            ? this.grafo[i][j]
                            : par.grafo[i][j];
            }
        }
        return new Individual(newG, newB);
    }


    public Individual copy() {
        Individual ind = new Individual(this.grafo, this.b);
        return ind;
    }

    private double[][] deepCopy(double[][] matrix) {
        double[][] copy = new double[matrix.length][];
        for (int i = 0; i < matrix.length; i++) {
            copy[i] = Arrays.copyOf(matrix[i], matrix[i].length);
        }
        return copy;
    }
    
	public double[][] getGrafo() {
		return grafo;
	}
	public void setGrafo(double[][] grafo) {
		this.grafo = grafo;
	}
	
	public double[] getB() {
		return b;
	}
	public void setB(double[] b) {
		this.b = b;
	}
	@Override
	public String toString() {
	    StringBuilder sb = new StringBuilder();
	    sb.append("Individual:\n");
	    sb.append("  grafo = [\n");
	    for (double[] linha : grafo) {
	        sb.append("    ").append(Arrays.toString(linha)).append("\n");
	    }
	    sb.append("  ]\n");
	    sb.append("  b = ").append(Arrays.toString(b)).append("\n");
	    return sb.toString();
	}

	
}
