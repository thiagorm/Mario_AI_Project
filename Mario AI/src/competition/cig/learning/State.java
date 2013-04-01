package competition.cig.learning;

public class State {
	
	private final int numberActions = 4;
	private double[] actions;
	
	public State()
	{
		this.actions = new double[numberActions];
		
		for(int i = 0; i < numberActions; i++)
		{
			this.actions[i] = 0;
		}
	}

	public double[] getActions() {
		return actions;
	}

	public void setActions(double[] actions) {
		this.actions = actions;
	}

	public int getNumberActions() {
		return numberActions;
	}
	
	

}
