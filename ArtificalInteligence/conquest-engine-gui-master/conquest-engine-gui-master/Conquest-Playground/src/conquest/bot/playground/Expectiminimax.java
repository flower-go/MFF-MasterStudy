package conquest.bot.playground;

import conquest.bot.state.Action;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

class Expectiminimax<S, A> implements Strategy<S, A> {


    private final Game<S, A> game;
    private final Generator<S, A> generator;
    private final Evaluator<S> evaluator;
    private final int maxDepth;

    public Expectiminimax(Game<S, A> game, Generator<S, A> generator, Evaluator<S> evaluator,
                          int maxDepth) {


        this.game = game;
        this.generator = generator;
        this.evaluator = evaluator;
        this.maxDepth = maxDepth;
    }

    /*
    Returns which action to take in given state
     */
    @Override
    public A action(S state) {
        List<A> actions = generator.actions(state);

            double max = - Double.MAX_VALUE;
            A actionMax= null;

            for (A action: actions
                    ) {
                double value = 0;
                for (Possibility<S> p: generator.possibleResults(state,action)
                        ) {
                    try {
                        value += p.prob* expectiminimax(p.state,maxDepth,  - Double.MAX_VALUE, Double.MAX_VALUE);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
                if(value >= max)
                {
                    actionMax = action;
                }
            }

            return actionMax;
    }

    private double expectiminimax(S state, int depth, double alpha, double beta) throws Exception {

            if(depth <= 0) return evaluator.evaluate(state);
            if(game.isDone(state)) return game.outcome(state);

            // List<ActionScore> scores = new ArrayList<>();

            List<A> actions = generator.actions(state);

            ActionScore best = null;

        for (A action:actions
             ) {

            ActionScore a = computeActionScore(action,state,depth, alpha, beta);

            // scores.add(a);
            if(best == null){
                best = a;
            }
            else if(game.player(state) == 1){
                // return scores.get(0).score;
                if (best.score < a.score){
                    best = a;
                }
                if(best.score >= beta) return best.score;
                alpha = alpha > best.score ? alpha: best.score;
            }
            else if (game.player(state) == 2){
                //return scores.get(scores.size() - 1).score;
                if (best.score > a.score){
                    best = a;
                }
                if(best.score <= alpha){
                    return best.score;
                }
                beta = beta > best.score ? best.score: beta;
            }

        }
        return best.score;
        // Collections.sort(scores);
    }

    private ActionScore computeActionScore(A action, S state, int depth, double alpha, double beta){
            double score = 0;
            for (Possibility<S> p: generator.possibleResults(state,action)
                    ) {
                try {
                    score += p.prob* expectiminimax(p.state, depth - 1,alpha, beta);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

        return new ActionScore(score, action);
    }

    private ActionScore computeMax()
    {
            return null;
    }

    private ActionScore computeMin()
    {
            return null;
    }

    class ActionScore implements Comparable<ActionScore>{
        double score;
        A action;
        public ActionScore(double score, A action){
            this.action = action;
            this.score = score;
        }

        @Override
        public int compareTo(ActionScore o) {
            return Double.compare(this.score, o.score);
        }
    }
}
