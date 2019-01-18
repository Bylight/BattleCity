package singelton;

/**
 * 用于保存当前游戏状态信息的单例类
 */
public class GameState {
	private static final GameState mGameState = new GameState();
	private long startTime;
	private int level;// 游戏关卡数
	private int botCount;// 当前关卡最大电脑坦克数目
	private int score_1;
	private int score_2;
	private int[] levelNum;

	private GameState() {
		reset();
	}

	public static GameState getGameState() {
		return mGameState;
	}

	/**
	 * 返回当前关卡最大电脑坦克数目
	 * 
	 * @return 当前关卡最大坦克数目
	 */
	public int getBotCount() {
		return botCount;
	}

	/**
	 * 返回当前关卡数
	 * 
	 * @return 当前关卡数
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * 增加游戏关卡数
	 */
	public void addLevel() {
		level++;
		botCount++;
		if (level > 14) {
			levelNum = new int[14];
			level = 1;
			botCount = 20;
		}
	}
	/**
	 * 判断当前随机值是否可用
	 * @param i
	 * @return 		是否需要更换随机值
	 */
	public boolean judgeLeve(int i) {
		if (levelNum[i] == 0) {
			levelNum[i] = 1;
			return true;
		}
		return false;
	}

	public int getScore1() {
		return score_1;
	}

	public int getScore2() {
		return score_2;
	}

	public void addScore1(int i) {
		score_1 += i;
	}

	public void addScore2(int i) {
		score_2 += i;
	}

	public long getTotalTime() {
		return (System.currentTimeMillis() - startTime) / 1000;
	}

	public void startTime() {
		startTime = System.currentTimeMillis();
	}

	public int getHardBot() {
		return (int) (100 * Properties.getProperties().getRate());
	}

	public void reset() {
		level = 1;
		botCount = 20;
		score_1 = 0;
		score_2 = 0;
		levelNum = new int[15];
		Properties.getProperties().resetRate();
	}
}
