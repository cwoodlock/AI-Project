package ie.gmit.sw.ai;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

import javax.imageio.ImageIO;
import javax.swing.*;
public class GameView extends JPanel implements ActionListener{
	private static final long serialVersionUID = 1L;
	public static final int DEFAULT_VIEW_SIZE = 800;	
	private int cellspan = 5;	
	private int cellpadding = 2;
	private Maze maze;
	private Sprite[] sprites;
	private int enemy_state = 5;
	private Timer timer;
	private int currentRow;
	private int currentCol;
	private boolean zoomOut = false;
	private int imageIndex = -1;
	private int offset = 48; //The number 0 is ASCII 48.
	private Color[] reds = {new Color(255,160,122), new Color(139,0,0), new Color(255, 0, 0)}; //Animate enemy "dots" to make them easier to see
	
	//Used to read in the immages
	private BufferedImage[] images;
	//Set size of the amount of resources I am going to use
	private static final int RESOURCES = 14;
	
	public GameView(Maze maze) throws Exception{
		//Assign images to BufferedImage Array
		init();
		this.maze = maze;
		setBackground(Color.LIGHT_GRAY);
		setDoubleBuffered(true);
		timer = new Timer(300, this);
		timer.start();
	}
	
	public void setCurrentRow(int row) {
		if (row < cellpadding){
			currentRow = cellpadding;
		}else if (row > (maze.size() - 1) - cellpadding){
			currentRow = (maze.size() - 1) - cellpadding;
		}else{
			currentRow = row;
		}
	}

	public void setCurrentCol(int col) {
		if (col < cellpadding){
			currentCol = cellpadding;
		}else if (col > (maze.size() - 1) - cellpadding){
			currentCol = (maze.size() - 1) - cellpadding;
		}else{
			currentCol = col;
		}
	}

	public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D)g;
              
        cellspan = zoomOut ? maze.size() : 5;         
        final int size = DEFAULT_VIEW_SIZE/cellspan;
        g2.drawRect(0, 0, GameView.DEFAULT_VIEW_SIZE, GameView.DEFAULT_VIEW_SIZE);
        
        for(int row = 0; row < cellspan; row++) {
        	for (int col = 0; col < cellspan; col++){  
        		int x1 = col * size;
        		int y1 = row * size;
        		
        		char ch = 'H';
       		
        		if (zoomOut){
        			ch = maze.get(row, col);
        			if (ch >= '5'){
	        			if (row == currentRow && col == currentCol){
	        				g2.setColor(Color.YELLOW);
	        			}else{
	        				g2.setColor(reds[(int) (Math.random() * 3)]);
	        			}
        				g2.fillRect(x1, y1, size, size);
        			}
        		}else{
        			ch = maze.get(currentRow - cellpadding + row, currentCol - cellpadding + col);
        		}
        		
        		if(ch == 'H') { //if equals to hedge
        			imageIndex = 0;
        		} else if(ch == 'S') { //if equals sword
        			imageIndex = 1;
        		}else if(ch == '?') { //if equals help
        			imageIndex = 2;
        		}else if(ch == 'B') { //if equals bomb
        			imageIndex = 3;
        		}else if(ch == 'X') { //if equals h-bomb
        			imageIndex = 4;
        		}else if(ch == 'P') { //if equals player
        			imageIndex = 5;
        		}else if(ch == '1') { //if equals spider black
        			imageIndex = 6;
        		}else if(ch == '2') { //if equals spider blue
        			imageIndex = 7;
        		}else if(ch == '3') { //if equals spider brown
        			imageIndex = 8;
        		}else if(ch == '4') { //if equals spider green
        			imageIndex = 9;
        		}else if(ch == '5') { //if equals spider grey
        			imageIndex = 10;
        		}else if(ch == '6') { //if equals spider orange
        			imageIndex = 11;
        		}else if(ch == '7') { //if equals spider red
        			imageIndex = 12;
        		}else if(ch == '8') { //if equals spider yellow
        			imageIndex = 13;
        		}else if(ch == 'E') { //if equals exit
        			imageIndex = 14;
        		}
        		
        		
        		if (imageIndex < 0){
        			g2.setColor(Color.LIGHT_GRAY);//Empty cell
        			g2.fillRect(x1, y1, size, size);   			
        		}else{
        			g2.drawImage(sprites[imageIndex].getNext(), x1, y1, null);
        		}
        	}
        }
	}
	
	public void toggleZoom(){
		zoomOut = !zoomOut;		
	}

	public void actionPerformed(ActionEvent e) {	
		if (enemy_state < 0 || enemy_state == 5){
			enemy_state = 6;
		}else{
			enemy_state = 5;
		}
		this.repaint();
	}
	
	public void setSprites(Sprite[] sprites){
		this.sprites = sprites;
	}
	
	//Adapted from https://stackoverflow.com/questions/8605262/loading-images-in-array-java
	private void init() throws Exception{
		
		
		images = new BufferedImage[RESOURCES];
		
		images[0] = ImageIO.read(new java.io.File("resources/images/objects/hedge.png"));
		images[1] = ImageIO.read(new java.io.File("resources/images/objects/sword.png"));
		images[2] = ImageIO.read(new java.io.File("resources/images/objects/help.png"));
		images[3] = ImageIO.read(new java.io.File("resources/images/objects/bomb.png"));
		images[4] = ImageIO.read(new java.io.File("resources/images/objects/h_bomb.png"));
		images[5] = ImageIO.read(new java.io.File("resources/images/player/d1.png"));
		images[6] = ImageIO.read(new java.io.File("resources/images/spiders/black_spider_1.png"));
		images[7] = ImageIO.read(new java.io.File("resources/images/spiders/blue_spider_1.png"));
		images[8] = ImageIO.read(new java.io.File("resources/images/spiders/brown_spider_1.png"));
		images[9] = ImageIO.read(new java.io.File("resources/images/spiders/green_spider_1.png"));
		images[10] = ImageIO.read(new java.io.File("resources/images/spiders/grey_spider_1.png"));
		images[11] = ImageIO.read(new java.io.File("resources/images/spiders/orange_spider_1.png"));
		images[12] = ImageIO.read(new java.io.File("resources/images/spiders/red_spider_1.png"));
		images[13] = ImageIO.read(new java.io.File("resources/images/spiders/yellow_spider_1.png"));
		images[14] = ImageIO.read(new java.io.File("resources/images/objects/exit.png"));

	}
}