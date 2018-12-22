package frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * v0.09
 * 	添加keyReleased()方法令按键松开时停下tank(可用于让坦克朝多个方向走)
 * @author bylight
 *
 */
public class Tank {
	private static final Color TANK_COLOR = Color.RED;
	private static final int MOVE_LENGTH = 5;
	
	public enum Direction {
		LEFT, RIGHT, UP, DOWN, STOP
	}
	private Direction dir = Direction.STOP;
	private boolean left = false;
	private boolean right = false;
	private boolean up = false;
	private boolean down = false;
	private int x;
	private int y;
	
	public Tank(int x, int y){
		this.x = x;
		this.y = y;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();	// c用于保存g的初始颜色
		
		move();	//通过方向改变tank坐标 
		g.setColor(TANK_COLOR);	// 设置颜色为红
		g.fillOval(x, y, 30, 30);	// 参数依次为x, y, width, height
		
		g.setColor(c); 	//恢复g的初始颜色
	}
	
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
			right = true;
		} else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
			left = true;
		} else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
			up = true;
		} else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
			down = true;
		}
		setDirection();
	}
	
	private void setDirection() {
		// TODO Auto-generated method stub
		if (left && !right && !up && !down) {
			dir = Direction.LEFT;
		} else if (!left && right && !up && !down) {
			dir = Direction.RIGHT;
		} else if (!left && !right && up && !down) {
			dir = Direction.UP;
		} else if (!left && !right && !up && down) {
			dir = Direction.DOWN;
		} else {
			dir = Direction.STOP;
		}	
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
	
	public void keyReleased(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
			right = false;
		} else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
			left = false;
		} else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
			up = false;
		} else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
			down = false;
		}
		setDirection();
	}
}
