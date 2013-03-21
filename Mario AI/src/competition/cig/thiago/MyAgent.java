package competition.cig.thiago;

import java.util.Random;

import ch.idsia.agents.Agent;
import ch.idsia.agents.SimpleAgent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

public class MyAgent implements Agent{

	boolean[] action = new boolean[Environment.numberOfButtons];
	String name = "MyAgent";
	
	protected boolean isMarioAbleToJump;
	protected boolean isMarioOnGround;
	public boolean isMarioAbleToShoot;
	protected int[] marioState = null;
	
	int count;
	
	byte[][] viewMatriz = null;
	int[] fullViewMatriz = null;
	float[] marioPos;
	int i, j, i_mario, j_mario;
	
	int zLevelScene = 1;
	int zLevelEnemies = 0;
	
	public MyAgent() {
        reset();
    }

    public void reset()
    {
    	action = new boolean[Environment.numberOfButtons];
        action[Mario.KEY_RIGHT] = true; 
        action[Mario.KEY_SPEED] = false;
    }

    public boolean[] getAction()
    {
    	
//    	if(killTheEnemy())
//		{
//			//action[Mario.KEY_SPEED]= action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
//    		action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
//		}
//    	else if(jumpObstacles())
//    	{	
//    		//action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
//    		action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
//    	}
//		else
//		{
//			action[Mario.KEY_JUMP] = false;
//		}
    	
    	action[Mario.KEY_RIGHT] = true;
		action[Mario.KEY_LEFT] = false;
		action[Mario.KEY_DOWN] = false;
		
		action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;

        return action;
    }


	public String getName() {
		return this.name;
	}


	public void giveIntermediateReward(float intermediateReward) {
	
		
	}
	
	public void integrateObservation(Environment environment) {
		
		viewMatriz = environment.getMergedObservationZZ(zLevelScene, zLevelEnemies);
		
		//fullViewMatriz = environment.getSerializedMergedObservationZZ(zLevelScene, zLevelEnemies);
		i = environment.getReceptiveFieldHeight();
		j = environment.getReceptiveFieldWidth();
		
		i_mario = (int) environment.getMarioFloatPos()[0];
		j_mario = (int) environment.getMarioFloatPos()[1];
		
//		System.out.println();
//		System.out.println("i: " + i);
//		System.out.println("j: " + j);
//		System.out.println();
		
		this.marioState = environment.getMarioState();
		
		isMarioAbleToJump = marioState[3] == 1;
		isMarioOnGround = marioState[2] == 1;
		isMarioAbleToShoot = marioState[4] == 1;
		
		marioView(viewMatriz);
	}

	
	public void setName(String name) {
		this.name = name;
	}

	public void marioView(byte[][] viewMatriz) 
    {
		System.out.println(" ...........................................................................................");
		System.out.print("     ");
		
		for (int j=0; j < viewMatriz.length; j++) 
		{
			System.out.print(String.format("%3d.", j));
		}
		
		System.out.println();
		
		for (int i=0; i < viewMatriz.length; i++)
		{
			System.out.print(String.format("%3d.", i));
			for (int j=0; j < viewMatriz.length; j++) 
			{
				if ((i == 9 ) && (j == 9)) 
				{
					System.out.print("   M");
				}
				else 
				{
					System.out.print(String.format("%4d", viewMatriz[i][j]));
				}
			}
			
			System.out.println();
		}
		
		System.out.println(" ...........................................................................................");
	}
	
	public boolean jumpObstacles()
	{
		boolean situation = false;
		
		if(this.viewMatriz[9][10] == -60 || this.viewMatriz[9][10] == -85  || this.viewMatriz[9][10] == -24 || this.viewMatriz[9][10] == 80 || this.viewMatriz[9][11] == 80)
			situation = true;
		else
			situation = false;
		
		return situation;
	}	
	
	public boolean killTheEnemy()
	{
		boolean situation = false;
		
		if((this.viewMatriz[9][10] == 80 || this.viewMatriz[9][11] == 80) && isMarioAbleToShoot)
			situation = true;
		else
			situation = false;
			
		return situation;
	}
}
