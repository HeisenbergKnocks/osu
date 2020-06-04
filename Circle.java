import java.util.ArrayList;

public class Circle {
	int x;
	int y;
	int startTime;
	int timeClicked;
	boolean finished;
	int number;
	boolean completionFrame;
	
	public Circle(int x, int y, int Time, int n) {
		this.x = x;
		this.y = y;
		
		startTime = Time;
		finished = false;
		timeClicked = 0;
		
		number = n;
		completionFrame = false;
	}
	
	
	public void mouse(int t, int mx, int my, boolean press) {
		if (press && Math.sqrt((x-mx)*(x-mx)+(y-my)*(y-my))<50) {
			timeClicked = t;
			finished = true;
			completionFrame = true;
		}
	}
	
	public void update(int t, int mx, int my, boolean press) {
		if (completionFrame) completionFrame = false;
		if (t-startTime > 10) {
			if (!finished) completionFrame = true;
			finished = true;
		}
		
		if (isActive(t)) {
			mouse(t,mx,my,press);
		}
	}
	
	public boolean isActive(int t) {
		if (finished) return false;
		if (t > startTime - 10) return true;
		return false;
	}
	
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	
	public int returnScore() {
      if(!finished) return 0;
      if(Math.abs(timeClicked - startTime) < 5) return 300;
      else if(Math.abs(timeClicked - startTime) < 7) return 100;
      else if(Math.abs(timeClicked - startTime) < 10) return 50;
      return 0;
	}
	

}
