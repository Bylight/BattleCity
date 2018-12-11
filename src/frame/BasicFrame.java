package frame;
import java.awt.Frame;

@SuppressWarnings("serial")
public class BasicFrame extends Frame {
	public void lauchFrame() {
		this.setLocation(400, 300);
		this.setSize(800, 600);
		this.setVisible(true);
	}
	
}
