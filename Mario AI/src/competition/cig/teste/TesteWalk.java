package competition.cig.teste;

import java.util.Random;

import javax.swing.JViewport;

import org.objectweb.asm.commons.TryCatchBlockSorter;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

public class TesteWalk implements Agent{

	private final int line = 16;
	private final int col = 101;
	
	boolean[] action = new boolean[Environment.numberOfButtons];
	String name = "MyAgent";
	
	protected boolean isMarioAbleToJump;
	protected boolean isMarioOnGround;
	public boolean isMarioAbleToShoot;
	protected int[] marioState = null;
	
	//private boolean walk;
	private boolean walkAiCondition = true, isCalculate = true;
	boolean jumpRight = false;
	boolean jumpLeft = false;
	
	
	int count;
	
	private float[] marioPosition;
	byte[][] viewMatriz = null;
	private int i = 0, j = 0, i_viewMatriz = 9, j_viewMatriz = 9;
	private int new_i = 0, old_i = 0, new_j = 0, old_j = 0;
	private float alfa = 1, gama = 1;
	
	private Directions [] [] map = new Directions [line][col];
	
	Random rand = new Random();
	int numRand = 0;
	float maior;
	
	private float wall = -60, reward = (float)-0.5;
	private float up_max = 0, right_max = 0, left_max = 0, down_max = 0;
	
	int zLevelScene = 1;
	int zLevelEnemies = 0;
	
	public TesteWalk() {
        reset();
    }

    public void reset()
    {
    	for(int i = 0; i < line; i++)
    	{
    		for(int j = 0; j < col; j++)
    		{
    			map[i][j] = new Directions();
    		}
    	}
    	
    	for(int l = 0; l < line; l++)
    	{
	    	for(int k = 0; k < col; k++)
	    	{
	    		if(k == col-1)
	    		{
		    		map[l][k].setUp(100);
		        	map[l][k].setRight(100);
		        	map[l][k].setLeft(100);
	    		}
	    	}
    	}
    }

	public boolean[] getAction() {
		
		walkAiCondition = false;
		
			if(map[i][j].getRight() > map[i][j].getUp() && map[i][j].getRight() > map[i][j].getLeft() && map[i][j].getRight() > map[i][j].getDown())
			{
				if(map[i][j].getRight() > 0)
				{
					action[Mario.KEY_JUMP] = false;
					action[Mario.KEY_LEFT] = false;
					action[Mario.KEY_DOWN] = false;
					
					action[Mario.KEY_RIGHT] = true;
					
					walkAiCondition = true;
				}
			}
			else if(map[i][j].getLeft() > map[i][j].getUp() && map[i][j].getLeft() > map[i][j].getRight() && map[i][j].getLeft() > map[i][j].getDown())
			{
				if(map[i][j].getLeft() > 0)
				{
					action[Mario.KEY_RIGHT] = false;
					action[Mario.KEY_JUMP] = false;
					action[Mario.KEY_DOWN] = false;
					
					action[Mario.KEY_LEFT] = true;
					
					walkAiCondition = true;
				}
			}
				
			
			if(!walkAiCondition)
			{
				
				numRand =  rand.nextInt(2) + 1;
				
				if(numRand == 1)//RIGHT
				{
					
					jumpLeft = false;
					jumpRight = false;
					
					action[Mario.KEY_JUMP] = false;
					action[Mario.KEY_LEFT] = false;
					action[Mario.KEY_DOWN] = false;
					
					action[Mario.KEY_RIGHT] = true;
					
					if(j + 1 < col)
					{
						if(viewMatriz[i_viewMatriz][j_viewMatriz+1] != -60)
						{
							up_max = map[i][j+1].getUp();
							right_max = map[i][j+1].getRight();
							left_max = map[i][j+1].getLeft();
							down_max = map[i][j+1].getDown();
							
							this.maior = verifyMaior(up_max, right_max, left_max, down_max);
							
							map[i][j].setRight(map[i][j].getRight() + (alfa * (reward + (gama * maior) - map[i][j].getRight())));

						}
					}
				}
				else if(numRand == 2)//LEFT
				{
					jumpLeft = false;
					jumpRight = false;
					action[Mario.KEY_DOWN] = false;
					
					action[Mario.KEY_RIGHT] = false;
					action[Mario.KEY_JUMP] = false;
					
					action[Mario.KEY_LEFT] = true;
					
					if(j - 1 > 0)
					{
						if(viewMatriz[i_viewMatriz][j_viewMatriz-1] != -60)
						{
							up_max = map[i][j-1].getUp();
							right_max = map[i][j-1].getRight();
							left_max = map[i][j-1].getLeft();
							down_max = map[i][j-1].getDown();
							
							this.maior = verifyMaior(up_max, right_max, left_max, down_max);
							
							map[i][j].setLeft(map[i][j].getLeft() + (alfa * (reward + (gama * maior) - map[i][j].getLeft())));

						}
					}
				
			}
		}
		
		return action;
	}

	public void integrateObservation(Environment environment) {
		
		viewMatriz = environment.getMergedObservationZZ(zLevelScene, zLevelEnemies);
		
		marioPosition = environment.getMarioFloatPos();
		
		j = (int) marioPosition[0] / 16;
		i = (int) marioPosition[1] / 16;
		
		this.marioState = environment.getMarioState();
		
		isMarioAbleToJump = marioState[3] == 1;
		isMarioOnGround = marioState[2] == 1;
		isMarioAbleToShoot = marioState[4] == 1;
	
		System.out.println("i: " + i);
		System.out.println("j: " + j);
		
		System.out.println();
	
		//System.out.println(viewMatriz.length);
		printEnviroment();
		
		calculateAI();
	}

	public void giveIntermediateReward(float intermediateReward) {
		
		
	}

	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	
	private boolean[] calculateAI()
	{
		
		return action;
	}
	
	
	private void walkAI()
	{
		this.walkAiCondition = false;
	}
	
	private float verifyMaior(float up_max, float right_max, float left_max, float down_max)
	{
		float maior = 0;
		
		if(up_max >= right_max && up_max >= left_max && up_max >= down_max)
			maior = up_max;
		else if(right_max >= up_max && right_max >= left_max && right_max >= down_max)
			maior = right_max;
		else if(left_max >= up_max && left_max >= right_max && left_max >= down_max)
			maior = left_max;
		else if(down_max >= up_max && down_max >= right_max && down_max >= left_max)
			maior = down_max;
		
		return maior;
	}
	
	private void printEnviroment()
	{
			System.out.println(map[i][j].getUp() + " " + map[i][j].getDown() + " " + map[i][j].getRight() + " " + map[i][j].getLeft());
			System.out.println();

	}

}

