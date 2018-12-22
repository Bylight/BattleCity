package frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * v0.08
 * 	ͨ�����巽���ö����Directionʵ��tank�ڰ����󰴷����Զ��ƶ�(������ʵ�ֵз�tank�Զ��ƶ�)
 * @author bylight
 *
 */
public class Tank {
	private static final Color TANK_COLOR = Color.RED;
	private static final int MOVE_LENGTH = 5;
	private enum Direction {
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
		Color c = g.getColor();	// c���ڱ���g�ĳ�ʼ��ɫ
		
		move();	//ͨ������ı�tank���� 
		g.setColor(TANK_COLOR);	// ������ɫΪ��
		g.fillOval(x, y, 30, 30);	// ��������Ϊx, y, width, height
		
		g.setColor(c); 	//�ָ�g�ĳ�ʼ��ɫ
	}
	
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
			left = false;
			right = true;
			up = false;
			down = false;
		} else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
			left = true;
			right = false;
			up = false;
			down = false;
		} else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
			left = false;
			right = false;
			up = true;
			down = false;
		} else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
			left = false;
			right = false;
			up = false;
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
}
