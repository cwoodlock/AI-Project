package ie.gmit.sw.ai.maze;

import java.awt.Color;

import ie.gmit.sw.ai.Help;
import ie.gmit.sw.ai.Spider;
public class Node {
	public enum NodeType {Wall, Passage};
	public enum Direction {North, South, East, West, None};
	private static final int MAX_EXITS = 4;
	private Node parent;
	private Direction direction = Direction.None;
	private NodeType type = NodeType.Wall;
	private Color color = Color.BLACK;
	private Direction[] paths = null;
	public boolean visited =  false;
	public boolean goal;
	private int row = -1;
	private int col = -1;
	private int distance;
	
	private char state;
	
	private Spider spider = null;
	private Help help = null;

	private boolean visitedByPlayer = false;
	private boolean helpOnTile = false;
	private boolean hasHelpPath = false;
	private boolean hasSpider = false;
	private boolean pathToGoal = false;
	
	public boolean isPathToGoal() {
		return pathToGoal;
	}

	public void setPathToGoal(boolean pathToGoal) {
		this.pathToGoal = pathToGoal;
	}

	public boolean isHelpOnTile() {
		return helpOnTile;
	}
	
	public void setHelpOnTile(boolean helpOnTile) {
		this.helpOnTile = helpOnTile;
	}
	
	public boolean isHasHelpPath() {
		return hasHelpPath;
	}

	public void setHasHelpPath(boolean hasHelpPath) {
		this.hasHelpPath = hasHelpPath;
	}

	public Help getHelp() {
		return help;
	}

	public boolean isHasSpider() {
		return hasSpider;
	}

	public boolean isVisitedByPlayer() {
		return visitedByPlayer;
	}

	public void setVisitedByPlayer(boolean visitedByPlayer) {
		this.visitedByPlayer = visitedByPlayer;
	}

	public void setHasSpider(boolean hasSpider) {
		this.hasSpider = hasSpider;
	}

	public void setHelp(Help help) {
		this.help = help;
	}
	
	public Spider getSpider() {
		return spider;
	}

	public void setSpider(Spider spider) {
		this.spider = spider;
	}

	public Node(int row, int col) {
		this.row = row;
		this.col = col;
	}
	
	public char getState() {
		return state;
	}

	public void setState(char state) {
		this.state = state;
	}
	
	public int getRow() {
		return row;
	}

	public int getCol() {
		return col;
	}

	public Node getParent() {
		return parent;
	}

	public void setParent(Node parent) {
		this.parent = parent;
	}

	public Color getColor() {
		return color;
	}

	public void setColor(Color color) {
		this.color = color;
	}

	
	public boolean hasDirection(Direction direction){	
		for (int i = 0; i < paths.length; i++) {
			if (paths[i] == direction) return true;
		}
		return false;
	}
	
	public Node[] children(Node[][] maze){		
		Node[] children = new Node[MAX_EXITS];
		if (col - 1 >= 0 && maze[row][col - 1].getState() != '0')
			children[0] = maze[row][col - 1]; // A West edge
		if (col + 1 < maze[row].length && maze[row][col + 1].getState() != '0')
			children[1] = maze[row][col + 1]; // An East Edge
		if (row - 1 >= 0 && maze[row - 1][col].getState() != '0')
			children[2] = maze[row - 1][col]; // A North edge
		if (row + 1 < maze.length && maze[row + 1][col].getState() != '0')
			children[3] = maze[row + 1][col]; // An South Edge

		int counter = 0;
		for (int i = 0; i < children.length; i++) {
			if (children[i] != null)
				counter++;
		}

		Node[] tmp = new Node[counter];
		int index = 0;
		for (int i = 0; i < children.length; i++) {
			if (children[i] != null) {
				tmp[index] = children[i];
				index++;
			}
		}

		return tmp;
	}
	
	public Node[] adjacentNodes(Node[][] maze){
		java.util.List<Node> adjacents = new java.util.ArrayList<Node>();
		
		if (row > 0) adjacents.add(maze[row - 1][col]); //Add North
		if (row < maze.length - 1) adjacents.add(maze[row + 1][col]); //Add South
		if (col > 0) adjacents.add(maze[row][col - 1]); //Add West
		if (col < maze[row].length - 1) adjacents.add(maze[row][col + 1]); //Add East
		
		return (Node[]) adjacents.toArray(new Node[adjacents.size()]);
	}
	
	public void addPath(Direction direction) {
		int index = 0;
		if (paths == null){
			paths = new Direction[index + 1];		
		}else{
			index = paths.length;
			Direction[] temp = new Direction[index + 1];
			for (int i = 0; i < paths.length; i++) temp[i] = paths[i];
			paths = temp;
		}
		
		paths[index] = direction;
	}
	
	public Direction[] getPaths() {
		return paths;
	}
	
	public NodeType getType() {
		return type;
	}

	public void setType(NodeType type) {
		this.type = type;
	}

	public Direction getDirection() {
		return direction;
	}

	public void setPassage(Direction direction) {
		this.direction = direction;
	}

	public boolean isVisited() {
		return visited;
	}

	public void setVisited(boolean visited) {
		this.color = Color.BLUE;
		this.visited = visited;
	}

	public boolean isGoalNode() {
		return goal;
	}

	public void setGoalNode(boolean goal) {
		this.goal = goal;
	}
	
	public int getHeuristic(Node goal){
		double x1 = this.col;
		double y1 = this.row;
		double x2 = goal.getCol();
		double y2 = goal.getRow();
		return (int) Math.sqrt(Math.pow((x2 - x1), 2) + Math.pow((y2 - y1), 2));
	}
	
	public int getPathCost() {
		return distance;
	}

	public void setPathCost(int distance) {
		this.distance = distance;
	}

	public String toString() {
		return "[" + row + "/" + col + "]";
	}
}