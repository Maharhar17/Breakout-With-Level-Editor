import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.LayoutManager;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;

import javax.imageio.ImageIO;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import javax.swing.JTextField;
import javax.swing.OverlayLayout;

public class gameScreen extends JComponent implements MouseListener {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	static int width = 700;
	static int height = 600;
	
	static int mouseIntX;
	static int mouseIntY;
	
	int x;
	int y;
	
	static int divide = 180;
	
	static ArrayList<breakableBlock> blocks = new ArrayList<breakableBlock>(); 
	
	static JButton startButton = new JButton ("Start Game");
	static JButton restartButton = new JButton ("Restart");
	static JButton imageButton = new JButton ("Upload Image");
	static JButton speedButton = new JButton ("Change Speeds");
	static JButton instructButton = new JButton ("Instructions");
	static JButton gridButton = new JButton ("Generate Grid");
	
	static JFrame frame = new JFrame ("BrickBreaker w/ Level Editor");
	static JPanel gamePanel;
	static JPanel userPanel;
	
	static double playerVelocityScale;
	static double ballVelocityScale;
	
	static JSplitPane split = new JSplitPane ();
	
	static playerPlatform plat;
	static ballBreaker ball;
	
	static int blockCount = 0;
	
	static boolean clearEverything = false;
	
	public gameScreen () {
		addMouseListener(this);
	}
	
	public static void main(String[] args) {
		userPanel = new JPanel();
		gamePanel = new JPanel();
		
		plat = new playerPlatform();
		ball = new ballBreaker();
		
		ballVelocityScale = 2;
		playerVelocityScale = 2.25;
	
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setSize(width + divide, height);
		frame.setResizable(false);
		gamePanel.setSize(width, height);
		gamePanel.setBackground(Color.WHITE);
		
		split.setDividerLocation(divide);
		split.setLeftComponent(userPanel);
		split.setRightComponent(gamePanel);
	
		LayoutManager overlay = new OverlayLayout (gamePanel);
		gamePanel.setLayout(overlay);
		
		startButton.addActionListener(new StartAction());
		userPanel.add(startButton);
		startButton.setFocusable(false);
		
		restartButton.addActionListener(new RestartAction());
		userPanel.add(restartButton);
		restartButton.setFocusable(false);
		
		imageButton.addActionListener(new ImageUploadAction());
		userPanel.add(imageButton);
		imageButton.setFocusable(false);
		
		gridButton.addActionListener(new GridAction());
		userPanel.add(gridButton);
		gridButton.setFocusable(false);
		
		speedButton.addActionListener(new SpeedAction());
		userPanel.add(speedButton);
		speedButton.setFocusable(false);
		
		instructButton.addActionListener(new InstructAction());
		userPanel.add(instructButton);
		instructButton.setFocusable(false);
		
		frame.add(split, BorderLayout.CENTER);
		split.setEnabled(false);
		
		gameScreen d = new gameScreen ();
		gamePanel.add(d);
		
		gamePanel.add(ball);
		gamePanel.add(plat);
		
		gamePanel.setVisible(true);
		userPanel.setVisible(true);
		frame.setVisible(true);	
	}
	
	static class GridAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			for (int i = 0; i < width - breakableBlock.w; i += breakableBlock.w) {
				for (int j = 0; j < (height - 300) - breakableBlock.h; j += breakableBlock.h) {
					if (Math.random() < 0.5) {
						blocks.add(new breakableBlock(i,j));
					}
				}
				for (int k = 0; k < blocks.size(); k++) {
					gamePanel.add(blocks.get(k));
				}
				gamePanel.revalidate();
			}
		}	
	}
	
	static class InstructAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			JOptionPane.showMessageDialog(frame, "- Click to place blocks. Press start when ready to play.\n- Move with arrow keys. Press space to launch\n"
					+ "- Click 'Generate Grid' to automatically create a playable grid\n"
					+ "- To launch in direction, press space while platform is moving in that direction.\n- If ball is bouncing straight up and down, moving the platform"
					+ "right as the ball hits will make the ball move in that direction.\n- Platform will disappear if ball hits the side or falls through.");
		}
	}
	
	static class SpeedAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			String playerSpeedScale = "";
			String ballSpeedScale = "";
			
			JTextField xField = new JTextField(5);
		    JTextField yField = new JTextField(5);
		    xField.setText(Double.toString(ballVelocityScale));
		    yField.setText(Double.toString(playerVelocityScale));

		    JPanel myPanel = new JPanel();
		    myPanel.add(new JLabel("Ball Speed:"));
		    myPanel.add(xField);
		    myPanel.add(new JLabel("Player Speed:"));
		    myPanel.add(yField);
			
		    int result = JOptionPane.showConfirmDialog(null, myPanel, 
		               "Please Enter X and Y Values", JOptionPane.OK_CANCEL_OPTION);
		    if (result == 0) {
		         ballSpeedScale = xField.getText();
		         playerSpeedScale = yField.getText();
		    }
		    
			try {
				int ballVelocityScaleInt = Integer.parseInt(ballSpeedScale);	
				ballVelocityScale = (double) ballVelocityScaleInt;				
			} catch (NumberFormatException e) {
				System.out.println("Exception");
				boolean ballError = false;
				
				
				if (ballSpeedScale.indexOf(".") != -1) {
					ballVelocityScale = parseDecimal(ballSpeedScale);
				} else {
					ballError = !ballError;
				}
				
				if (ballError) {
					System.out.println("ball error");
					ballVelocityScale = 2;
				}
			}
			
			try {
				int playerVelocityScaleInt = Integer.parseInt(playerSpeedScale);
				playerVelocityScale = (double) playerVelocityScaleInt;
			} catch (NumberFormatException ex) {
				boolean playerError = false;
					
				if (playerSpeedScale.indexOf(".") != -1) {
					playerVelocityScale = parseDecimal(playerSpeedScale);
				} else {
					playerError = !playerError;
				}
					
				if (playerError) {
					System.out.println("player error");
					playerVelocityScale = 2.25;
				}
			}
		}	
		
		public double parseDecimal (String num) {
			int decimalIndex = num.indexOf(".");
			int wholePart = Integer.parseInt(num.substring(0, decimalIndex), 10);
			
			String decimalNumber = num.substring(decimalIndex + 1, num.length());
			double decimalPart = Integer.parseInt(decimalNumber) / Math.pow(10, decimalNumber.length());
			
			return wholePart + decimalPart;
		}
	}
	
	static class StartAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			if (playerPlatform.platStart == false) {
				playerPlatform.platStart = true;
				ballBreaker.startBall = true;
				playerPlatform.ballLaunch = true;
			}
		}
	}
	
	static class RestartAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			clearBlocks();
		}
	}
	
	static class ImageUploadAction implements ActionListener {
		@Override
		public void actionPerformed(ActionEvent arg0) {
			try {
				clearBlocks();
			} catch (NullPointerException e) {}
			JButton open = new JButton();
			JFileChooser fc = new JFileChooser();
			fc.setCurrentDirectory(new java.io.File("C:/Users"));
			fc.setDialogTitle("Choose Your Image");
			if (fc.showOpenDialog(open) == JFileChooser.APPROVE_OPTION) {
				try {
					Image image = ImageIO.read(fc.getSelectedFile().getAbsoluteFile());
					generateImageGrid(image);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			
		}
	}
	
	public static void generateImageGrid (Image image) {
		try {
			Image gridImage = image.getScaledInstance(width, height - 150, Image.SCALE_SMOOTH);
			BufferedImage blockImage = convertToBufferedImage(gridImage);
			for (int i = 0; i < width - breakableBlock.w; i += breakableBlock.w) {
				for (int j = 0; j < (height - 300) - breakableBlock.h; j += breakableBlock.h) {
					if (Math.random() < 0.5) {
						BufferedImage block = blockImage.getSubimage(i, j, (int) breakableBlock.w, (int) breakableBlock.h);
						blocks.add(new breakableBlock(i,j,block));
					}
				}
			}
			for (int k = 0; k < blocks.size(); k++) {
				gamePanel.add(blocks.get(k));
			}
			gamePanel.revalidate();
		} catch (NullPointerException e) {
			System.out.println("That file does not work.");
		}
		
	}
	
	public static BufferedImage convertToBufferedImage(Image image)
	{
	    BufferedImage newImage = new BufferedImage(
	        image.getWidth(null), image.getHeight(null),
	        BufferedImage.TYPE_INT_ARGB);
	    Graphics2D g = newImage.createGraphics();
	    g.drawImage(image, 0, 0, null);
	    g.dispose();
	    return newImage;
	}
	
	public static void clearBlocks () {
		clearPlayer();
		for (int i = 0; i < blocks.size(); i++) {
			gamePanel.remove(blocks.get(i));
		}
		blocks.removeAll(blocks);
	}
	
	public static void clearPlayer () {
		ballBreaker.startBall = false;
		playerPlatform.platStart = false;
	}
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if (ballBreaker.startBall == false) {
			blocks.add(new breakableBlock (e.getX() - (breakableBlock.w / 2), e.getY() - (breakableBlock.h / 2)));
			for (int i = 0; i < blocks.size(); i++) {
				gamePanel.add(blocks.get(i));
			}
			blockCount = blocks.size();
			gamePanel.revalidate();
		}
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
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}
}
