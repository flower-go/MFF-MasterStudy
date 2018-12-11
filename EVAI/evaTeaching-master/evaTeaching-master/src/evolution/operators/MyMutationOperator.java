package evolution.operators;

import evolution.Population;
import evolution.RandomNumberGenerator;
import evolution.individuals.RealIndividual;

public class MyMutationOperator implements Operator {
    double mutationProbability;
    double geneChangeProbability;
    RandomNumberGenerator rng = RandomNumberGenerator.getInstance();
    double variance = 1.0;
    double discount = 1.0;

    public MyMutationOperator(double mutationProbability, double geneChangeProbability, double variance, double discount) {
        this.mutationProbability = mutationProbability;
        this.geneChangeProbability = geneChangeProbability;
        this.variance = variance;
        this.discount = discount;
    }

    @Override
    public void operate(Population parents, Population offspring) {
        int size = parents.getPopulationSize();

        for (int i = 0; i < size; i++) {

            RealIndividual p1 = (RealIndividual) parents.get(i);
            RealIndividual o1 = (RealIndividual) p1.clone();

            if (rng.nextDouble() < mutationProbability) {
                for (int j = 0; j < o1.length(); j++) {
                    if (rng.nextDouble() < geneChangeProbability) {
                        o1.set(j, ((Double) o1.get(j)) + variance*RandomNumberGenerator.getInstance().nextGaussian());
                    }
                }
            }

            offspring.add(o1);
        }
        variance = variance*discount;
    }

}
