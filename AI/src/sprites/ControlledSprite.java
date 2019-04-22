/*Colm Woodlock
 * G00341460
 * Adapted from base project and labs from AI module
 */
package sprites;

import javax.imageio.*;
import javax.swing.JOptionPane;

import java.awt.image.*;
public class ControlledSprite extends Sprite{	
	
	private int health;
	
	public ControlledSprite(String name, int frames, String... images) throws Exception{
		super(name, frames, images);
		health = 100;
	}
	
	public void setDirection(Direction d){
		switch(d.getOrientation()) {
		case 0: case 1:
			super.setImageIndex(0); //UP or DOWN
			break;
		case 2:
			super.setImageIndex(1);  //LEFT
			break;
		case 3:
			super.setImageIndex(2);  //LEFT
		default:
			break; //Ignore...
		}		
	}

	public int getHealth() {
		return health;
	}
	
	public void setHealth(int health) {
		this.health = health;
	}
	
	public void incHealth(int incAmount) {
		health += incAmount;
	}
	
	public void decHealth(int decAmount) {
		health -= decAmount;
	}
	
	public void kill(){
		JOptionPane.showMessageDialog(null, "You Died! Game Over!");
		System.exit(0);
	}
}