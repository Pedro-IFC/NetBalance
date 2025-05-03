package solver.ag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class AgSolver extends AgMethods{
	static final private Random rand = new Random();
	static final private int POP_SIZE = 500;
	static final private int GEN = 500;
	static final private double MU_TAX = 0.01;
	
	static public Individual bestRealocate(double[][] A, double[] b) {
		return AgSolver.runAg(A, b);
	}

	static private Individual runAg(double[][] A, double[] b) {
        Individual template = new Individual(A, b);
        List<Individual> population = Arrays.asList(init_pop(template));

        Individual best = null;
        double bestFitness = Double.NEGATIVE_INFINITY;

        for (int gen = 0; gen < GEN; gen++) {
            double[] fitness = new double[population.size()];
            for (int i = 0; i < population.size(); i++) {
                fitness[i] = population.get(i).fitness();
                if (fitness[i] > bestFitness) {
                    bestFitness = fitness[i];
                    best = population.get(i).copy();
                }
            }

            List<Individual> newPop = new ArrayList<>(POP_SIZE);
            while (newPop.size() < POP_SIZE) {
                Individual[] pais = selectParentRoletaViciada(population, fitness);
                Individual filho = crossover(pais[0], pais[1]);
                filho = mutation(filho);
                newPop.add(filho);
            }
            population = newPop;
            System.out.println(gen + " - best: " + bestFitness);
        }

        return best;
    }

    static private Individual[] init_pop(Individual ind) {
        Individual[] response = new Individual[POP_SIZE];
        for (int i = 0; i < POP_SIZE; i++) {
            response[i] = ind.copy().randomize();
        }
        return response;
    }
    static private Individual crossover(Individual pai1, Individual pai2) {
        return pai1.cross(pai2);
    }

    static private Individual mutation(Individual ind) {
        if (rand.nextDouble() < MU_TAX) {
            ind.mutate();
        }
        return ind;
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
