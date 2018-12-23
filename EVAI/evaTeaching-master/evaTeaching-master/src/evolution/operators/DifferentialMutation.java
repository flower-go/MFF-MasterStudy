package evolution.operators;

import evolution.FitnessFunction;
import evolution.Population;
import evolution.RandomNumberGenerator;
import evolution.individuals.Individual;
import evolution.individuals.RealIndividual;
import evolution.real.Real;
import evolution.real.RealFitnessFunction;

public class DifferentialMutation implements Operator {

    double F;
    double mutationProbab;
    RandomNumberGenerator rng = RandomNumberGenerator.getInstance();
    int dimenzion;
    double CR;
    double mutPerBit;
    RealFitnessFunction function ;
    boolean adaptive;

    public DifferentialMutation(double mutationProbab, double F, int dimenzion, double CR, double mutPerBit, RealFitnessFunction function, boolean adaptive) {

        this.mutationProbab = mutationProbab;
        this.dimenzion = dimenzion;
        this.CR = CR;
        this.mutPerBit = mutPerBit;
        this.function = function;
        this.F = F;
        this.adaptive = adaptive;
    }

    @Override
    public void operate(Population parents, Population offspring) {
        int size = parents.getPopulationSize();
        if(adaptive) {
            this.F = 0.5 + (1 - 0.5) * rng.nextDouble();
        }

        for (int i = 0; i < size; i++) {

            RealIndividual p1 = (RealIndividual) parents.get(i);
            RealIndividual o1 = (RealIndividual) p1.clone();

            if (rng.nextDouble() < mutationProbab) {
                int a = selectDifferent(i);
                int b = selectDifferent(i,a);
                int c = selectDifferent(i,a,b);

                RealIndividual indA = (RealIndividual) parents.get(a);
                RealIndividual indB = (RealIndividual) parents.get(b);
                RealIndividual indC = (RealIndividual) parents.get(c);

                for(int j = 0; j < o1.length(); j++){
                    if(rng.nextDouble() < mutPerBit){
                        o1.set(j,F*((double)indA.get(j) - (double)indB.get(j)));
                    }
                    if(rng.nextDouble() < 1.0-CR){
                        o1.set(j,indC.get(j));                    }
                }

            }
            if(function.evaluate(o1) > function.evaluate(p1)){
                offspring.add(o1);
            }
            else offspring.add(p1);

        }
    }


    private int selectDifferent(int i){
        int result;
        do{
            result = rng.nextInt(dimenzion);
        }while(i == result);
        return result;
    }

    private int selectDifferent(int i, int y){
        int result;
        do{
            result = rng.nextInt(dimenzion);
        }while(result == i || result == y);
        return result;
    }

    private int selectDifferent(int i, int y, int z){
        int result;
        do{
            result = rng.nextInt(dimenzion);
        }while(result == i || result == y || result == z);
        return result;
    }
}
