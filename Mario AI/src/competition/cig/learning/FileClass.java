package competition.cig.learning;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;

public class FileClass {
	
	File file;
	FileWriter fileWriter;
	FileReader fileReader;
	PrintWriter printWriter;
	BufferedReader bufferedReader;
	
	public FileClass()
	{
		this.file = new File("C:/files/qValuesMatriz.txt");

	}
	
	public void writeFile(Map grid)
	{
		try
		{
			if(!file.exists())
			{
				file.createNewFile();
			}
			this.fileWriter = new FileWriter(file, true);
			this.printWriter = new PrintWriter(fileWriter);
			
			for(int i = 0; i < grid.getLines(); i++)
			{
				for(int j = 0; j < grid.getCol(); j++)
				{
					for(int k = 0; k < grid.map[i][j].getNumberActions(); k++)
						this.printWriter.println(String.valueOf(grid.map[i][j].getActions()[k]));
				}
			}
			
			this.fileWriter.close();
			this.printWriter.close();
			
		}catch(IOException e)
		{
			e.printStackTrace();
		}
	}
	
	public ArrayList<Double> readerFile()
	{
		ArrayList<Double> list = new ArrayList<Double>();
		String line = "";
		
		try
		{
			this.fileReader = new FileReader(file);
			this.bufferedReader = new BufferedReader(fileReader);
			

			while (bufferedReader.ready())
			 {
				 line = bufferedReader.readLine();
				 list.add(Double.valueOf(line));
			 }
		
			
			this.fileReader.close();
			this.bufferedReader.close();
			
		}catch(IOException e)
		{
			e.printStackTrace();
		}
		
		return list;
	}
}
