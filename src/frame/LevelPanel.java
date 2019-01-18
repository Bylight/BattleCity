package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import singelton.GameState;

/**
 * ��ʾ�ؿ���� ��ʾ��ǰ�Ĺؿ���
 */
public class LevelPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainFrame frame;// ������
	private String levelStr;// ���������˸�Ĺؿ��ַ���
	private String ready = "";// ׼����ʾ

	/**
	 * �ؿ���幹�췽��
	 * @param frame
	 *            ������
	 */
	public LevelPanel(MainFrame frame) {
		this.frame = frame;
		levelStr = "�ؿ� " + GameState.getGameState().getLevel();// ��ʼ���ؿ��ַ���
		Thread t = new LevelPanelThread();// �����ؿ���嶯���߳�
		t.start();// �����߳�
	}

	/**
	 * ��д��ͼ����
	 */
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);// ʹ�ú�ɫ
		g.fillRect(0, 0, getWidth(), getHeight());// ���һ�������������ĺ�ɫ����
		g.setFont(new Font("����", Font.BOLD, 50));// ���û�ͼ����
		g.setColor(Color.CYAN);// ʹ��cyanɫ
		g.drawString(levelStr, 335, 300);// ���ƹؿ��ַ���
		g.setColor(Color.RED);// ʹ�ú�ɫ
		g.drawString(ready, 335, 400);// ����׼����ʾ
	}

	/**
	 * ��ת��Ϸ���
	 */
	private void gotoGamePanel() {
		System.gc();
		frame.setPanel(new GamePanel(frame));// ��������ת���˹ؿ���Ϸ���
	}

	/**
	 * �ؿ���嶯���߳�
	 *
	 */
	private class LevelPanelThread extends Thread {
		public void run() {
			for (int i = 0; i < 6; i++) {// ѭ��6��
				if (i % 2 == 0) {// ���ѭ��������ż��
					levelStr = "�ؿ�" + GameState.getGameState().getLevel();// �ؿ��ַ���������ʾ
				} else {
					levelStr = "";// �ؿ��ַ�������ʾ�κ�����
				}
				if (i == 4) {// ���ѭ����������
					ready = "׼��!";// ׼����ʾ��ʾ����
				}
				repaint();// �ػ����
				try {
					Thread.sleep(500);// ����0.5��
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			gotoGamePanel();// ��ת����Ϸ���
		}
	}
}
