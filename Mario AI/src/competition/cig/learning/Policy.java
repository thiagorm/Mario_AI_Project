package competition.cig.learning;

public class Policy {
	
	public Policy()
	{
		
	}
	
	
	public double getQValue(State state, int action)
	{
		double qValue = 0;
		
		qValue = state.getActions()[action];
		
		return qValue;
	}
	
	public double getMaxQValue(State state)
	{
		double maxQValue = 0;
		
		maxQValue = verifyHigherValue(state);
		
		return maxQValue;
	}
	
	public void setQValue(State state, int action, double newQVAlue)
	{
		double[] actions;
		
		actions = state.getActions();
		
		if(actions[0] != 100 || actions[1] != 100 || actions[2] != 100 || actions[3] != 100)
		{
			for(int i = 0; i < state.getNumberActions(); i++)
			{
				if(i == action)
				{
					actions[i] = newQVAlue;
				}
			}
			
			state.setActions(actions);
		}

	}
	
	
	private double verifyHigherValue(State state)
	{
		double maior = -Double.MAX_VALUE;
		
		for(int i = 0; i < state.getNumberActions(); i++)
		{
			if(state.getActions()[i] > maior)
			{
				maior = state.getActions()[i];
			}
		}
		
		return maior;
	}

}
