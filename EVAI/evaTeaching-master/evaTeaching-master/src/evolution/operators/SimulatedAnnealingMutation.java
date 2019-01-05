package evolution.operators;

import evolution.Population;
import evolution.RandomNumberGenerator;
import evolution.individuals.RealIndividual;
import evolution.real.Real;
import evolution.real.RealFitnessFunction;

public class SimulatedAnnealingMutation implements Operator {
    private double temperature;
    // Cooling rate
    double coolingRate = 0.003;
    RandomNumberGenerator rng = RandomNumberGenerator.getInstance();
    double sigma;
    double mutProbab;
    double geneChangeProbability;
    RealFitnessFunction function;
    public SimulatedAnnealingMutation(double TempStart, double sigma, double mutProbab, double geneChangeProbability, RealFitnessFunction function){
        temperature = TempStart;
        this.sigma = sigma;
        this.mutProbab = mutProbab;
        this.geneChangeProbability = geneChangeProbability;
        this.function = function;
    }

    @Override
    public void operate(Population parents, Population offspring) {
        int size = parents.getPopulationSize();


        for (int i = 0; i < size; i++) {
            RealIndividual p1 = (RealIndividual) parents.get(i);
            RealIndividual o1 = (RealIndividual) p1.clone();
            if (rng.nextDouble() < mutProbab) {
                o1 = changing(o1);
            }
            offspring.add(o1);
        }
    }

    public RealIndividual changing(RealIndividual o1){

        RealIndividual currentSolution = (RealIndividual) o1.clone();
        RealIndividual old = (RealIndividual) o1.clone();
        RealIndividual best = (RealIndividual) o1.clone();
        while (temperature > 1) {

            //change genes
            for (int j = 0; j < o1.length(); j++) {
                if (rng.nextDouble() < geneChangeProbability) {
                    currentSolution.set(j, ((Double) old.get(j)) + sigma * RandomNumberGenerator.getInstance().nextGaussian());
                }
            }

// Get energy of solutions
            double currentEnergy = function.evaluate(currentSolution);
            double neighbourEnergy = function.evaluate(old);

            // Decide if we should accept the neighbour
            if (acceptanceProbability(currentEnergy, neighbourEnergy, temperature) < Math.random()) {
                currentSolution = old;
            }


            // Cool system
            temperature *= (1-coolingRate);
        }

        return currentSolution;
    }

    // Calculate the acceptance probability
    public static double acceptanceProbability(double energy, double newEnergy, double temperature) {
        // If the new solution is better, accept it
        if (newEnergy < energy) {
            return 1.0;
        }
        // If the new solution is worse, calculate an acceptance probability
        return Math.exp((energy - newEnergy) / temperature);
    }

}
