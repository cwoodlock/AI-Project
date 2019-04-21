package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ie.gmit.sw.ai.maze.MazeGenerator;
import ie.gmit.sw.ai.maze.MazeGeneratorFactory;
import ie.gmit.sw.ai.maze.MazeView;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.*;

import java.util.Random;
public class GameRunner implements KeyListener{
	private static final int MAZE_DIMENSION = 100;
	private static final int IMAGE_COUNT = 14;
	
	private ControlledSprite player;
	
	private GameView view;
	private Maze model;
	
	private int currentRow;
	private int currentCol;
	
	private Random rand = new Random(); //used to generate a random number
	
	//Player info
	private int maxHealth = 20;
	private int maxStrength = 10;
	private int currentHealth = 20;
	private int currentStrength = 5;
	
	//Node info
	private Node[][] maze;
	private Node goal;
	
	public GameRunner() throws Exception{
		//MazeGeneratorFactory factory = MazeGeneratorFactory.getInstance();
		//MazeGenerator generator = factory.getMazeGenerator(MazeGenerator.GeneratorAlgorithm.RecursiveBacktracker, MAZE_DIMENSION, MAZE_DIMENSION);
		
		Maze m = new Maze(MAZE_DIMENSION);


		maze = m.getMaze();
		view = new GameView(maze, goal);
    	
    	Sprite[] sprites = getSprites();
    	view.setSprites(sprites);
    	
    	placePlayer();
    	
    	Dimension d = new Dimension(GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
    	view.setPreferredSize(d);
    	view.setMinimumSize(d);
    	view.setMaximumSize(d);
    	
    	JFrame f = new JFrame("GMIT - B.Sc. in Computing (Software Development) by Colm Woodlock");
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.addKeyListener(this);
        f.getContentPane().setLayout(new FlowLayout());
        f.add(view);
        f.setSize(1000,1000);
        f.setLocation(100,100);
        f.pack();
        f.setVisible(true);
        
        Traversator t = new BestFirstTraversator(goal);
        
        t.traverse(maze, maze[0][0]);
	}
	
	
	//The player is placed in the game here 
	private void placePlayer(){   	
    	currentRow = (int) (MAZE_DIMENSION * Math.random());
    	currentCol = (int) (MAZE_DIMENSION * Math.random());
    	maze[currentRow][currentCol].setState('5'); //Player is at index 5
    	updateView(); 		
	}
	
	//Draw new position on window
	private void updateView(){
		view.setCurrentRow(currentRow);
		view.setCurrentCol(currentCol);
	}

	//Key press events/Game Controls
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_RIGHT && currentCol < MAZE_DIMENSION - 1) {
        	if (isValidMove(currentRow, currentCol + 1)){
				player.setDirection(Direction.RIGHT);
				currentCol++;  //Move player Right
        	}   		
        }else if (e.getKeyCode() == KeyEvent.VK_LEFT && currentCol > 0) {
        	if (isValidMove(currentRow, currentCol - 1)) {
				player.setDirection(Direction.LEFT);
				currentCol--;	//Move Player Left
			}
        }else if (e.getKeyCode() == KeyEvent.VK_UP && currentRow > 0) {
        	if (isValidMove(currentRow - 1, currentCol)) {
				player.setDirection(Direction.UP);
				currentRow--; //Move Player Up
			}
        }else if (e.getKeyCode() == KeyEvent.VK_DOWN && currentRow < MAZE_DIMENSION - 1) {
        	if (isValidMove(currentRow + 1, currentCol)){
        		player.setDirection(Direction.DOWN);
				currentRow++; //Move Player Down
        	}         	  	
        }else if (e.getKeyCode() == KeyEvent.VK_Z){
        	view.toggleZoom(); //Toggle zoom in and out
        }else if(e.getKeyCode() == KeyEvent.VK_ESCAPE){
        	System.exit(0);	 //Exit the game
        }else if(e.getKeyCode() == KeyEvent.VK_I) {
        	JOptionPane.showMessageDialog(null, "Info: \n Current Health: " + currentHealth + "\n Current Strength: " + currentStrength);
        }else {
        	return;
        }
        
        updateView();       
    }
    public void keyReleased(KeyEvent e) {} //Ignore
	public void keyTyped(KeyEvent e) {} //Ignore
   
	private boolean isValidMove(int row, int col){
		
		if (row <= maze.length - 1 && col <= maze.length - 1 && maze[row][col].getState() == ' '){
			maze[currentRow][currentCol].setState(' ');
			maze[row][col].setState('5');
			return true;
		}else{
			{//Adapted from https://www.mkyong.com/swing/java-swing-how-to-make-a-confirmation-dialog/
			 //Adapted from https://stackoverflow.com/questions/5887709/getting-random-numbers-in-java
				if (JOptionPane.showConfirmDialog(null, "Would you like to interact with this item?", "WARNING",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
	        	{
	        	    // If user clicks yes and it is a question mark 
					if(row <= model.size() - 1 && col <= model.size() - 1 && maze[row][col].getState() == '\u0032')
					{
						int n = rand.nextInt(2);
						if(n == 0) {
							JOptionPane.showMessageDialog(null, "Press Esc to exit the game");
							maze[row][col].setState('0');;
						}else if(n== 1) {
							JOptionPane.showMessageDialog(null, "Use Arrow buttons to navigate");
							maze[row][col].setState('0');;
						}else if(n == 2) {
							JOptionPane.showMessageDialog(null, "Press Z to zoom the map");
							maze[row][col].setState('0');;
						}
					
					//if it is a sword
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && maze[row][col].getState() == '\u0031') {
						JOptionPane.showMessageDialog(null, "Your attack has increased!");
						if(currentStrength < maxStrength) {
							currentStrength++;
						}
						maze[row][col].setState('0');;
						
					//if it is a bomb	
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && maze[row][col].getState() == '\u0033'){
						JOptionPane.showMessageDialog(null, "Boom");
						currentHealth -= 5;
						isAlive(currentHealth);
						maze[row][col].setState('0');;
						
					//If it is a H-Bomb	
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && maze[row][col].getState() == '\u0034') {
						JOptionPane.showMessageDialog(null, "BOOM!");
						currentHealth -= 10;
						isAlive(currentHealth);
						maze[row][col].setState('0');;
						
						//If it is a Black Spider	
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && maze[row][col].getState() == '\u0036') {
						JOptionPane.showMessageDialog(null, "Black Spider!");
						maze[row][col].setState('\u0020');
						
						//If it is a Blue Spider
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && maze[row][col].getState() == '\u0037') {
						JOptionPane.showMessageDialog(null, "Blue Spider!");
						maze[row][col].setState('\u0020');
						
						//If it is a Brown Spider	
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && maze[row][col].getState() == '\u0038') {
						JOptionPane.showMessageDialog(null, "Brown Spider!");
						maze[row][col].setState('\u0020');
						
						//If it is a Green Spider	
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && maze[row][col].getState() == '\u0039') {
						JOptionPane.showMessageDialog(null, "Green Spider!");
						maze[row][col].setState('\u0020');
						
						//If it is a Grey Spider	
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && maze[row][col].getState() == '\u003A') {
						JOptionPane.showMessageDialog(null, "Grey Spider!");
						maze[row][col].setState('\u0020');
						
						//If it is a Orange	Spider
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && maze[row][col].getState() == '\u003B') {
						JOptionPane.showMessageDialog(null, "Orange Spider!");
						maze[row][col].setState('\u0020');
						
						//If it is a Red Spider	
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && maze[row][col].getState() == '\u003C') {
						JOptionPane.showMessageDialog(null, "Red Spider!");
						maze[row][col].setState('\u0020');
						
						//If it is a Yellow Spider	
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && maze[row][col].getState() == '\u003D') {
						JOptionPane.showMessageDialog(null, "Yellow Spider!");
						maze[row][col].setState('\u0020');
						
					}else{
						// removes block in front of the character
						maze[row][col].setState('\u0020');
					    JOptionPane.showMessageDialog(null, "Item Destroyed");
					}
	        	}else{
	        	    // If user clicks no do nothing
	        	}
			}
			return false; //Can't move


		}
	}
	
	private Sprite[] getSprites() throws Exception{
		//Read in the images from the resources directory as sprites. Note that each
		//sprite will be referenced by its index in the array, e.g. a 3 implies a Bomb...
		//Ideally, the array should dynamically created from the images... 
		
		player = new ControlledSprite("Main Player", 3, "resources/images/player/d1.png", "resources/images/player/d2.png", "resources/images/player/d3.png", "resources/images/player/l1.png", "resources/images/player/l2.png", "resources/images/player/l3.png", "resources/images/player/r1.png", "resources/images/player/r2.png", "resources/images/player/r3.png");
		
		Sprite[] sprites = new Sprite[IMAGE_COUNT];
		sprites[0] = new Sprite("Hedge", 1, "resources/images/objects/hedge.png");
		sprites[1] = new Sprite("Sword", 1, "resources/images/objects/sword.png");
		sprites[2] = new Sprite("Help", 1, "resources/images/objects/help.png");
		sprites[3] = new Sprite("Bomb", 1, "resources/images/objects/bomb.png");
		sprites[4] = new Sprite("Hydrogen Bomb", 1, "resources/images/objects/h_bomb.png");
		sprites[5] = player;
		sprites[6] = new Sprite("Black Spider", 2, "resources/images/spiders/black_spider_1.png", "resources/images/spiders/black_spider_2.png");
		sprites[7] = new Sprite("Blue Spider", 2, "resources/images/spiders/blue_spider_1.png", "resources/images/spiders/blue_spider_2.png");
		sprites[8] = new Sprite("Brown Spider", 2, "resources/images/spiders/brown_spider_1.png", "resources/images/spiders/brown_spider_2.png");
		sprites[9] = new Sprite("Green Spider", 2, "resources/images/spiders/green_spider_1.png", "resources/images/spiders/green_spider_2.png");
		sprites[10] = new Sprite("Grey Spider", 2, "resources/images/spiders/grey_spider_1.png", "resources/images/spiders/grey_spider_2.png");
		sprites[11] = new Sprite("Orange Spider", 2, "resources/images/spiders/orange_spider_1.png", "resources/images/spiders/orange_spider_2.png");
		sprites[12] = new Sprite("Red Spider", 2, "resources/images/spiders/red_spider_1.png", "resources/images/spiders/red_spider_2.png");
		sprites[13] = new Sprite("Yellow Spider", 2, "resources/images/spiders/yellow_spider_1.png", "resources/images/spiders/yellow_spider_2.png");
		return sprites;
	}
	
	private boolean isAlive(int health) {
		if(health > 0) {
			return true;
		} else {
			JOptionPane.showMessageDialog(null, "You Died! Game Over!");
			System.exit(0);
			return false;
		}
	}
	
	public static void main(String[] args) throws Exception{
		new GameRunner();
	}
}