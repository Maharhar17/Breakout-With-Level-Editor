import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.Timer;

public class ballBreaker extends JComponent implements ActionListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Timer tm = new Timer(5, this);
	static int d = 25;
	static double delta_x;
	static double delta_y;
	static int velX;
	static int velY;
	static double x;
	static double y;
	static boolean touch;
	static boolean startBall = false;
	static Ellipse2D circle;
	static Rectangle2D circleRect;
	
	BufferedImage ball;
	Image ballScaled;
	
	public ballBreaker () {
		x = playerPlatform.x + (playerPlatform.w / 2);
		y = playerPlatform.y - d;
		velX = 0;
		velY = 0;
		delta_x = 0;
		delta_y = 0;
		
		try {
			ball = ImageIO.read(getClass().getResource("/resources/Ball.bmp"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		ballScaled = ball.getScaledInstance(d, d, Image.SCALE_SMOOTH);	
	}
	
	public void paintComponent (Graphics g) {
		circleRect = new Rectangle2D.Double(x, y, d, d);
		if (startBall) {
			super.paintComponent(g);
			AffineTransform t = new AffineTransform();
			t.translate(x, y);
			g.setColor(Color.BLACK);
			Graphics2D g2 = (Graphics2D) g;
			g2.transform(t);
			g2.drawImage(ballScaled, 0, 0, this);
			g2.dispose();
			tm.start();
		}
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (startBall) {
			if (playerPlatform.ballLaunch) {
				x = playerPlatform.x;
			} else {
				delta_x = x + velX * gameScreen.ballVelocityScale;
				delta_y = y + velY * gameScreen.ballVelocityScale;
				
				if ((delta_x + d) > gameScreen.width) {
					x = gameScreen.width - d;
					velX = -velX;
				}
				
				if (delta_x < 0) {
					x = 0;
					velX = -velX;
				}
				
				if (delta_y < 0) {
					y = 0;
					velY = -velY;
				}
				
				if (y > gameScreen.height) {
					gameScreen.clearPlayer();
				} 
				
				x = delta_x;
				y = delta_y;
			}
		} else {
			x = playerPlatform.x + (playerPlatform.w / 2);
			y = playerPlatform.y - d;
			velX = 0;
			velY = 0;
		}
	}
}
