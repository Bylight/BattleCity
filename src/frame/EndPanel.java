package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import singelton.GameState;
import singelton.Properties;

/**
 * ��ʾ�ؿ���� ��ʾ��ǰ�Ĺؿ���
 */
public class EndPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainFrame frame;// ������
	private String levelStr;// ���������˸�Ĺؿ��ַ���
	private String ready = "";// ׼����ʾ

	/**
	 * @param frame
	 *            ������
	 */
	public EndPanel(MainFrame frame) {
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
		g.setColor(Color.CYAN);// ʹ��CYANɫ
		g.drawString("���һ", 150, 200);// �����������
		g.setColor(Color.RED);// ʹ�ú�ɫ
		g.drawString("������:" + GameState.getGameState().getLevel(), 120, 320);
		g.drawString("�÷�: " + GameState.getGameState().getScore1(), 120, 390);
		if (Properties.getProperties().getPalyerNum() == 2) {
			g.setColor(Color.CYAN);// ʹ��CYANɫ
			g.drawString("��Ҷ�", 550, 200);// �����������
			g.setColor(Color.RED);// ʹ�ú�ɫ
			g.drawString("������:" + GameState.getGameState().getLevel(), 520, 320);
			g.drawString("�÷�: " + GameState.getGameState().getScore2(), 520, 390);
		}
		long temp = GameState.getGameState().getTotalTime();
		g.drawString("��ʱ��" + temp / 60 + "��" + temp % 60 + "��", 215, 480);

	}

	/**
	 * ��ת�����
	 */
	private void gotoLoginPanel() {
		System.gc();
		frame.setPanel(new LoginPanel(frame));// ��������ת���˹ؿ���Ϸ���
		GameState.getGameState().reset();
	}

	/**
	 * �ؿ���嶯���߳�
	 *
	 */
	private class LevelPanelThread extends Thread {
		public void run() {
			for (int i = 0; i < 6; i++) {// ѭ��6��
				try {
					Thread.sleep(1000);// ����1��
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			gotoLoginPanel();// ��ת����Ϸ���
		}
	}
}
