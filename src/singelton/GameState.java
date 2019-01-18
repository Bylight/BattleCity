package singelton;

/**
 * ���ڱ��浱ǰ��Ϸ״̬��Ϣ�ĵ�����
 */
public class GameState {
	private static final GameState mGameState = new GameState();
	private long startTime;
	private int level;// ��Ϸ�ؿ���
	private int botCount;// ��ǰ�ؿ�������̹����Ŀ
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
	 * ���ص�ǰ�ؿ�������̹����Ŀ
	 * 
	 * @return ��ǰ�ؿ����̹����Ŀ
	 */
	public int getBotCount() {
		return botCount;
	}

	/**
	 * ���ص�ǰ�ؿ���
	 * 
	 * @return ��ǰ�ؿ���
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * ������Ϸ�ؿ���
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
	 * �жϵ�ǰ���ֵ�Ƿ����
	 * @param i
	 * @return 		�Ƿ���Ҫ�������ֵ
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
