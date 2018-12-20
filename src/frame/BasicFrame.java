package frame;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * v0.03更新
 * 	1. 重写paint()方法（窗口重画时自动调用）画出代表坦克的实心圆
 * 	2. 使用setBackground()方法设置背景颜色
 * @author bylight
 *
 */

@SuppressWarnings("serial")
public class BasicFrame extends Frame {
	
	private static int x_tank = 50;
	private static int y_tank = 50;
	
	// 画出代表坦克的实心圆
	// g为前景色
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		Color c = g.getColor();	// c用于保存g的初始颜色
		
		g.setColor(Color.RED);	// 设置颜色为红
		g.fillOval(x_tank, y_tank, 30, 30);	// 参数依次为x, y, width, height
		
		g.setColor(c); 	//恢复g的初始颜色
		
		y_tank += 50;
	}

	public void lauchFrame() {
		
		setVisible(true);
		setLocation(400, 300);
		setSize(800, 600);
		//不允许改变窗口大小
		setResizable(false);
		//匿名类实现关闭窗口
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);//0表示正常退出
			}
		});
		//设置窗口标题
		setTitle("坦克大战");
		//设置背景颜色
		setBackground(Color.GREEN);
	}
	
	private class PaintThread implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				repaint();
				
				try {
					Thread.sleep(50);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
		}
		
	}
}
