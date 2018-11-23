package game.controllers.pacman.examples;

import game.PacManSimulator;
import game.controllers.ghosts.game.GameGhosts;
import game.controllers.pacman.PacManHijackController;
import game.core.G;
import game.core.Game;

public final class MyPacMan extends PacManHijackController
{
	@Override
	public void tick(Game game, long timeDue) {
		
		// Code your agent here.
		Problem<Integer> p = new MazeProblem(game);
		Node<Integer> result = UCS.search(p);
		int[] res = {result.state};

		pacman.set(game.getNextPacManDir(game.getTarget(game.getCurPacManLoc(),res,true, Game.DM.PATH),true, Game.DM.PATH));
	}
		// Dummy implementation: move in a random direction.  You won't live long this way,
		//int[] directions=game.getPossiblePacManDirs(false);
		//pacman.set(directions[G.rnd.nextInt(directions.length)]);


	private int returnDirection(Node<Integer> end){
		Node<Integer> second = end;
		while(end.parent != null)
		{
			second = end;
			end = end.parent;
		}
		return second.action;
	}
		
	public static void main(String[] args) {
		PacManSimulator.play(new MyPacMan(), new GameGhosts());
	}
}