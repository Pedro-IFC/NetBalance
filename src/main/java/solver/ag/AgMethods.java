package solver.ag;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Random;

public class AgMethods {
	static final private Random rand = new Random();
	public static List<Individual> sampleWithoutReplacement(List<Individual> pop, double[] fitness, int size) {
		List<Individual> copy = new ArrayList<>(pop);
		Collections.shuffle(copy, rand);
		return copy.subList(0, size);
	}

	public static Individual getBestIndividual(List<Individual> subset, double[] fitness) {
		return Collections.max(subset, Comparator.comparingDouble(ind -> fitness[subset.indexOf(ind)]));
	}

	public static Individual weightedRandomChoice(List<Individual> pop, double[] fitness) {
		double total = Arrays.stream(fitness).sum();
		double r = rand.nextDouble() * total;
		double cumulative = 0.0;
		for (int i = 0; i < pop.size(); i++) {
			cumulative += fitness[i];
			if (r <= cumulative) return pop.get(i);
		}
		return pop.get(pop.size() - 1);
	}
}
