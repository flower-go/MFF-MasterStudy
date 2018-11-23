package evolution.binPacking;

import evolution.Population;
import evolution.RandomNumberGenerator;
import evolution.individuals.ArrayIndividual;
import evolution.operators.Operator;

import java.util.ArrayList;
import java.util.Vector;

/**
 * A mutation which swaps the values on different positions in a single individual.
 *
 * @author Martin Pilat
 */
public class MyMutation implements Operator {

    double mutationProbability;
    double geneChangeProbability;
    Vector<Double> weights;
    RandomNumberGenerator rng = RandomNumberGenerator.getInstance();

    /**
     * Constructor, sets the probabilities
     * @param mutationProbability the probability of mutating an individual
     * @param geneChangeProbability the percentage of genes which will be swapped in a mutated individual
     */

    public MyMutation(double mutationProbability, double geneChangeProbability, Vector<Double> weights) {
        this.mutationProbability = mutationProbability;
        this.geneChangeProbability = geneChangeProbability;
        this.weights = weights;
    }

    public void operate(Population parents, Population offspring) {

        int size = parents.getPopulationSize();

        for (int i = 0; i < size; i++) {

            ArrayIndividual p1 = (ArrayIndividual) parents.get(i);
            ArrayIndividual o1 = (ArrayIndividual) p1.clone();

            if (rng.nextDouble() < mutationProbability) {
                for (int j = 0; j < geneChangeProbability * p1.length(); j++) {
                    int r1 = RandomNumberGenerator.getInstance().nextInt(p1.length());
                    ArrayList<Integer> lighter = getLighter(o1,(int)o1.get(r1));
                    int r2 = 0;
                    if(0 < lighter.size())
                    {
                        r2 = RandomNumberGenerator.getInstance().nextInt(lighter.size());
                        r2 = lighter.get(r2);
                    }
                    else{
                        r2 = RandomNumberGenerator.getInstance().nextInt(p1.length());
                    }
                    Object v1 = o1.get(r1);
                    Object v2 = o1.get(r2);

                    o1.set(r1, v2);
                    o1.set(r2, v1);
                }
            }

            offspring.add(o1);
        }
    }

    private ArrayList<Integer> getLighter(ArrayIndividual o1, int r1){
        ArrayList result = new ArrayList<>();
        int[] binWeights = getBinWeights(o1);
        int weightOfR1 = binWeights[r1];
        for(int i = 0; i < o1.length(); i++){
            if( binWeights[(int)o1.get(i)] < weightOfR1)
                result.add(i);
        }
        return result;
    }

    public int[] getBinWeights(ArrayIndividual ind) {

        int[] binWeights = new int[10];

        for (int i = 0; i < ind.length(); i++) {

            binWeights[(int)ind.get(i)] += weights.get(i);
        }

        return binWeights;

    }

}
