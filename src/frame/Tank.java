package frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * v0.11
 * 	��ӶԿո�ͻس��ļ����������˹��췽����ʵ��Missile�����new�ʹ���
 * 	�������⣺Missile����ȡ����tank���򣬶�tankδ�ƶ�ʱ����Ϊstop
 * @author bylight
 *
 */
public class Tank {
	private static final Color TANK_COLOR = Color.RED;
	private static final int MOVE_LENGTH = 5;
	private static final int WIDTH = 30;
	private static final int HEIGHT = 30;
	
	private BasicFrame basicFrame;
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
	
	public Tank(int x, int y, BasicFrame basicFrame) {
		this(x, y);
		this.basicFrame = basicFrame;
	}
	
	public void draw(Graphics g) {
		Color c = g.getColor();	// c���ڱ���g�ĳ�ʼ��ɫ
		
		move();	//ͨ������ı�tank���� 
		g.setColor(TANK_COLOR);	// ������ɫΪ��
		g.fillOval(x, y, WIDTH, HEIGHT);	// ��������Ϊx, y, width, height
		
		g.setColor(c); 	//�ָ�g�ĳ�ʼ��ɫ
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
		} else if (key == KeyEvent.VK_SPACE || key == KeyEvent.VK_ENTER) {
			if (basicFrame != null) {
				basicFrame.setMissile(setFire());	
			}
		}
		setDirection();
	}
	
	private Missile setFire() {
		// TODO Auto-generated method stub
		Missile myMissile = new Missile(WIDTH / 2 + x, HEIGHT / 2 + y, dir);
		return myMissile;
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
