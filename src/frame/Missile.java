package frame;

import java.awt.Color;
import java.awt.Graphics;

/**
 * v0.10
 * 	新增了Missile类，实现了draw()和move()
 * @author bylight
 *
 */
public class Missile {
	private static final Color TANK_COLOR = Color.RED;
	private static final int MOVE_LENGTH = 10;
	
	private Tank.Direction dir;
	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = false;
	private int x;
	private int y;
	
	public Missile(int x, int y, Tank.Direction dir){
		this.x = x;
		this.y = y;
		this.dir = dir;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();
		g.setColor(Color.BLACK);
		g.fillOval(x, y, 10, 10);
		g.setColor(c);
		
		move();
	}
	
	private void move() {
		switch (dir) {
		case UP:
			y -= MOVE_LENGTH;
			break;
		case DOWN:
			y += MOVE_LENGTH;
			break;
		case LEFT:
			x -= MOVE_LENGTH;
			break;
		case RIGHT:
			x += MOVE_LENGTH;
			break;
		case STOP:
			break;
		}
	}
}
