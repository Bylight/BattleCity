package frame;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import javax.swing.JPanel;

import singelton.GameState;
import singelton.Properties;

/**
 * 显示关卡面板 显示当前的关卡数
 */
public class EndPanel extends JPanel {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MainFrame frame;// 主窗体
	private String levelStr;// 面板中央闪烁的关卡字符串
	private String ready = "";// 准备提示

	/**
	 * @param frame
	 *            主窗体
	 */
	public EndPanel(MainFrame frame) {
		this.frame = frame;
		levelStr = "关卡 " + GameState.getGameState().getLevel();// 初始化关卡字符串
		Thread t = new LevelPanelThread();// 创建关卡面板动画线程
		t.start();// 开启线程
	}

	/**
	 * 重写绘图方法
	 */
	@Override
	public void paint(Graphics g) {
		g.setColor(Color.BLACK);// 使用黑色
		g.fillRect(0, 0, getWidth(), getHeight());// 填充一个覆盖整个面板的黑色矩形
		g.setFont(new Font("楷体", Font.BOLD, 50));// 设置绘图字体
		g.setColor(Color.CYAN);// 使用CYAN色
		g.drawString("玩家一", 150, 200);// 绘制玩家姓名
		g.setColor(Color.RED);// 使用红色
		g.drawString("过关数:" + GameState.getGameState().getLevel(), 120, 320);
		g.drawString("得分: " + GameState.getGameState().getScore1(), 120, 390);
		if (Properties.getProperties().getPalyerNum() == 2) {
			g.setColor(Color.CYAN);// 使用CYAN色
			g.drawString("玩家二", 550, 200);// 绘制玩家姓名
			g.setColor(Color.RED);// 使用红色
			g.drawString("过关数:" + GameState.getGameState().getLevel(), 520, 320);
			g.drawString("得分: " + GameState.getGameState().getScore2(), 520, 390);
		}
		long temp = GameState.getGameState().getTotalTime();
		g.drawString("用时：" + temp / 60 + "分" + temp % 60 + "秒", 215, 480);

	}

	/**
	 * 跳转主面板
	 */
	private void gotoLoginPanel() {
		System.gc();
		frame.setPanel(new LoginPanel(frame));// 主窗体跳转到此关卡游戏面板
		GameState.getGameState().reset();
	}

	/**
	 * 关卡面板动画线程
	 *
	 */
	private class LevelPanelThread extends Thread {
		public void run() {
			for (int i = 0; i < 6; i++) {// 循环6次
				try {
					Thread.sleep(1000);// 休眠1秒
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
			gotoLoginPanel();// 跳转到游戏面板
		}
	}
}
