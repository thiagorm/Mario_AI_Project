package competition.cig.teste;

public class Directions {
	
	private float up;
	private float down;
	private float right;
	private float left;
	
	public Directions()
	{
		this.up = 0;
		this.down = 0;
		this.right = 0;
		this.left = 0;
	}

	public float getUp() {
		return up;
	}

	public void setUp(float up) {
		this.up = up;
	}

	public float getDown() {
		return down;
	}

	public void setDown(float down) {
		this.down = down;
	}

	public float getRight() {
		return right;
	}

	public void setRight(float right) {
		this.right = right;
	}

	public float getLeft() {
		return left;
	}

	public void setLeft(float left) {
		this.left = left;
	}
	
	
	
}
