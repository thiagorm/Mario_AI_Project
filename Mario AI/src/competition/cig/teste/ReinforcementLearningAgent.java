package competition.cig.teste;

import java.util.Random;

import javax.swing.JViewport;

import org.junit.runner.RunWith;
import org.objectweb.asm.commons.TryCatchBlockSorter;

import com.sun.xml.internal.ws.api.pipe.NextAction;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

public class ReinforcementLearningAgent implements Agent{

	private final int line = 16;
	private final int col = 31;
	private final int max_episodios = 10000;
	
	boolean[] action = new boolean[Environment.numberOfButtons];
	private String name = "MyAgent";
	
	protected boolean isMarioAbleToJump;
	protected boolean isMarioOnGround;
	public boolean isMarioAbleToShoot;
	protected int[] marioState = null;
	
	//private boolean walk;
	private boolean walkAiCondition = false, isCalculate = true;
	boolean right = false;
	boolean jumpLeft = false;
	boolean activeRandom = true;
	float[] full;
	
	
	private int count;
	
	private float[] marioPosition;
	private byte[][] viewMatriz = null;
	int[] marioCenter;
	private int i = 0, j = 0, i_viewMatriz = 9, j_viewMatriz = 9;
	private int new_i = 0, old_i = 0, new_j = 0, old_j = 0, number_actions = 4;
	private float alfa = (float) 1, gama = (float)1;
	
	private Directions[][] map = new Directions[line][col];
	private int[][] visitas = new int[line][col];
	
	private Random rand = new Random();
	private int numRand = 0, jumpRand = 0, walkRunRand = 0, randEGreedy = 0;
	private float maior;
	
	private float wall = -60, reward = (float)-1, max_reward = 100, min_reward = -100, eGreedy = (float) 1,collision = -10, reward_movFor = 10, reward_jump_right = 15, reward_jump_left = -20;
	private float up_max = 0, right_max = 0, left_max = 0, down_max = 0;
	
	private int zLevelScene = 1;
	private int zLevelEnemies = 0;
	
	public ReinforcementLearningAgent() {
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
    	
//		map[11][col-1].setUp(max_reward);
//		map[11][col-1].setDown(max_reward);
//    	map[11][col-1].setRight(max_reward);
//    	map[11][col-1].setLeft(max_reward);
    	
    	for(int l = 0; l < line; l++)
    	{
	    	for(int k = 0; k < col; k++)
	    	{
	    		if(k == col-1)
	    		{
		    		map[l][k].setUp(max_reward);
		    		map[l][k].setDown(max_reward);
		        	map[l][k].setRight(max_reward);
		        	map[l][k].setLeft(max_reward);
	    		}
//	    		if(k == 0)
//	    		{
//		    		map[l][k].setUp(min_reward);
//		    		map[l][k].setDown(min_reward);
//		        	map[l][k].setRight(min_reward);
//		        	map[l][k].setLeft(min_reward);
//	    		}
	    	}
    	}
    }

	public boolean[] getAction() {
	
		
		action[Mario.KEY_RIGHT] = false;
		action[Mario.KEY_LEFT] = false;
		action[Mario.KEY_DOWN] = false;
		action[Mario.KEY_JUMP] = false;
		action[Mario.KEY_SPEED] = false;
		
		
		if(walkAiCondition)
		{
				
			action[Mario.KEY_SPEED] = true;
			
			if(map[i][j].getUp() >  map[i][j].getLeft() && map[i][j].getUp() >  map[i][j].getRight() && map[i][j].getUp() > map[i][j].getDown())
			{
				
					action[Mario.KEY_RIGHT] = false;
					action[Mario.KEY_LEFT] = false;
					action[Mario.KEY_DOWN] = false;
					action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;		
						
			}
		    else if(map[i][j].getRight() > map[i][j].getUp() && map[i][j].getRight() > map[i][j].getLeft() && map[i][j].getRight() > map[i][j].getDown())
			{
					action[Mario.KEY_JUMP] = false;
					action[Mario.KEY_LEFT] = false;
					action[Mario.KEY_DOWN] = false;
					action[Mario.KEY_RIGHT] = true;
					
					
			}
			else if(map[i][j].getLeft() > map[i][j].getUp() && map[i][j].getLeft() > map[i][j].getRight() && map[i][j].getLeft() > map[i][j].getDown())
			{
					action[Mario.KEY_RIGHT] = false;
					action[Mario.KEY_JUMP] = false;
					action[Mario.KEY_DOWN] = false;
					action[Mario.KEY_LEFT] = true;
					
			}
			else if(map[i][j].getDown() > map[i][j].getUp() && map[i][j].getDown() > map[i][j].getRight() && map[i][j].getDown() > map[i][j].getLeft())
			{
					action[Mario.KEY_LEFT] = false;
					action[Mario.KEY_RIGHT] = false;
					action[Mario.KEY_JUMP] = false;
					action[Mario.KEY_DOWN] = false;
			}

		}
		if(!walkAiCondition)
		{
				numRand =  rand.nextInt(4) + 1;

				if(numRand == 1)//LEFT
				{
				
					action[Mario.KEY_RIGHT] = false;
					action[Mario.KEY_DOWN] = false;
					action[Mario.KEY_JUMP] = false;
					action[Mario.KEY_SPEED] = false;
					action[Mario.KEY_LEFT] = true;
					
					jumpRand = rand.nextInt(2)+1;
					
					if(jumpRand == 1)//JUMP
					{
						action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
						
						if( i > 0 && j < col-1 && j > 0)//verificar limite superior e lado direito da tela
						{
							up_max = map[i-1][j].getUp();
							right_max = map[i-1][j].getRight();
							left_max = map[i-1][j].getLeft();
							down_max = map[i-1][j].getDown();
							
							this.maior = verifyMaior(up_max, right_max, left_max, down_max);
							
							map[i][j].setUp(map[i][j].getUp() + alfa * (reward + ((gama * maior) - map[i][j].getUp())));
							
						}
					}
					else//NOT JUMP
						action[Mario.KEY_JUMP] = false;
					
					walkRunRand = rand.nextInt(2)+1;
					
					if(walkRunRand == 1)//RUN
					{
						action[Mario.KEY_SPEED] = true;
					}
					else//WALK
						action[Mario.KEY_SPEED] = false;
					
					if(j > 0 && j < col-1)//verificar limite do lado esquerdo da tela
					{
						if(viewMatriz[i_viewMatriz][j_viewMatriz-1] != -60)//verificar se proximo estado é parede
						{
							up_max = map[i][j-1].getUp();
							right_max = map[i][j-1].getRight();
							left_max = map[i][j-1].getLeft();
							down_max = map[i][j-1].getDown();
							
							this.maior = verifyMaior(up_max, right_max, left_max, down_max);
							
							map[i][j].setLeft(map[i][j].getLeft() + alfa * (reward + (gama * maior) - map[i][j].getLeft()));
						}
					}	
				}
				else if(numRand == 2)//STAY
				{
					
					action[Mario.KEY_RIGHT] = false;
					action[Mario.KEY_DOWN] = false;
					action[Mario.KEY_JUMP] = false;
					action[Mario.KEY_SPEED] = false;
					action[Mario.KEY_LEFT] = false;
					
					jumpRand = rand.nextInt(2)+1;
					
					if(jumpRand == 1)//JUMP
					{
						action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
						
						if( i > 0 && j < col-1 && j > 0)//verificar limite superior e lado direito da tela
						{
							up_max = map[i-1][j].getUp();
							right_max = map[i-1][j].getRight();
							left_max = map[i-1][j].getLeft();
							down_max = map[i-1][j].getDown();
							
							this.maior = verifyMaior(up_max, right_max, left_max, down_max);
							
							map[i][j].setUp(map[i][j].getUp() + alfa * (reward + (gama * maior) - map[i][j].getUp()));
						}
					}
					else//NOT JUMP
						action[Mario.KEY_JUMP] = false;
					
					walkRunRand = rand.nextInt(2)+1;
					
					if(walkRunRand == 1)//RUN
					{
						action[Mario.KEY_SPEED] = true;
					}
					else//WALK
						action[Mario.KEY_SPEED] = false;
					
					
					if(i < line && j < col-1 && j > 0)//verificar limite inferior e lado direito da tela
					{
						if(viewMatriz[i_viewMatriz+1][j_viewMatriz] != -60)//verificar se mario não está no chão
						{
							up_max = map[i+1][j].getUp();
							right_max = map[i+1][j].getRight();
							left_max = map[i+1][j].getLeft();
							down_max = map[i+1][j].getDown();
							
							this.maior = verifyMaior(up_max, right_max, left_max, down_max);
							
							map[i][j].setDown(map[i][j].getDown() + alfa * (reward + (gama * maior) - map[i][j].getDown()));
						}
					}
				}
				else if(numRand == 3)//RIGHT
				{
					
					action[Mario.KEY_DOWN] = false;
					action[Mario.KEY_JUMP] = false;
					action[Mario.KEY_SPEED] = false;
					action[Mario.KEY_LEFT] = false;
					action[Mario.KEY_RIGHT] = true;
					
					jumpRand = rand.nextInt(2)+1;
					
					if(jumpRand == 1)//JUMP
					{
						action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
						
						if( i > 0 && j < col-1 && j > 0)//verificar limite superior e lado direito da tela
						{
							up_max = map[i-1][j].getUp();
							right_max = map[i-1][j].getRight();
							left_max = map[i-1][j].getLeft();
							down_max = map[i-1][j].getDown();
							
							this.maior = verifyMaior(up_max, right_max, left_max, down_max);
							
							map[i][j].setUp(map[i][j].getUp() + alfa * (reward + (gama * maior) - map[i][j].getUp()));
						}
					}
					else//NOT JUMP
						action[Mario.KEY_JUMP] = false;
					
					walkRunRand = rand.nextInt(2)+1;
					
					if(walkRunRand == 1)//RUN
					{
						action[Mario.KEY_SPEED] = true;
					}
					else//WALK
						action[Mario.KEY_SPEED] = false;
				
					if(j > 0 && j < col-1)//verificar limite do lado direito da tela
					{
						if(viewMatriz[i_viewMatriz][j_viewMatriz+1] != -60)
						{
							right = true;
							up_max = map[i][j+1].getUp();
							right_max = map[i][j+1].getRight();
							left_max = map[i][j+1].getLeft();
							down_max = map[i][j+1].getDown();

							this.maior = verifyMaior(up_max, right_max, left_max, down_max);
							
							map[i][j].setRight(map[i][j].getRight() + alfa * (reward + (gama * maior) - map[i][j].getRight()));
							
							//map[i][j].setRight((1 - alfa) * map[i][j].getRight() + alfa * (reward + gama * maior));
							
							//map[i][j].setRight(map[i][j].getRight() + ((alfa * (1/(visitas[i][j] + 1)) * (reward + (gama * maior) - map[i][j].getRight()))));
							
							//visitas[i][j]++;
						}
					}	
				}
				else if(numRand == 4)//PLUS RIGHT
				{
					action[Mario.KEY_DOWN] = false;
					action[Mario.KEY_JUMP] = false;
					action[Mario.KEY_SPEED] = false;
					action[Mario.KEY_LEFT] = false;
					action[Mario.KEY_RIGHT] = true;
					
					jumpRand = rand.nextInt(2)+1;
					
					if(jumpRand == 1)//JUMP
					{
						action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
						
						if( i > 0 && j < col-1 && j > 0)//verificar limite superior e lado direito da tela
						{
								up_max = map[i-1][j].getUp();
								right_max = map[i-1][j].getRight();
								left_max = map[i-1][j].getLeft();
								down_max = map[i-1][j].getDown();
								
								this.maior = verifyMaior(up_max, right_max, left_max, down_max);
								
								map[i][j].setUp(map[i][j].getUp() + alfa * (reward + (gama * maior) - map[i][j].getUp()));
						}	
					}
					else//NOT JUMP
						action[Mario.KEY_JUMP] = false;
					
					walkRunRand = rand.nextInt(2)+1;
					
					if(walkRunRand == 1)//RUN
					{
						action[Mario.KEY_SPEED] = true;
					}
					else//WALK
						action[Mario.KEY_SPEED] = false;
				
					if((j < col-1 && j > 0))//verificar limite do lado direito da tela
					{
						if(viewMatriz[i_viewMatriz][j_viewMatriz+1] != -60)
						{
							right = true;
							up_max = map[i][j+1].getUp();
							right_max = map[i][j+1].getRight();
							left_max = map[i][j+1].getLeft();
							down_max = map[i][j+1].getDown();

							this.maior = verifyMaior(up_max, right_max, left_max, down_max);
							
							map[i][j].setRight(map[i][j].getRight() + alfa * (reward + (gama * maior) - map[i][j].getRight()));
						}
					}	
				}
				
				if(this.j == col-1)
				{
					this.count++;
				}
				
				if(this.count == max_episodios)
				{
					this.walkAiCondition = true;
				}
			}
		
		return action;
	}

	public void integrateObservation(Environment environment) {
		
		viewMatriz = environment.getMergedObservationZZ(zLevelScene, zLevelEnemies);
		
		marioCenter = environment.getMarioReceptiveFieldCenter();
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
		
		System.out.println("Contador: " + count);
		
		System.out.println();
		
		
		//System.out.println(viewMatriz.length);
		printEnviroment();
		
		//calculateAI();
		
	}

	public void giveIntermediateReward(float intermediateReward) {
		
		//System.out.println(intermediateReward);
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
		float maior = -Float.MAX_VALUE;
		float[] max_reward = new float[number_actions];
		
		max_reward[0] = up_max;
		max_reward[1] = right_max;
		max_reward[2] = left_max;
		max_reward[3] = down_max;
	
		
		for(int i = 0; i < number_actions; i++)
		{
			if(max_reward[i] > maior)
				maior = max_reward[i];
		}
		
		return maior;
	}
	
	private void printEnviroment()
	{
			System.out.println(map[i][j].getUp() + " " + map[i][j].getDown() + " " + map[i][j].getRight() + " " + map[i][j].getLeft());
			System.out.println();

	}

}
