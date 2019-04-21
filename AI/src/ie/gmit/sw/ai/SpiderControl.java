package ie.gmit.sw.ai;

import java.util.LinkedList;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;

import ie.gmit.sw.ai.maze.Node;

public class SpiderControl {
	private Node goalNode;
	private Node[][] globalNode;
	private LinkedList<Spider> spiders;
	private Timer timer;
	private int spiderNum;
	
	public SpiderControl(Node goalNode, Node[][] globMaze) {
		spiders = new LinkedList<Spider>();
		this.goalNode = goalNode;
		this.globalNode = globMaze;

		timer = new Timer();
		
		spiderNum = 20;
	}
	
	public void createSpiders(){
		
		for (int i = 0; i < spiderNum; i++) {
			spiders.add(new Spider(randPos(), randPos(), globalNode, i % Spider.MAX_SPIDER_LEVEL));
		}
		
		
	}
	
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
