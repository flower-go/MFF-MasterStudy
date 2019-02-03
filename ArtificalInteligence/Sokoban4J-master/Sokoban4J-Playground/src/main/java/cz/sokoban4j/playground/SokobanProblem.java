package cz.sokoban4j.playground;

import cz.sokoban4j.simulation.actions.EDirection;
import cz.sokoban4j.simulation.actions.compact.CAction;
import cz.sokoban4j.simulation.actions.compact.CMove;
import cz.sokoban4j.simulation.actions.compact.CPush;
import cz.sokoban4j.simulation.board.compact.BoardCompact;
import cz.sokoban4j.simulation.board.compact.CTile;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SokobanProblem implements Problem<BoardCompact, EDirection> {

    BoardCompact compact;
    boolean[][] alive;


    public SokobanProblem(BoardCompact compact) {
        this.compact = compact;
        alive = new boolean[compact.width()][compact.height()];
        List<Position> taargets = findTargets(compact);
        bfs(alive, taargets, compact);
    }

    @Override
    public BoardCompact initialState() {
        return compact;
    }

    @Override
    public List<EDirection> actions(BoardCompact state) {
        List<EDirection> actions = new ArrayList<EDirection>(4);

        for (CPush push : CPush.getActions()) {
            if (push.isPossible(state)) {
                actions.add(push.getDirection());
            }
        }
        for (CMove move : CMove.getActions()) {
            if (move.isPossible(state)) {
                actions.add(move.getDirection());
            }
        }

        return actions;
    }

    @Override
    public BoardCompact result(BoardCompact state, EDirection action) {
        CMove m = new CMove(action);
        CPush p = new CPush(action);
        BoardCompact cloned = state.clone();
        if (p.isPossible(cloned)) {
            p.perform(cloned);
            return cloned;
        } else if (m.isPossible(cloned)) {
            m.perform(cloned);
            return cloned;
        }
        return null;
    }

    @Override
    public boolean isGoal(BoardCompact state) {
        return state.isVictory();
    }

    @Override
    public int cost(BoardCompact state, EDirection action) {
        return 1;
    }

    @Override
    public int estimate(BoardCompact state) {
        return state.boxInPlaceCount;
    }

    private boolean [][] bfs(boolean [][] finded,List<Position> nextGeneration, BoardCompact board) {

        if(nextGeneration.size() == 0) return finded;
        List<Position> forFinding = new ArrayList<>();
        for (Position p : nextGeneration
                ) {
            if(!finded[p.x][p.y]){
                forFinding.addAll(findAscentors(p, board.width(),board.height(),board, finded));
            }

        }

        return bfs(finded,forFinding,board);
    }

    private List<Position> findAscentors(Position p, int wMax, int hMax, BoardCompact board, boolean [][] finded) {
        List<Position> result = new ArrayList<>();

        // x+1,y
        int xN = p.x + 1;
        int yN = p.y;
        int xP = p.x + 2;
        int yP = p.y;
        if(isOk(xN,yN,xP,yP,wMax,hMax,board)){
            result.add(new Position(xN,yN));
            finded[xN][yN] = true;
        }
        // x-1, y
        xN = p.x - 1;
        yN = p.y;
        xP = p.x - 2;
        yP = p.y;
        if(isOk(xN,yN,xP,yP,wMax,hMax,board)){
            result.add(new Position(xN,yN));
            finded[xN][yN] = true;
        }

        //x, y+1
        xN = p.x;
        yN = p.y + 1;
        xP = p.x;
        yP = p.y + 2;
        if(isOk(xN,yN,xP,yP,wMax,hMax,board)){
            result.add(new Position(xN,yN));
            finded[xN][yN] = true;
        }

        //x, y - 1
        xN = p.x;
        yN = p.y - 1;
        xP = p.x;
        yP = p.y - 2;
        if(isOk(xN,yN,xP,yP,wMax,hMax,board)){
            result.add(new Position(xN,yN));
            finded[xN][yN] = true;
        }
        return result;
    }

    // is possible to get from pushplace to neighbour
    private boolean isOk(int xNeighbour, int yNeighbour, int xPushPlace, int yPushPlace, int wMax, int hMax, BoardCompact board){
        if (xNeighbour < wMax
                && xPushPlace < wMax
                && yNeighbour < hMax
                && yPushPlace < hMax
                && CTile.isWalkable(board.tile(xNeighbour,yNeighbour))
                &&CTile.isWalkable(board.tile(xPushPlace, yPushPlace))) {
            return true;
        }
        return false;
    }

    private List<Position> findTargets(BoardCompact board) {
        List<Position> result = new ArrayList<>();
        int[][] tiles = board.tiles;
        for (int x = 0; x < board.width(); x++) {
            for (int y = 0; y < board.height(); y++) {
                if (CTile.forSomeBox(board.tile(x, y))) {
                    result.add(new Position(x, y));
                }
            }
        }
        return result;
    }

    class Position {
        public Position(int x, int y) {
            this.x = x;
            this.y = y;
        }

        public int x;
        public int y;
    }
}