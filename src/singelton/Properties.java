package singelton;

import enumtype.*;

/**
 * ���ڱ�����Ϸ���õĵ�����
 */
public class Properties {
	private static final Properties mProperties = new Properties();
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private int playerNum;// �����
	private boolean isBGMon;// BGM״̬
	private int botMaxInMap;// ����������̹����
	private boolean isSoundOn;// ��Ϸ��Ч״̬
	private int gameMode;// ��Ϸ�Ѷ�
	private double rate;
	private double tempRate;

	private Properties() {
		isBGMon = true;
		playerNum = 1;
		isSoundOn = true;
		gameMode = 1;
		botMaxInMap = 6;
		rate = 0.3;
		tempRate = rate;
	}

	public static Properties getProperties() {
		return mProperties;
	}

	public int getWidth() {
		return WIDTH;
	}

	public int getHeight() {
		return HEIGHT;
	}

	/**
	 * ��ȡBGM״̬
	 * 
	 * @return BGM����״̬
	 */
	public boolean getBGMState() {
		return isBGMon;
	}

	/**
	 * �ر�BGM
	 */
	public void closeBGM() {
		isBGMon = false;
	}

	/**
	 * ����BGM
	 */
	public void turnOnBGM() {
		isBGMon = true;
	}

	/**
	 * ��ȡ��Ϸ��Ч״̬
	 * 
	 * @return ��Ϸ��Ч����״̬
	 */
	public boolean getSoundState() {
		return isSoundOn;
	}

	/**
	 * �ر���Ϸ��Ч
	 */
	public void closeSound() {
		isSoundOn = false;
	}

	/**
	 * ����BGM
	 */
	public void turnOnSound() {
		isSoundOn = true;
	}

	/**
	 * ��ȡ�����Ŀ
	 * 
	 * @return �����Ŀ
	 */
	public int getPalyerNum() {
		return playerNum;
	}

	/**
	 * �޸��������Ϊ1
	 */
	public void setOnePlayer() {
		playerNum = 1;
	}

	/**
	 * �޸��������Ϊ2
	 */
	public void setTwoPlayer() {
		playerNum = 2;
	}

	public void toEasyMode() {
		botMaxInMap = 6;
		gameMode = 1;
		rate = 0.3;
	}

	public void toMiddleMode() {
		botMaxInMap = 8;
		gameMode = 2;
		rate = 0.5;
	}

	public void toHardMode() {
		botMaxInMap = 10;
		gameMode = 3;
		rate = 0.7;
	}

	public int getBotMaxInMap() {
		return botMaxInMap;
	}

	public int getGameMode() {
		return gameMode;
	}

	public double getRate() {
		return tempRate;
	}

	public void addRate() {
		tempRate += 0.05;
	}

	public void resetRate() {
		tempRate = rate;
	}
}
