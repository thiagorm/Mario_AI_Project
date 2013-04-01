package competition.cig.teste;

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

	    			map[l][k].setUp(100);
	        		map[l][k].setRight(100);
	        		map[l][k].setLeft(100);
	        		map[l][k].setDown(100);
	    	}
		}
		System.out.println(map[0][0].getDown());
		
//		for(int i = 0; i < line; i++)
//    	{
//    		for(int j = 0; j < col; j++)
//    		{
//    			//if(i == 0)
//    				//[i][j].setUp(20);
//    			if(j == col-1)
//    				System.out.println(map[i][j].getUp() + " " + map[i][j].getRight() + " " + map[i][j].getLeft());
//    			else
//    				System.out.print(map[i][j].getUp() + " " + map[i][j].getRight() + " " + map[i][j].getLeft() + "   ");
//    				
//    		}
//    	}
//		
	}
}
 