import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.*;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.swing.*;
import javax.swing.Timer;

public class Driver extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
	/*INSTRUCTIONS
	 * Press 'z' or 'x' or click to interact with objects
	 * There are two types of objects: circles and sliders
	 * 
	 * Click on the circles when the outer ring reaches the inner ring
	 * The circles are numbered in order of completion
	 * 
	 * Sliders are a series of circles connected by a line.  When a slider starts, a moving circle will travel between the numbered circles
	 * Move your mouse over the moving circle while clicking or holding 'x' or 'z'.
	 * 
	 * The rhythm of the song will help you time your clicks
	 * 
	 * The more accurate you are with your timing, the more points you get!
	 * Your accuracy is determined by the points you got/total points.  Get the highest accuracy you can!
	 */

	//variables here
	int fps = 60;
	ArrayList<Slider> sliders = new ArrayList<Slider>();
	ArrayList<Circle> circles = new ArrayList<Circle>();
	ArrayList<Particle> particles = new ArrayList<Particle>();
	ArrayList<Msg> messages = new ArrayList<Msg>();
	int score = 0;
	int totalScore = 0;
	Clip clip;
	
	int responseTime = 50;
	int circleSize = 100;
	
	Font numberFont = new Font("Arial",0,50);
	Font scoreFont = new Font("Arial",0,20);
	//keys
	boolean keys[] = new boolean[256];
	int mouseX = 0;
	int mouseY = 0;
	boolean press=false;
	boolean mouseDown = false;
	
	int tick = 0;
	
	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0, 801, 801);
		
		g.setFont(scoreFont);
		g.setColor(new Color(255,255,255));
		g.drawString("score: "+score, 25, 25);
		g.drawString("acc: "+Math.round(100*score/(totalScore+0.1))+"%", 25, 50);
	
		g.setFont(numberFont);
		//circles
	    for (int i = 0; i < circles.size(); i++) {
				Circle s = circles.get(i);
				//skip premature and finished circles
				if (s.startTime>tick+responseTime) continue;
				if (s.finished) continue;
				
				if (s.startTime-tick>0)
					g.drawOval(s.getX()-circleSize/2-(s.startTime-tick), s.getY()-circleSize/2-(s.startTime-tick), circleSize+(s.startTime-tick)*2, circleSize+(s.startTime-tick)*2);
	
				g.drawOval(s.getX()-circleSize/2, s.getY()-circleSize/2, circleSize, circleSize);
				g.drawString(""+s.number, s.getX()-12, s.getY()+12);
		}
	    //sliders
		for (int i = 0; i < sliders.size(); i++) {
			Slider s = sliders.get(i);
			//skip premature circles
			if (s.startTime>tick+responseTime) continue;
			if (s.finished) continue;
			
			if (s.startTime-tick>0)
				g.drawOval(s.getX()[0]-circleSize/2-(s.startTime-tick), s.getY()[0]-circleSize/2-(s.startTime-tick), circleSize+(s.startTime-tick)*2, circleSize+(s.startTime-tick)*2);
						
			for (int j = 0; j < s.getX().length; j++) {
				if (j<s.getX().length-1)
					g.drawLine(s.getX()[j], s.getY()[j], s.getX()[j+1], s.getY()[j+1]);
				//g.setColor(new Color(0,0,0));
				//g.fillOval(s.getX()[j]-circleSize/2, s.getY()[j]-circleSize/2, circleSize, circleSize);
				//g.setColor(new Color(255,255,255));
				g.drawOval(s.getX()[j]-circleSize/2, s.getY()[j]-circleSize/2, circleSize, circleSize);
				g.drawString(""+(s.number+j), s.getX()[j]-12, s.getY()[j]+12);
			}
			g.drawOval(s.getCircleX()-circleSize/2, s.getCircleY()-circleSize/2, circleSize, circleSize);
		}
		//particles
		for (int i = 0; i < particles.size(); i++) {
			Particle p = particles.get(i);
			drawParticle(g,p.x,p.y,p.angle);
		}
		//messages
		for (int i = 0; i < messages.size(); i++) {
			Msg m = messages.get(i);
			g.setColor(m.getColor());
			g.drawString(m.text, m.x, m.y);
		}
		
		if (press) {
			g.setColor(new Color(100,100,100));
		}
		g.drawOval(mouseX-10, mouseY-10, 20, 20);
	}

	public void update() {
		if (tick==100) clip.start();
		//if (s.returnScore()!=-1)System.out.println(s.returnScore());
		totalScore = 0;
		score = 0;
		for (int i = 0; i < sliders.size(); i++) {
			Slider s = sliders.get(i);
			if (s.finished) totalScore+=300;
			score += s.returnScore();
			s.update(tick, mouseX, mouseY, press);
			
			if (s.completionFrame) {
				int[] co = {0, 0, 0};
				String m = ""+s.returnScore();
				if (s.returnScore()==0) {
					m = "X";
					co[0]=255;
				}
				if (s.returnScore()==50) {
					co[0]=200;
					co[1]=100;
				}
				if (s.returnScore()==100) {
					co[1]=255;
				}
				if (s.returnScore()==300) {
					co[1]=200;
					co[2]=255;
				}
				messages.add(new Msg(s.getCircleX(), s.getCircleY(), m, co));
			}
		}
		for (int i = 0; i < circles.size(); i++) {
			Circle c = circles.get(i);
			if (c.finished) totalScore+=300;
			score += c.returnScore();
			c.update(tick, mouseX, mouseY, press);
			
			if (c.completionFrame) {
				int[] co = {0, 0, 0};
				String m = ""+c.returnScore();
				if (c.returnScore()==0) {
					m = "X";
					co[0]=255;
				}
				if (c.returnScore()==50) {
					co[0]=200;
					co[1]=100;
				}
				if (c.returnScore()==100) {
					co[1]=255;
				}
				if (c.returnScore()==300) {
					co[1]=200;
					co[2]=255;
				}
				messages.add(new Msg(c.getX(), c.getY(), m, co));
			}
		}
		for (int i = 0; i < particles.size(); i++) {
			particles.get(i).update();
			if (!particles.get(i).isActive()) {
				particles.remove(i);
				i--;
			}
		}
		for (int i = 0; i < messages.size(); i++) {
			messages.get(i).update();
			if (!messages.get(i).isActive()) {
				messages.remove(i);
				i--;
			}
		}
		//keypress
		if (keys[90]||keys[88]||mouseDown) {
			press = true;
		}
		else {
			press = false;
		}
		if (press&&tick%7==0) spawnParticles(1,mouseX,mouseY);
		
		tick++;
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		update();
		repaint();
	}

	public static void main(String[] args) {
		Driver d = new Driver();

	}
	
	public Driver() {
		JFrame f = new JFrame();
		f.setTitle("Title");
		f.setSize(800, 800);
		f.setBackground(Color.BLACK);
		f.setResizable(false);
		f.addKeyListener(this);
		f.addMouseMotionListener(this);
		f.addMouseListener(this);
		
		f.add(this);

		Timer t = new Timer(20, this);
		t.start();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setVisible(true);

		/*   init stuff    */
	    try {
	        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(new File("Winter Wind x Megalovania shortened.wav").getAbsoluteFile());
	        clip = AudioSystem.getClip();
	        clip.open(audioInputStream);
	        //clip.start();
	    } catch(Exception ex) {
	        System.out.println("Error with playing sound.");
	        ex.printStackTrace();
	    }


		//the song starts at t=100
		//intro
		circles.add(new Circle(400,400,100,1));
		circles.add(new Circle(500,300,250-12,2));
		circles.add(new Circle(400,300,300-12,3));
		circles.add(new Circle(500,200,350-12,4));
		circles.add(new Circle(100,200,400-12,5));
		
		circles.add(new Circle(400,400,475,1));
		circles.add(new Circle(500,300,525,2));
		circles.add(new Circle(400,300,575,3));
		circles.add(new Circle(500,200,625,4));
		circles.add(new Circle(100,200,675,5));
		circles.add(new Circle(200,200,725,6));
		circles.add(new Circle(300,200,775,7));
		circles.add(new Circle(400,400,825,8));
		
		//theme 1 part 1
		circles.add(new Circle(100,200,950,1));
		sliders.add(new Slider(new int[] {250,350}, new int[] {200,200},975, 2));
		circles.add(new Circle(500,200,1025,4));
		circles.add(new Circle(400,300,1050,5));
		circles.add(new Circle(500,400,1075,6));
		circles.add(new Circle(400,500,1100,7));
		
		circles.add(new Circle(100,600,1150-12,1));
		sliders.add(new Slider(new int[] {250,350}, new int[] {600,600},1175-12,2));
		circles.add(new Circle(500,600,1225-12,4));
		circles.add(new Circle(400,500,1250-12,5));
		circles.add(new Circle(500,400,1275-12,6));
		circles.add(new Circle(400,300,1300-12,7));
		
		//theme 1 part 2
		circles.add(new Circle(100,500,1325,1));
		circles.add(new Circle(100,200,1350,2));
		sliders.add(new Slider(new int[] {250,350}, new int[] {200,200},1375,3));
		circles.add(new Circle(500,200,1425,5));
		circles.add(new Circle(500,400,1450,6));
		sliders.add(new Slider(new int[] {350,250}, new int[] {400,400},1475,7));
		circles.add(new Circle(100,400,1525,9));
		
		circles.add(new Circle(100,300,1550-6,1));
		circles.add(new Circle(100,200,1575-6,2));
		sliders.add(new Slider(new int[] {250,450}, new int[] {200,200},1600-6,3));
		circles.add(new Circle(600,200,1650+6,5));
		
		//theme 1 part 1 (again)
		int o1 = 750;
		circles.add(new Circle(100,200,950+o1,1));
		sliders.add(new Slider(new int[] {250,350}, new int[] {200,200},975+o1, 2));
		circles.add(new Circle(500,200,1025+o1,4));
		circles.add(new Circle(400,300,1050+o1,5));
		circles.add(new Circle(500,400,1075+o1,6));
		circles.add(new Circle(400,500,1100+o1,7));
		
		circles.add(new Circle(100,600,1150-12+o1,1));
		sliders.add(new Slider(new int[] {250,350}, new int[] {600,600},1175-12+o1,2));
		circles.add(new Circle(500,600,1225-12+o1,4));
		circles.add(new Circle(400,500,1250-12+o1,5));
		circles.add(new Circle(500,400,1275-21+o1,6));
		circles.add(new Circle(400,300,1300-12+o1,7));
		circles.add(new Circle(200,350,1325-12+o1,8));
		
		//theme 1 part 3
		circles.add(new Circle(150,300,2100-12,1));
		circles.add(new Circle(200,250,2125-12,2));
		circles.add(new Circle(250,200,2150-12,3));
		circles.add(new Circle(200,400,2175-12,4));
		
		circles.add(new Circle(300,350,2225,1));
		circles.add(new Circle(400,450,2250,2));
		circles.add(new Circle(400,550,2275,3));
		
		sliders.add(new Slider(new int[] {150,300,225},new int[] {500,500,700},2300,1));
		circles.add(new Circle(300,650,2425,2));
		
		//transition
		
		//sliders.add(new Slider(new int[] {250,350,300}, new int[] {200,200,300}, 1550));
		
		
		//TEMPLATES
		//circles.add(new Circle(200,200,950));
		//sliders.add(new Slider(new int[] {100,200,300}, new int[] {200,250,350}, 250));
		
		
	}
	
	public void drawParticle(Graphics g, int x, int y, double angle) {
		int[] xPoints = new int[10];
		int[] yPoints = new int[10];
		int r = 9;
		for (int i = 0; i < 5; i++) {
			xPoints[i*2] = x + (int)(r*Math.cos(angle+i*0.4*Math.PI));
			yPoints[i*2] = y + (int)(r*Math.sin(angle+i*0.4*Math.PI));
			
			xPoints[i*2+1] = x + (int)(r/2*Math.cos(angle+(i+0.5)*0.4*Math.PI));
			yPoints[i*2+1] = y + (int)(r/2*Math.sin(angle+(i+0.5)*0.4*Math.PI));
		}
		g.drawPolygon(xPoints, yPoints, 10);
	}
	public void spawnParticles(int num, int x, int y) {
		for (int i = 0; i < num; i++) {
			particles.add(new Particle(x,y));
		}
	}

	Timer t;
	@Override
	public void mouseClicked(MouseEvent e) {
		//mouseDown = true;

	}
	@Override
	public void mouseMoved(MouseEvent m) {
		m.translatePoint(-6, -31);
		mouseX = m.getX();
		mouseY = m.getY();
	}
	@Override
	public void keyPressed(KeyEvent e) {
		keys[e.getKeyCode()] = true;
	}

	@Override
	public void keyReleased(KeyEvent e) {
		keys[e.getKeyCode()] = false;
	}

	@Override
	public void keyTyped(KeyEvent e) {
		// TODO Auto-generated method stub

	}



	@Override
	public void mouseEntered(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mouseExited(MouseEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseDown = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		mouseDown = false;
	}

	@Override
	public void mouseDragged(MouseEvent m) {
		//mouseDown = true;
		m.translatePoint(-6, -31);
		mouseX =m.getX();
		mouseY =m.getY();
	}

}
