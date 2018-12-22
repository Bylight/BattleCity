package frame;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * v0.09
 * 	在keyMoniitor()中添加keyReleased()方法令按键松开时停下tank
 * @author bylight
 *
 */

@SuppressWarnings("serial")
public class BasicFrame extends Frame {
	private static final int FRAME_WIDTH = 800;
	private static final int FRAME_HEIGHT = 600;
	private static final Color BACKGROUND_COLOR = Color.WHITE;
	private static int x_tank = 50;
	private static int y_tank = 50;
	//虚拟缓存
	private Image offScreenImage = null;
	
	private Tank myTank = new Tank(x_tank, y_tank);
	
	// 画出代表坦克的实心圆
	// g为前景色
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		myTank.draw(g);
	}

	public void lauchFrame() {
		
		setLocation(400, 300);
		setSize(FRAME_WIDTH, FRAME_HEIGHT);
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
		setBackground(BACKGROUND_COLOR);
		
		//Runnable接口实现类的实例作为Thread的target参数传入Thread
		new Thread(new PaintThread()).start();
		
		this.addKeyListener(new KeyMonitor());
		
		setVisible(true);
		
	}
	
	//重写update
	public void update(Graphics g) {
		//初始化offScreenImage
		if (offScreenImage == null) {
			offScreenImage = this.createImage(FRAME_WIDTH, FRAME_HEIGHT);
		}
		//获取offScreenImage的画笔
		Graphics gOffScreen = offScreenImage.getGraphics();
		//修改画笔颜色为背景色
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(BACKGROUND_COLOR);
		//重画前刷新背景
		gOffScreen.fillRect(0, 0, FRAME_WIDTH , FRAME_HEIGHT );
		//还原画笔颜色
		gOffScreen.setColor(c);
		//将获取的画笔传入paint(使用offScreenImage的画笔
		//即在offScreenImage上进行paint)
		paint(gOffScreen);
		
		//画笔g是用于画前台缓存
		g.drawImage(offScreenImage, 0, 0, null);
	}

	//实现Runnable接口的内部类
	private class PaintThread implements Runnable {
		@Override
		public void run() {
			// TODO Auto-generated method stub
			while (true) {
				//调用Frame的repaint()
				//Frame中实际调用顺序repaint()->update()->paint()
				repaint();
				
				//通过sleep方法令线程定时休眠
				try {
					Thread.sleep(100);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	
	//继承KeyAdpater的键盘监听类
	//也可以通过实现KeyListener接口，但KeyListener接口中有三个方法必须实现，较不灵活
	private class KeyMonitor extends KeyAdapter {

		@Override
		public void keyReleased(KeyEvent e) {
			// TODO Auto-generated method stub
			myTank.keyReleased(e);
		}

		@Override
		public void keyPressed(KeyEvent e) {
			// TODO Auto-generated method stub
			myTank.keyPressed(e);
		}
		
	}
}