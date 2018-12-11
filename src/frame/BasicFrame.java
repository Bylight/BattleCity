package frame;

import java.awt.Frame;
import java.awt.event.*;

/**
 * v0.02���£�
 * 	1. ʹ��setResizable()�������ô��ڴ�С������ı�
 * 	2. ����������ʵ�ִ��ڹر�(��дWindowAdapter�е�windowClosing����)
 * 	3. ʹ��setTitle()�������ô��ڱ���
 * @author javen
 *
 */

@SuppressWarnings("serial")
public class BasicFrame extends Frame {
	public void lauchFrame() {
		setVisible(true);
		setLocation(400, 300);
		setSize(800, 600);
		//������ı䴰�ڴ�С
		setResizable(false);
		//������ʵ�ֹرմ���
		addWindowListener(new WindowAdapter() {
			public void windowClosing(WindowEvent e) {
				System.exit(0);//0��ʾ�����˳�
			}
		});
		//���ô��ڱ���
		setTitle("̹�˴�ս");
	}
	
}
