package singelton;

import enumtype.*;

/**
 * 用于保存游戏配置的单例类
 */
public class Properties {
	private static final Properties mProperties = new Properties();
	private static final int WIDTH = 800;
	private static final int HEIGHT = 600;

	private int playerNum;// 玩家数
	private boolean isBGMon;// BGM状态
	private int botMaxInMap;// 场上最大电脑坦克数
	private boolean isSoundOn;// 游戏音效状态
	private int gameMode;// 游戏难度
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
	 * 获取BGM状态
	 * 
	 * @return BGM开关状态
	 */
	public boolean getBGMState() {
		return isBGMon;
	}

	/**
	 * 关闭BGM
	 */
	public void closeBGM() {
		isBGMon = false;
	}

	/**
	 * 开启BGM
	 */
	public void turnOnBGM() {
		isBGMon = true;
	}

	/**
	 * 获取游戏音效状态
	 * 
	 * @return 游戏音效开关状态
	 */
	public boolean getSoundState() {
		return isSoundOn;
	}

	/**
	 * 关闭游戏音效
	 */
	public void closeSound() {
		isSoundOn = false;
	}

	/**
	 * 开启BGM
	 */
	public void turnOnSound() {
		isSoundOn = true;
	}

	/**
	 * 获取玩家数目
	 * 
	 * @return 玩家数目
	 */
	public int getPalyerNum() {
		return playerNum;
	}

	/**
	 * 修改玩家人数为1
	 */
	public void setOnePlayer() {
		playerNum = 1;
	}

	/**
	 * 修改玩家人数为2
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
