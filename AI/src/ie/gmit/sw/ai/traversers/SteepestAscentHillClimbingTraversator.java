package ie.gmit.sw.ai.traversers;

import java.util.*;
import ie.gmit.sw.ai.maze.*;
public class SteepestAscentHillClimbingTraversator implements Traversator{
	private Node goal;
	private LinkedList<Node> positions;
	
	public SteepestAscentHillClimbingTraversator(Node goal){
		this.goal = goal;
		positions = new LinkedList<Node>();
	}
	
	public void traverse(Node[][] maze, Node node) {
		LinkedList<Node> queue = new LinkedList<Node>();
		queue.addFirst(node);
		
        long time = System.currentTimeMillis();
    	int visitCount = 0;
    	
		while(!queue.isEmpty()){
			node = queue.poll();
			positions.add(node);
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
			Node[] children = node.children(maze);
			Collections.sort(Arrays.asList(children),(Node current, Node next) -> next.getHeuristic(goal) - current.getHeuristic(goal));
			
			for (int i = 0; i < children.length; i++) {			
				if (children[i] != null && !children[i].isVisited()){
					children[i].setParent(node);
					queue.addFirst(children[i]); //LIFO
				}
			}
		}
	}

	@Override
	public Node getPosition() {
		if (!positions.isEmpty()) {
			return positions.pop();
		} else{
			return null;
		}
	}
}