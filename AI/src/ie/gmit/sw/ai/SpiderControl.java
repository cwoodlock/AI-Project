package ie.gmit.sw.ai;

import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import ie.gmit.sw.ai.maze.Node;

public class SpiderControl {
	//Current goal node to pass into traverser
	private Node goalNode;
	//Maze represented by a node array
	private Node[][] globalNode;
	//Linked list of all the spiders on the maze
	private LinkedList<Spider> spiders;
	//A timer to be used
	private Timer timer;
	//The number of spiders that will be spawned
	private int spiderNum;
	
	//Constructor for the class
	public SpiderControl(Node goalNode, Node[][] globMaze) {
		spiders = new LinkedList<Spider>();
		this.goalNode = goalNode;
		this.globalNode = globMaze;
		//Creates the new timer
		timer = new Timer();
		//Assignes a value to the amount of spiders to generate
		spiderNum = 20;
	}
	
	//Method to create the spiders
	public void createSpiders(){
		//Add spiders of varying levels to the linked list the levels determine the type of traversals they use
		for (int i = 0; i < spiderNum; i++) {
			spiders.add(new Spider(randPos(), randPos(), globalNode, i % Spider.MAX_SPIDER_LEVEL));
		}
		//Set their patroling time
		for (Spider spider : spiders) {
			timer.schedule(spider, spider.getDuration(), spider.getDuration());
		}
		
		
	}
	
	//Generate a random position to spawn the spiders at
	private Node randPos(){
		boolean foundPos = false;
		int row = 0;
		int col = 0;

		do {
			row = (int) (globalNode.length * Math.random());
			col = (int) (globalNode.length * Math.random());

			if (globalNode[row][col].getState() != 'W') {
				foundPos = true;
			}
		} while (!foundPos);

		return new Node(row, col);	
	}
}
