import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.Random;

import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {

	static final int SCREEN_WIDTH = 600;
	static final int SCREEN_HEIGHT = 600;
	static final int UNIT_SIZE = 25;
	static final int GAME_UNITS = (SCREEN_WIDTH * SCREEN_HEIGHT) / UNIT_SIZE;
	static final int DELAY = 100;
	final int x[] = new int[GAME_UNITS];
	final int y[] = new int[GAME_UNITS];
	int bodyParts = 6;
	int applesEaten, appleX, appleY;
	char direction = 'R', headTail = '_';

	boolean running = false;
	Timer timer;
	Random random;
	String turnPos = "0,0", currentPos = "0,0";

	GamePanel() {
		random = new Random();
		this.setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		this.setBackground(Color.black);
		this.setFocusable(true);
		this.addKeyListener(new MyKeyAdapter());
		startGame();
	}

	public void startGame() {
		newApple();
		running = true;
		timer = new Timer(DELAY, this);
		timer.start();

	}

	static void reverse(int a[], int n) {
		int i, k, t;

		for (i = 0; i < n / 2; i++) {
			t = a[i];
			a[i] = a[n - i - 1];
			a[n - i - 1] = t;
		}
		// printing the reversed array
		System.out.println("Reversed array is: \n");
		for (k = 0; k < n; k++) {
			System.out.println(a[k]);
		}
	}

	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		draw(g);
	}

	public void draw(Graphics g) {
		if (running) {

			for (int i = 0; i < SCREEN_HEIGHT / UNIT_SIZE; i++) {
				g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, SCREEN_HEIGHT);
				g.drawLine(0, i * UNIT_SIZE, SCREEN_WIDTH, i * UNIT_SIZE);
			}

			g.setColor(Color.red);
			g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

			for (int i = 0; i < bodyParts; i++) {
				if (i == 0) {
					g.setColor(Color.green);
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				} else {

					// for green defolt color
					// g.setColor(new Color(45, 180, 0));

//					 random color snake
					g.setColor(new Color(random.nextInt(255), random.nextInt(255), random.nextInt(255)));
					g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
				}
			}

			g.setColor(Color.red);
			g.setFont(new Font("Ink Free", Font.BOLD, 40));
			FontMetrics metrics = getFontMetrics(g.getFont());
			g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
					g.getFont().getSize());

		} else {
			gameOver(g);
		}
	}

	public void newApple() {

		appleX = random.nextInt((int) SCREEN_WIDTH / UNIT_SIZE) * UNIT_SIZE;
		appleY = random.nextInt((int) SCREEN_HEIGHT / UNIT_SIZE) * UNIT_SIZE;

		for (int i = 0; i < bodyParts; i++) {
			if (appleX == x[i] && appleY == y[i]) {
				newApple();
			}

		}

	}

	public void move() {
		for (int i = bodyParts; i > 0; i--) {
			x[i] = x[i - 1];
			y[i] = y[i - 1];
		}

		if (headTail == 's') {
//			System.out.println("REVERSEEE");

			reverse(x, bodyParts);
			reverse(y, bodyParts);

			headTail = '_';
			System.out.println(headTail);

			if (direction == 'U')
				direction = 'D';
			else if (direction == 'D')
				direction = 'U';

			if (direction == 'R')
				direction = 'L';
			else if (direction == 'L')
				direction = 'R';

		} else if (direction == 'U') {
			y[0] = y[0] - UNIT_SIZE;
		} else if (direction == 'D') {
			y[0] = y[0] + UNIT_SIZE;
		} else if (direction == 'L') {
			x[0] = x[0] - UNIT_SIZE;
		} else if (direction == 'R') {
			x[0] = x[0] + UNIT_SIZE;
		}

		currentPos = x[0] + "," + y[0];
	}

	public void checkApple() {
		if (x[0] == appleX && y[0] == appleY) {
			bodyParts++;
			applesEaten++;
			newApple();
		}
	}

	public void checkCollisions() {
		// checking if the snake has run into itself
		for (int i = bodyParts; i > 0; i--) {
			if ((x[0] == x[i]) && (y[0] == y[i])) {
				running = false;
			}
		}

		// if snake collid with left and right borders
		if (x[0] < 0 || x[0] > SCREEN_WIDTH) {
			running = false;
		}

		// if snake collid with Top and Bottom borders
		if (y[0] < 0 || y[0] > SCREEN_HEIGHT) {
			running = false;
		}
	}

	public void gameOver(Graphics g) {
		// Game Over Screen
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 75));
		FontMetrics metrics1 = getFontMetrics(g.getFont());
		g.drawString("GAME OVER", (SCREEN_WIDTH - metrics1.stringWidth("Game Over")) / 2, SCREEN_WIDTH / 2);
		// score
		g.setColor(Color.red);
		g.setFont(new Font("Ink Free", Font.BOLD, 40));
		FontMetrics metrics2 = getFontMetrics(g.getFont());
		g.drawString("Score: " + applesEaten, (SCREEN_WIDTH - metrics2.stringWidth("Score: " + applesEaten)) / 2,
				g.getFont().getSize());

	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if (running) {
			move();
			checkApple();
			checkCollisions();
		}
		repaint();
	}

	public class MyKeyAdapter extends KeyAdapter {
		@Override
		public void keyPressed(KeyEvent e) {
			switch (e.getKeyCode()) {
			case KeyEvent.VK_S:
				if (headTail == '_') {
					headTail = 's';
					System.out.println(headTail);
				}
				break;
			case KeyEvent.VK_LEFT:
				if (direction != 'R' && !(currentPos.equals(turnPos))) {
					direction = 'L';
					turnPos = x[0] + "," + y[0];
				}
				break;

			case KeyEvent.VK_RIGHT:
				if (direction != 'L' && !(currentPos.equals(turnPos))) {
					direction = 'R';
					turnPos = x[0] + "," + y[0];
				}
				break;
			case KeyEvent.VK_UP:
				if (direction != 'D' && !(currentPos.equals(turnPos))) {
					direction = 'U';
					turnPos = x[0] + "," + y[0];
				}
				break;
			case KeyEvent.VK_DOWN:
				if (direction != 'U' && !(currentPos.equals(turnPos))) {
					direction = 'D';
					turnPos = x[0] + "," + y[0];

				}
				break;

			}
		}
	}

}
