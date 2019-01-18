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
 * 登陆面板（选择游戏模式）
 */
public class LoginPanel extends JPanel implements KeyListener {

	private static final long serialVersionUID = 1L;
	private static final int TITLE_1 = 270, TITLE_2 = 330, TITLE_3 = 390, TITLE_4 = 450;// 坦克图标可选择的四个Y坐标
	private Image backgroud;// 背景图片
	private Image tank;// 坦克图标
	private int tankY = 270;// 坦克图标Y坐标
	private MainFrame mMainFrame;

	/**
	 * 登陆面板构造方法
	 * 
	 * @param mMainFrame
	 *            主窗体
	 */
	public LoginPanel(MainFrame mMainFrame) {
		this.mMainFrame = mMainFrame;
		addListener();// 添加组件监听
		try {
			//backgroud = ImageIO.read(new File(ImageUtil.LOGIN_BACKGROUD_IMAGE_URL));// 读取背景图片
			this.setBackground(Color.black);
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
		Font font = new Font("黑体", Font.BOLD, 35);// 创建体字
		g.setFont(font);// 使用字体
		g.setColor(Color.BLACK);// 使用黑色
		g.drawString("单人游戏模式", 290, 300);// 绘制第一行文字
		g.drawString("双人游戏模式", 290, 360);// 绘制第二行文字
		g.drawString("游戏设置", 320, 420);// 绘制第三行文字

		g.drawImage(tank, 250, tankY, this);// 绘制坦克图标
	}

	/**
	 * 跳转关卡面板
	 */
	private void gotoLevelPanel() {
		mMainFrame.removeKeyListener(this);// 主窗体删除键盘监听
		mMainFrame.setPanel(new LevelPanel(mMainFrame));// 主窗体跳转至关卡面板
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
			if (tankY == TITLE_1) {
				tankY = TITLE_3;
			} else if (tankY == TITLE_3) {
				tankY = TITLE_2;
			} else if (tankY == TITLE_2) {
				tankY = TITLE_1;
			}
			repaint();// 按键按下之后，需要重新绘图
		} else if (code == KeyEvent.VK_DOWN || code == KeyEvent.VK_S) {// 如果按下的是“↓”“S”
			if (tankY == TITLE_1) {
				tankY = TITLE_2;
			} else if (tankY == TITLE_2) {
				tankY = TITLE_3;
			} else if (tankY == TITLE_3) {
				tankY = TITLE_1;
			}
			repaint();// 按键按下之后，需要重新绘图
		} else if (code == KeyEvent.VK_ENTER || code == KeyEvent.VK_NUMPAD0 || code == KeyEvent.VK_SPACE) {// 如果按下的是“Enter”“num0”
			if (tankY == TITLE_1) {// 如果坦克图标在第一个位置
				Properties.getProperties().setOnePlayer();
				gotoLevelPanel();// 跳转关卡面板
				GameState.getGameState().startTime();
			}
			if (tankY == TITLE_2) {
				Properties.getProperties().setTwoPlayer();// 游戏模式为双人模式
				gotoLevelPanel();// 跳转关卡面板
				GameState.getGameState().startTime();
			}
			if (tankY == TITLE_3) {
				mMainFrame.removeKeyListener(this);
				mMainFrame.setPanel(new SettingPanel(mMainFrame));
			}
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
