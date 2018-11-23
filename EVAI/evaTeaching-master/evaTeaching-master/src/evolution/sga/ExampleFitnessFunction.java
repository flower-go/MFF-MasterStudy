package evolution.sga;

import evolution.FitnessFunction;
import evolution.individuals.BooleanIndividual;
import evolution.individuals.Individual;

/**
 * @author Martin Pilat
 */
public class ExampleFitnessFunction implements FitnessFunction {

    /**
     * THis is an example fitness function

     * @param ind The individual which shall be evaluated
     * @return The number of 1s in the individual
     */

    public double evaluate(Individual ind) {

        BooleanIndividual bi = (BooleanIndividual) ind;
        boolean[] genes = bi.toBooleanArray();

        double fitness = 0.0;
        double objective = 0.0;

       /* for (int i = 0; i < genes.length; i++) {
            if (genes[i])
                fitness += 1.0;
        }*/
       boolean odd = false;
       for(int  i = 0; i < genes.length; i++){
           if(genes[i] == odd){
               fitness += 1.0;
               objective += 1.0;
           }

           odd = !odd;
       }

/*
        boolean odd = genes[0];
        for(int  i = 1; i < genes.length; i++){
            if(genes[i] != odd)
                fitness += 1.0;
            odd = genes[i];
        }
*/

        /*boolean odd = false;
        boolean prew = genes[0];
        if(!prew) fitness += 2;
        for(int  i = 0; i < genes.length; i++){
            if(genes[i] == odd){
                fitness += 1.0;
                objective +=1.0;
            }
            if(i > 1 && genes[i] != prew)
                fitness += 1.0;
            prew = genes[i];
            odd = !odd;
        }*/




        ind.setObjectiveValue(objective); //this sets the objective value, can be different from the fitness function

        return fitness;
    }

}
