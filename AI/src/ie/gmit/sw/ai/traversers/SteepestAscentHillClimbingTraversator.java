package ie.gmit.sw.ai.traversers;

import java.util.*;
import ie.gmit.sw.ai.maze.*;
public class SteepestAscentHillClimbingTraversator implements Traversator{
	private Nade goal;
	
	public SteepestAscentHillClimbingTraversator(Nade goal){
		this.goal = goal;
	}
	
	public void traverse(Nade[][] maze, Nade node) {
		LinkedList<Nade> queue = new LinkedList<Nade>();
		queue.addFirst(node);
		
        long time = System.currentTimeMillis();
    	int visitCount = 0;
    	
		while(!queue.isEmpty()){
			node = queue.poll();
			visitCount++;
			node.setVisited(true);		
			
			if (node.isGoalNode()){
		        time = System.currentTimeMillis() - time; //Stop the clock
		        TraversatorStats.printStats(node, time, visitCount);
				break;
			}
			
			try { //Simulate processing each expanded node
				Thread.sleep(1);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			//Sort the children of the current node in order of increasing h(n)
			Nade[] children = node.children(maze);
			Collections.sort(Arrays.asList(children),(Nade current, Nade next) -> next.getHeuristic(goal) - current.getHeuristic(goal));
			
			for (int i = 0; i < children.length; i++) {			
				if (children[i] != null && !children[i].isVisited()){
					children[i].setParent(node);
					queue.addFirst(children[i]); //LIFO
				}
			}
		}
	}
}