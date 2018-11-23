package game.controllers.pacman.examples;

import game.core.Game;

import java.util.ArrayList;
import java.util.List;

public class MazeProblem implements Problem<Integer> {
    private Game game;
    private List<Integer> ghostPOsitions = new ArrayList<>();
    int[] activePills;
    int[] activePowerPills;


    public MazeProblem(Game game){
        this.game = game;
        for(int i = 0; i < game.getCurLevel(); i++)
        {
            ghostPOsitions.add(game.getCurGhostLoc(i));
        }
        activePills = game.getPillIndicesActive();
        activePowerPills = game.getPowerPillIndicesActive();
    }

    @Override
    public Integer initialState() {
        return game.getCurPacManLoc();
    }

    @Override
    public List<Integer> actions(Integer state) {
        List<Integer> result = new ArrayList<>();
        for(int i = 0; i < 4; i++){
            if(game.getNeighbour(state,i) != -1) result.add(i);
        }

        return result;
    }

    @Override
    public Integer result(Integer state, int action) {
        return game.getNeighbour(state,action);
    }

    @Override
    public boolean isGoal(Integer state) {

        if(state == game.getCurPacManLoc()) return false;

        //int index = game.getPillIndex(state);
        /*if(index != -1){
            if(game.checkPill(index)) return true;
        }
        index = game.getPowerPillIndex(state);
        if(index != -1){
            if(game.checkPowerPill(index)) return true;
        }

        index = ghostPOsitions.indexOf(state);
        if(index != -1){
            if(game.isEdible(index)) return true;
        }*/

        return true;
    }

    @Override
    public int cost(Integer state, int action) {
            int neigh = game.getNeighbour(state,action);
            int ghost = ghostPOsitions.indexOf(state);
            if(ghost != -1){
                boolean pathToDanger = !game.isEdible(ghost);
                if(pathToDanger) return 200;
                else return 1;
            }
            int pill = game.getPillIndex(state);
            if(pill != -1 && game.checkPill(pill)) return 6;
            pill = game.getPowerPillIndex(state);
            if(pill != -1 && game.checkPowerPill(pill)) return 2;

            return 20;

    }
}
