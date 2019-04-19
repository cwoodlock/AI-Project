package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

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
		model = new Maze(MAZE_DIMENSION);
    	view = new GameView(model);
    	
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
    	model.set(currentRow, currentCol, '5'); //Player is at index 5
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
		
		if (row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == ' '){
			model.set(currentRow, currentCol, '\u0020'); // u0020 is a blank space, replace last position with blank space
			model.set(row, col, '5');
			return true;
		}else{
			{//Adapted from https://www.mkyong.com/swing/java-swing-how-to-make-a-confirmation-dialog/
			 //Adapted from https://stackoverflow.com/questions/5887709/getting-random-numbers-in-java
				if (JOptionPane.showConfirmDialog(null, "Would you like to interact with this item?", "WARNING",JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION)
	        	{
	        	    // If user clicks yes and it is a question mark 
					if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0032')
					{
						int n = rand.nextInt(2);
						if(n == 0) {
							JOptionPane.showMessageDialog(null, "Press Esc to exit the game");
							model.set(row, col, '0');
						}else if(n== 1) {
							JOptionPane.showMessageDialog(null, "Use Arrow buttons to navigate");
							model.set(row, col, '0');
						}else if(n == 2) {
							JOptionPane.showMessageDialog(null, "Press Z to zoom the map");
							model.set(row, col, '0');
						}
					
					//if it is a sword
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0031') {
						JOptionPane.showMessageDialog(null, "Your attack has increased!");
						if(currentStrength < maxStrength) {
							currentStrength++;
						}
						model.set(row, col, '0');
						
					//if it is a bomb	
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0033'){
						JOptionPane.showMessageDialog(null, "Boom");
						currentHealth -= 5;
						isAlive(currentHealth);
						model.set(row, col, '0');
						
					//If it is a H-Bomb	
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0034') {
						JOptionPane.showMessageDialog(null, "BOOM!");
						currentHealth -= 10;
						isAlive(currentHealth);
						model.set(row, col, '0');
						
						//If it is a Black Spider	
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0036') {
						JOptionPane.showMessageDialog(null, "Black Spider!");
						model.set(row, col, '\u0020');
						
						//If it is a Blue Spider
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0037') {
						JOptionPane.showMessageDialog(null, "Blue Spider!");
						model.set(row, col, '\u0020');
						
						//If it is a Brown Spider	
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0038') {
						JOptionPane.showMessageDialog(null, "Brown Spider!");
						model.set(row, col, '\u0020');
						
						//If it is a Green Spider	
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u0039') {
						JOptionPane.showMessageDialog(null, "Green Spider!");
						model.set(row, col, '\u0020');
						
						//If it is a Grey Spider	
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u003A') {
						JOptionPane.showMessageDialog(null, "Grey Spider!");
						model.set(row, col, '\u0020');
						
						//If it is a Orange	Spider
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u003B') {
						JOptionPane.showMessageDialog(null, "Orange Spider!");
						model.set(row, col, '\u0020');
						
						//If it is a Red Spider	
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u003C') {
						JOptionPane.showMessageDialog(null, "Red Spider!");
						model.set(row, col, '\u0020');
						
						//If it is a Yellow Spider	
					}else if(row <= model.size() - 1 && col <= model.size() - 1 && model.get(row, col) == '\u003D') {
						JOptionPane.showMessageDialog(null, "Yellow Spider!");
						model.set(row, col, '\u0020');
						
					}else{
						// removes block in front of the character
						model.set(row, col, '\u0020');
					    JOptionPane.showMessageDialog(null, "Item Destroyed");
					}
	        	}else{
	        	    // If user clicks no do nothing
	        	}
			}
			return false; //Can't move


		}
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