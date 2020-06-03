
public class Particle {
	int x;
	int y;
	int vx;
	int vy;
	int t;
	int grav;
	double angle;
	double vangle;
	
	public Particle(int x, int y, int grav) {
		this.x = x;
		this.y = y;
		this.grav = grav;
		angle = 0;
		vx = (int)(Math.random()*6-3);
		vy = (int)(Math.random()*3-6);
		vangle = Math.random()*0.5-0.25;
		t = (int)(Math.random()*5+8);
	}
	
	public boolean isActive() {return t>0;}
	
	public void update() {
		x+=vx;
		y+=vy;
		vy+=grav;
		angle+=vangle;
		t--;
	}
	
}
