package ie.gmit.sw.ai.traversers;

import ie.gmit.sw.ai.*;
import ie.gmit.sw.ai.maze.Nade;

import java.util.*;
public class AStarTraversator{
	private Nade goal;

	public AStarTraversator(Nade goal){
		this.goal = goal;
	}

	public void traverse(Nade[][] maze, Nade Nade) {
        long time = System.currentTimeMillis();
    	int visitCount = 0;

		PriorityQueue<Nade> open = new PriorityQueue<Nade>(20, (Nade current, Nade next)-> (current.getPathCost() + current.getHeuristic(goal)) - (next.getPathCost() + next.getHeuristic(goal)));
		java.util.List<Nade> closed = new ArrayList<Nade>();

		open.offer(Nade);
		Nade.setPathCost(0);
		while(!open.isEmpty()){
			Nade = open.poll();
			closed.add(Nade);
			Nade.setVisited(true);
			visitCount++;

			if (Nade.isGoalNode()){
		        time = System.currentTimeMillis() - time; //Stop the clock
		        //TraversatorStats.printStats(Nade, time, visitCount);
				break;
			}

			try { //Simulate processing each expanded Nade
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}

			//Process adjacent Nades
			Nade[] children = Nade.children(maze);
			for (int i = 0; i < children.length; i++) {
				Nade child = children[i];
				int score = Nade.getPathCost() + 1 + child.getHeuristic(goal);
				int existing = child.getPathCost() + child.getHeuristic(goal);

				if ((open.contains(child) || closed.contains(child)) && existing < score){
					continue;
				}else{
					open.remove(child);
					closed.remove(child);
					child.setParent(Nade);
					child.setPathCost(Nade.getPathCost() + 1);
					open.add(child);
				}
			}
		}
	}
}
