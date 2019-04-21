package ie.gmit.sw.ai;

import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.BruteForceTraversator;
import ie.gmit.sw.ai.traversers.IDAStarTraversator;
import ie.gmit.sw.ai.traversers.RandomWalk;
import ie.gmit.sw.ai.traversers.RecursiveDFSTraversator;
import ie.gmit.sw.ai.traversers.SteepestAscentHillClimbingTraversator;
import ie.gmit.sw.ai.traversers.Traversator;

public class Help {
	//Current node to pass into traverser
	private Node currentNode;
	private Node newNode;
	//Current goal node to pass into traverser
	private Node goalNode;
	//Maze represented by a node array
	private Node[][] globalNode;
	
	private Traversator traversator;
	
	//Used to determine what type of traverser the siders will use
	private int bookLevel = 0;
	//Set the max number of spider types
	public static int MAX_BOOK_LEVEL = 4;
	//Set the maximum length of a path that could be painted
	public static int MAX_PATH = 20;
	
	public Help(Node currentNode, Node goalNode, Node[][] globalNode, int bookLevel) {
		super();
		this.currentNode = currentNode;
		this.goalNode = goalNode;
		this.globalNode = globalNode;
		this.bookLevel = bookLevel;
		
		createBookTraversers();
	}

	private void createBookTraversers() {
		// TODO Auto-generated method stub
		//create different book traversers depending on their level higher the level the bette the traverser
				if(bookLevel == 0) {
					traversator = new RecursiveDFSTraversator();
				}else if(bookLevel == 1) {
					traversator = new BruteForceTraversator(true);
				}else if(bookLevel == 2) {
					traversator = new SteepestAscentHillClimbingTraversator(goalNode);
				}else if(bookLevel == 3) {
					traversator = new IDAStarTraversator(goalNode);
				}else {
					return;
				}
				
	}
	
	
	public void pathPainter(){
		for (int i = 0; i < MAX_PATH; i++) {
			//This needs to get the current positon 
			Node pathNode = null;
			if (pathNode != null) {
				globalNode[pathNode.getRow()][pathNode.getCol()].setHasHelpPath(true);
			}
			else{
				break;
			}
			
		}
	}
}
