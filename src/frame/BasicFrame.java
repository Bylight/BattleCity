package frame;

import java.awt.Color;
import java.awt.Frame;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * v0.041更新
 * 	问题：v0.04版本中重画频率太快导致“闪烁现象”
 * 	思路：使用“双缓冲”消除闪烁现象
 *  解决方法：将所有东西画在虚拟缓存中，实际显示的是另一个缓存 
 * @author bylight
 *
 */

@SuppressWarnings("serial")
public class BasicFrame extends Frame {
	
	private static int x_tank = 50;
	private static int y_tank = 50;
	//虚拟缓存
	private Image offScreenImage = null;
	
	// 画出代表坦克的实心圆
	// g为前景色
	@Override
	public void paint(Graphics g) {
		// TODO Auto-generated method stub
		Color c = g.getColor();	// c用于保存g的初始颜色
		
		g.setColor(Color.RED);	// 设置颜色为红
		g.fillOval(x_tank, y_tank, 30, 30);	// 参数依次为x, y, width, height
		
		g.setColor(c); 	//恢复g的初始颜色
		
		y_tank += 5;
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
		
		//Runnable接口实现类的实例作为Thread的target参数传入Thread
		new Thread(new PaintThread()).start();
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
	
	//重写update
	public void update(Graphics g) {
		//初始化offScreenImage
		if (offScreenImage == null) {
			offScreenImage = this.createImage(800, 600);
		}
		//获取offScreenImage的画笔
		Graphics gOffScreen = offScreenImage.getGraphics();
		//修改画笔颜色为背景色(此处为GREEN)
		Color c = gOffScreen.getColor();
		gOffScreen.setColor(Color.GREEN);
		//重画前刷新背景
		gOffScreen.fillRect(0, 0, 800, 600);
		//将获取的画笔传入paint(使用offScreenImage的画笔
		//即在offScreenImage上进行paint)
		paint(gOffScreen);
		
		//画笔g是用于画前台缓存
		g.drawImage(offScreenImage, 0, 0, null);
	}
}
