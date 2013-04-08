package competition.cig.learning;

import java.util.ArrayList;

public class ReaderFile {
	
	public static void main(String[] args) {
		
	FileClass fileClass = new FileClass();
	Map grid = new Map();
	
	ArrayList<Double> list = new ArrayList<Double>();
	double[] array = new double[grid.map[0][0].getNumberActions()];
	int countList = 0, countArray = 0;
	String str = "|\t";
	
	list = fileClass.readerFile();
	
	
	for(int i = 0; i < grid.getLines(); i++)
	{
		for(int j = 0; j < grid.getCol(); j++)
		{
			while(countArray != 4)
			{
				array[countArray] = list.get(countList);
				countArray++;
				countList++;
			}
			
			countArray = 0;
			
			grid.map[i][j].setActions(array.clone());
			
//			System.out.println("i:" + i);
//			System.out.println("j:" + j);
//			
//			for(int k = 0; k < grid.map[0][0].getNumberActions(); k++)
//				System.out.println(grid.map[i][j].getActions()[k]);
//			
//			System.out.println("--------------------------------------------------------");
		}
	}
	
		System.out.println(" ...........................................................................................");
		
//		for (int j=0; j < grid.getCol(); j++) 
//		{
//			System.out.print(String.format("%3d.    ", j));
//		}
//		
//		System.out.println();
		
		for (int i=0; i < grid.getLines(); i++)
		{
//			System.out.print(String.format("%3d.", i));
			for (int j=0; j < grid.getCol(); j++) 
			{
				
				for(int k = 0; k < grid.map[0][0].getNumberActions(); k++)
					str += grid.map[i][j].getActions()[k] + " ";
				
				str += "\t";
				
//				System.out.print("  ");
			}
			System.out.println(str + "|");
			str = "|\t";
			System.out.println();
//			System.out.println();
		}
		
		System.out.println(" ...........................................................................................");
	
	}
}
