import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.util.*;
import javax.swing.*;
import javax.swing.Timer;

public class Driver extends JPanel implements ActionListener, KeyListener, MouseListener, MouseMotionListener {
	//EVERY ANIMATED DRIVER YOU COULD EVER NEED IS HERE
	//variables here
	int fps = 60;
	int test = 0;
	int[] x = {100,200,300,300};
	int[] y = {200,100,200,500};
	ArrayList<Slider> sliders = new ArrayList<Slider>();
	int score=0;
	
	int responseTime = 75;
	int circleSize = 100;
	//keys
	boolean keys[] = new boolean[256];
	int mouseX = 0;
	int mouseY = 0;
	boolean press=false;
	
	int tick = 0;

	@Override
	public void paint(Graphics g) {
		super.paintComponent(g);
		
		// background
		g.setColor(new Color(100, 231, 100));
		g.fillRect(0, 0, 2000, 1600);

		g.setColor(new Color(0,0,0));
		g.drawString("score: "+score, 25, 25);
		
		
		if (press) {
			g.setColor(new Color(255,255,255));
		}
		for (int i = 0; i < sliders.size(); i++) {
			Slider s = sliders.get(i);
			//skip premature circles
			if (s.startTime>tick+responseTime) continue;
			if (s.finished) continue;
			
			if (s.startTime-tick>0)
				g.drawOval(s.getX()[0]-circleSize/2-(s.startTime-tick)/2, s.getY()[0]-circleSize/2-(s.startTime-tick)/2, circleSize+s.startTime-tick, circleSize+s.startTime-tick);
			
			for (int j = 0; j < s.getX().length; j++) {
				g.drawOval(s.getX()[j]-circleSize/2, s.getY()[j]-circleSize/2, circleSize, circleSize);
			}
			g.drawOval(s.getCircleX()-circleSize/2, s.getCircleY()-circleSize/2, circleSize, circleSize);
		}
		g.drawOval(mouseX-20, mouseY-20, 40, 40);
	}

	public void update() {
	
		//if (s.returnScore()!=-1)System.out.println(s.returnScore());
		score = 0;
		for (int i = 0; i < sliders.size(); i++) {
			score += sliders.get(i).returnScore();
			sliders.get(i).update(tick, mouseX, mouseY, press);
		}
		//keypress
		if (keys[90]||keys[88]) {
			press = true;
		}
		else {
			press = false;
		}
		
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

		Timer t = new Timer(17, this);
		t.start();
		f.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		f.setVisible(true);

		/*   init stuff    */
		
		// Transparent 16 x 16 pixel cursor image.
		BufferedImage cursorImg = new BufferedImage(16, 16, BufferedImage.TYPE_INT_ARGB);

		// Create a new blank cursor.
		Cursor blankCursor = Toolkit.getDefaultToolkit().createCustomCursor(
		    cursorImg, new Point(0, 0), "blank cursor");

		// Set the blank cursor to the JFrame.
		//f.getContentPane().setCursor(blankCursor);
		sliders.add(new Slider(new int[] {100,200,300}, new int[] {200,250,350},50));
		sliders.add(new Slider(new int[] {300,200,300}, new int[] {100,200,500},200));
		sliders.add(new Slider(new int[] {100,200,300}, new int[] {200,450,350},350));
	}

	Timer t;
	@Override
	public void mouseClicked(MouseEvent e) {
		press = true;

	}
	@Override
	public void mouseMoved(MouseEvent m) {
		// TODO Auto-generated method stub
		m.translatePoint(-6, -31);
		mouseX =m.getX();
		mouseY =m.getY();
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
		press = true;
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		press = false;
	}

	@Override
	public void mouseDragged(MouseEvent arg0) {

	}



}
