
public class Particle {
	int x;
	int y;
	int vx;
	int vy;
	int t;
	double angle;
	double vangle;
	
	public Particle(int x, int y) {
		this.x = x;
		this.y = y;
		angle = 0;
		vx = (int)(Math.random()*6-3);
		vy = (int)(Math.random()*3-6);
		vangle = Math.random()*0.5-0.25;
		t = 10;
	}
	
	public boolean isActive() {return t>0;}
	
	public void update() {
		x+=vx;
		y+=vy;
		vy+=4;
		angle+=vangle;
		t--;
	}
	
}
