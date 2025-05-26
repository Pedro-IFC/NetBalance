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
    	try {
    	    double[] numeros = Solver.resolverEquacao(this.grafo, this.b, 2);

    	    if (numeros == null || numeros.length == 0) {
    	        return 0.0;
    	    }

    	    double soma = 0.0;
            for (double num : numeros) {
                if(num<0)
            	    return 0;
            	soma += num;
            }

    	    return 1 / (soma / numeros.length);
    	} catch (Exception e) {
    	    return 0.0;
    	}
    }
    public Individual mutate() {
        double[][] novoGrafo = deepCopy(this.grafo);
        int n = novoGrafo.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double pesoAtual = novoGrafo[i][j];
                double variacao = (rand.nextDouble() * 2 - 1) * 20;
                if (pesoAtual > 0) {
                    double novoPeso = Math.max(0, pesoAtual + variacao);
                    novoGrafo[i][j] = novoPeso;
                    novoGrafo[j][i] = novoPeso;
                } else if (pesoAtual < 0) {
                    double novoPeso = Math.min(0, pesoAtual - variacao);
                    novoGrafo[i][j] = novoPeso;
                    novoGrafo[j][i] = novoPeso;
                }
            }
        }
        return new Individual(novoGrafo, this.b);
    }

    public Individual randomize() {
        double[][] novoGrafo = deepCopy(this.grafo);
        int n = novoGrafo.length;
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double pesoAtual = novoGrafo[i][j];
                double variacao = (rand.nextDouble() * 2 - 1) * 20;
                if (pesoAtual > 0) {
                    double novoPeso = Math.max(0, pesoAtual + variacao);
                    novoGrafo[i][j] = novoPeso;
                    novoGrafo[j][i] = novoPeso;
                } else if (pesoAtual < 0) {
                    double novoPeso = Math.min(0, pesoAtual - variacao);
                    novoGrafo[i][j] = novoPeso;
                    novoGrafo[j][i] = novoPeso;
                }
            }
        }
        return new Individual(novoGrafo, this.b);
    }

    public Individual cross(Individual par) {
        double[][] grafoPai1 = this.grafo;
        double[][] grafoPai2 = par.getGrafo();
        int n = grafoPai1.length;
        double[][] filho = new double[n][n];

        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                double peso1 = grafoPai1[i][j];
                double peso2 = grafoPai2[i][j];
                double pesoEscolhido = rand.nextBoolean() ? peso1 : peso2;
                if (peso1 > 0) {
                    pesoEscolhido = Math.abs(pesoEscolhido);
                } else if (peso1 < 0) {
                    pesoEscolhido = -Math.abs(pesoEscolhido);
                } else {
                    pesoEscolhido = 0.0;
                }
                filho[i][j] = pesoEscolhido;
                filho[j][i] = pesoEscolhido;
            }
        }

        return new Individual(filho, this.b);
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
