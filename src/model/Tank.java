package model;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.List;

import enumtype.Direction;
import enumtype.TankType;
import frame.GamePanel;
import model.wall.GrassWall;
import model.wall.Wall;
import singelton.Properties;
import util.AudioPlayer;
import util.AudioUtil;
import util.ImageUtil;

/**
 * 坦克
 */
public class Tank extends DisplayableImage {
	GamePanel gamePanel;// 游戏面板
	Direction direction;// 移动方向
	protected boolean alive = true;// 是否存活
	protected int speed = 3;// 移动速度
	TankType type;// 坦克类型
	private String upImage;// 向上移动时的图片
	private String downImage;// 向下移动时的图片
	private String rightImage;// 向右移动时的图片
	private String leftImage;// 向左移动时的图片

	private boolean attackCoolDown = true;// 攻击冷却状态
	private int attackCoolDownTime = 500;// 攻击冷却时间，毫秒

	protected boolean hasBullet;// 该坦克子弹是否存在，存在时无法继续发射子弹

	private int life = 1;// 生命数

	/**
	 * 坦克构造方法
	 * 
	 * @param x
	 *            初始化横坐标
	 * @param y
	 *            初始化纵坐标
	 * @param url
	 *            图片路径
	 * @param gamePanel
	 *            游戏面板
	 * @param type
	 *            坦克类型
	 */
	public Tank(int x, int y, String url, GamePanel gamePanel, TankType type) {
		super(x, y, url);
		hasBullet = false;
		this.gamePanel = gamePanel;
		this.type = type;
		direction = Direction.UP;// 初始化方向向上
		switch (type) {// 判断坦克类型
		case PLAYER1:// 如果是玩家1
			upImage = ImageUtil.PLAYER1_UP_IMAGE_URL;// 记录玩家1四个方向的图片
			downImage = ImageUtil.PLAYER1_DOWN_IMAGE_URL;
			rightImage = ImageUtil.PLAYER1_RIGHT_IMAGE_URL;
			leftImage = ImageUtil.PLAYER1_LEFT_IMAGE_URL;
			life = 1;
			break;
		case PLAYER2:// 如果是玩家2
			upImage = ImageUtil.PLAYER2_UP_IMAGE_URL;// 记录玩家2四个方向的图片
			downImage = ImageUtil.PLAYER2_DOWN_IMAGE_URL;
			rightImage = ImageUtil.PLAYER2_RIGHT_IMAGE_URL;
			leftImage = ImageUtil.PLAYER2_LEFT_IMAGE_URL;
			life = 1;
			break;
		case BOTTANK_NORMAL:// 如果是普通电脑
			upImage = ImageUtil.NORMAL_BOT_UP_IMAGE_URL;// 记录普通电脑四个方向的图片
			downImage = ImageUtil.NORMAL_BOT_DOWN_IMAGE_URL;
			rightImage = ImageUtil.NORMAL_BOT_RIGHT_IMAGE_URL;
			leftImage = ImageUtil.NORMAL_BOT_LEFT_IMAGE_URL;
			life = 2;
			break;
		case BOTTANK_WEIGHT:
			upImage = ImageUtil.WEIGHT_BOT_UP_IMAGE_URL;// 记录重型电脑四个方向的图片
			downImage = ImageUtil.WEIGHT_BOT_DOWN_IMAGE_URL;
			rightImage = ImageUtil.WEIGHT_BOT_RIGHT_IMAGE_URL;
			leftImage = ImageUtil.WEIGHT_BOT_LEFT_IMAGE_URL;
			life = 3;
			speed = 2;
			break;
		case BOTTANK_QUICK:
			upImage = ImageUtil.QUICK_BOT_UP_IMAGE_URL;// 记录轻型电脑四个方向的图片
			downImage = ImageUtil.QUICK_BOT_DOWN_IMAGE_URL;
			rightImage = ImageUtil.QUICK_BOT_RIGHT_IMAGE_URL;
			leftImage = ImageUtil.QUICK_BOT_LEFT_IMAGE_URL;
			life = 1;
			speed = 6;
			break;
		}
	}

	@Override
	public Rectangle getRect() {
		return new Rectangle(x, y, width - 3, height - 3);
	}

	/**
	 * 向左移动
	 */
	public void leftWard() {
		if (direction != Direction.LEFT) {// 如果移动之前的方向不是左移
			setImage(leftImage);// 更换左移图片
		}
		direction = Direction.LEFT;// 移动方向设为左
		if (!hitWall(x - speed, y) && !hitTank(x - speed, y)) {// 如果左移之后的位置不会撞到墙块和坦克
			x -= speed;// 横坐标递减
			moveToBorder();// 判断是否移动到面板的边界
		}
	}

	/**
	 * 向右移动
	 */
	public void rightWard() {
		if (direction != Direction.RIGHT) {// 如果移动之前的方向不是左移
			setImage(rightImage);// 更换右移图片
		}
		direction = Direction.RIGHT;// 移动方向设为右
		if (!hitWall(x + speed, y) && !hitTank(x + speed, y)) {// 如果右移之后的位置不会撞到墙块和坦克
			x += speed;// 横坐标递增
			moveToBorder();// 判断是否移动到面板的边界
		}
	}

	/**
	 * 向上移动
	 */
	public void upWard() {
		if (direction != Direction.UP) {// 如果移动之前的方向不是上移
			setImage(upImage);// 更换上移图片
		}
		direction = Direction.UP;// 移动方向设为上
		if (!hitWall(x, y - speed) && !hitTank(x, y - speed)) {// 如果上移之后的位置不会撞到墙块和坦克
			y -= speed;// 纵坐标递减
			moveToBorder();// 判断是否移动到面板的边界
		}
	}

	/**
	 * 向下移动
	 */
	public void downWard() {
		if (direction != Direction.DOWN) {// 如果移动之前的方向不是下移
			setImage(downImage);// 更换下移图片
		}
		direction = Direction.DOWN;// 移动方向设为下
		if (!hitWall(x, y + speed) && !hitTank(x, y + speed)) {// 如果下移之后的位置不会撞到墙块和坦克
			y += speed;// 纵坐标递增
			moveToBorder();// 判断是否移动到面板的边界
		}
	}

	/**
	 * 判断是否撞到墙块
	 * 
	 * @param x
	 *            坦克横坐标
	 * @param y
	 *            坦克纵坐标
	 * @return 撞到任意墙块，则返回true
	 */
	private boolean hitWall(int x, int y) {
		Rectangle next = new Rectangle(x, y, width - 3, height - 3);// 创建坦克移动后的目标区域
		List<Wall> walls = gamePanel.getWalls();// 获取所有墙块
		for (int i = 0, lengh = walls.size(); i < lengh; i++) {// 遍历所有墙块
			Wall w = walls.get(i);// 获取墙块对象
			if (w instanceof GrassWall) {// 如果是草地
				continue;// 执行下一次循环
			} else if (w.hit(next)) {// 如果撞到墙块
				return true;// 返回撞到墙块
			}
		}
		return false;
	}

	/**
	 * 判断是否撞到其他坦克
	 * 
	 * @param x
	 *            自身坦克的横坐标
	 * @param y
	 *            自身坦克的纵坐标
	 * @return 撞到任意坦克，则返回true
	 */
	boolean hitTank(int x, int y) {
		Rectangle next = new Rectangle(x, y, width, height);// 创建坦克移动后的目标区域
		List<Tank> tanks = gamePanel.getTanks();// 获取所有坦克
		for (int i = 0, lengh = tanks.size(); i < lengh; i++) {// 遍历所有坦克
			Tank t = tanks.get(i);// 获取tank对象
			if (!this.equals(t)) {// 如果此坦克与自身不是同一个对象
				if (t.isAlive() && t.hit(next)) {// 如果此坦克存活并且与自身相撞
					return true;// 返回相撞
				}
			}
		}
		return false;
	}

	/**
	 * 移动到面板的边界
	 */
	protected void moveToBorder() {
		if (x < 0) {// 如果坦克横坐标小于0
			x = 0;// 让坦克横坐标等于0
		} else if (x > gamePanel.getWidth() - width) {// 如果坦克横坐标超出了最大范围
			x = gamePanel.getWidth() - width;// 让坦克横坐标保持最大值
		}
		if (y < 0) {// 如果坦克纵坐标小于0
			y = 0;// 让坦克纵坐标等于0
		} else if (y > gamePanel.getHeight() - height) {// 如果坦克纵坐标超出了最大范围
			y = gamePanel.getHeight() - height;// 让坦克纵坐标保持最大值
		}
	}

	/**
	 * 获取坦克头点
	 * 
	 * @return 头点对象
	 */
	private Point getHeadPoint() {
		Point p = new Point();// 创建点对象，作为头点
		switch (direction) {// 判断移动方向
		case UP:// 如果向上
			p.x = x + width / 2;// 头点横坐标取x加宽度的一般
			p.y = y;// 头点纵坐标取y
			break;
		case DOWN:// 如果向下
			p.x = x + width / 2;// 头点横坐标取x加宽度的一般
			p.y = y + height;// 头点纵坐标取y加高度的一般
			break;
		case RIGHT:// 如果向右
			p.x = x + width;// 头点横坐标取x加宽度的一般
			p.y = y + height / 2;// 头点纵坐标取y加高度的一般
			break;
		case LEFT:// 如果向左
			p.x = x;// 头点横坐标取x
			p.y = y + height / 2;// 头点纵坐标取y加高度的一般
			break;
		default:// 默认
			p = null;// 头点为null
		}
		return p;// 返回头点
	}

	/**
	 * 攻击
	 */
	public void attack() {
		if (attackCoolDown && !hasBullet) {// 如果攻击功能完成冷却
			Point p = getHeadPoint();// 获取坦克头点对象

			Bullet b = new Bullet(p.x - Bullet.LENGTH / 2, p.y - Bullet.LENGTH / 2, direction, gamePanel, this);// 在坦克头位置发射与坦克角度相同的子弹
			gamePanel.addBullet(b);// 游戏面板添加子弹
			if (Properties.getProperties().getSoundState()) {
				AudioPlayer fire = new AudioPlayer(AudioUtil.FIRE);
				fire.new AudioThread().start();
			}
			new AttackCD().start();// 攻击功能开始冷却
		}
	}

	/**
	 * 坦克是否存活
	 * 
	 * @return 存活状态
	 */
	public boolean isAlive() {
		return alive;
	}

	/**
	 * 设置存活状态
	 * 
	 * @param alive
	 *            存活状态
	 */
	public void setAlive(boolean alive) {
		this.alive = alive;
	}

	/**
	 * 设置移动速度
	 * 
	 * @param speed
	 */
	public void setSpeed(int speed) {
		this.speed = speed;
	}

	/**
	 * 获取坦克生命数量
	 * 
	 * @return
	 */
	public synchronized final int getLife() {
		return this.life;
	}

	/**
	 * 减少坦克生命数量
	 */
	public final void setLife() {
		if (life > 0) {
			life--;
		} else {
			return;
		}
	}

	/**
	 * 获取坦克类型
	 * 
	 * @return
	 */
	public TankType getTankType() {
		return type;
	}

	/**
	 * 设置是否有子弹存在
	 * 
	 * @param hasBullet
	 */
	public void setHasBullet(Boolean hasBullet) {
		this.hasBullet = hasBullet;
	}

	/**
	 * 攻击冷却时间线程
	 */
	private class AttackCD extends Thread {
		public void run() {// 线程主方法
			attackCoolDown = false;// 将攻击功能设为冷却状态
			try {
				Thread.sleep(attackCoolDownTime);// 休眠0.5秒
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			attackCoolDown = true;// 将攻击功能解除冷却状态
		}
	}

	/**
	 * 获取攻击功能是否处于冷却
	 * 
	 * @return 攻击是否冷却
	 */
	public boolean isAttackCoolDown() {
		return attackCoolDown;
	}

	/**
	 * 设置攻击冷却时间
	 * 
	 * @param attackCoolDownTime
	 *            冷却毫秒数
	 */
	public void setAttackCoolDownTime(int attackCoolDownTime) {
		this.attackCoolDownTime = attackCoolDownTime;
	}
}
