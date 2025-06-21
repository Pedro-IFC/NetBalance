package solver.ag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

import solver.Solver;

import java.io.FileWriter;
import java.io.IOException;

public class AgSolver extends AgMethods{
	static final private Random rand = new Random();
	static final private int POP_SIZE = 200;
	static final private int GEN = 200;
	static final private double MU_TAX = 0.1;
	

    static public Individual bestRealocateIsland(double[][] posicoesIniciais, double[][] minimo, double[][] maximo, double[] b, 
                                                int numIslands, int migrationInterval, int migrationSize) {
        return runAgIsland(posicoesIniciais, minimo, maximo, b, numIslands, migrationInterval, migrationSize);
    }

    static private Individual runAgIsland(double[][] posicoesIniciais, double[][] minimo, double[][] maximo, double[] b, 
                                         int numIslands, int migrationInterval, int migrationSize) {
        Individual template = new Individual(posicoesIniciais, minimo, maximo, b);
        double[][] tester = generateTester(posicoesIniciais, minimo, maximo);
        
        List<List<Individual>> islands = new ArrayList<>(numIslands);
        for (int i = 0; i < numIslands; i++) {
            List<Individual> pop = new ArrayList<>(POP_SIZE);
            pop.add(template.copy());
            for (int j = 1; j < POP_SIZE; j++) {
                pop.add(template.randomize());
            }
            islands.add(pop);
        }

        Individual bestGlobal = null;
        double bestFitnessGlobal = Double.NEGATIVE_INFINITY;
        int torneioSize = 3;
        for (int gen = 0; gen < GEN; gen++) {
            for (int i = 0; i < numIslands; i++) {
                List<Individual> population = islands.get(i);
                double[] fitness = new double[population.size()];
                for (int j = 0; j < population.size(); j++) {
                    fitness[j] = population.get(j).fitness(tester);
                    if (fitness[j] > bestFitnessGlobal) {
                        bestFitnessGlobal = fitness[j];
                        bestGlobal = population.get(j).copy();
                    }
                }
                List<Individual> newPop = new ArrayList<>(POP_SIZE);
                while (newPop.size() < POP_SIZE) {
                    Individual[] pais = selectParentTorneioIsland(population, tester, torneioSize);
                    Individual filho = crossover(pais[0], pais[1]);
                    filho = mutation(filho);
                    newPop.add(filho);
                }
                islands.set(i, newPop);
            }
            if ((gen % migrationInterval == 0) && (gen != 0) && (numIslands > 1)) {
                List<List<Individual>> migrants = new ArrayList<>(numIslands);
                for (List<Individual> island : islands) {
                    island.sort(Comparator.comparingDouble(ind -> -ind.fitness(tester)));
                    List<Individual> topMigrants = new ArrayList<>(
                        island.subList(0, Math.min(migrationSize, island.size()))
                    );
                    migrants.add(topMigrants);
                }

                for (int i = 0; i < numIslands; i++) {
                    int nextIsland = (i + 1) % numIslands;
                    List<Individual> receivers = islands.get(nextIsland);
                    
                    receivers.sort(Comparator.comparingDouble(ind -> ind.fitness(tester)));
                    
                    List<Individual> incoming = migrants.get(i);
                    for (int j = 0; j < incoming.size() && j < receivers.size(); j++) {
                        receivers.set(j, incoming.get(j).copy());
                    }
                }
            }
        }
        return bestGlobal != null ? bestGlobal.copy() : template.copy();
    }

    static private double[][] generateTester(double[][] posicoesIniciais, double[][] minimo, double[][] maximo) {
        double[][] tester = new double[posicoesIniciais.length][posicoesIniciais.length];
        for (int i = 0; i < posicoesIniciais.length; i++) {
            for (int j = 0; j < posicoesIniciais.length; j++) {
                double val = rand.nextDouble() * (maximo[i][j] - minimo[i][j]);
                tester[i][j] = val;
                tester[j][i] = val;
            }
        }
        return tester;
    }

    static private Individual[] selectParentTorneioIsland(List<Individual> pop, double[][] tester, int torneioSize) {
        List<Individual> copy = new ArrayList<>(pop);
        Collections.shuffle(copy, rand);
        
        List<Individual> tournament1 = copy.subList(0, torneioSize);
        List<Individual> tournament2 = copy.subList(torneioSize, 2 * torneioSize);
        
        return new Individual[] {
            getBestInGroup(tournament1, tester),
            getBestInGroup(tournament2, tester)
        };
    }

    static private Individual getBestInGroup(List<Individual> group, double[][] tester) {
        Individual best = group.get(0);
        double bestFit = best.fitness(tester);
        for (Individual ind : group) {
            double fit = ind.fitness(tester);
            if (fit > bestFit) {
                best = ind;
                bestFit = fit;
            }
        }
        return best;
    }

	
	static public Individual bestRealocate(double[][] posicoesIniciais, double[][] minimo, double[][] maximo, double[] b) {
		return AgSolver.runAg(posicoesIniciais, minimo, maximo, b);
	}

	static private Individual runAg(double[][] posicoesIniciais, double[][] minimo, double[][] maximo, double[] b) {
	    Individual template = new Individual(posicoesIniciais, minimo, maximo, b);

		double[][] tester = new double[posicoesIniciais.length][posicoesIniciais.length];
        
        for (int i = 0; i < posicoesIniciais.length; i++) {
            for (int j = 0; j < posicoesIniciais.length; j++) {
            	double val = rand.nextDouble() * (maximo[i][j] - minimo[i][j]);
            	tester[i][j] = val;
            	tester[j][i] = val;
            }
        }
        
	    List<Individual> population = Arrays.asList(init_pop(template));

	    Individual best = null;
	    double bestFitness = Double.NEGATIVE_INFINITY;

	    StringBuilder historicoJson = new StringBuilder();
	    historicoJson.append("[\n");

        historicoJson.append("  {\n");
        historicoJson.append("    \"geracao\": ").append(0).append(",\n");
        historicoJson.append("    \"grafo\": ").append(matrizToJson(template.getGrafo())).append(",\n");
        historicoJson.append("    \"b\": ").append(vetorToJson(template.getB())).append(",\n");
        historicoJson.append("    \"fitness\": ").append(template.fitness(tester)).append("\n");
        historicoJson.append("  },\n");

        for (int gen = 0; gen < GEN; gen++) {
	        double[] fitness = new double[population.size()];
	        bestFitness = population.get(0).fitness(tester);
	        for (int i = 0; i < population.size(); i++) {
	            fitness[i] = population.get(i).fitness(tester);
	            if (fitness[i] >= bestFitness) {
	                bestFitness = fitness[i];
	                best = population.get(i).copy();
	                historicoJson.append("  {\n");
		            historicoJson.append("    \"geracao\": ").append(gen+1).append(",\n");
		            historicoJson.append("    \"grafo\": ").append(matrizToJson(population.get(i).getGrafo())).append(",\n");
		            historicoJson.append("    \"b\": ").append(vetorToJson(population.get(i).getB())).append(",\n");
		            historicoJson.append("    \"fitness\": ").append(fitness[i]).append("\n");
		            historicoJson.append("  },\n");
	            }
	        }

	        List<Individual> newPop = new ArrayList<>(POP_SIZE);
	        while (newPop.size() < POP_SIZE) {
	            Individual[] pais = selectParentTorneio(population, fitness);
	            Individual filho = crossover(pais[0], pais[1]);
	            filho = mutation(filho);
	            newPop.add(filho);
	        }
	        population = newPop;
	    }

	    if (historicoJson.lastIndexOf(",") == historicoJson.length() - 2) {
	        historicoJson.delete(historicoJson.length() - 2, historicoJson.length());
	        historicoJson.append("\n");
	    }
	    historicoJson.append("]");

	    try (FileWriter writer = new FileWriter("historico_individuos.json")) {
	        writer.write(historicoJson.toString());
	    } catch (IOException e) {
	        e.printStackTrace();
	    }

	    return best.copy();
	}
	private static String matrizToJson(double[][] matriz) {
	    StringBuilder sb = new StringBuilder();
	    sb.append("[");
	    for (int i = 0; i < matriz.length; i++) {
	        sb.append("[");
	        for (int j = 0; j < matriz[i].length; j++) {
	            sb.append(matriz[i][j]);
	            if (j < matriz[i].length - 1) sb.append(", ");
	        }
	        sb.append("]");
	        if (i < matriz.length - 1) sb.append(", ");
	    }
	    sb.append("]");
	    return sb.toString();
	}
	private static String vetorToJson(double[] vetor) {
	    StringBuilder sb = new StringBuilder();
	    sb.append("[");
	    for (int i = 0; i < vetor.length; i++) {
	        sb.append(vetor[i]);
	        if (i < vetor.length - 1) sb.append(", ");
	    }
	    sb.append("]");
	    return sb.toString();
	}

    static private Individual[] init_pop(Individual ind) {
        Individual[] response = new Individual[POP_SIZE];
        response[0]=ind.copy();
        for (int i = 1; i < POP_SIZE; i++) {
            response[i] = ind.randomize();
        }
        return response;
    }
    static private Individual crossover(Individual pai1, Individual pai2) {
        return pai1.cross(pai2);
    }

    static private Individual mutation(Individual ind) {
        return ind.mutate(MU_TAX);
    }
    static private Individual[] selectParentTorneio(List<Individual> pop, double[] fitness) {
		int torneioSize = 3;
		List<Individual> chosen = sampleWithoutReplacement(pop, fitness, torneioSize * 2);

		Individual pai1 = getBestIndividual(chosen.subList(0, torneioSize - 1), fitness);
		Individual pai2 = getBestIndividual(chosen.subList(torneioSize, 5), fitness);
		return new Individual[]{pai1, pai2};
	}
	static private Individual[] selectParentRoletaViciada(List<Individual> pop, double[] fitness) {
		Individual pai1 = weightedRandomChoice(pop, fitness);
		Individual pai2 = weightedRandomChoice(pop, fitness);
		return new Individual[]{pai1, pai2};
	}
	static private Individual[] selectParentRoleta(List<Individual> pop) {
		Individual pai1 = pop.get(rand.nextInt(pop.size()));
		Individual pai2 = pop.get(rand.nextInt(pop.size()));
		return new Individual[]{pai1, pai2};
	}
}
