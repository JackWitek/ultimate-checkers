package windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsEnvironment;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Scanner;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;

//import org.omg.CORBA.portable.InputStream;

import models.CheckersEnum.Turn;
import models.CheckersEnum.Piece;
import models.CheckersEnum.Player;
import models.GameBoard;

public class StartWindow {

	final static JFrame startMenu = new JFrame("Ultimate Checkers");

	// This method creates the initial window
	public static void createAndShowStartWindow() {

		// Create and set up the window.

		startMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// panel will hold the buttons and text
		JPanel panel = new JPanel() {

			private static final long serialVersionUID = 1L;

			// Fills the panel with a red/black gradient
			protected void paintComponent(Graphics grphcs) {
				Graphics2D g2d = (Graphics2D) grphcs;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				Color color1 = new Color(119, 29, 29);
				Color color2 = Color.BLACK;
				GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight() - 100, color2);
				g2d.setPaint(gp);
				g2d.fillRect(0, 0, getWidth(), getHeight());

				super.paintComponent(grphcs);
			}
		};

		panel.setOpaque(false);

		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

	    Font customFont = null;
	    

		try {
		     customFont = Font.createFont(Font.TRUETYPE_FONT, ClassLoader.getSystemClassLoader().getResourceAsStream("data/AlegreyaSC-Medium.ttf")).deriveFont(32f);

		     GraphicsEnvironment ge =  GraphicsEnvironment.getLocalGraphicsEnvironment();
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, ClassLoader.getSystemClassLoader().getResourceAsStream("data/AlegreyaSC-Medium.ttf")));
		

		} catch (IOException|FontFormatException e) {
		     //Handle exception
		}
		

		
		// Some info text at the top of the window
		JLabel welcome = new JLabel("<html><center><font color='white'> Welcome to <br> Ultimate Checkers! </font></html>",
				SwingConstants.CENTER);
		welcome.setFont(customFont);
	

		welcome.setPreferredSize(new Dimension(200, 25));
		welcome.setAlignmentX(Component.CENTER_ALIGNMENT);


		// "Rigid Area" is used to align everything
		panel.add(Box.createRigidArea(new Dimension(0, 40)));
		panel.add(welcome);
		panel.add(Box.createRigidArea(new Dimension(0, 50)));
		//panel.add(choose);
		panel.add(Box.createRigidArea(new Dimension(0, 20)));

		// The start Window will have four buttons
		// Button1 will open a new window with a standard checkers board
		JButton button1 = new JButton("New Game (Standard Setup)");

		button1.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						NewGameSettings.createAndShowGameSettings();
						startMenu.dispose();
					}
				});
			}

		});
		// Setting up button1
		button1.setAlignmentX(Component.CENTER_ALIGNMENT);
		button1.setMaximumSize(new Dimension(250, 40));

		// Button2 will open a new window where a custom setup can be created
		JButton button2 = new JButton("New Game (Custom Setup)");
		button2.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						// Begin creating custom game board setup for checkers
						NewCustomWindow.createAndShowCustomGame();
						// Close the start menu window and remove it from memory
						startMenu.dispose();
					}
				});
			}

		});

		// Setting up button2
		button2.setAlignmentX(Component.CENTER_ALIGNMENT);
		button2.setMaximumSize(new Dimension(250, 40));

		// Button3 is not implemented yet, it will be used to load a saved game
		JButton button3 = new JButton("Load A Saved Game");
		button3.setAlignmentX(Component.CENTER_ALIGNMENT);
		button3.setMaximumSize(new Dimension(250, 40));
		button3.addActionListener(new ActionListener() {

			final JFrame popupWrongFormat = new PopupFrame(
					"Wrong Format!",
					"<html><center>"
							+ "The save file is in the wrong format! <br><br>"
							+ "Please make sure the load file is in csv format with an 8x8 table, "
							+ "followed by whose turn it is, "
							+ "the white player type and the black player type."
							+ "</center></html>");

			public void actionPerformed(ActionEvent e) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						// This will create the file chooser
						JFileChooser chooser = new JFileChooser();

						FileNameExtensionFilter filter = new FileNameExtensionFilter("CSV Files", "csv");
						chooser.setFileFilter(filter);

						int returnVal = chooser.showOpenDialog(startMenu);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							File file = chooser.getSelectedFile();

							try {
								checkAndPlay(file);
							} catch (Exception e) {
								popupWrongFormat.setVisible(true);
							}

						} else {
							// Do nothing. Load cancelled by user.
						}
					}

				});
			}

		});

		// Button4 closes the game
		JButton button4 = new JButton("Exit Ultimate Checkers");
		button4.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						// Close the start menu window and remove it from memory
						startMenu.dispose();
					}
				});
			}

		});

		// Setting up button4
		button4.setAlignmentX(Component.CENTER_ALIGNMENT);
		button4.setMaximumSize(new Dimension(250, 40));

		// The four buttons are added to the panel and aligned
		panel.add(button1);
		panel.add(Box.createRigidArea(new Dimension(0, 20)));
		panel.add(button2);
		panel.add(Box.createRigidArea(new Dimension(0, 20)));
		panel.add(button3);
		panel.add(Box.createRigidArea(new Dimension(0, 80)));
		panel.add(button4);

		// Setting up the start Menu
		startMenu.setSize(400, 600);
		startMenu.setResizable(false);

		// Centers the window with respect to the user's screen resolution
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - startMenu.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - startMenu.getHeight()) / 2);
		startMenu.setLocation(x, y);

		startMenu.add(panel, BorderLayout.CENTER);
		startMenu.setVisible(true);

	}

	// This method will check if the file can be made into an 8 by 8 board
	private static void checkAndPlay(File file) throws FileNotFoundException {

		final JFrame popupNotEnoughPieces = new PopupFrame("Not Enough Pieces!", "<html><center>"
				+ "There are not enough pieces on the board! <br><br>"
				+ "Please make sure that there is at least one piece of each team on the board before starting."
				+ "</center></html>");

		final JFrame popupTooManyPieces = new PopupFrame("Too Many Pieces!", "<html><center>"
				+ "There are too many pieces on the board! <br><br>"
				+ "Please make sure that there is a maximum of 12 pieces on each team." + "</center></html>");

		// Convert file to 2D array
		String[][] fileBoard = new String[8][8];
		String delimiter = ",";

		@SuppressWarnings("resource")
		Scanner sc = new Scanner(file);

		String line;
		String pastMoveList = "";

		for (int i = 0; i < 8; i++) {
			line = sc.nextLine();
			fileBoard[i] = line.split(delimiter);
		}
		
		// Convert string lines to enumerated types
		String turn = sc.nextLine();
		Turn toPlay = Turn.valueOf(turn);
		
		String white = sc.nextLine();
        Player whitePlayer = Player.valueOf(white);
        
        String black = sc.nextLine();
        Player blackPlayer = Player.valueOf(black);
        
        // Read the past move list into a string
		while (sc.hasNextLine()) {
			pastMoveList += sc.nextLine() + '\n';
		}

		// Create blank game board
		final GameBoard board = new GameBoard(false, toPlay, blackPlayer, whitePlayer);

		Piece newPiece;

		boolean success;

		for (int y = 0; y < 8; y++) {
			// Check the board setup row by row starting at the bottom row
			for (int x = 0; x < 8; x++) {
				// Check the value of the ComboBox if the square is not
				// forbidden
				if (((x + y) % 2) != 1) {

					newPiece = Piece.valueOf(fileBoard[7 - y][x]);

					// Try to add the selected piece to the board
					success = board.model.setPiece(x, y, newPiece);

					// If we were unable to add the piece to the board
					// it must have 12 pieces on that team already
					if (!success) {

						popupTooManyPieces.setVisible(true);
						return;
					}

				}

			}
		}

		// If setup is valid, game is started
		if (board.model.validStartSetup()) {

			// Load the past move list into the board
			board.model.setPastMoveListString(pastMoveList);

			// The custom setup is complete
			board.model.setupComplete();

			// Start the game
			javax.swing.SwingUtilities.invokeLater(new Runnable() {
				public void run() {
					NewGameWindow.createAndShowNewGame(board);
					startMenu.dispose();
				}
			});

		} else {
			// Cannot start a game without any pieces
			popupNotEnoughPieces.setVisible(true);
			// Return without closing the window
			return;
		}

	}
}
