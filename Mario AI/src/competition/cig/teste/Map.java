package competition.cig.teste;

public class Map {
	
	private final int col = 100;
	private final int line = 100;
	
	private Directions[][] map;
	
	public Map()
	{
		map = new Directions[col][line];
		for(int i = 0; i < line; i++)
    	{
    		for(int j = 0; j < col; j++)
    		{
    			map[i][j].setUp(0);
    			map[i][j].setRight(0);
    			map[i][j].setLeft(0);
    		}
    	}
	}

	public Directions[][] getMap() {
		return map;
	}

	public void setMap(Directions[][] map) {
		this.map = map;
	}

	public int getCol() {
		return col;
	}

	public int getLine() {
		return line;
	}
	
	
	
}
