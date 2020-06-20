import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.geom.AffineTransform;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.Timer;

public class playerPlatform extends JComponent implements ActionListener, KeyListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Timer tm = new Timer(5, this);
	static int w = 90;
	static int h = 20;
	double delta_x;
	static int velX;
	static double x;
	static double y;
	
	BufferedImage player;
	Image playerScaled;
	static AffineTransform at = new AffineTransform ();
	
	static boolean platStart = false;
	static boolean ballLaunch = true;
	static Rectangle2D platBlock;
	
	public playerPlatform () {
		try {
			player = ImageIO.read(getClass().getResource("/resources/PlayerBar.bmp"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		x = (gameScreen.width / 2) - (w / 2);
		y = gameScreen.height - 100;
		addKeyListener(this);
		setFocusable(true);
		setFocusTraversalKeysEnabled(false);
		
		playerScaled = player.getScaledInstance(w, h, Image.SCALE_SMOOTH);
	}
	
	public void paintComponent (Graphics g) {
		platBlock = new Rectangle2D.Double(x, y, w, h);
		if (platStart) {
			super.paintComponent(g);
			AffineTransform t = new AffineTransform();
			t.translate(x, y);
			Graphics2D g2 = (Graphics2D) g;
			g2.transform(t);
			g2.drawImage(playerScaled, 0, 0, this);
			g2.dispose();
		}
		tm.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent arg0) {
		if (platStart) {
			delta_x = x + (velX * gameScreen.playerVelocityScale);
			
			if (delta_x < 0) {
				x = 0; 
			} else if ((delta_x + w) > gameScreen.width) {
				x = gameScreen.width - w;
			} else {
				x = delta_x;
			}
		} else {
			x = (gameScreen.width / 2) - (w / 2);
			y = gameScreen.height - 100;
		}
		
		if (platBlock.intersects(ballBreaker.circleRect)) {
			if ((ballBreaker.x > (x) && ballBreaker.x < (x + w)) || ((ballBreaker.x + ballBreaker.d) > (x) && (ballBreaker.x + ballBreaker.d) < (x + w))) {
						if ((ballBreaker.y + ballBreaker.d) > (y + (int) gameScreen.ballVelocityScale + 1)) {
							ballBreaker.velY = -ballBreaker.velY;
							if (ballBreaker.velX == 0) {
								ballBreaker.velX = velX;
							}
						}
			}
			
			if ((ballBreaker.delta_x + 3 > (x + w)) || (ballBreaker.delta_x + ballBreaker.d) - 3 < x) {
				gameScreen.clearPlayer();
			}
		}

		repaint();
		}

	@Override
	public void keyPressed(KeyEvent e) {
		int c = e.getKeyCode();
		
		if (c == KeyEvent.VK_RIGHT) {
			velX = 1;
		}
		
		if (c == KeyEvent.VK_LEFT) {
			velX = -1;
		}
		
		if (c == KeyEvent.VK_SPACE) {
			if (ballLaunch) {
				ballBreaker.velX = velX;
				ballBreaker.velY = -1;
				ballLaunch = false;
			}
		}
	}

	@Override
	public void keyReleased(KeyEvent arg0) {
		velX = 0;
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
		// TODO Auto-generated method stub
		
	}
	
}
