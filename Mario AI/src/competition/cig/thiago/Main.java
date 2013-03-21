package competition.cig.thiago;

import java.util.Scanner;

public class Main {
	
	public static void main(String[] args) {
		
		final int line = 10;
		final int col = 5;
		
		Directions[][] map = new Directions[line][col];
		float[][] teste = new float[5][2]; 
		
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
		
		for(int i = 0; i < line; i++)
    	{
    		for(int j = 0; j < col; j++)
    		{
    			//if(i == 0)
    				//[i][j].setUp(20);
    			if(j == col-1)
    				System.out.println(map[i][j].getUp() + " " + map[i][j].getRight() + " " + map[i][j].getLeft());
    			else
    				System.out.print(map[i][j].getUp() + " " + map[i][j].getRight() + " " + map[i][j].getLeft() + "   ");
    				
    		}
    	}
		
	}
}
 