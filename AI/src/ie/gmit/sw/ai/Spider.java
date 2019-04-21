package ie.gmit.sw.ai;

import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.BeamTraversator;
import ie.gmit.sw.ai.traversers.BestFirstTraversator;
import ie.gmit.sw.ai.traversers.BruteForceTraversator;
import ie.gmit.sw.ai.traversers.RandomWalk;
import ie.gmit.sw.ai.traversers.RecursiveDFSTraversator;
import ie.gmit.sw.ai.traversers.Traversator;

public class Spider {
	//Current node to pass into traverser
	private Node currentNode;
	private Node newNode;
	//Current goal node to pass into traverser
	private Node goalNode;
	//Maze represented by a node array
	private Node[][] globalNode;
	
	private Traversator traversator;
	
	//Used to store health value
	private double health;
	//Used to determine what type of traverser the siders will use
	private int spiderLevel = 0;
	//Set the max number of spider types
	public static int MAX_SPIDER_LEVEL = 4;
	//How long the spiders will move
	private long duration;
	
	//Constructor
	public Spider(Node currentNode, Node goalNode, Node[][] globalNode, int spiderLevel) {
		super();
		this.currentNode = currentNode;
		this.goalNode = goalNode;
		this.globalNode = globalNode;
		this.spiderLevel = spiderLevel;
		health = 50;
		duration = 1000;
		
		//create The traversers dependig on what level the sider is
		createSpiderTraversers();
	}
	
	private void createSpiderTraversers() {
		//create different spider traversers depending on their level higher the level the bette the traverser
		if(spiderLevel == 0) {
			traversator = new RandomWalk();
		}else if(spiderLevel == 1) {
			traversator = new BruteForceTraversator(true);
		}else if(spiderLevel == 2) {
			traversator = new RecursiveDFSTraversator();
		}else if(spiderLevel == 3) {
			traversator = new BestFirstTraversator(goalNode);
		}else if(spiderLevel == 4) {
			traversator = new BeamTraversator(goalNode,2);
		}else {
			return;
		}
	}

	//Getters and setters
	public double getHealth() {
		return health;
	}
	public void setHealth(double health) {
		this.health = health;
	}
	public int getSpiderLevel() {
		return spiderLevel;
	}
	public void setSpiderLevel(int spiderLevel) {
		this.spiderLevel = spiderLevel;
	}
	
	public void decHealth(int incAmount) {
		health -= incAmount;
	}
	
	public void kill() {
		System.out.println("Killed a spider");
		currentNode.setState('0');
	}
	
	public void move() {
		
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}
	
	
	
	
	
}
