package frame;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * v0.03����
 * 	1. ��дpaint()�����������ػ�ʱ�Զ����ã���������̹�˵�ʵ��Բ
 * 	2. ʹ��setBackground()�������ñ�����ɫ
 * @author bylight
 *
 */

@SuppressWarnings("serial")
public class BasicFrame extends Frame {
	
	private static int x_tank = 50;
	private static int y_tank = 50;
	
	// ��������̹�˵�ʵ��Բ
	// gΪǰ��ɫ
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		Color c = g.getColor();	// c���ڱ���g�ĳ�ʼ��ɫ
		
		g.setColor(Color.RED);	// ������ɫΪ��
		g.fillOval(x_tank, y_tank, 30, 30);	// ��������Ϊx, y, width, height
		
		g.setColor(c); 	//�ָ�g�ĳ�ʼ��ɫ
		
		y_tank += 50;
	}

	public void lauchFrame() {
		
		setVisible(true);
		setLocation(400, 300);
		setSize(800, 600);
		//������ı䴰�ڴ�С
		setResizable(false);
		//������ʵ�ֹرմ���
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);//0��ʾ�����˳�
			}
		});
		//���ô��ڱ���
		setTitle("̹�˴�ս");
		//���ñ�����ɫ
		setBackground(Color.GREEN);
	}
	
	private class PaintThread implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				repaint();
				
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
}
