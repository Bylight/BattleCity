package frame;

import java.awt.Frame;
import java.awt.event.*;

/**
 * v0.02更新：
 * 	1. 使用setResizable()方法设置窗口大小不允许改变
 * 	2. 采用匿名类实现窗口关闭(重写WindowAdapter中的windowClosing方法)
 * 	3. 使用setTitle()方法设置窗口标题
 * @author javen
 *
 */

@SuppressWarnings("serial")
public class BasicFrame extends Frame {
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
	}
	
}
