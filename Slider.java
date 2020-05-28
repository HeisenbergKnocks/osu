import java.util.ArrayList;

public class Slider {
	int[] x;
	int[] y;
	int startTime;
	int size;
	int pointsDone;
	int pointsMissed;
	boolean checkForMiss;
	double circleX;
	double circleY;
	int speed;
	boolean finished;
	
	public Slider(int[] x, int[] y, int Time) {
		this.x = x;
		this.y = y;
		
		circleX = x[0];
		circleY = y[0];
		pointsDone = 1;
		speed = 5;
		startTime = Time;
		checkForMiss = true;
		pointsMissed = 0;
		finished = false;
	}
	
	public void move() {
		int tarX = x[pointsDone];
		int tarY = y[pointsDone];
		double angle = Math.atan2(tarY-y[pointsDone-1], tarX-x[pointsDone-1]);
		circleX += speed*Math.cos(angle);
		circleY += speed*Math.sin(angle);
		
		if (Math.sqrt((circleX-tarX)*(circleX-tarX)+(circleY-tarY)*(circleY-tarY))<speed) {
			pointsDone++;
			checkForMiss = true;
		}

	}
	
	public void mouse(int mx, int my, boolean press) {
		if (!checkForMiss) return;
		if (!press||Math.sqrt((circleX-mx)*(circleX-mx)+(circleY-my)*(circleY-my))>100) {
			//System.out.println("DIST: "+Math.sqrt((circleX-mx)*(circleX-mx)+(circleY-my)*(circleY-my)));
			checkForMiss=false;
			pointsMissed++;
		}
	}
	
	public void update(int t, int mx, int my, boolean press) {

		if (pointsDone>=x.length) finished = true;
		
		if (isActive(t)) {
			move();
			mouse(mx,my,press);
		}
	}
	
	public boolean isActive(int t) {
		if (finished) return false;
		if (t > startTime) return true;
		return false;
	}
	
	public int[] getX() {
		return x;
	}
	public int[] getY() {
		return y;
	}
	public int getCircleX() {
		return (int)circleX;
	}
	public int getCircleY() {
		return (int)circleY;
	}
	
	public int returnScore() {
		//System.out.println("p missed "+pointsMissed+"/"+pointsDone);
		if (!finished) return 0;
		if (pointsMissed==0) return 300;
		else if (1-pointsMissed/pointsDone>=0.66) return 100;
		else if (1-pointsMissed/pointsDone>=0.33) return 50;
		else return 0;
	}
	

}
