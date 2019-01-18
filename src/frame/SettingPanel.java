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

import singelton.Properties;
import util.ImageUtil;

/**
 * ������壨ѡ����Ϸģʽ��
 */
public class SettingPanel extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private static final int SET_BGM = 270, SET_SOUND = 330, SET_MODE = 390, RETURN_HOME = 450;// ̹��ͼ���ѡ����ĸ�Y����
	private Image backgroud;// ����ͼƬ
	private Image tank;// ̹��ͼ��
	private int tankY = 270;// ̹��ͼ��Y����
	private MainFrame mMainFrame;

	private String BGMState;
	private String soundState;
	private String gameMode;

	public SettingPanel(MainFrame mMainFrame) {
		this.mMainFrame = mMainFrame;
		addListener();// ����������

		getBGMState();
		getSoundState();
		getGameMode();
		try {
			backgroud = ImageIO.read(new File(ImageUtil.LOGIN_BACKGROUD_IMAGE_URL));// ��ȡ����ͼƬ
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
		g.drawString("�������֣�" + BGMState, 280, 300);// ���Ƶ�һ������
		g.drawString("��Ϸ��Ч��" + soundState, 280, 360);// ���Ƶڶ�������
		g.drawString("��Ϸ�Ѷȣ�" + gameMode, 280, 420);// ���Ƶ���������
		g.drawString("�������˵�", 300, 480);
		g.drawImage(tank, 240, tankY, this);// ����̹��ͼ��
	}

	private void getBGMState() {
		if (Properties.getProperties().getBGMState()) {
			BGMState = "����";
		} else {
			BGMState = "�ر�";
		}
	}

	/**
	 * ����BGM״̬
	 */
	private void updateBGMState() {
		if (!Properties.getProperties().getBGMState()) {
			Properties.getProperties().turnOnBGM();
			BGMState = "����";
		} else {
			Properties.getProperties().closeBGM();
			BGMState = "�ر�";
		}
	}

	private void getSoundState() {
		if (!Properties.getProperties().getSoundState()) {
			soundState = "�ر�";
		} else {
			soundState = "����";
		}
	}

	/**
	 * ������Ч״̬
	 */
	private void updateSoundState() {
		if (Properties.getProperties().getSoundState()) {
			Properties.getProperties().closeSound();
			;
			soundState = "�ر�";
		} else {
			Properties.getProperties().turnOnSound();
			soundState = "����";
		}
	}

	private void getGameMode() {
		switch (Properties.getProperties().getGameMode()) {
		case 2:
			gameMode = "�е�";
			break;
		case 3:
			gameMode = "����";
			break;
		default:
			gameMode = "��";
		}
	}

	/**
	 * ������Ϸ�Ѷ�
	 */
	private void updateGameMode() {
		switch (Properties.getProperties().getGameMode()) {
		case 1:
			Properties.getProperties().toMiddleMode();
			gameMode = "�е�";
			break;
		case 2:
			Properties.getProperties().toHardMode();
			gameMode = "����";
			break;
		default:
			Properties.getProperties().toEasyMode();
			gameMode = "��";
		}

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
			if (tankY == SET_BGM) {
				tankY = RETURN_HOME;
			} else if (tankY == RETURN_HOME) {
				tankY = SET_MODE;
			} else if (tankY == SET_MODE) {
				tankY = SET_SOUND;
			} else if (tankY == SET_SOUND) {
				tankY = SET_BGM;
			}
			repaint();// ��������֮����Ҫ���»�ͼ
		} else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {// ������µ��ǡ�������S��
			if (tankY == RETURN_HOME) {
				tankY = SET_BGM;
			} else if (tankY == SET_BGM) {
				tankY = SET_SOUND;
			} else if (tankY == SET_SOUND) {
				tankY = SET_MODE;
			} else if (tankY == SET_MODE) {
				tankY = RETURN_HOME;
			}
			repaint();// ��������֮����Ҫ���»�ͼ
		} else if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_NUMPAD0 || code == KeyEvent.VK_SPACE) {// ������µ��ǡ�Enter����num0��
			if (tankY == SET_BGM) {// ���̹��ͼ���ڵ�һ��λ��
				updateBGMState();
			}
			if (tankY == SET_SOUND) {
				updateSoundState();
			}
			if (tankY == SET_MODE) {
				updateGameMode();
			}
			if (tankY == RETURN_HOME) {
				mMainFrame.removeKeyListener(this);
				mMainFrame.setPanel(new LoginPanel(mMainFrame));
			}
			repaint();
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
