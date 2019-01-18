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
 * 设置面板（选择游戏模式）
 */
public class SettingPanel extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private static final int SET_BGM = 270, SET_SOUND = 330, SET_MODE = 390, RETURN_HOME = 450;// 坦克图标可选择的四个Y坐标
	private Image backgroud;// 背景图片
	private Image tank;// 坦克图标
	private int tankY = 270;// 坦克图标Y坐标
	private MainFrame mMainFrame;

	private String BGMState;
	private String soundState;
	private String gameMode;

	public SettingPanel(MainFrame mMainFrame) {
		this.mMainFrame = mMainFrame;
		addListener();// 添加组件监听

		getBGMState();
		getSoundState();
		getGameMode();
		try {
			backgroud = ImageIO.read(new File(ImageUtil.LOGIN_BACKGROUD_IMAGE_URL));// 读取背景图片
			tank = ImageIO.read(new File(ImageUtil.PLAYER1_RIGHT_IMAGE_URL));// 读取坦克图标
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 重写绘图方法
	 */
	@Override
	public void paint(Graphics g) {
		g.drawImage(backgroud, 0, 0, getWidth(), getHeight(), this);// 绘制背景图片，填满整个面板
		Font font = new Font("黑体", Font.BOLD, 35);// 设置字体
		g.setFont(font);// 使用字体
		g.setColor(Color.BLACK);// 使用黑色
		g.drawString("背景音乐：" + BGMState, 280, 300);// 绘制第一行文字
		g.drawString("游戏音效：" + soundState, 280, 360);// 绘制第二行文字
		g.drawString("游戏难度：" + gameMode, 280, 420);// 绘制第三行文字
		g.drawString("返回主菜单", 300, 480);
		g.drawImage(tank, 240, tankY, this);// 绘制坦克图标
	}

	private void getBGMState() {
		if (Properties.getProperties().getBGMState()) {
			BGMState = "开启";
		} else {
			BGMState = "关闭";
		}
	}

	/**
	 * 更新BGM状态
	 */
	private void updateBGMState() {
		if (!Properties.getProperties().getBGMState()) {
			Properties.getProperties().turnOnBGM();
			BGMState = "开启";
		} else {
			Properties.getProperties().closeBGM();
			BGMState = "关闭";
		}
	}

	private void getSoundState() {
		if (!Properties.getProperties().getSoundState()) {
			soundState = "关闭";
		} else {
			soundState = "开启";
		}
	}

	/**
	 * 更新音效状态
	 */
	private void updateSoundState() {
		if (Properties.getProperties().getSoundState()) {
			Properties.getProperties().closeSound();
			;
			soundState = "关闭";
		} else {
			Properties.getProperties().turnOnSound();
			soundState = "开启";
		}
	}

	private void getGameMode() {
		switch (Properties.getProperties().getGameMode()) {
		case 2:
			gameMode = "中等";
			break;
		case 3:
			gameMode = "困难";
			break;
		default:
			gameMode = "简单";
		}
	}

	/**
	 * 更新游戏难度
	 */
	private void updateGameMode() {
		switch (Properties.getProperties().getGameMode()) {
		case 1:
			Properties.getProperties().toMiddleMode();
			gameMode = "中等";
			break;
		case 2:
			Properties.getProperties().toHardMode();
			gameMode = "困难";
			break;
		default:
			Properties.getProperties().toEasyMode();
			gameMode = "简单";
		}

	}

	/**
	 * 添加组件监听
	 */
	private void addListener() {
		mMainFrame.addKeyListener(this);// 主窗体载入键盘监听，本类已实现KeyListener接口
	}

	/**
	 * 当按键按下时
	 */
	@Override
	public void keyPressed(KeyEvent e) {
		int code = e.getKeyCode();// 获取按下的按键值
		if (code == KeyEvent.VK_UP || code == KeyEvent.VK_W) {// 如果按下的是“↑”“W”
			if (tankY == SET_BGM) {
				tankY = RETURN_HOME;
			} else if (tankY == RETURN_HOME) {
				tankY = SET_MODE;
			} else if (tankY == SET_MODE) {
				tankY = SET_SOUND;
			} else if (tankY == SET_SOUND) {
				tankY = SET_BGM;
			}
			repaint();// 按键按下之后，需要重新绘图
		} else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {// 如果按下的是“↓”“S”
			if (tankY == RETURN_HOME) {
				tankY = SET_BGM;
			} else if (tankY == SET_BGM) {
				tankY = SET_SOUND;
			} else if (tankY == SET_SOUND) {
				tankY = SET_MODE;
			} else if (tankY == SET_MODE) {
				tankY = RETURN_HOME;
			}
			repaint();// 按键按下之后，需要重新绘图
		} else if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_NUMPAD0 || code == KeyEvent.VK_SPACE) {// 如果按下的是“Enter”“num0”
			if (tankY == SET_BGM) {// 如果坦克图标在第一个位置
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
	 * 按键抬起时
	 */
	@Override
	public void keyReleased(KeyEvent e) {
		// 不实现此方法，但不可删除
	}

	/**
	 * 键入某按键事件
	 */
	@Override
	public void keyTyped(KeyEvent e) {
		// 不实现此方法，但不可删除
	}

}
