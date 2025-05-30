package solver.ag;

import java.util.Arrays;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import solver.Solver;

public class Individual {
    private static final Random rand = new Random();
    private double[][] grafo;
    private double[][] posicoes;
    private double[][] minimo;
    private double[][] maximo;
    private double[] b;

    public Individual(double[][] posicoesIniciais, double[][] minimo, double[][] maximo, double[] b) {
        this.minimo = minimo;
        this.posicoes = posicoesIniciais;
        this.maximo = maximo;
        this.b = b;
        this.grafo = new double[posicoesIniciais.length][posicoesIniciais[0].length];
        
        for (int i = 0; i < grafo.length; i++) {
            for (int j = 0; j < grafo[i].length; j++) {
            	double val= 0.0;
            	if(maximo[i][j]>0){
            		val = this.posicoes[i][j] * ThreadLocalRandom.current().nextDouble(minimo[i][j], maximo[i][j]);
            	}
            	grafo[i][j] = val;
            	grafo[j][i] = val;
            }
        }
    }
    
    public double fitness(double[][] tester) {
    	try {
    		double supersoma = 0;
    		for(int k = 0; k<10; k++) {
    	        double[][] newGrafo = new double[posicoes.length][posicoes.length];
    	        for (int i = 0; i < posicoes.length; i++) {
    	            for (int j = 0; j < posicoes.length; j++) {
    	            	newGrafo[i][j] = posicoes[i][j] * tester[i][j];
    	            	newGrafo[j][i] = posicoes[i][j] * tester[j][i];
    	            }
    	        }
        	    double[] numeros = Solver.resolverEquacao(newGrafo, this.b, 2);

        	    if (numeros == null || numeros.length == 0) {
        	        return 0.0;
        	    }

        	    double soma = 0.0;
                for (double num : numeros) {
                    soma = soma + (num<0? -num:num);
                }
                supersoma+=10*(1/ (soma / numeros.length));
    		}
    	    return supersoma/10;
    	} catch (Exception e) {
    	    return 0.0;
    	}
    }

    public Individual mutate(double MU_TAX) {
        double[][] newGrafo = new double[posicoes.length][posicoes.length];
        for (int i = 0; i < posicoes.length; i++) {
            newGrafo[i] = Arrays.copyOf(posicoes[i], posicoes[i].length);
        }
        for (int i = 0; i < newGrafo.length; i++) {
            for (int j = 0; j < newGrafo.length; j++) {
            	if(i!=j) {
                	if(((1.0 - 0.01) * rand.nextDouble()) < MU_TAX) {
                		newGrafo[i][j] = -(newGrafo[i][j]-1);
                		newGrafo[j][i] = -(newGrafo[j][i]-1);
                	}
            	}
            }
        }

        return new Individual(newGrafo, this.minimo, this.maximo, this.b);
    }

    public Individual randomize() {
        double[][] newGrafo = new double[posicoes.length][posicoes.length];
        
        for (int i = 0; i < newGrafo.length; i++) {
            for (int j = 0; j < newGrafo.length; j++) {
            	if(i!=j) {
	            	newGrafo[j][i] = 0;
	            	newGrafo[i][j] = 0;
	            	if(rand.nextDouble() < 0.5) {
		            	newGrafo[j][i] = 1;
		            	newGrafo[i][j] = 1;
	            	}
            	}else {
            		newGrafo[i][j] = 1;
            	}
            }
        }
        return new Individual(newGrafo, this.minimo, this.maximo, this.b);
    }

    public Individual cross(Individual par) {
        double[][] newGrafo = new double[posicoes.length][posicoes.length];
        for (int i = 0; i < posicoes.length; i++) {
            for (int j = 0; j < posicoes[i].length; j++) {
            	if(i!=j) {
            		double k = rand.nextBoolean() ? this.posicoes[i][j] : par.posicoes[i][j];
                    newGrafo[i][j] = k;
                    newGrafo[j][i] = k;
                }
            }
        }
        return new Individual(newGrafo, minimo, maximo, b);
    }

    public double[][] getGrafo() {
        return grafo;
    }

    public double[] getB() {
        return b;
    }

    public Individual copy() {
        Individual ind = new Individual(this.posicoes, this.maximo, this.minimo, this.b);
        return ind;
    }
    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Individual:\n");
        for (double[] linha : posicoes) {
            sb.append(Arrays.toString(linha)).append("\n");
        }
        return sb.toString();
    }
}