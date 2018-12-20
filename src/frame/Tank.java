package frame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyEvent;

/**
 * v0.07
 * 	�����Tank�࣬��Tank����ʵ��draw()��keyPressed()
 * @author bylight
 *
 */
public class Tank {
	private static final Color TANK_COLOR = Color.RED;
	private static final int MOVE_LENGTH = 5;
	private int x;
	private int y;
	public Tank(int x, int y){
		this.x = x;
		this.y = y;
	}
	public void draw(Graphics g) {
		Color c = g.getColor();	// c���ڱ���g�ĳ�ʼ��ɫ
		
		g.setColor(TANK_COLOR);	// ������ɫΪ��
		g.fillOval(x, y, 30, 30);	// ��������Ϊx, y, width, height
		
		g.setColor(c); 	//�ָ�g�ĳ�ʼ��ɫ
	}
	
	public void keyPressed(KeyEvent e) {
		// TODO Auto-generated method stub
		int key = e.getKeyCode();
		if (key == KeyEvent.VK_RIGHT || key == KeyEvent.VK_D) {
			x += MOVE_LENGTH;
		} else if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_A) {
			x -= MOVE_LENGTH;
		} else if (key == KeyEvent.VK_UP || key == KeyEvent.VK_W) {
			y -= MOVE_LENGTH;
		} else if (key == KeyEvent.VK_DOWN || key == KeyEvent.VK_S) {
			y += MOVE_LENGTH;
		}
	}
}
