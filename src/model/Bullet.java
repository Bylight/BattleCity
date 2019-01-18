package model;

import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

import enumtype.Direction;
import enumtype.TankType;
import frame.GamePanel;
import model.wall.BaseWall;
import model.wall.BrickWall;
import model.wall.GrassWall;
import model.wall.IronWall;
import model.wall.RiverWall;
import model.wall.Wall;
import singelton.Properties;
import util.AudioPlayer;
import util.AudioUtil;

/**
 * �ӵ�
 *
 */
public class Bullet extends DisplayableImage {
	Direction direction;
	static final int LENGTH = 8;// �ӵ��ģ������壩�߳�
	private GamePanel gamePanel;// ��Ϸ���
	private int speed = 7;// �ƶ��ٶ�
	private boolean alive = true;// �ӵ��Ƿ����Ч��
	Color color = Color.ORANGE;// �ӵ���ɫ.��ɫ
	private Tank owner;

	private boolean isHitIronWall = false;

	/**
	 * 
	 * �ӵ����췽��
	 * 
	 * @param x
	 *            �ӵ��ĳ�ʼ������
	 * @param y
	 *            �ӵ���ʼ������
	 * @param direction
	 *            �ӵ����䷽��
	 * @param gamePanel
	 *            ��Ϸ������
	 * @param owner
	 *            �����ӵ���̹������
	 */
	public Bullet(int x, int y, Direction direction, GamePanel gamePanel, Tank owner) {
		super(x, y, LENGTH, LENGTH);
		this.direction = direction;
		this.gamePanel = gamePanel;
		this.owner = owner;
		owner.setHasBullet(true);
		init();// ��ʼ�����
	}

	/**
	 * ��ʼ�����
	 */
	private void init() {
		Graphics g = image.getGraphics();// ��ȡͼƬ�Ļ�ͼ����
		g.setColor(Color.BLACK);// ʹ�ú�ɫ��ͼ
		g.fillRect(0, 0, LENGTH, LENGTH);// ����һ����������ͼƬ�ĺ�ɫʵ�ľ���
		g.setColor(color);// ʹ���ӵ���ɫ����ɫ��ͼ
		g.fillOval(0, 0, LENGTH, LENGTH);// ����һ����������ͼƬ��ʵ��Բ��
		g.drawOval(0, 0, LENGTH - 1, LENGTH - 1);// ��Բ�λ���һ���߿򣬷�ֹ���磬��߼�С1����
	}

	/**
	 * �ӵ��ƶ�
	 */
	public void move() {
		switch (direction) {// �ж��ƶ�����
		case UP:// �������
			upward();// �����ƶ�
			break;
		case DOWN:// �������
			downward();// �����ƶ�
			break;
		case LEFT:// �������
			leftward();// �����ƶ�
			break;
		case RIGHT:// �������
			rightward();// �����ƶ�
			break;
		}
	}

	/**
	 * �����ƶ�
	 */
	private void leftward() {
		x -= speed;// ���������
		moveToBorder();// �ƶ������߽�ʱ�����ӵ�
	}

	/**
	 * �����ƶ�
	 */
	private void rightward() {
		x += speed;// ����������
		moveToBorder();// �ƶ������߽�ʱ�����ӵ�
	}

	/**
	 * �����ƶ�
	 */
	private void upward() {
		y -= speed;// ���������
		moveToBorder();// �ƶ������߽�ʱ�����ӵ�
	}

	/**
	 * �����ƶ�
	 */
	private void downward() {
		y += speed;// ����������
		moveToBorder();// �ƶ������߽�ʱ�����ӵ�
	}

	/**
	 * ����̹��
	 */
	public void hitTank() {
		List<Tank> tanks = gamePanel.getTanks();// ��ȡ����̹�˵ļ���
		for (int i = 0, lengh = tanks.size(); i < lengh; i++) {// ����̹�˼���
			Tank t = tanks.get(i);// ��ȡ̹�˶���
			if (t.isAlive() && this.hit(t)) {// ���̹���Ǵ��Ĳ����ӵ�������̹��
				int temp = 1;
				switch (owner.type) {// �ж��ӵ�����ʲô̹��
				case PLAYER1:// ��������1���ӵ�
					temp = 1;
					if (t instanceof BotTank) {// ������е�̹���ǵ���
						this.dispose();
						t.setAlive(false);// ����̹������
						((BotTank) t).setBeatBy(temp);
					} else if (t instanceof Tank) {// ������е������
						this.dispose();
					}
					break;
				case PLAYER2:// ��������2���ӵ�
					temp = 2;
					if (t instanceof BotTank) {// ������е�̹���ǵ���
						this.dispose();
						t.setAlive(false);// ����̹������
						((BotTank) t).setBeatBy(temp);
					} else if (t instanceof Tank) {// ������е������
						this.dispose();
					}
					break;
				default:
					if (t instanceof BotTank) {// ������е�̹���ǵ���
						this.dispose();
					} else if (t instanceof Tank) {// ������е������
						this.dispose();
						t.setAlive(false);// ���̹������
					}
					break;
				// default:// Ĭ��
				// this.dispose();
				// t.setAlive(false);// ̹������
				}
			}
		}
	}

	/**
	 * ���л���
	 */
	public void hitBase() {
		BaseWall b = gamePanel.getBase();// ��ȡ����
		if (this.hit(b)) {// ����ӵ����л���
			this.dispose();
			b.setAlive(false);// ��������
		}
	}

	/**
	 * ����ǽ��
	 */
	public void hitWall() {
		List<Wall> walls = gamePanel.getWalls();// ��ȡ����ǽ��
		for (int i = 0, lengh = walls.size(); i < lengh; i++) {// ��������ǽ��
			Wall w = walls.get(i);// ��ȡǽ�����
			if (this.hit(w)) {// ����ӵ�����ǽ��
				if (w instanceof BrickWall) {// �����שǽ
					if (Properties.getProperties().getSoundState()) {
						new AudioPlayer(AudioUtil.HIT).new AudioThread().start();
					}
					this.dispose();
					// alive = false;// �ӵ�����
					w.setAlive(false);// שǽ����
				}
				if (w instanceof IronWall) {// ����Ǹ�ש
					// alive = false;// �ӵ�����
					this.dispose();
					if (this.isHitIronWall) {
						w.setAlive(false);
					}
					if (Properties.getProperties().getSoundState()) {
						new AudioPlayer(AudioUtil.HIT).new AudioThread().start();
					}
				}
				if (w instanceof RiverWall) {
					if (this.isHitIronWall) {
						this.dispose();
						w.setAlive(false);
					}
				}
				if (w instanceof GrassWall) {
					if (this.isHitIronWall) {
						this.dispose();
						w.setAlive(false);
					}
				}
			}
		}
	}

	/**
	 * �ӵ�����
	 */
	public void hitBullet() {
		List<Bullet> bullets = gamePanel.getBullets();
		for (int i = 0; i < bullets.size(); i++) {
			Bullet b = bullets.get(i);
			if (this.alive) {
				if (this.hit(b) && this.owner != b.owner) {
					// this.alive=false;
					b.dispose();// �����ӵ�
					this.dispose();// �����ӵ�
				}
			}
		}
	}

	/**
	 * �ƶ������߽�ʱ�����ӵ�
	 */
	private void moveToBorder() {
		if (x < 0 || x > gamePanel.getWidth() - getWidth() || y < 0 || y > gamePanel.getHeight() - getHeight()) {// ����ӵ������뿪��Ϸ���
			this.dispose();// �����ӵ�
		}
	}

	/**
	 * �����ӵ�
	 */
	private synchronized void dispose() {
		this.alive = false;// ����Ч��״̬��Ϊfalse
		owner.setHasBullet(false);
	}

	/**
	 * ��ȡ�ӵ����״̬
	 * 
	 * @return
	 */
	public boolean isAlive() {
		return alive;
	}

	public void setIsHitIronWall(boolean b) {
		this.isHitIronWall = b;
	}
}
