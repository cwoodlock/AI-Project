package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;

import ie.gmit.sw.ai.maze.Node;
public class GameView extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_VIEW_SIZE = 800;
	
	private int cellspan = 5;	
	private int cellpadding = 2;
	private Node[][] maze;
	
	private Sprite[] sprites;
	
	private int enemy_state = 5;
	private Timer timer;
	private int currentRow;
	private int currentCol;
	private boolean zoomOut = false;
	private int imageIndex = -1;
	private int offset = 48; //The number 0 is ASCII 48.
	private Color[] reds = {new Color(255,160,122), new Color(139,0,0), new Color(255, 0, 0)}; //Animate enemy "dots" to make them easier to see
	private Node goal;
	
	public GameView(Node[][] maze, Node goal) throws Exception{
		this.goal =goal;
		this.maze = maze;
		setBackground(Color.LIGHT_GRAY);
		setDoubleBuffered(true);
		timer = new Timer(300, this);
		timer.start();
	}
	
	public void setCurrentRow(int row) {
		if (row < cellpadding){
			currentRow = cellpadding;
		}else if (row > (maze.length - 1) - cellpadding){
			currentRow = (maze.length - 1) - cellpadding;
		}else{
			currentRow = row;
		}
	}

	public void setCurrentCol(int col) {
		if (col < cellpadding){
			currentCol = cellpadding;
		}else if (col > (maze.length - 1) - cellpadding){
			currentCol = (maze.length - 1) - cellpadding;
		}else{
			currentCol = col;
		}
	}

	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
              
        cellspan = zoomOut ? maze.length : 5;         
        final int size = DEFAULT_VIEW_SIZE/cellspan;
        g2.drawRect(0, 0, GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
        
        for(int row = 0; row < cellspan; row++) {
        	for (int col = 0; col < cellspan; col++){  
        		int x1 = col * size;
        		int y1 = row * size;
        		
        		char ch = '0';
        		boolean tileHasSpider = false;
				boolean tileHasHelpPath = false;
				
				//Check if zoomed out
        		if (zoomOut){
        			//set ch to the current node
        			ch = maze[row][col].getState();
        			//check if there is a spider present
					tileHasSpider = maze[row][col].isHasSpider();
					//Check if there is a help path present
					tileHasHelpPath = maze[row][col].isHasHelpPath();
        			
        		}else{//if not zoomed out
        			//get the state of the current postion including padding 
        			ch = maze[currentRow - cellpadding + row][currentCol - cellpadding + col].getState();
					//Check if there is a spider
        			tileHasSpider = maze[currentRow - cellpadding + row][currentCol - cellpadding + col].isHasSpider();
					//Check if there is a help path
        			tileHasHelpPath = maze[currentRow - cellpadding + row][currentCol - cellpadding + col].isHasHelpPath();
        		}
        		
        		//If a tile has a spider
        		if (tileHasSpider) {
					if (zoomOut) { //and if it is zoomed out
						g2.setColor(Color.red); //paint tile red
						g2.fillRect(x1, y1, size, size);
					} else {
						//Otherwise set the image to paint on the tile to index 6
						imageIndex = 6;
					}
        		
					//Otherwise if the player is present 
	        	} else if (row == currentRow && col == currentCol) {
	        		if (zoomOut) { //and if zoomed out
	        				g2.setColor(Color.BLUE); //paint tile blue
	        				g2.fillRect(x1, y1, size, size);
	        		}
	
	        	} else if (ch == '0') { // if the tile is a hedge
	        		if (zoomOut) { //if zoomed out
	        			g2.setColor(Color.green); //paint hedges green
	        			g2.fillRect(x1, y1, size, size);
	        		} else {
	        			//otherwise set the image to paint on the tile to index 0
	        			imageIndex = 0;
	        		}
	        		
	        	} else if (ch == '\u0031') { //if the tile is a sword
	        		if (zoomOut) { //if zoomed out
	        			g2.setColor(Color.cyan); //paint the tile cyan
	        			g2.fillRect(x1, y1, size, size);
	
	        		} else {
	        			//otherwise set the image to paint on the tile to index 1
	        			imageIndex = 1;
	        		}
	
	        	}else if (ch == '\u0032') { //If tile is a help marker
	        		imageIndex = 2; //Set the image to paint on the tile to index 2
	
	        		if (zoomOut) { //if zoomed out 
	        			g2.setColor(Color.yellow); //paint tile yellow
	        			g2.fillRect(x1, y1, size, size);
	        		}
	        		
	        	} else if (ch == '\u0033') { //if tile is a bomb
	        		imageIndex = 3; // set the image to paint on the tile to index 3
	
	        		if (zoomOut) { //if zoomed out
	        			g2.setColor(Color.black); //paint tile black
	        			g2.fillRect(x1, y1, size, size);
	
	        		}
	        	} else if (ch == '\u0034') { //if the tile has a H-bomb
	        		imageIndex = 4; //set the image to paint on the tile to index 4
	
	        		if (zoomOut) { //if zoomed out
	        			g2.setColor(Color.magenta); //paint the tile magenta
	        			g2.fillRect(x1, y1, size, size);
	        		}
	        		
	        	}else if (ch == ' ' && tileHasHelpPath == false)// if the nose is empty and there isn't a 
	
	        	{
	        		imageIndex = 14; //Set the image to paint on the tile to 14
	
	        		if (zoomOut) { //if zoomed out 
	        			g2.setColor(Color.gray); //paint the path gray
	        			g2.fillRect(x1, y1, size, size);
	
	
	        		}
	        	} else if (ch == 'G')// G for Goal
	        	{
	        		imageIndex = 15; //set goal node to index 15
	        		if (zoomOut) { //if zoomed out
	        			g2.setColor(Color.white); //paint exit white
	        			g2.fillRect(x1, y1, size, size);
	        		}
	        	} else if (ch == '\u0036' || ch == '\u0037' || ch == '\u0038' 
	        			|| ch == '\u0039' || ch == '\u003A' || ch == '\u003B' 
	        			|| ch == '\u003C' || ch == '\u003D') { //if one of the spiders
	        		imageIndex = enemy_state; //assign the index to the enemy state
	        	}
        		
	        	else if (tileHasHelpPath && ch == ' ') { //if user has activated the help path
	        		if (zoomOut) { //and is zoomed out
	        			g2.setColor(Color.YELLOW); //set the path to yellow
	        			g2.fillRect(x1, y1, size, size);
	        		} else {
	        			imageIndex = 16; //set the image to paint on the tile to 16
	        		}
	        	} else {
	        		imageIndex = -1; //reset the image index
	        	}
        		if (!zoomOut) { //if not zoomed out
					paintTile(g2, size, x1, y1); //paint the tile 
				}
        		
        	}
        }
	}
	
	private void paintTile(Graphics2D g2, int size, int x1, int y1) {
		// TODO Auto-generated method stub
		if (imageIndex >= 0) { // if zoomed in
			g2.drawImage(sprites[imageIndex].getNext(), x1, y1, null);
		} else { //if zoomed out
			g2.setColor(Color.LIGHT_GRAY);
			g2.fillRect(x1, y1, size, size);
		}
		
	}

	public void toggleZoom(){
		zoomOut = !zoomOut;		
	}

	public void actionPerformed(ActionEvent e) {	
		if (enemy_state < 0 || enemy_state == 5){
			enemy_state = 6;
		}
		this.repaint();
	}
	
	public void setSprites(Sprite[] sprites){
		this.sprites = sprites;
	}
}