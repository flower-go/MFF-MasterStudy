package game.controllers.pacman.examples;
import java.util.List;

// S = state type
public interface Problem<S> {
    S initialState();
    List<Integer> actions(S state);
    S result(S state, int action);
    boolean isGoal(S state);
    int cost(S state, int action);
}