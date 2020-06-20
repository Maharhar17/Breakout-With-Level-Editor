import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JOptionPane;
import javax.swing.Timer;

public class breakableBlock extends JComponent implements ActionListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	double x;
	double y;
	static double w = 50;
	static double h = 15;
	
	int numColors = 5;
	int seedNum = (int) (Math.random() * (numColors) + 1);
	
	BufferedImage blockColor;
	BufferedImage blueBlock;
	BufferedImage redBlock;
	BufferedImage lightGreen;
	BufferedImage orangeBlock;
	BufferedImage purpleBlock;
	
	Image newBlockColor;
	
	boolean notDestroyed = true;
	Rectangle2D rectBlock;
	
	Timer tm = new Timer (5, this);
	
	public breakableBlock (double xCoord, double yCoord) {
		x = xCoord;
		y = yCoord;
		
		try {
			blueBlock = ImageIO.read(getClass().getResource("/resources/Dark Blue Block.bmp"));
			redBlock = ImageIO.read(getClass().getResource("/resources/Red Block.bmp"));
			lightGreen = ImageIO.read(getClass().getResource("/resources/Light Green Block.bmp"));
			orangeBlock = ImageIO.read(getClass().getResource("/resources/Orange Block.bmp"));
			purpleBlock = ImageIO.read(getClass().getResource("/resources/Purple Block.bmp"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		if (seedNum == 1) {
			blockColor = blueBlock;
		} else if (seedNum == 2) {
			blockColor = redBlock;
		} else if (seedNum == 3){
			blockColor = lightGreen;
		} else if (seedNum == 4) {
			blockColor = orangeBlock;
		} else {
			blockColor = purpleBlock;
		}
	
		newBlockColor = blockColor.getScaledInstance((int) w, (int) h, Image.SCALE_SMOOTH);
		rectBlock = new Rectangle2D.Double(x, y, w, h);
	}
	
	public breakableBlock (double xCoord, double yCoord, BufferedImage image) {
		x = xCoord;
		y = yCoord;
		newBlockColor = image.getScaledInstance((int) w, (int) h, Image.SCALE_SMOOTH);
		rectBlock = new Rectangle2D.Double(x, y, w, h);
	}
	
	public void paintComponent (Graphics g) {
		super.paintComponent(g);
		Graphics2D g2 = (Graphics2D) g;
		g2.setColor(Color.BLACK);
		if (notDestroyed) {
			g2.drawImage(newBlockColor, (int) x, (int) y, this);
			g2.draw(rectBlock);
		}
		tm.start();
	}
	
	@Override
	public void actionPerformed(ActionEvent e) {
		if (notDestroyed && gameScreen.blocks.contains(this)) {
			if (rectBlock.intersects(ballBreaker.circleRect)) {
				gameScreen.blockCount -= 1;
				notDestroyed = false;
				if ((ballBreaker.x + 3 > (x + w)) || (ballBreaker.x + ballBreaker.d) - 3 < x) {
					ballBreaker.velX = -ballBreaker.velX;
				} else {
					ballBreaker.velY = -ballBreaker.velY;
				}
			}
		}
	}
}
