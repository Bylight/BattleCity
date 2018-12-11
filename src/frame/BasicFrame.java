package frame;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * v0.03����
 * 	1. ��дpaint()������������̹�˵�ʵ��Բ
 * 	2. ʹ��setBackground()�������ñ�����ɫ
 * @author javen
 *
 */

@SuppressWarnings("serial")
public class BasicFrame extends Frame {
	// ��������̹�˵�ʵ��Բ
	// gΪǰ��ɫ
	@Override
	public void print(Graphics g) {
		// TODO Auto-generated method stub
		Color c = g.getColor();	// c���ڱ���g�ĳ�ʼ��ɫ
		
		g.setColor(Color.RED);	// ������ɫΪ��
		g.fillOval(50, 50, 30, 30);	// ��������Ϊx, y, width, height
		
		g.setColor(c); 	//�ָ�g�ĳ�ʼ��ɫ
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
	
}
