package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ie.gmit.sw.ai.maze.MazeGenerator;
import ie.gmit.sw.ai.maze.MazeGeneratorFactory;
import ie.gmit.sw.ai.maze.MazeView;
import ie.gmit.sw.ai.maze.Node;
import ie.gmit.sw.ai.traversers.*;
import net.sourceforge.jFuzzyLogic.FIS;
import net.sourceforge.jFuzzyLogic.FunctionBlock;
import net.sourceforge.jFuzzyLogic.plot.JFuzzyChart;
import net.sourceforge.jFuzzyLogic.rule.Variable;

import java.util.Random;
public class GameRunner implements KeyListener{
	private static final int MAZE_DIMENSION = 100;
	private static final int IMAGE_COUNT = 14;
	
	private ControlledSprite player;
	private SpiderControl spider;
	
	private GameView view;
	private Maze model;
	
	private int currentRow;
	private int currentCol;
	
	private Random rand = new Random(); //used to generate a random number
	
	
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
    	init();
    	
    	placePlayer();
    	
    	
    	spider = new SpiderControl(new Node(currentRow, currentCol), maze);
    	spider.createSpiders();
    	
    	this.goal = m.getGoal();
		Random random = new Random();
		int help = random.nextInt(4);
       
	}
	
	private void init() {
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
        }else {
        	return;
        }
        
        updateView();
        
        //Fuzzy logic for fight with enemy
        fuzzyFight();
    }
    private void fuzzyFight() {
		// TODO Auto-generated method stub
    	if (maze[currentRow][currentCol].isHasSpider()) {
			// Load fcl
			String fileName = "fcl/strenght.fcl";
			FIS fis = FIS.load(fileName, true);

			// Error handling
			if (fis == null) {
				System.err.println("Can't load file: '" + fileName + "'");
				return;
			}

			FunctionBlock functionBlock = fis.getFunctionBlock("fight");
			Random random = new Random();
			Spider spider = maze[currentRow][currentCol].getSpider();

			// PLAYER SCORE
			fis.setVariable("health", player.getHealth() / 10);
			//Generate and assign the luck value for the player
			int luck = random.nextInt(10);
			fis.setVariable("luck", luck);
			
			// Evaluate
			fis.evaluate();
			
			// Show output variable's chart
			Variable outcome = functionBlock.getVariable("result");
			//Assign the outcome to a variable
			int damage = (int) outcome.getValue();
			//Print out damage dealt so user can see
			System.out.println("Player dealt " + damage);
			//Decrease spiders health
			spider.decHealth(damage);

			// ENEMY SCORE
			fis.setVariable("health", spider.getHealth() / 10);
			//Generate and assign the luck value for the enemy
			luck = random.nextInt(10);
			fis.setVariable("luck", luck);
			
			// Evaluate
			fis.evaluate();
			
			// Show output variable's chart
			outcome = functionBlock.getVariable("result");
			//Assign the outcome to a variable
			damage = (int) outcome.getValue();
			//Print out damage delt so user can see
			System.out.println("Enemy dealt " + damage);
			//Decrease players health
			player.decHealth(damage);

			// AFTER FIGHT
			System.out.println("Player health: " + player.getHealth());
			System.out.println("Enemy health: " + spider.getHealth());
			
			//Run checks to see if player or spider are dead
			if (player.getHealth() <= 0) {
				player.kill();	
			}
			
			if (spider.getHealth() <= 0) {
				spider.kill();
			}
		}
	}


	public void keyReleased(KeyEvent e) {} //Ignore
	public void keyTyped(KeyEvent e) {} //Ignore
   
	private boolean isValidMove(int row, int col){
		
		if (row <= maze.length - 1 && col <= maze.length - 1 && maze[row][col].getState() == ' '){
			maze[currentRow][currentCol].setState(' ');
			maze[row][col].setState('5');
			return true;
		}else {	
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
	
	public static void main(String[] args) throws Exception{
		new GameRunner();
	}
}