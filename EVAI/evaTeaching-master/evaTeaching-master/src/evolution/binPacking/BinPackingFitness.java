package evolution.binPacking;

import evolution.FitnessFunction;
import evolution.individuals.Individual;
import evolution.individuals.IntegerIndividual;

import java.util.Vector;

public class BinPackingFitness implements FitnessFunction {

    Vector<Double> weights;
    int K;

    public BinPackingFitness(Vector<Double> weights, int K) {
        this.weights = weights;
        this.K = K;
    }

    public int[] getBinWeights(Individual ind) {

        int[] binWeights = new int[K];

        int[] bins = ((IntegerIndividual) ind).toIntArray();

        for (int i = 0; i < bins.length; i++) {

            binWeights[bins[i]] += weights.get(i);
        }

        return binWeights;

    }

    @Override
    public double evaluate(Individual ind) {

        int[] binWeights = getBinWeights(ind);
        int sum = 0;

        double min = Integer.MAX_VALUE;
        double max = Integer.MIN_VALUE;
        for (int i = 0; i < K; i++) {
            if (binWeights[i] < min) {
                min = binWeights[i];
            }
            if (binWeights[i] > max) {
                max = binWeights[i];
            }
        }

        ind.setObjectiveValue(max - min);    // tohle doporucuji zachovat

        //sem muzete vlozit vlastni vypocet fitness, muzete samozrejme vyuzit spocitane hmotnosti hromadek


       /*float average = sum/(float)binWeights.length;
        float var = 0;

        for (int i = 0; i < K; i++) {
            var += Math.pow(binWeights[i] - average, 2);
        }
        var  = var / (K-1);
        var = var / average;

        return 1/var;*/
        return 1 / (max - min);
       /* float average = sum/(float)binWeights.length;
        float var = 0;

        for (int i = 0; i < K; i++) {
            var += Math.abs(binWeights[i] - average);
        }
        var  = var / (K-1);
        var = var / average;

        return 1/var;*/



    }
}
