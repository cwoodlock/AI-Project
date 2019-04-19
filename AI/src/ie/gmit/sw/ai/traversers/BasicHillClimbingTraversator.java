package ie.gmit.sw.ai.traversers;

import ie.gmit.sw.ai.*;
import ie.gmit.sw.ai.maze.Nade;
public abstract class BasicHillClimbingTraversator implements Traversator{
	private Nade goal;
	
	public BasicHillClimbingTraversator(Nade goal){
		this.goal = goal;
	}
	
	public void traverse(Nade[][] maze, Nade node) {
        long time = System.currentTimeMillis();
    	int visitCount = 0;
    	
    	Nade next = null;
		while(node != null){
			node.setVisited(true);	
			visitCount++;
			
			if (node.isGoalNode()){
		        time = System.currentTimeMillis() - time; //Stop the clock
		        TraversatorStats.printStats(node, time, visitCount);
				break;
			}
			
			try { //Simulate processing each expanded node
				Thread.sleep(10);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			

			Nade[] children = node.children(maze);			
			int fnext = Integer.MAX_VALUE;			
			for (int i = 0; i < children.length; i++) {					
				if (children[i].getHeuristic(goal) < fnext){
					next = children[i];
					fnext = next.getHeuristic(goal);	
				}
			}

						
			if (fnext >= node.getHeuristic(goal)){
				System.out.println("Cannot improve on current node " + node.toString() + " \nh(n)=" + node.getHeuristic(goal) + " = Local Optimum...");
				break;
			}
			node = next;	
			next = null;
		}
	}
}