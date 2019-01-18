package model;

import java.awt.Rectangle;
import java.util.List;
import java.util.Random;

import enumtype.Direction;
import enumtype.TankType;
import frame.GamePanel;
import util.ImageUtil;

/**
 * ����̹���� Ϊ�˿�����Ϸ�Ѷȣ�����̹����������ƶ��ĸ���Ӧ�ÿ���Ϊ�����������С
 * ���ҵ���̹�����ÿ���ƶ��ķ��򶼲���ͬ������ĳһ�����������ƶ���ʱ��ҲӦ�ò���ͬ
 *
 */
public class BotTank extends Tank {
	private Random random = new Random();// ���������
	private Direction dir;// �ƶ�����
	private int freshTime = GamePanel.FRESHTIME;// ˢ��ʱ�䣬������Ϸ����ˢ��ʱ��
	private int moveTimer = 0;// �ƶ���ʱ��
	private int beatBy = 1;
	private boolean pause = false;// ����̹����ͣ״̬

	/**
	 * ��ȡ����̹����ͣ״̬
	 */
	public boolean isPause() {
		return pause;
	}

	/**
	 * ���õ���̹����ͣ״̬
	 */
	public void setPause(boolean pause) {
		this.pause = pause;
	}

	/**
	 * 
	 * ����̹�˹��췽��
	 * 
	 * @param x
	 *            ������
	 * @param y
	 *            ������
	 * @param gamePanel
	 *            ��Ϸ���
	 * @param type
	 *            ̹������
	 */

	public BotTank(int x, int y, GamePanel gamePanel, TankType type) {
		super(x, y, getUrl(type), gamePanel, type);// ���ø��๹�췽����ʹ��Ĭ�ϻ�����̹��ͼƬ
		dir = Direction.DOWN;// �ƶ�����Ĭ������
		// setSpeed(2);//���õ���̹���ƶ��ٶ�
		setAttackCoolDownTime(1000);// ���ù�����ȴʱ��
	}

	private static String getUrl(TankType type) {
		String downImage = ImageUtil.NORMAL_BOT_DOWN_IMAGE_URL;
		switch (type) {// �ж�̹������
		case BOTTANK_NORMAL:// �������ͨ����
			downImage = ImageUtil.NORMAL_BOT_DOWN_IMAGE_URL;
			break;
		case BOTTANK_WEIGHT:
			downImage = ImageUtil.WEIGHT_BOT_DOWN_IMAGE_URL;
			break;
		case BOTTANK_QUICK:
			downImage = ImageUtil.QUICK_BOT_DOWN_IMAGE_URL;
			break;
		}
		return downImage;
	}

	/**
	 * ����̹��չ���ж��ķ���
	 */
	public void go() {
		if (isAttackCoolDown() && !hasBullet) {// �����ȴ�������Ϳ��Թ���
			attack();// ����
		}
		if (moveTimer > random.nextInt(1000) + 500) {// ����ƶ���ʱ�����������0.5~1.5�룬�����һ������
			dir = randomDirection();
			moveTimer = 0;// ���ü�ʱ��
		} else {
			moveTimer += freshTime;// ��ʱ������ˢ��ʱ�����
		}
		switch (dir) {// ���ݷ���ѡ�����ĸ������ƶ�
		case UP:
			upWard();
			break;
		case DOWN:
			downWard();
			break;
		case LEFT:
			leftWard();
			break;
		case RIGHT:
			rightWard();
			break;
		}
	}

	/**
	 * ��ȡ�������
	 * 
	 * @return
	 */
	private Direction randomDirection() {
		Direction[] dirs = Direction.values();// ��ȡ�������ö��ֵ
		Direction oldDir = dir;// ����ԭ���ķ���
		Direction newDir = dirs[random.nextInt(4)];
		if (oldDir == newDir || newDir == Direction.UP) {// �������̹��ԭ���ķ���ͱ�������ķ�����ͬ�����ߵ���̹���µķ��������ϣ���ô�������һ�η���
			return dirs[random.nextInt(4)];
		}
		return newDir;
	}

	/**
	 * ��д�ƶ������ı߽��¼�
	 */
	protected void moveToBorder() {
		if (x < 0) {// ���̹�˺�����С��0
			x = 0;// ��̹�˺��������0
			dir = randomDirection();// ��������ƶ�����
		} else if (x > gamePanel.getWidth() - width) {// ���̹�˺����곬�������Χ
			x = gamePanel.getWidth() - width;// ��̹�˺����걣�����ֵ
			dir = randomDirection();// ��������ƶ�����
		}
		if (y < 0) {// ���̹��������С��0
			y = 0;// ��̹�����������0
			dir = randomDirection();// ��������ƶ�����
		} else if (y > gamePanel.getHeight() - height) {// ���̹�������곬�������Χ
			y = gamePanel.getHeight() - height;// ��̹�������걣�����ֵ
			dir = randomDirection();// ��������ƶ�����
		}
	}

	/**
	 * ��д����̹�˷���
	 */
	@Override
	boolean hitTank(int x, int y) {
		Rectangle next = new Rectangle(x, y, width, height);// ������ײλ��
		List<Tank> tanks = gamePanel.getTanks();// ��ȡ����̹�˼���
		for (int i = 0, lengh = tanks.size(); i < lengh; i++) {// ����̹�˼���
			Tank t = tanks.get(i);// ��ȡ̹�˶���
			if (!this.equals(t)) {// �����̹�˶����뱾������ͬһ��
				if (t.isAlive() && t.hit(next)) {// ����Է�˵�Ǵ��ģ������뱾��������ײ
					if (t instanceof BotTank) {// ����Է�Ҳ�ǵ���
						dir = randomDirection();// ��������ƶ�����
					}
					return true;// ������ײ
				}
			}
		}
		return false;// δ������ײ
	}

	/**
	 * ��д����������ÿ�ι���ֻ��4%���ʻᴥ�����๥������
	 */
	@Override
	public void attack() {
		int rnum = random.nextInt(100);// �������������Χ��0-99
		if (rnum < 4) {// ��������С��4
			super.attack();// ִ�и��๥������
		}
	}

	public int getBeatBy() {
		return beatBy;
	}

	public void setBeatBy(int i) {
		beatBy = i;
	}
}
