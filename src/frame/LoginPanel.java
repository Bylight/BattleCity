package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JPanel;

import singelton.GameState;
import singelton.Properties;
import util.ImageUtil;

/**
 * ��½��壨ѡ����Ϸģʽ��
 */
public class LoginPanel extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private static final int TITLE_1 = 270, TITLE_2 = 330, TITLE_3 = 390, TITLE_4 = 450;// ̹��ͼ���ѡ����ĸ�Y����
	private Image backgroud;// ����ͼƬ
	private Image tank;// ̹��ͼ��
	private int tankY = 270;// ̹��ͼ��Y����
	private MainFrame mMainFrame;

	/**
	 * ��½��幹�췽��
	 * 
	 * @param mMainFrame
	 *            ������
	 */
	public LoginPanel(MainFrame mMainFrame) {
		this.mMainFrame = mMainFrame;
		addListener();// ����������
		try {
			//backgroud = ImageIO.read(new File(ImageUtil.LOGIN_BACKGROUD_IMAGE_URL));// ��ȡ����ͼƬ
			this.setBackground(Color.black);
			tank = ImageIO.read(new File(ImageUtil.PLAYER1_RIGHT_IMAGE_URL));// ��ȡ̹��ͼ��
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ��д��ͼ����
	 */
	@Override
	public void paint(Graphics g) {
		g.drawImage(backgroud, 0, 0, getWidth(), getHeight(), this);// ���Ʊ���ͼƬ�������������
		Font font = new Font("����", Font.BOLD, 35);// ��������
		g.setFont(font);// ʹ������
		g.setColor(Color.BLACK);// ʹ�ú�ɫ
		g.drawString("������Ϸģʽ", 290, 300);// ���Ƶ�һ������
		g.drawString("˫����Ϸģʽ", 290, 360);// ���Ƶڶ�������
		g.drawString("��Ϸ����", 320, 420);// ���Ƶ���������

		g.drawImage(tank, 250, tankY, this);// ����̹��ͼ��
	}

	/**
	 * ��ת�ؿ����
	 */
	private void gotoLevelPanel() {
		mMainFrame.removeKeyListener(this);// ������ɾ�����̼���
		mMainFrame.setPanel(new LevelPanel(mMainFrame));// ��������ת���ؿ����
	}

	/**
	 * ����������
	 */
	private void addListener() {
		mMainFrame.addKeyListener(this);// ������������̼�����������ʵ��KeyListener�ӿ�
	}

	/**
	 * ����������ʱ
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();// ��ȡ���µİ���ֵ
		if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {// ������µ��ǡ�������W��
			if (tankY == TITLE_1) {
				tankY = TITLE_3;
			} else if (tankY == TITLE_3) {
				tankY = TITLE_2;
			} else if (tankY == TITLE_2) {
				tankY = TITLE_1;
			}
			repaint();// ��������֮����Ҫ���»�ͼ
		} else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {// ������µ��ǡ�������S��
			if (tankY == TITLE_1) {
				tankY = TITLE_2;
			} else if (tankY == TITLE_2) {
				tankY = TITLE_3;
			} else if (tankY == TITLE_3) {
				tankY = TITLE_1;
			}
			repaint();// ��������֮����Ҫ���»�ͼ
		} else if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_NUMPAD0 || code == KeyEvent.VK_SPACE) {// ������µ��ǡ�Enter����num0��
			if (tankY == TITLE_1) {// ���̹��ͼ���ڵ�һ��λ��
				Properties.getProperties().setOnePlayer();
				gotoLevelPanel();// ��ת�ؿ����
				GameState.getGameState().startTime();
			}
			if (tankY == TITLE_2) {
				Properties.getProperties().setTwoPlayer();// ��ϷģʽΪ˫��ģʽ
				gotoLevelPanel();// ��ת�ؿ����
				GameState.getGameState().startTime();
			}
			if (tankY == TITLE_3) {
				mMainFrame.removeKeyListener(this);
				mMainFrame.setPanel(new SettingPanel(mMainFrame));
			}
		}

	}

	/**
	 * ����̧��ʱ
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// ��ʵ�ִ˷�����������ɾ��
	}

	/**
	 * ����ĳ�����¼�
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// ��ʵ�ִ˷�����������ɾ��
	}

}
