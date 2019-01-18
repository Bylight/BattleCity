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
 * 游戏面板
 */
public class GamePanel extends JPanel implements KeyListener {
	public static final int FRESHTIME = 20;// 界面刷新时间
	private static final int botX[] = { 10, 367, 754 };// 电脑坦克出生的3个横坐标位置

	private BufferedImage image;// 在面板中显示的主图片
	private Graphics g;// 图片画笔
	private MainFrame frame;// 主窗体

	private Tank play1, play2;// 玩家1、玩家2
	private boolean space_key, s_key, w_key, a_key, d_key, up_key, down_key, left_key, right_key, num0_key;// 按键是否按下标志，左侧单词是按键名
	private volatile boolean finish;// 游戏是否结束

	private List<Bullet> bullets;// 所有子弹集合
	private volatile List<Tank> allTanks;// 所有坦克集合
	private List<Tank> botTanks;// 电脑坦克集合
	private List<Tank> playerTanks;// 玩家坦克集合
	private BaseWall base;// 基地
	private List<Wall> walls;// 所有墙块
	private List<Boom> boomImage;// 坦克阵亡后的爆炸效果集合

	private Random r = new Random();// 随机数对象
	private int createBotTimer = 0;// 创建电脑坦克计时器
	private Tank survivor;// （玩家）幸存者,用于绘制最后一个爆炸效果

	private List<AudioClip> audios = AudioUtil.getAudios();// 所有背景音效的集合

	private int botReadyCount;// 准备出场的电脑坦克总数
	private int botSurplusCount;// 电脑坦克剩余量

	/**
	 * 游戏面板构造方法
	 * 
	 * @param frame
	 *            主窗体
	 * @param level
	 *            关卡
	 */
	public GamePanel(MainFrame frame) {
		botReadyCount = GameState.getGameState().getBotCount();// 初始化待出场坦克数目
		botSurplusCount = GameState.getGameState().getBotCount();// 初始化剩余坦克数目

		this.frame = frame;
		frame.setSize(Properties.getProperties().getWidth(), Properties.getProperties().getHeight());
		setBackground(Color.BLACK);
		init();
		Thread t = new FreshThead();
		t.start();
		setBGM();// 设置背景音效
		addListener();// 开启监听
	}

	/**
	 * 设置背景音乐
	 */
	private void setBGM() {
		if (Properties.getProperties().getBGMState() == true) {
			new AudioPlayer(AudioUtil.START).new AudioThread().start();// 播放背景音效
		}
	}

	/**
	 * 组件初始化
	 */
	private void init() {
		bullets = new ArrayList<>();// 实例化子弹集合
		allTanks = new ArrayList<>();// 实例化所有坦克集合
		walls = new ArrayList<>();// 实例化所有墙块集合
		boomImage = new ArrayList<>();// 实例化爆炸效果集合

		image = new BufferedImage(Properties.getProperties().getWidth(), Properties.getProperties().getHeight(),
				BufferedImage.TYPE_INT_BGR);// 实例化主图片，采用面板实际大小
		g = image.getGraphics();// 获取主图片绘图对象

		this.initPlayerTanks();// 实例化玩家坦克
		this.initBotTanks();// 实例化电脑坦克

		allTanks.addAll(playerTanks);// 所有坦克集合添加玩家坦克集合
		allTanks.addAll(botTanks);// 所有坦克集合添加电脑坦克集合

		base = new BaseWall(360, 520);// 实例化基地
		initWalls();// 初始化地图中的墙块
	}

	/**
	 * 实例化玩家坦克集合
	 */
	private void initPlayerTanks() {
		playerTanks = new Vector<>();// 实例化玩家坦克集合
		play1 = new Tank(278, 537, ImageUtil.PLAYER1_UP_IMAGE_URL, this, TankType.PLAYER1);// 实例化玩家1
		if (Properties.getProperties().getPalyerNum() == 2) {// 如果是双人模式则同时实例化玩家2
			play2 = new Tank(448, 537, ImageUtil.PLAYER2_UP_IMAGE_URL, this, TankType.PLAYER2);// 实例化玩家2
			playerTanks.add(play2);// 玩家坦克集合添加玩家2
		}
		playerTanks.add(play1);// 玩家坦克集合添加玩家1
	}

	/**
	 * 实例化电脑坦克集合
	 */
	private void initBotTanks() {
		botTanks = new ArrayList<>();// 实例化电脑坦克集合
		botTanks.add(new BotTank(botX[0], 1, this, TankType.BOTTANK_QUICK));// 在第一个位置添加电脑坦克
		botTanks.add(new BotTank(botX[1], 1, this, TankType.BOTTANK_NORMAL));// 在第二个位置添加电脑坦克
		botTanks.add(new BotTank(botX[2], 1, this, TankType.BOTTANK_NORMAL));// 在第三个位置添加电脑坦克
		botReadyCount -= 3;// 准备出场的坦克总数减去初始化数量
	}

	/**
	 * 组件监听
	 */
	private void addListener() {
		frame.addKeyListener(this);// 主窗体载入键盘监听
	}

	/**
	 * 初始化地图中的墙块
	 */
	@SuppressWarnings("static-access")
	public void initWalls() {
		Random rand = new Random();
		int temp = rand.nextInt(14) + 1;
		while (!GameState.getGameState().judgeLeve(temp)) {
			temp = (temp + 3) % 14;
		}
		Map map = Map.getMap(temp);// 获取当前关卡的地图对象
		walls.addAll(map.getWalls());// 墙块集合添加当前地图中所有墙块
		walls.add(base);// 墙块集合添加基地
	}

	/**
	 * 重写绘制组件方法
	 */
	public void paint(Graphics g) {
		paintTankActoin();// 执行坦克动作
		createBotTank();// 循环创建电脑坦克
		paintImage();// 绘制主要的图片
		g.drawImage(image, 0, 0, this); // 将主图片绘制到面板上
		System.gc();
	}

	/**
	 * 绘制主图片
	 */
	private void paintImage() {
		g.setColor(Color.BLACK);// 使用黑色背景
		g.fillRect(0, 0, image.getWidth(), image.getHeight());// 填充一个覆盖整个图片的黑色矩形

		panitBoom();// 绘制爆炸效果
		paintBotCount();// 在屏幕顶部绘制剩余坦克数量
		panitBotTanks();// 绘制电脑坦克
		panitPlayerTanks();// 绘制玩家坦克

		allTanks.addAll(playerTanks);// 坦克集合添加玩家坦克集合
		allTanks.addAll(botTanks);// 坦克集合添加电脑坦克集合
		panitWalls();// 绘制墙块
		panitBullets();// 绘制子弹

		if (botSurplusCount == 0) {// 如果目前关卡所有电脑都被消灭
			stopThread();// 结束游戏帧刷新线程
			paintBotCount();// 在屏幕顶部绘制剩余坦克数量
			g.setFont(new Font("楷体", Font.BOLD, 50));// 设置绘图字体
			g.setColor(Color.green);// 使用绿色
			g.drawString("胜   利 !", 250, 400);// 在指定坐标绘制文字
			gotoNextLevel();// 进入下一关卡
		}

		if (Properties.getProperties().getPalyerNum() == 1) {// 如果是单人模式
			if (!play1.isAlive()) {// 如果玩家1阵亡,并且玩家1的生命数等于0
				stopThread();// 结束游戏帧刷新线程
				boomImage.add(new Boom(play1.x, play1.y));// 添加玩家1爆炸效果
				panitBoom();// 绘制爆炸效果
				paintGameOver();// 在屏幕中央绘制game over

				gotoEndPanel();
			}
		} else if (Properties.getProperties().getPalyerNum() == 2) {// 如果是双人模式
			if (play1.isAlive() && !play2.isAlive() && play2.getLife() == 0) {// 如果玩家1是
																				// 幸存者
				survivor = play1;// 幸存者是玩家1
			} else if (!play1.isAlive() && play1.getLife() == 0 && play2.isAlive()) {
				survivor = play2;// 幸存者是玩家2
			} else if (!(play1.isAlive() || play2.isAlive())) {// 如果两个玩家全部阵亡
				stopThread();// 结束游戏帧刷新线程
				boomImage.add(new Boom(survivor.x, survivor.y));// 添加幸存者爆炸效果
				panitBoom();// 绘制爆炸效果
				paintGameOver();// 在屏幕中央绘制game over

				gotoEndPanel();// 重新进入本关卡
			}
		}

		if (!base.isAlive()) {// 如果基地被击中
			stopThread();// 结束游戏帧刷新线程
			paintGameOver();// 在屏幕中央绘制game over
			base.setImage(ImageUtil.BREAK_BASE_IMAGE_URL);// 基地使用阵亡图片
			gotoEndPanel();
		}
		g.drawImage(base.getImage(), base.x, base.y, this);// 绘制基地
	}

	/**
	 * 在屏幕顶部绘制剩余坦克数量
	 */
	private void paintBotCount() {
		g.setColor(Color.ORANGE);// 使用橙色
		g.drawString("敌方坦克剩余：" + botSurplusCount, 337, 15);// 在指定坐标绘制字符串
	}

	/**
	 * 在屏幕中央绘制game over
	 */
	private void paintGameOver() {
		g.setFont(new Font("楷体", Font.BOLD, 50));// 设置绘图字体
		g.setColor(Color.RED);// 设置绘图颜色
		g.drawString("Game Over !", 250, 400);// 在指定坐标绘制文字
		if (Properties.getProperties().getSoundState()) {
			new AudioPlayer(AudioUtil.GAMEOVER).new AudioThread().start();// 新建一个音效线程，用于播放音效
		}
	}

	/**
	 * 绘制爆炸效果
	 */
	private void panitBoom() {
		for (int i = 0; i < boomImage.size(); i++) {// 循环遍历爆炸效果集合
			Boom boom = boomImage.get(i);// 获取爆炸对象
			if (boom.isAlive()) {// 如果爆炸效果有效
				if (Properties.getProperties().getSoundState()) {
					AudioClip blast = audios.get(2);// 获取爆炸音效对象
					blast.play();// 播放爆炸音效
				}
				boom.show(g);// 展示爆炸效果
			} else {// 如果爆炸效果无效
				boomImage.remove(i);// 在集合中刪除此爆炸对象
				i--;// 循环变量-1，保证下次循环i的值不会变成i+1，以便有效遍历集合，且防止下标越界
			}
		}
	}

	/**
	 * 绘制墙块
	 */
	private void panitWalls() {
		for (int i = 0; i < walls.size(); i++) {// 循环遍历墙块集合
			Wall w = walls.get(i);// 获取墙块对象
			if (w.isAlive()) {// 如果墙块有效
				g.drawImage(w.getImage(), w.x, w.y, this);// 绘制墙块
			} else {// 如果墙块无效
				walls.remove(i);// 在集合中刪除此墙块
				i--;// 循环变量-1，保证下次循环i的值不会变成i+1，以便有效遍历集合，且防止下标越界
			}
		}
	}

	/**
	 * 绘制子弹
	 */
	private void panitBullets() {
		for (int i = 0; i < bullets.size(); i++) {// 循环遍历子弹集合
			Bullet b = bullets.get(i);// 获取子弹对象
			if (b.isAlive()) {// 如果子弹有效
				b.move();// 子弹执行移动操作
				b.hitBase();// 子弹执行击中基地判断
				b.hitWall();// 子弹执行击中墙壁判断
				b.hitTank();// 子弹执行击中坦克判断
				// Fb.hitIronWall();
				b.hitBullet();// 子弹执行抵消判断
				g.drawImage(b.getImage(), b.x, b.y, this);// 绘制子弹
			} else {// 如果子弹无效
				bullets.remove(i);// 在集合中刪除此子弹
				i--;// 循环变量-1，保证下次循环i的值不会变成i+1，以便有效遍历集合，且防止下标越界
			}
		}
	}

	/**
	 * 绘制电脑坦克
	 * 
	 */
	private void panitBotTanks() {
		for (int i = 0; i < botTanks.size(); i++) {// 循环遍历电脑坦克集合
			BotTank t = (BotTank) botTanks.get(i);// 获取电脑坦克对象
			if (!t.isAlive() && t.getLife() > 1) {
				t.setAlive(true);
				t.setLife();
			}
			if (t.isAlive()) {// 如果坦克存活
				if (!t.isPause()) {// 如果电脑坦克不处于暂停状态
					t.go();// 电脑坦克展开行动
				}
				g.drawImage(t.getImage(), t.x, t.y, this);// 绘制坦克
			} else {// 如果坦克阵亡
				updateScore(t.getBeatBy(), t.getTankType());
				botTanks.remove(i);// 集合中删除此坦克
				i--;// 循环变量-1，保证下次循环i的值不会变成i+1，以便有效遍历集合，且防止下标越界
				boomImage.add(new Boom(t.x, t.y));// 在坦克位置创建爆炸效果
				decreaseBot();// 剩余坦克数量-1
				System.out.println(t.getTankType());
			}
		}
	}

	/**
	 * 普通坦克5分，特殊坦克10分
	 * 
	 * @param i
	 * @param tankType
	 */
	private void updateScore(int i, TankType tankType) {
		int num = 0;
		switch (tankType) {// 判断坦克类型
		case BOTTANK_NORMAL:// 如果是普通电脑
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
	 * 绘制玩家坦克
	 */
	private void panitPlayerTanks() {
		for (int i = 0; i < playerTanks.size(); i++) {// 循环遍历玩家坦克
			Tank t = playerTanks.get(i);// 获取玩家坦克对象if (!t.isAlive() &&
										// t.getLife() > 1) {
			if (!t.isAlive() && t.getLife() > 1) {
				t.setAlive(true);
				t.setLife();
			}
			if (t.isAlive()) {// 如果坦克存活
				g.drawImage(t.getImage(), t.x, t.y, this);// 绘制坦克
			} else {// 如果坦克阵亡
				playerTanks.remove(i);// 集合中删除此坦克
				boomImage.add(new Boom(t.x, t.y));// 在坦克位置创建爆炸效果
				if (Properties.getProperties().getSoundState()) {
					AudioClip blast = audios.get(2);
					blast.play();
				}
				t.setLife();
				if (t.isAlive()) {
					if (t.getTankType() == TankType.PLAYER1) {
						play1 = new Tank(278, 537, ImageUtil.PLAYER1_UP_IMAGE_URL, this, TankType.PLAYER1);// 实例化玩家1
						playerTanks.add(play1);// 玩家坦克集合添加玩家1
					}
					if (t.getTankType() == TankType.PLAYER2) {
						play2 = new Tank(448, 537, ImageUtil.PLAYER2_UP_IMAGE_URL, this, TankType.PLAYER2);// 实例化玩家2
						playerTanks.add(play2);// 玩家坦克集合添加玩家2
					}
				}

			}
		}
	}

	/**
	 * 结束游戏帧刷新
	 */
	private synchronized void stopThread() {
		frame.removeKeyListener(this);// 主窗体删除本类键盘事件监听对象
		finish = true;// 游戏停止标志为true
	}

	/**
	 * 游戏帧刷新线程内部类
	 */
	private class FreshThead extends Thread {
		public void run() {// 线程主方法
			while (!finish) {// 如果游戏未停止
				repaint();// 执行本类重绘方法
				System.gc();// 绘制一次会产生大量垃圾对象，回收内存
				try {
					Thread.sleep(FRESHTIME);// 指定时间后重新绘制界面
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}

	/**
	 * 添加电脑坦克，如果场上坦克未到达最大值，每4秒钟之后在三个出生位置随机选择其一，创建电脑坦克。
	 */
	private void createBotTank() {
		int index = r.nextInt(3);
		createBotTimer += FRESHTIME;// 计时器按照刷新时间递增
		// “当场上电脑小于场上最大数时” 并且 “准备上场的坦克数量大于0” 并且 “计时器记录已过去2秒钟”
		if (botTanks.size() < Properties.getProperties().getBotMaxInMap() && botReadyCount > 0
				&& createBotTimer >= 2000) {
			Rectangle bornRect = new Rectangle(botX[index], 1, 35, 35);// 创建坦克随机出生区域
			for (int i = 0, lengh = allTanks.size(); i < lengh; i++) {// 循环遍历所有坦克集合
				Tank t = allTanks.get(i);// 获取坦克对象
				if (t.isAlive() && t.hit(bornRect)) {// 如果场上存在与随机位置重合并存活的坦克
					return;// 结束方法
				}
			}
			Random rand = new Random();
			int temp = rand.nextInt(100) + 1;
			if (temp < GameState.getGameState().getBotCount()) {
				botTanks.add(new BotTank(botX[index], 1, GamePanel.this, TankType.BOTTANK_WEIGHT));
			} else {
				temp = rand.nextInt(100) + 1;
				if (temp >= GameState.getGameState().getBotCount()) {
					botTanks.add(new BotTank(botX[index], 1, GamePanel.this, TankType.BOTTANK_NORMAL));// 在随机位置创造电脑坦克
				} else {
					botTanks.add(new BotTank(botX[index], 1, GamePanel.this, TankType.BOTTANK_QUICK));// 在随机位置创造电脑坦克
				}
			}
			if (Properties.getProperties().getSoundState()) {
				new AudioPlayer(AudioUtil.ADD).new AudioThread().start();
			}
			botReadyCount--;// 准备上场电脑数量-1
			createBotTimer = 0;// 产生电脑计时器重新计时
		}
	}

	/**
	 * 进入下一关卡
	 */
	private void gotoNextLevel() {
		GameState.getGameState().addLevel();
		Random rand = new Random();
		int temp = rand.nextInt(14) + 1;
		while (!GameState.getGameState().judgeLeve(temp)) {
			temp = (temp + 3) % 14;
		}
		Thread jump = new JumpPageThead(temp);// 创建跳转到下一关卡的线程
		jump.start();// 启动线程
	}

	/**
	 * 进入结算页面
	 */
	private void gotoEndPanel() {
		// Thread jump = new
		// JumpPageThead(GameState.getGameState().getLevel());// 创建重新进入本关卡的线程
		//// Thread jump = new JumpPageThead(Level.level.previsousLevel());
		// jump.start();// 启动线程
		System.gc();
		frame.setPanel(new EndPanel(frame));// 主窗体跳转到此关卡游戏面板
	}

	/**
	 * 剩余坦克数量减少1
	 */
	public void decreaseBot() {
		botSurplusCount--;// 电脑剩余数量-1
	}

	/**
	 * 按键按下时
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		switch (e.getKeyCode()) {// 判断按下的按键值
		case KeyEvent.VK_SPACE:// 如果按下的是“space”
			space_key = true;// “Y”按下标志为true
			break;
		case KeyEvent.VK_W:// 如果按下的是“W”
			w_key = true;// “W”按下标志为true
			a_key = false;// “A”按下标志为false
			s_key = false;// “S”按下标志为false
			d_key = false;// “D”按下标志为false
			break;
		case KeyEvent.VK_A:// 如果按下的是“A”
			w_key = false;// “W”按下标志为false
			a_key = true;// “A”按下标志为true
			s_key = false;// “S”按下标志为false
			d_key = false;// “D”按下标志为false
			break;
		case KeyEvent.VK_S:// 如果按下的是“S”
			w_key = false;// “W”按下标志为false
			a_key = false;// “A”按下标志为false
			s_key = true;// “S”按下标志为true
			d_key = false;// “D”按下标志为false
			break;
		case KeyEvent.VK_D:// 如果按下的是“D”
			w_key = false;// “W”按下标志为false
			a_key = false;// “A”按下标志为false
			s_key = false;// “S”按下标志为false
			d_key = true;// “D”按下标志为true
			break;
		case KeyEvent.VK_NUMPAD0:// 如果按下的是小键盘数字0
			num0_key = true;// 小键盘数字1按下标志为true
			break;
		case KeyEvent.VK_UP:// 如果按下的是“↑”
			up_key = true;// “↑”按下标志为true
			down_key = false;// “↓”按下标志为false
			right_key = false;// “→”按下标志为false
			left_key = false;// “←”按下标志为false
			break;
		case KeyEvent.VK_DOWN:// 如果按下的是“↓”
			up_key = false;// “↑”按下标志为false
			down_key = true;// “↓”按下标志为true
			right_key = false;// “→”按下标志为false
			left_key = false;// “←”按下标志为false
			break;
		case KeyEvent.VK_LEFT:// 如果按下的是“←”
			up_key = false;// “↑”按下标志为false
			down_key = false;// “↓”按下标志为false
			right_key = false;// “→”按下标志为false
			left_key = true;// “←”按下标志为true
			break;
		case KeyEvent.VK_RIGHT:// 如果按下的是“→”
			up_key = false;// “↑”按下标志为false
			down_key = false;// “↓”按下标志为false
			right_key = true;// “→”按下标志为true
			left_key = false;// “←”按下标志为false
			break;
		}
		if (Properties.getProperties().getPalyerNum() == 1 && num0_key) {
			space_key = num0_key;
		}
	}

	/**
	 * 根据按键按下状态，让坦克执行相应动作
	 */
	private void paintTankActoin() {
		if (space_key) {// 如果“Y”键是按下状态
			play1.attack();// 玩家1坦克攻击
		}
		if (w_key) {// 如果“W”键是按下状态
			play1.upWard();// 玩家1坦克向上移动
		}
		if (d_key) {// 如果“D”键是按下状态
			play1.rightWard();// 玩家1坦克向右移动
		}
		if (a_key) {// 如果“A”键是按下状态
			play1.leftWard();// 玩家1坦克左移动
		}
		if (s_key) {// 如果“S”键是按下状态
			play1.downWard();// 玩家1坦克向下移动
		}
		if (Properties.getProperties().getPalyerNum() == 2) {// 双人模式
			if (num0_key) {// 如果“0”键是按下状态
				play2.attack();// 玩家2坦克攻击
			}
			if (up_key) {// 如果“←”键是按下状态
				play2.upWard();// 玩家2坦克向上移动
			}
			if (right_key) {// 如果“→”键是按下状态
				play2.rightWard();// 玩家2坦克向右移动
			}
			if (left_key) {// 如果“↑”键是按下状态
				play2.leftWard();// 玩家2坦克左移动
			}
			if (down_key) {// 如果“↓”键是按下状态
				play2.downWard();// 玩家2坦克后移动
			}
		}
	}

	/**
	 * 按键抬起时
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		switch (e.getKeyCode()) {
		case KeyEvent.VK_SPACE:// 如果抬起的是“Y”
			space_key = false;// “Y”按下标志为false
			break;
		case KeyEvent.VK_W:// 如果抬起的是“W”
			w_key = false;// “W”按下标志为false
			break;
		case KeyEvent.VK_A:// 如果抬起的是“A”
			a_key = false;// “A”按下标志为false
			break;
		case KeyEvent.VK_S:// 如果抬起的是“S”
			s_key = false;// “S”按下标志为false
			break;
		case KeyEvent.VK_D:// 如果抬起的是“D”
			d_key = false;// “D”按下标志为false
			break;
		case KeyEvent.VK_NUMPAD0:// 如果抬起的是小键盘0
			num0_key = false;// 小键盘1按下标志为false
			break;
		case KeyEvent.VK_UP:// 如果抬起的是“↑”
			up_key = false;// “↑”按下标志为false
			break;
		case KeyEvent.VK_DOWN:// 如果抬起的是“↓”
			down_key = false;// “↓”按下标志为false
			break;
		case KeyEvent.VK_LEFT:// 如果抬起的是“←”
			left_key = false;// “←”按下标志为false
			break;
		case KeyEvent.VK_RIGHT:// 如果抬起的是“→”
			right_key = false;// “→”按下标志为false
			break;
		}
		if (Properties.getProperties().getPalyerNum() == 1 && !num0_key) {
			space_key = num0_key;
		}
	}

	/**
	 * 向子弹集合中添加子弹
	 * 
	 * @param b
	 *            添加的子弹
	 */
	public void addBullet(Bullet b) {
		bullets.add(b);// 子弹集合中添加子弹
	}

	/**
	 * 获取所有墙块集合
	 * 
	 * @return 所有墙块
	 */
	public List<Wall> getWalls() {
		return walls;
	}

	/**
	 * 获取基地对象
	 * 
	 * @return 基地
	 */
	public BaseWall getBase() {
		return base;
	}

	/**
	 * 获取所有坦克集合
	 * 
	 * @return 所有坦克
	 */
	public List<Tank> getTanks() {
		return allTanks;
	}

	/**
	 * 获取游戏面板所有子弹
	 * 
	 * @return
	 */
	public List<Bullet> getBullets() {
		return bullets;
	}

	/**
	 * 获取游戏面板中所有电脑坦克
	 * 
	 * @return 面板中所有存在的电脑坦克
	 */
	public List<Tank> getBotTanks() {
		return botTanks;
	}

	/**
	 * 游戏结束跳转线程
	 */
	private class JumpPageThead extends Thread {
		int level;// 跳转的关卡

		/**
		 * 跳转线程构造方法
		 * 
		 * @param level
		 *            - 跳转的关卡
		 */
		public JumpPageThead(int level) {
			this.level = level;
		}

		/**
		 * 线程主方法
		 */
		public void run() {
			try {
				Thread.sleep(1000);// 1秒钟后
				frame.setPanel(new LevelPanel(frame));// 主窗体跳转到指定关卡
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

	public void keyTyped(KeyEvent e) {
		// 不实现此方法，但不可删除
	}

	/**
	 * 获取玩家坦克
	 * 
	 * @return
	 */
	public List<Tank> getPlayerTanks() {
		// TODO Auto-generated method stub
		return playerTanks;
	}
}
