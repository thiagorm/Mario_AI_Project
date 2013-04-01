package competition.cig.learning;

public class Map {
	
	private final int lines = 16;
	private final int col = 31;
	
	public State[][] map;
	private double[] max_state = {100, 100, 100, 100};
	
	private double max_reward = 100, reward = -3;

	public Map()
	{
		this.map = new State[lines][col];
		
		for(int i = 0; i < this.lines; i++)
		{
			for(int j = 0; j < this.col; j++)
			{
				System.out.println(j);
				map[i][j] = new State();
				if(j == this.col-1)
					map[i][j].setActions(max_state);
			}
		}
	}

	public int getLines() {
		return lines;
	}

	public int getCol() {
		return col;
	}

	public double getMax_reward() {
		return max_reward;
	}

	public void setMax_reward(double max_reward) {
		this.max_reward = max_reward;
	}

	public double getReward() {
		return reward;
	}

	public void setReward(double reward) {
		this.reward = reward;
	}
	
	
	
	

}
