package competition.cig.learning;

import java.util.Random;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

public class ReinforcementLearningAgent implements Agent{

	private String name = "ReinforcementAgent";
	
	private final int max_episodios = 800;
	
	boolean[] array_action = new boolean[Environment.numberOfButtons];
	
	protected boolean isMarioAbleToJump;
	protected boolean isMarioOnGround;
	public boolean isMarioAbleToShoot;
	protected int[] marioState = null;
	private float[] marioPosition;
	private byte[][] viewMatriz = null;
	int[] marioCenter;
	private int zLevelScene = 1;
	private int zLevelEnemies = 0;
	
	private int i = 0, j = 0, iViewMatriz = 0, jViewMatriz = 0;
	
	private Random rand;
	private int random = 0, jumpRandom = 0, walkRandom = 0, eGreedy= 0, count = 0;

	private Map grid;
	private double alfa, gamma, reward;
	private double qValue = 0, maxQValue = 0, newQValue = 0;
	private State nextState;
	int action = 0;
	private Policy policy;
	boolean isWalk = false;
	
	FileClass fileClass;
	
	private double ground = -60;
	private double tube = -85;
	private double coin = 1;
	private double square = -24;
	private double floorHill = -62;

	public void reset() 
	{
		rand = new Random();
		this.policy = new Policy();
		this.alfa = 1;
		this.gamma = 1;
		this.eGreedy = 2;
		
		this.grid = new Map();
		reward = grid.getReward();
		fileClass = new FileClass();
	}
	
	public boolean[] getAction() 
	{
		
		array_action[Mario.KEY_DOWN] = false;
		array_action[Mario.KEY_JUMP] = false;
		array_action[Mario.KEY_LEFT] = false;
		array_action[Mario.KEY_RIGHT] = false;
		array_action[Mario.KEY_SPEED] = false;
		
		
		if(isWalk)
		{
			this.action = this.verifyHigherAction(grid.map[i][j]);
			
			array_action = this.verifyAction(action);
			array_action[Mario.KEY_SPEED] = true;
		}
		
		
		if(!isWalk)
		{
			this.action = this.selectAction(grid.map[i][j]);
			nextState = this.selectNextState(this.action);
			
			if(nextState != null)
			{
				qValue = policy.getQValue(grid.map[i][j], action);
				maxQValue = policy.getMaxQValue(nextState);
			
				newQValue = grid.map[i][j].getActions()[action] + alfa * (reward + (gamma * maxQValue) - grid.map[i][j].getActions()[action]);
			
				policy.setQValue(grid.map[i][j], action, newQValue);
				
				array_action = verifyAction(action);
				
				if(action != 3)//CUIDADO
					array_action[Mario.KEY_RIGHT] = true;
			}
			
			if(this.j ==  grid.getCol()-1)
			{
				this.count++;
			}
			
			if(count == max_episodios)
			{
				isWalk = true;
				fileClass.writeFile(grid);
			}
		}
		
		
		return array_action;
	}


	public void integrateObservation(Environment environment) 
	{
		
		viewMatriz = environment.getMergedObservationZZ(zLevelScene, zLevelEnemies);
		
		marioCenter = environment.getMarioReceptiveFieldCenter();
		marioPosition = environment.getMarioFloatPos();
		
		iViewMatriz = marioCenter[0];
		jViewMatriz = marioCenter[1];
		
		j = (int) marioPosition[0] / 16;
		i = (int) marioPosition[1] / 16;
		
		this.marioState = environment.getMarioState();
		
		isMarioAbleToJump = marioState[3] == 1;
		isMarioOnGround = marioState[2] == 1;
		isMarioAbleToShoot = marioState[4] == 1;
		
		System.out.println("i: " + i);
		System.out.println("j: " + j);
		
		System.out.println();
		
		System.out.println("Contador: " + count);
		System.out.println();
		
		System.out.println("Action: " + action);
		System.out.println();
		
		this.debug();
		
	}
	
	
	public void giveIntermediateReward(float intermediateReward) 
	{
		
		
	}

	public void setName(String name) 
	{
		this.name = name;
	}
	
	public String getName() 
	{
		
		return name;
	}
	
	private int selectAction(State state)
	{
		int action = 0;
		
//		eGreedy = rand.nextInt(2) + 1;
//		
//		switch(eGreedy)
//		{
//			case 1:
				action = rand.nextInt(4);
//				break;
//				
//			case 2: 
//				action = verifyHigherAction(state);
//				break;
//		}
//		
		return action;
	}
	
	private int verifyHigherAction(State state)
	{
		int action = 0;
		double maior = -Double.MAX_VALUE;
		
		for(int i = 0; i < state.getNumberActions(); i++)
		{
			if(state.getActions()[i] > maior)
			{
				action = i;
				maior = state.getActions()[i];
			}
		}
		
		return action;
	}
	
	private State selectNextState(int action)
	{
		State nextState = null;
		
		switch(action)
		{
			case 0://UP
				if( i > 0 && j < grid.getCol()-1)
				{
					nextState = grid.map[i-1][j];
				}
				break;
			case 1://DOWN
				if( i > 0 && j < grid.getCol()-1)
				{
					if(viewMatriz[iViewMatriz+1][jViewMatriz] != ground)
							nextState = grid.map[i+1][j];
				}
				break;
			case 2://RIGHT
				if( i > 0 && j < grid.getCol()-1)
				{
					if(viewMatriz[iViewMatriz][jViewMatriz+1] != ground &&isMarioAbleToJump)//CUIDADO
						nextState = grid.map[i][j+1];
				}
				break;
			case 3://LEFT
				if(j > 0 && j < grid.getCol()-1)
				{
					if(viewMatriz[iViewMatriz][jViewMatriz-1] != ground && isMarioAbleToJump)//CUIDADO
						nextState = grid.map[i][j-1];
				}
				break;
		}
		
		return nextState;
	}
	
	private boolean[] verifyAction(int action)
	{
		array_action[Mario.KEY_SPEED] = true;
		switch(action)
		{
			case 0://UP
				array_action[Mario.KEY_RIGHT] = false;
				array_action[Mario.KEY_DOWN] = false;
				array_action[Mario.KEY_LEFT] = false;
				//array_action[Mario.KEY_SPEED] = false;
				array_action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
				break;
			case 1://STAY
				array_action[Mario.KEY_RIGHT] = false;
				array_action[Mario.KEY_LEFT] = false;
				array_action[Mario.KEY_JUMP] = false;
				array_action[Mario.KEY_DOWN] = false;
				array_action[Mario.KEY_SPEED] = false;
				break;
			case 2://RIGHT
				
				array_action[Mario.KEY_LEFT] = false;
				array_action[Mario.KEY_JUMP] = false;
				array_action[Mario.KEY_DOWN] = false;
				//array_action[Mario.KEY_SPEED] = false;
				array_action[Mario.KEY_RIGHT] = true;
				
//				jumpRandom = rand.nextInt(2) + 1;
//				
//				if(jumpRandom == 1)
//				{
//					array_action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
//				}
//				
//				walkRandom = rand.nextInt(2) + 1;
//				
//				if(walkRandom == 1)
//				{
//					array_action[Mario.KEY_SPEED] = true;
//				}
				break;
			case 3://LEFT
				array_action[Mario.KEY_JUMP] = false;
				array_action[Mario.KEY_DOWN] = false;
				array_action[Mario.KEY_RIGHT] = false;
				//array_action[Mario.KEY_SPEED] = false;
				array_action[Mario.KEY_LEFT] = true;
				
//				jumpRandom = rand.nextInt(2) + 1;
//				
//				if(jumpRandom == 1)
//				{
//					array_action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;	
//				}
//				
//				walkRandom = rand.nextInt(2) + 1;
//				
//				if(walkRandom == 1)
//				{
//					array_action[Mario.KEY_SPEED] = true;
//				}
				break;
		}
		
		return array_action;
	}
	
	private void debug()
	{
			System.out.println(grid.map[i][j].getActions()[0] + " " + grid.map[i][j].getActions()[1] + " " + grid.map[i][j].getActions()[2] + " " + grid.map[i][j].getActions()[3]);
			System.out.println();

	}

}
