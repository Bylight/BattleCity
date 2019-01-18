package frame;

import java.applet.AudioClip;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Vector;

import javax.swing.JPanel;

import enumtype.TankType;
import model.Boom;
import model.BotTank;
import model.Bullet;
import model.Map;
import model.Tank;
import model.wall.BaseWall;
import model.wall.Wall;
import singelton.GameState;
import singelton.Properties;
import util.AudioPlayer;
import util.AudioUtil;
import util.ImageUtil;

/**
 * ��Ϸ���
 */
public class GamePanel extends JPanel implements KeyListener {
	public static final int FRESHTIME = 20;// ����ˢ��ʱ��
	private static final int botX[] = { 10, 367, 754 };// ����̹�˳�����3��������λ��

	private BufferedImage image;// ���������ʾ����ͼƬ
	private Graphics g;// ͼƬ����
	private MainFrame frame;// ������

	private Tank play1, play2;// ���1�����2
	private boolean space_key, s_key, w_key, a_key, d_key, up_key, down_key, left_key, right_key, num0_key;// �����Ƿ��±�־����൥���ǰ�����
	private volatile boolean finish;// ��Ϸ�Ƿ����

	private List<Bullet> bullets;// �����ӵ�����
	private volatile List<Tank> allTanks;// ����̹�˼���
	private List<Tank> botTanks;// ����̹�˼���
	private List<Tank> playerTanks;// ���̹�˼���
	private BaseWall base;// ����
	private List<Wall> walls;// ����ǽ��
	private List<Boom> boomImage;// ̹��������ı�ըЧ������

	private Random r = new Random();// ���������
	private int createBotTimer = 0;// ��������̹�˼�ʱ��
	private Tank survivor;// ����ң��Ҵ���,���ڻ������һ����ըЧ��

	private List<AudioClip> audios = AudioUtil.getAudios();// ���б�����Ч�ļ���

	private int botReadyCount;// ׼�������ĵ���̹������
	private int botSurplusCount;// ����̹��ʣ����

	/**
	 * ��Ϸ��幹�췽��
	 * 
	 * @param frame
	 *            ������
	 * @param level
	 *            �ؿ�
	 */
	public GamePanel(MainFrame frame) {
		botReadyCount = GameState.getGameState().getBotCount();// ��ʼ��������̹����Ŀ
		botSurplusCount = GameState.getGameState().getBotCount();// ��ʼ��ʣ��̹����Ŀ

		this.frame = frame;
		frame.setSize(Properties.getProperties().getWidth(), Properties.getProperties().getHeight());
		setBackground(Color.BLACK);
		init();
		Thread t = new FreshThead();
		t.start();
		setBGM();// ���ñ�����Ч
		addListener();// ��������
	}

	/**
	 * ���ñ�������
	 */
	private void setBGM() {
		if (Properties.getProperties().getBGMState() == true) {
			new AudioPlayer(AudioUtil.START).new AudioThread().start();// ���ű�����Ч
		}
	}

	/**
	 * �����ʼ��
	 */
	private void init() {
		bullets = new ArrayList<>();// ʵ�����ӵ�����
		allTanks = new ArrayList<>();// ʵ��������̹�˼���
		walls = new ArrayList<>();// ʵ��������ǽ�鼯��
		boomImage = new ArrayList<>();// ʵ������ըЧ������

		image = new BufferedImage(Properties.getProperties().getWidth(), Properties.getProperties().getHeight(),
				BufferedImage.TYPE_INT_BGR);// ʵ������ͼƬ���������ʵ�ʴ�С
		g = image.getGraphics();// ��ȡ��ͼƬ��ͼ����

		this.initPlayerTanks();// ʵ�������̹��
		this.initBotTanks();// ʵ��������̹��

		allTanks.addAll(playerTanks);// ����̹�˼���������̹�˼���
		allTanks.addAll(botTanks);// ����̹�˼�����ӵ���̹�˼���

		base = new BaseWall(360, 520);// ʵ��������
		initWalls();// ��ʼ����ͼ�е�ǽ��
	}

	/**
	 * ʵ�������̹�˼���
	 */
	private void initPlayerTanks() {
		playerTanks = new Vector<>();// ʵ�������̹�˼���
		play1 = new Tank(278, 537, ImageUtil.PLAYER1_UP_IMAGE_URL, this, TankType.PLAYER1);// ʵ�������1
		if (Properties.getProperties().getPalyerNum() == 2) {// �����˫��ģʽ��ͬʱʵ�������2
			play2 = new Tank(448, 537, ImageUtil.PLAYER2_UP_IMAGE_URL, this, TankType.PLAYER2);// ʵ�������2
			playerTanks.add(play2);// ���̹�˼���������2
		}
		playerTanks.add(play1);// ���̹�˼���������1
	}

	/**
	 * ʵ��������̹�˼���
	 */
	private void initBotTanks() {
		botTanks = new ArrayList<>();// ʵ��������̹�˼���
		botTanks.add(new BotTank(botX[0], 1, this, TankType.BOTTANK_QUICK));// �ڵ�һ��λ����ӵ���̹��
		botTanks.add(new BotTank(botX[1], 1, this, TankType.BOTTANK_NORMAL));// �ڵڶ���λ����ӵ���̹��
		botTanks.add(new BotTank(botX[2], 1, this, TankType.BOTTANK_NORMAL));// �ڵ�����λ����ӵ���̹��
		botReadyCount -= 3;// ׼��������̹��������ȥ��ʼ������
	}

	/**
	 * �������
	 */
	private void addListener() {
		frame.addKeyListener(this);// ������������̼���
	}

	/**
	 * ��ʼ����ͼ�е�ǽ��
	 */
	@SuppressWarnings("static-access")
	public void initWalls() {
		Random rand = new Random();
		int temp = rand.nextInt(14) + 1;
		while (!GameState.getGameState().judgeLeve(temp)) {
			temp = (temp + 3) % 14;
		}
		Map map = Map.getMap(temp);// ��ȡ��ǰ�ؿ��ĵ�ͼ����
		walls.addAll(map.getWalls());// ǽ�鼯����ӵ�ǰ��ͼ������ǽ��
		walls.add(base);// ǽ�鼯����ӻ���
	}

	/**
	 * ��д�����������
	 */
	public void paint(Graphics g) {
		paintTankActoin();// ִ��̹�˶���
		createBotTank();// ѭ����������̹��
		paintImage();// ������Ҫ��ͼƬ
		g.drawImage(image, 0, 0, this); // ����ͼƬ���Ƶ������
		System.gc();
	}

	/**
	 * ������ͼƬ
	 */
	private void paintImage() {
		g.setColor(Color.BLACK);// ʹ�ú�ɫ����
		g.fillRect(0, 0, image.getWidth(), image.getHeight());// ���һ����������ͼƬ�ĺ�ɫ����

		panitBoom();// ���Ʊ�ըЧ��
		paintBotCount();// ����Ļ��������ʣ��̹������
		panitBotTanks();// ���Ƶ���̹��
		panitPlayerTanks();// �������̹��

		allTanks.addAll(playerTanks);// ̹�˼���������̹�˼���
		allTanks.addAll(botTanks);// ̹�˼�����ӵ���̹�˼���
		panitWalls();// ����ǽ��
		panitBullets();// �����ӵ�

		if (botSurplusCount == 0) {// ���Ŀǰ�ؿ����е��Զ�������
			stopThread();// ������Ϸ֡ˢ���߳�
			paintBotCount();// ����Ļ��������ʣ��̹������
			g.setFont(new Font("����", Font.BOLD, 50));// ���û�ͼ����
			g.setColor(Color.green);// ʹ����ɫ
			g.drawString("ʤ   �� !", 250, 400);// ��ָ�������������
			gotoNextLevel();// ������һ�ؿ�
		}

		if (Properties.getProperties().getPalyerNum() == 1) {// ����ǵ���ģʽ
			if (!play1.isAlive()) {// ������1����,�������1������������0
				stopThread();// ������Ϸ֡ˢ���߳�
				boomImage.add(new Boom(play1.x, play1.y));// ������1��ըЧ��
				panitBoom();// ���Ʊ�ըЧ��
				paintGameOver();// ����Ļ�������game over

				gotoEndPanel();
			}
		} else if (Properties.getProperties().getPalyerNum() == 2) {// �����˫��ģʽ
			if (play1.isAlive() && !play2.isAlive() && play2.getLife() == 0) {// ������1��
																				// �Ҵ���
				survivor = play1;// �Ҵ��������1
			} else if (!play1.isAlive() && play1.getLife() == 0 && play2.isAlive()) {
				survivor = play2;// �Ҵ��������2
			} else if (!(play1.isAlive() || play2.isAlive())) {// ����������ȫ������
				stopThread();// ������Ϸ֡ˢ���߳�
				boomImage.add(new Boom(survivor.x, survivor.y));// ����Ҵ��߱�ըЧ��
				panitBoom();// ���Ʊ�ըЧ��
				paintGameOver();// ����Ļ�������game over

				gotoEndPanel();// ���½��뱾�ؿ�
			}
		}

		if (!base.isAlive()) {// ������ر�����
			stopThread();// ������Ϸ֡ˢ���߳�
			paintGameOver();// ����Ļ�������game over
			base.setImage(ImageUtil.BREAK_BASE_IMAGE_URL);// ����ʹ������ͼƬ
			gotoEndPanel();
		}
		g.drawImage(base.getImage(), base.x, base.y, this);// ���ƻ���
	}

	/**
	 * ����Ļ��������ʣ��̹������
	 */
	private void paintBotCount() {
		g.setColor(Color.ORANGE);// ʹ�ó�ɫ
		g.drawString("�з�̹��ʣ�ࣺ" + botSurplusCount, 337, 15);// ��ָ����������ַ���
	}

	/**
	 * ����Ļ�������game over
	 */
	private void paintGameOver() {
		g.setFont(new Font("����", Font.BOLD, 50));// ���û�ͼ����
		g.setColor(Color.RED);// ���û�ͼ��ɫ
		g.drawString("Game Over !", 250, 400);// ��ָ�������������
		if (Properties.getProperties().getSoundState()) {
			new AudioPlayer(AudioUtil.GAMEOVER).new AudioThread().start();// �½�һ����Ч�̣߳����ڲ�����Ч
		}
	}

	/**
	 * ���Ʊ�ըЧ��
	 */
	private void panitBoom() {
		for (int i = 0; i < boomImage.size(); i++) {// ѭ��������ըЧ������
			Boom boom = boomImage.get(i);// ��ȡ��ը����
			if (boom.isAlive()) {// �����ըЧ����Ч
				if (Properties.getProperties().getSoundState()) {
					AudioClip blast = audios.get(2);// ��ȡ��ը��Ч����
					blast.play();// ���ű�ը��Ч
				}
				boom.show(g);// չʾ��ըЧ��
			} else {// �����ըЧ����Ч
				boomImage.remove(i);// �ڼ����Єh���˱�ը����
				i--;// ѭ������-1����֤�´�ѭ��i��ֵ������i+1���Ա���Ч�������ϣ��ҷ�ֹ�±�Խ��
			}
		}
	}

	/**
	 * ����ǽ��
	 */
	private void panitWalls() {
		for (int i = 0; i < walls.size(); i++) {// ѭ������ǽ�鼯��
			Wall w = walls.get(i);// ��ȡǽ�����
			if (w.isAlive()) {// ���ǽ����Ч
				g.drawImage(w.getImage(), w.x, w.y, this);// ����ǽ��
			} else {// ���ǽ����Ч
				walls.remove(i);// �ڼ����Єh����ǽ��
				i--;// ѭ������-1����֤�´�ѭ��i��ֵ������i+1���Ա���Ч�������ϣ��ҷ�ֹ�±�Խ��
			}
		}
	}

	/**
	 * �����ӵ�
	 */
	private void panitBullets() {
		for (int i = 0; i < bullets.size(); i++) {// ѭ�������ӵ�����
			Bullet b = bullets.get(i);// ��ȡ�ӵ�����
			if (b.isAlive()) {// ����ӵ���Ч
				b.move();// �ӵ�ִ���ƶ�����
				b.hitBase();// �ӵ�ִ�л��л����ж�
				b.hitWall();// �ӵ�ִ�л���ǽ���ж�
				b.hitTank();// �ӵ�ִ�л���̹���ж�
				// Fb.hitIronWall();
				b.hitBullet();// �ӵ�ִ�е����ж�
				g.drawImage(b.getImage(), b.x, b.y, this);// �����ӵ�
			} else {// ����ӵ���Ч
				bullets.remove(i);// �ڼ����Єh�����ӵ�
				i--;// ѭ������-1����֤�´�ѭ��i��ֵ������i+1���Ա���Ч�������ϣ��ҷ�ֹ�±�Խ��
			}
		}
	}

	/**
	 * ���Ƶ���̹��
	 * 
	 */
	private void panitBotTanks() {
		for (int i = 0; i < botTanks.size(); i++) {// ѭ����������̹�˼���
			BotTank t = (BotTank) botTanks.get(i);// ��ȡ����̹�˶���
			if (!t.isAlive() && t.getLife() > 1) {
				t.setAlive(true);
				t.setLife();
			}
			if (t.isAlive()) {// ���̹�˴��
				if (!t.isPause()) {// �������̹�˲�������ͣ״̬
					t.go();// ����̹��չ���ж�
				}
				g.drawImage(t.getImage(), t.x, t.y, this);// ����̹��
			} else {// ���̹������
				updateScore(t.getBeatBy(), t.getTankType());
				botTanks.remove(i);// ������ɾ����̹��
				i--;// ѭ������-1����֤�´�ѭ��i��ֵ������i+1���Ա���Ч�������ϣ��ҷ�ֹ�±�Խ��
				boomImage.add(new Boom(t.x, t.y));// ��̹��λ�ô�����ըЧ��
				decreaseBot();// ʣ��̹������-1
				System.out.println(t.getTankType());
			}
		}
	}

	/**
	 * ��̹ͨ��5�֣�����̹��10��
	 * 
	 * @param i
	 * @param tankType
	 */
	private void updateScore(int i, TankType tankType) {
		int num = 0;
		switch (tankType) {// �ж�̹������
		case BOTTANK_NORMAL:// �������ͨ����
			num = 5;
			break;
		default:
			num = 10;
		}
		if (i == 1) {
			GameState.getGameState().addScore1(num);
		} else {
			GameState.getGameState().addScore2(num);
		}
		// System.out.println(GameState.getGameState().getScore1());
	}

	/**
	 * �������̹��
	 */
	private void panitPlayerTanks() {
		for (int i = 0; i < playerTanks.size(); i++) {// ѭ���������̹��
			Tank t = playerTanks.get(i);// ��ȡ���̹�˶���if (!t.isAlive() &&
										// t.getLife() > 1) {
			if (!t.isAlive() && t.getLife() > 1) {
				t.setAlive(true);
				t.setLife();
			}
			if (t.isAlive()) {// ���̹�˴��
				g.drawImage(t.getImage(), t.x, t.y, this);// ����̹��
			} else {// ���̹������
				playerTanks.remove(i);// ������ɾ����̹��
				boomImage.add(new Boom(t.x, t.y));// ��̹��λ�ô�����ըЧ��
				if (Properties.getProperties().getSoundState()) {
					AudioClip blast = audios.get(2);
					blast.play();
				}
				t.setLife();
				if (t.isAlive()) {
					if (t.getTankType() == TankType.PLAYER1) {
						play1 = new Tank(278, 537, ImageUtil.PLAYER1_UP_IMAGE_URL, this, TankType.PLAYER1);// ʵ�������1
						playerTanks.add(play1);// ���̹�˼���������1
					}
					if (t.getTankType() == TankType.PLAYER2) {
						play2 = new Tank(448, 537, ImageUtil.PLAYER2_UP_IMAGE_URL, this, TankType.PLAYER2);// ʵ�������2
						playerTanks.add(play2);// ���̹�˼���������2
					}
				}

			}
		}
	}

	/**
	 * ������Ϸ֡ˢ��
	 */
	private synchronized void stopThread() {
		frame.removeKeyListener(this);// ������ɾ����������¼���������
		finish = true;// ��Ϸֹͣ��־Ϊtrue
	}

	/**
	 * ��Ϸ֡ˢ���߳��ڲ���
	 */
	private class FreshThead extends Thread {
		public void run() {// �߳�������
			while (!finish) {// �����Ϸδֹͣ
				repaint();// ִ�б����ػ淽��
				System.gc();// ����һ�λ���������������󣬻����ڴ�
				try {
					Thread.sleep(FRESHTIME);// ָ��ʱ������»��ƽ���
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * ��ӵ���̹�ˣ��������̹��δ�������ֵ��ÿ4����֮������������λ�����ѡ����һ����������̹�ˡ�
	 */
	private void createBotTank() {
		int index = r.nextInt(3);
		createBotTimer += FRESHTIME;// ��ʱ������ˢ��ʱ�����
		// �������ϵ���С�ڳ��������ʱ�� ���� ��׼���ϳ���̹����������0�� ���� ����ʱ����¼�ѹ�ȥ2���ӡ�
		if (botTanks.size() < Properties.getProperties().getBotMaxInMap() && botReadyCount > 0
				&& createBotTimer >= 2000) {
			Rectangle bornRect = new Rectangle(botX[index], 1, 35, 35);// ����̹�������������
			for (int i = 0, lengh = allTanks.size(); i < lengh; i++) {// ѭ����������̹�˼���
				Tank t = allTanks.get(i);// ��ȡ̹�˶���
				if (t.isAlive() && t.hit(bornRect)) {// ������ϴ��������λ���غϲ�����̹��
					return;// ��������
				}
			}
			Random rand = new Random();
			int temp = rand.nextInt(100) + 1;
			if (temp < GameState.getGameState().getBotCount()) {
				botTanks.add(new BotTank(botX[index], 1, GamePanel.this, TankType.BOTTANK_WEIGHT));
			} else {
				temp = rand.nextInt(100) + 1;
				if (temp >= GameState.getGameState().getBotCount()) {
					botTanks.add(new BotTank(botX[index], 1, GamePanel.this, TankType.BOTTANK_NORMAL));// �����λ�ô������̹��
				} else {
					botTanks.add(new BotTank(botX[index], 1, GamePanel.this, TankType.BOTTANK_QUICK));// �����λ�ô������̹��
				}
			}
			if (Properties.getProperties().getSoundState()) {
				new AudioPlayer(AudioUtil.ADD).new AudioThread().start();
			}
			botReadyCount--;// ׼���ϳ���������-1
			createBotTimer = 0;// �������Լ�ʱ�����¼�ʱ
		}
	}

	/**
	 * ������һ�ؿ�
	 */
	private void gotoNextLevel() {
		GameState.getGameState().addLevel();
		Random rand = new Random();
		int temp = rand.nextInt(14) + 1;
		while (!GameState.getGameState().judgeLeve(temp)) {
			temp = (temp + 3) % 14;
		}
		Thread jump = new JumpPageThead(temp);// ������ת����һ�ؿ����߳�
		jump.start();// �����߳�
	}

	/**
	 * �������ҳ��
	 */
	private void gotoEndPanel() {
		// Thread jump = new
		// JumpPageThead(GameState.getGameState().getLevel());// �������½��뱾�ؿ����߳�
		//// Thread jump = new JumpPageThead(Level.level.previsousLevel());
		// jump.start();// �����߳�
		System.gc();
		frame.setPanel(new EndPanel(frame));// ��������ת���˹ؿ���Ϸ���
	}

	/**
	 * ʣ��̹����������1
	 */
	public void decreaseBot() {
		botSurplusCount--;// ����ʣ������-1
	}

	/**
	 * ��������ʱ
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {// �жϰ��µİ���ֵ
		case KeyEvent.VK_SPACE:// ������µ��ǡ�space��
			space_key = true;// ��Y�����±�־Ϊtrue
			break;
		case KeyEvent.VK_W:// ������µ��ǡ�W��
			w_key = true;// ��W�����±�־Ϊtrue
			a_key = false;// ��A�����±�־Ϊfalse
			s_key = false;// ��S�����±�־Ϊfalse
			d_key = false;// ��D�����±�־Ϊfalse
			break;
		case KeyEvent.VK_A:// ������µ��ǡ�A��
			w_key = false;// ��W�����±�־Ϊfalse
			a_key = true;// ��A�����±�־Ϊtrue
			s_key = false;// ��S�����±�־Ϊfalse
			d_key = false;// ��D�����±�־Ϊfalse
			break;
		case KeyEvent.VK_S:// ������µ��ǡ�S��
			w_key = false;// ��W�����±�־Ϊfalse
			a_key = false;// ��A�����±�־Ϊfalse
			s_key = true;// ��S�����±�־Ϊtrue
			d_key = false;// ��D�����±�־Ϊfalse
			break;
		case KeyEvent.VK_D:// ������µ��ǡ�D��
			w_key = false;// ��W�����±�־Ϊfalse
			a_key = false;// ��A�����±�־Ϊfalse
			s_key = false;// ��S�����±�־Ϊfalse
			d_key = true;// ��D�����±�־Ϊtrue
			break;
		case KeyEvent.VK_NUMPAD0:// ������µ���С��������0
			num0_key = true;// С��������1���±�־Ϊtrue
			break;
		case KeyEvent.VK_UP:// ������µ��ǡ�����
			up_key = true;// ���������±�־Ϊtrue
			down_key = false;// ���������±�־Ϊfalse
			right_key = false;// ���������±�־Ϊfalse
			left_key = false;// ���������±�־Ϊfalse
			break;
		case KeyEvent.VK_DOWN:// ������µ��ǡ�����
			up_key = false;// ���������±�־Ϊfalse
			down_key = true;// ���������±�־Ϊtrue
			right_key = false;// ���������±�־Ϊfalse
			left_key = false;// ���������±�־Ϊfalse
			break;
		case KeyEvent.VK_LEFT:// ������µ��ǡ�����
			up_key = false;// ���������±�־Ϊfalse
			down_key = false;// ���������±�־Ϊfalse
			right_key = false;// ���������±�־Ϊfalse
			left_key = true;// ���������±�־Ϊtrue
			break;
		case KeyEvent.VK_RIGHT:// ������µ��ǡ�����
			up_key = false;// ���������±�־Ϊfalse
			down_key = false;// ���������±�־Ϊfalse
			right_key = true;// ���������±�־Ϊtrue
			left_key = false;// ���������±�־Ϊfalse
			break;
		}
		if (Properties.getProperties().getPalyerNum() == 1 && num0_key) {
			space_key = num0_key;
		}
	}

	/**
	 * ���ݰ�������״̬����̹��ִ����Ӧ����
	 */
	private void paintTankActoin() {
		if (space_key) {// �����Y�����ǰ���״̬
			play1.attack();// ���1̹�˹���
		}
		if (w_key) {// �����W�����ǰ���״̬
			play1.upWard();// ���1̹�������ƶ�
		}
		if (d_key) {// �����D�����ǰ���״̬
			play1.rightWard();// ���1̹�������ƶ�
		}
		if (a_key) {// �����A�����ǰ���״̬
			play1.leftWard();// ���1̹�����ƶ�
		}
		if (s_key) {// �����S�����ǰ���״̬
			play1.downWard();// ���1̹�������ƶ�
		}
		if (Properties.getProperties().getPalyerNum() == 2) {// ˫��ģʽ
			if (num0_key) {// �����0�����ǰ���״̬
				play2.attack();// ���2̹�˹���
			}
			if (up_key) {// ������������ǰ���״̬
				play2.upWard();// ���2̹�������ƶ�
			}
			if (right_key) {// ������������ǰ���״̬
				play2.rightWard();// ���2̹�������ƶ�
			}
			if (left_key) {// ������������ǰ���״̬
				play2.leftWard();// ���2̹�����ƶ�
			}
			if (down_key) {// ������������ǰ���״̬
				play2.downWard();// ���2̹�˺��ƶ�
			}
		}
	}

	/**
	 * ����̧��ʱ
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:// ���̧����ǡ�Y��
			space_key = false;// ��Y�����±�־Ϊfalse
			break;
		case KeyEvent.VK_W:// ���̧����ǡ�W��
			w_key = false;// ��W�����±�־Ϊfalse
			break;
		case KeyEvent.VK_A:// ���̧����ǡ�A��
			a_key = false;// ��A�����±�־Ϊfalse
			break;
		case KeyEvent.VK_S:// ���̧����ǡ�S��
			s_key = false;// ��S�����±�־Ϊfalse
			break;
		case KeyEvent.VK_D:// ���̧����ǡ�D��
			d_key = false;// ��D�����±�־Ϊfalse
			break;
		case KeyEvent.VK_NUMPAD0:// ���̧�����С����0
			num0_key = false;// С����1���±�־Ϊfalse
			break;
		case KeyEvent.VK_UP:// ���̧����ǡ�����
			up_key = false;// ���������±�־Ϊfalse
			break;
		case KeyEvent.VK_DOWN:// ���̧����ǡ�����
			down_key = false;// ���������±�־Ϊfalse
			break;
		case KeyEvent.VK_LEFT:// ���̧����ǡ�����
			left_key = false;// ���������±�־Ϊfalse
			break;
		case KeyEvent.VK_RIGHT:// ���̧����ǡ�����
			right_key = false;// ���������±�־Ϊfalse
			break;
		}
		if (Properties.getProperties().getPalyerNum() == 1 && !num0_key) {
			space_key = num0_key;
		}
	}

	/**
	 * ���ӵ�����������ӵ�
	 * 
	 * @param b
	 *            ��ӵ��ӵ�
	 */
	public void addBullet(Bullet b) {
		bullets.add(b);// �ӵ�����������ӵ�
	}

	/**
	 * ��ȡ����ǽ�鼯��
	 * 
	 * @return ����ǽ��
	 */
	public List<Wall> getWalls() {
		return walls;
	}

	/**
	 * ��ȡ���ض���
	 * 
	 * @return ����
	 */
	public BaseWall getBase() {
		return base;
	}

	/**
	 * ��ȡ����̹�˼���
	 * 
	 * @return ����̹��
	 */
	public List<Tank> getTanks() {
		return allTanks;
	}

	/**
	 * ��ȡ��Ϸ��������ӵ�
	 * 
	 * @return
	 */
	public List<Bullet> getBullets() {
		return bullets;
	}

	/**
	 * ��ȡ��Ϸ��������е���̹��
	 * 
	 * @return ��������д��ڵĵ���̹��
	 */
	public List<Tank> getBotTanks() {
		return botTanks;
	}

	/**
	 * ��Ϸ������ת�߳�
	 */
	private class JumpPageThead extends Thread {
		int level;// ��ת�Ĺؿ�

		/**
		 * ��ת�̹߳��췽��
		 * 
		 * @param level
		 *            - ��ת�Ĺؿ�
		 */
		public JumpPageThead(int level) {
			this.level = level;
		}

		/**
		 * �߳�������
		 */
		public void run() {
			try {
				Thread.sleep(1000);// 1���Ӻ�
				frame.setPanel(new LevelPanel(frame));// ��������ת��ָ���ؿ�
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void keyTyped(KeyEvent e) {
		// ��ʵ�ִ˷�����������ɾ��
	}

	/**
	 * ��ȡ���̹��
	 * 
	 * @return
	 */
	public List<Tank> getPlayerTanks() {
		// TODO Auto-generated method stub
		return playerTanks;
	}
}
