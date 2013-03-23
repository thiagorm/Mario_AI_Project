package competition.cig.thiago;

import java.util.Random;

import javax.swing.JViewport;

import org.objectweb.asm.commons.TryCatchBlockSorter;

import ch.idsia.agents.Agent;
import ch.idsia.benchmark.mario.engine.sprites.Mario;
import ch.idsia.benchmark.mario.environments.Environment;

public class ReinforcementAgent implements Agent{

	private final int line = 16;
	private final int col = 31;
	private final int max_episodios = 200;
	
	boolean[] action = new boolean[Environment.numberOfButtons];
	private String name = "MyAgent";
	
	protected boolean isMarioAbleToJump;
	protected boolean isMarioOnGround;
	public boolean isMarioAbleToShoot;
	protected int[] marioState = null;
	
	//private boolean walk;
	private boolean walkAiCondition = false, isCalculate = true;
	boolean jumpRight = false;
	boolean jumpLeft = false;
	
	
	private int count;
	
	private float[] marioPosition;
	private byte[][] viewMatriz = null;
	private int i = 0, j = 0, i_viewMatriz = 9, j_viewMatriz = 9;
	private int new_i = 0, old_i = 0, new_j = 0, old_j = 0;
	private float alfa = 1, gama = 1;
	
	private Directions[][] map = new Directions[line][col];
	private int[][] visitas = new int[line][col];
	
	private Random rand = new Random();
	private int numRand = 0, jumpRand = 0;
	private float maior;
	
	private float wall = -60, reward = (float)-0.5, max_reward = 100;
	private float up_max = 0, right_max = 0, left_max = 0, down_max = 0;
	
	private int zLevelScene = 1;
	private int zLevelEnemies = 0;
	
	public ReinforcementAgent() {
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
		    		map[l][k].setUp(max_reward);
		    		map[l][k].setDown(max_reward);
		        	map[l][k].setRight(max_reward);
		        	map[l][k].setLeft(max_reward);
	    		}
	    	}
    	}
    }

	public boolean[] getAction() {

		if(walkAiCondition)
		{
			
			action[Mario.KEY_RIGHT] = false;
			action[Mario.KEY_LEFT] = false;
			action[Mario.KEY_DOWN] = false;
			action[Mario.KEY_JUMP] = false;	
			
			if(map[i][j].getUp() >  map[i][j].getLeft() && map[i][j].getUp() >  map[i][j].getRight() && map[i][j].getUp() > map[i][j].getDown())
			{
				
				if(map[i][j].getUp() > 0)
				{
						action[Mario.KEY_RIGHT] = false;
						action[Mario.KEY_LEFT] = false;
						action[Mario.KEY_DOWN] = false;
						
						action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;		
						
						System.out.println("UP");
				}
			}
		    else if(map[i][j].getRight() > map[i][j].getUp() && map[i][j].getRight() > map[i][j].getLeft() && map[i][j].getRight() > map[i][j].getDown())
			{
				if(map[i][j].getRight() > 0)
				{
					action[Mario.KEY_JUMP] = false;
					action[Mario.KEY_LEFT] = false;
					action[Mario.KEY_DOWN] = false;
					
					action[Mario.KEY_RIGHT] = true;
					
					System.out.println("RIGHT");
					
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
					
					System.out.println("LEFT");
				}
			}
			else if(map[i][j].getDown() > map[i][j].getUp() && map[i][j].getDown() > map[i][j].getRight() && map[i][j].getDown() > map[i][j].getLeft())
			{
				if(map[i][j].getDown() > 0)
				{
					action[Mario.KEY_LEFT] = false;
					action[Mario.KEY_RIGHT] = false;
					action[Mario.KEY_JUMP] = false;
					action[Mario.KEY_DOWN] = false;
					
					System.out.println("DOWN");
				}
			}
//			else if(map[i][j].getUp() == map[i][j].getRight() && isMarioAbleToJump)
//			{
//				if(map[i][j].getUp() > 0 && map[i][j].getRight() > 0)
//				{
//					action[Mario.KEY_LEFT] = false;
//					action[Mario.KEY_JUMP] = false;
//					action[Mario.KEY_DOWN] = false;
//					action[Mario.KEY_RIGHT] = true;
//				}
//			}
//Supervisão de um agente externo
				
		}
		if(!walkAiCondition)
		{
			
			numRand =  rand.nextInt(6) + 1;
			
			if(numRand == 1)//UP
			{
				
				action[Mario.KEY_LEFT] = false;
				action[Mario.KEY_RIGHT] = true;//CUIDADO
				action[Mario.KEY_DOWN] = false;
				action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
				
				if( i > 0 && j < col-1)//verificar limite superior e lado direito da tela
				{
					if(isMarioAbleToJump)//calcular UP apenas quando mario estiver no chão
					{
						up_max = map[i-1][j].getUp();
						right_max = map[i-1][j].getRight();
						left_max = map[i-1][j].getLeft();
						down_max = map[i-1][j].getDown();
						
						this.maior = verifyMaior(up_max, right_max, left_max, down_max);
						
						map[i][j].setUp(map[i][j].getUp() + (alfa * (reward + (gama * maior) - map[i][j].getUp())));
						
						//map[i][j].setUp(map[i][j].getUp() + ((alfa * (1/(visitas[i][j] + 1))) * (reward + (gama * maior) - map[i][j].getUp())));
						
						//visitas[i][j]++;
					}
				}
				
				
			}
			else if(numRand == 2)//RIGHT
			{
				
				action[Mario.KEY_JUMP] = false;
				action[Mario.KEY_LEFT] = false;
				action[Mario.KEY_DOWN] = false;
				action[Mario.KEY_RIGHT] = true;
				
				if(j < col-1)//verificar limite do lado direito da tela
				{
					if(viewMatriz[i_viewMatriz][j_viewMatriz+1] != -60)//verificar se o proximo estado é uma parede
					{
						up_max = map[i][j+1].getUp();
						right_max = map[i][j+1].getRight();
						left_max = map[i][j+1].getLeft();
						down_max = map[i][j+1].getDown();
						
						this.maior = verifyMaior(up_max, right_max, left_max, down_max);
						
						map[i][j].setRight(map[i][j].getRight() + (alfa * (reward + (gama * maior) - map[i][j].getRight())));
						//map[i][j].setRight(map[i][j].getRight() + ((alfa * (1/(visitas[i][j] + 1)) * (reward + (gama * maior) - map[i][j].getRight()))));
						
						//visitas[i][j]++;
					}
				}
						
			}
			else if(numRand == 3)//LEFT
			{

				action[Mario.KEY_DOWN] = false;
				action[Mario.KEY_RIGHT] = false;
				action[Mario.KEY_JUMP] = false;
				action[Mario.KEY_LEFT] = true;
				
				if(j > 0 && j < col-1)//verificar limite do lado esquerdo da tela
				{
					if(viewMatriz[i_viewMatriz][j_viewMatriz-1] != -60)//verificar se proximo estado é parede
					{
						up_max = map[i][j-1].getUp();
						right_max = map[i][j-1].getRight();
						left_max = map[i][j-1].getLeft();
						down_max = map[i][j-1].getDown();
						
						this.maior = verifyMaior(up_max, right_max, left_max, down_max);
						
						map[i][j].setLeft(map[i][j].getLeft() + (alfa * (reward + (gama * maior) - map[i][j].getLeft())));
						//map[i][j].setLeft(map[i][j].getLeft() + ((alfa * (1/(visitas[i][j] + 1))) * (reward + (gama * maior) - map[i][j].getLeft())));
						
						//visitas[i][j]++;
					}
				}
			}
			else if(numRand == 4)//Jump Right
			{
				
				action[Mario.KEY_LEFT] = false;
				action[Mario.KEY_DOWN] = false;
				action[Mario.KEY_RIGHT] = true;
				action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
				
				if( i > 0 && j < col-1)
				{
					if(isMarioAbleToJump)
					{
						up_max = map[i-1][j].getUp();
						right_max = map[i-1][j].getRight();
						left_max = map[i-1][j].getLeft();
						down_max = map[i-1][j].getDown();
						
						this.maior = verifyMaior(up_max, right_max, left_max, down_max);
						
						map[i][j].setUp(map[i][j].getUp() + (alfa * (reward + (gama * maior) - map[i][j].getUp())));
						//map[i][j].setUp(map[i][j].getUp() + ((alfa * (1/(visitas[i][j] + 1))) * (reward + (gama * maior) - map[i][j].getUp())));
						
						//visitas[i][j]++;
					}
				}

				if(j < col-1)
				{
					if(viewMatriz[i_viewMatriz][j_viewMatriz+1] != -60)
					{
						if(!isMarioAbleToJump)
						{
							up_max = map[i][j+1].getUp();
							right_max = map[i][j+1].getRight();
							left_max = map[i][j+1].getLeft();
							down_max = map[i][j+1].getDown();
							
							this.maior = verifyMaior(up_max, right_max, left_max, down_max);
							
							map[i][j].setRight(map[i][j].getRight() + (alfa * (reward + (gama * maior) - map[i][j].getRight())));
							//map[i][j].setRight(map[i][j].getRight() + ((alfa * (1/(visitas[i][j] + 1)) * (reward + (gama * maior) - map[i][j].getRight()))));
							
							//visitas[i][j]++;
						}
					}
				}
				
				
			}
			else if(numRand == 5)//Jump Left
			{
				
				action[Mario.KEY_RIGHT] = false;
				action[Mario.KEY_DOWN] = false;
				action[Mario.KEY_LEFT] = true;
				action[Mario.KEY_JUMP] = isMarioAbleToJump || !isMarioOnGround;
				
				if( i > 0 && j < col-1)
				{
					if(isMarioAbleToJump)
					{
						up_max = map[i-1][j].getUp();
						right_max = map[i-1][j].getRight();
						left_max = map[i-1][j].getLeft();
						down_max = map[i-1][j].getDown();
						
						this.maior = verifyMaior(up_max, right_max, left_max, down_max);
						
						map[i][j].setUp(map[i][j].getUp() + (alfa * (reward + (gama * maior) - map[i][j].getUp())));
						//map[i][j].setUp(map[i][j].getUp() + ((alfa * (1/(visitas[i][j] + 1))) * (reward + (gama * maior) - map[i][j].getUp())));
						
						//visitas[i][j]++;
					}
				}
				
				if(j > 0 && j < col-1)
				{
					if(viewMatriz[i_viewMatriz][j_viewMatriz-1] != -60)
					{
						if(!isMarioAbleToJump)
						{
							up_max = map[i][j-1].getUp();
							right_max = map[i][j-1].getRight();
							left_max = map[i][j-1].getLeft();
							down_max = map[i][j-1].getDown();
							
							this.maior = verifyMaior(up_max, right_max, left_max, down_max);
							
							map[i][j].setLeft(map[i][j].getLeft() + (alfa * (reward + (gama * maior) - map[i][j].getLeft())));
							//map[i][j].setLeft(map[i][j].getLeft() + ((alfa * (1/(visitas[i][j] + 1))) * (reward + (gama * maior) - map[i][j].getLeft())));
							
							//visitas[i][j]++;
						}
					}
				}
				
			}
			else if(numRand == 6)
			{
				action[Mario.KEY_RIGHT] = false;
				action[Mario.KEY_LEFT] = false;
				action[Mario.KEY_DOWN] = false;				
				action[Mario.KEY_JUMP] = false;
				
				if(i < line && j < col-1)//verificar limite inferior e lado direito da tela
				{
					if(viewMatriz[i_viewMatriz+1][j_viewMatriz] != -60)//verificar se mario não está no chão
					{
						up_max = map[i+1][j].getUp();
						right_max = map[i+1][j].getRight();
						left_max = map[i+1][j].getLeft();
						down_max = map[i+1][j].getDown();
						
						this.maior = verifyMaior(up_max, right_max, left_max, down_max);
						
						map[i][j].setDown(map[i][j].getDown() + (alfa * (reward + (gama * maior) - map[i][j].getDown())));
						//map[i][j].setDown(map[i][j].getDown() + ((alfa * (1/(visitas[i][j] + 1))) * (reward + (gama * maior) - map[i][j].getDown())));
						
						//visitas[i][j]++;
					}
				}
				
			}
			
			if(this.j == col-1)
			{
				if(map[i][j].getUp() == 100 && map[i][j].getDown() == 100 && map[i][j].getRight() == 100 && map[i][j].getLeft() == 100)
					this.count++;
			}
			
			if(this.count == max_episodios)
			{
				this.walkAiCondition = true;
				action[Mario.KEY_RIGHT] = false;
				action[Mario.KEY_LEFT] = false;
				action[Mario.KEY_DOWN] = false;
				action[Mario.KEY_JUMP] = false;
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
		
		System.out.println("Contador: " + count);
		
		System.out.println();
		
		//System.out.println("Visitas: " + visitas[i][j]);
		
		
		//System.out.println(viewMatriz.length);
		printEnviroment();
		
		//calculateAI();
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
		
		if(up_max > right_max && up_max > left_max && up_max > down_max)
			maior = up_max;
		else if(right_max > up_max && right_max > left_max && right_max > down_max)
			maior = right_max;
		else if(left_max > up_max && left_max > right_max && left_max > down_max)
			maior = left_max;
		else if(down_max > up_max && down_max > right_max && down_max > left_max)
			maior = down_max;
		else if(up_max == max_reward && down_max == max_reward && right_max == max_reward && left_max == max_reward)
			maior = max_reward;
		else
			maior = 0;
		
		return maior;
	}
	
	private void printEnviroment()
	{
			System.out.println(map[i][j].getUp() + " " + map[i][j].getDown() + " " + map[i][j].getRight() + " " + map[i][j].getLeft());
			System.out.println();

	}

}
