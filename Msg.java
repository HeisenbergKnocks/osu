import java.awt.Color;

public class Msg {
	int x;
	int y;
	int vy;
	int t;
	int[] color;
	String text;
	
	public Msg(int x, int y, String m, int[] c) {
		this.x = x;
		this.y = y;
		vy = -4;
		t = 40;
		text = m;
		color = c;
	}
	
	public boolean isActive() {return t>0;}
	

	
	public void update() {
		y+=vy;
		t--;
	}
	
	public Color getColor() {
		return new Color(color[0],color[1],color[2],t*4);
	}
	
}
