import java.awt.Toolkit;

public class SnakeGame {
	public static void main(String args[]) {

		GameFrame frame = new GameFrame();
		frame.setIconImage(Toolkit.getDefaultToolkit().getImage("snake.png"));
	}
}
