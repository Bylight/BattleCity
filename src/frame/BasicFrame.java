package frame;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * v0.09
 * 	��keyMoniitor()�����keyReleased()��������ɿ�ʱͣ��tank
 * @author bylight
 *
 */

@SuppressWarnings("serial")
public class BasicFrame extends Frame {
	private static final int FRAME_WIDTH = 800;
	private static final int FRAME_HEIGHT = 600;
	private static final Color BACKGROUND_COLOR = Color.WHITE;
	private static int x_tank = 50;
	private static int y_tank = 50;
	//���⻺��
	private Image offScreenImage = null;
	
	private Tank myTank = new Tank(x_tank, y_tank);
	
	// ��������̹�˵�ʵ��Բ
	// gΪǰ��ɫ
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		myTank.draw(g);
	}

	public void lauchFrame() {
		
		setLocation(400, 300);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
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
		setBackground(BACKGROUND_COLOR);
		
		//Runnable�ӿ�ʵ�����ʵ����ΪThread��target��������Thread
		new Thread(new PaintThread()).start();
		
		this.addKeyListener(new KeyMonitor());
		
		setVisible(true);
		
	}
	
	//��дupdate
	public void update(Graphics g) {
		//��ʼ��offScreenImage
		if (offScreenImage == null) {
			offScreenImage = this.createImage(FRAME_WIDTH, FRAME_HEIGHT);
		}
		//��ȡoffScreenImage�Ļ���
		Graphics gOffScreen = offScreenImage.getGraphics();
		//�޸Ļ�����ɫΪ����ɫ
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(BACKGROUND_COLOR);
		//�ػ�ǰˢ�±���
		gOffScreen.fillRect(0, 0, FRAME_WIDTH , FRAME_HEIGHT );
		//��ԭ������ɫ
		gOffScreen.setColor(c);
		//����ȡ�Ļ��ʴ���paint(ʹ��offScreenImage�Ļ���
		//����offScreenImage�Ͻ���paint)
		paint(gOffScreen);
		
		//����g�����ڻ�ǰ̨����
		g.drawImage(offScreenImage, 0, 0, null);
	}

	//ʵ��Runnable�ӿڵ��ڲ���
	private class PaintThread implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				//����Frame��repaint()
				//Frame��ʵ�ʵ���˳��repaint()->update()->paint()
				repaint();
				
				//ͨ��sleep�������̶߳�ʱ����
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	//�̳�KeyAdpater�ļ��̼�����
	//Ҳ����ͨ��ʵ��KeyListener�ӿڣ���KeyListener�ӿ�����������������ʵ�֣��ϲ����
	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			myTank.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			myTank.keyPressed(e);
		}
		
	}
}