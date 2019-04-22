/*Colm Woodlock
 * G00341460
 * Adapted from base project and labs from AI module
 */
package ie.gmit.sw.ai.sprites;

public enum Direction {
	UP (0), 
	DOWN (1), 
	LEFT (2), 
	RIGHT (3);

	private final int orientation;

    private Direction(int orientation) {
        this.orientation = orientation;
    }
    
    public int getOrientation() {
        return this.orientation;
    }
}