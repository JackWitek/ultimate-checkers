package windows;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GradientPaint;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.SwingConstants;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

import models.CheckersEnum.Turn;
import models.GameBoard;

public class NewGameWindow {

	private static GameBoard boardReference;
	private static JLabel turnInfo;
	private static JTextPane moves;
	private static JButton save;
	private static JButton resign;
	private static JFrame newGameWindow;
	private static JScrollPane movesScroll;

	// This method creates the new game window
	public static void createAndShowNewGame(GameBoard board) {

		boardReference = board;

		// Create and set up the window.
		newGameWindow = new JFrame("Ultimate Checkers");
		newGameWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// A panel to contain two other panels
		JPanel outerPanel = new JPanel();
		outerPanel.setLayout(new BoxLayout(outerPanel, BoxLayout.X_AXIS));

		newGameWindow.setSize(750, 500);
		newGameWindow.setResizable(false);

		// Centres the window with respect to the user's screen resolution
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - newGameWindow.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - newGameWindow.getHeight()) / 2);
		newGameWindow.setLocation(x, y);

		// outerPanel will have 3 inner panels (rowLabelPanel, gamePanel and pastMovesPanel)

		// Panel for numerical row labels
		JPanel rowLabelPanel = new GradientPanel();
		rowLabelPanel.setOpaque(false);
		rowLabelPanel.setMaximumSize(new Dimension(60, 500));

		// Game panel for GameBoard and alphabetical label for columns
		JPanel gamePanel = new GradientPanel();
		gamePanel.setOpaque(false);
		gamePanel.setLayout(new BoxLayout(gamePanel, BoxLayout.Y_AXIS));
		gamePanel.setPreferredSize(new Dimension(400, 500));
		gamePanel.setMaximumSize(new Dimension(400, 500));

		// Panel to show the past moves, button and move status notifications
		JPanel pastMovesPanel = new GradientPanel();
		pastMovesPanel.setOpaque(false);
		pastMovesPanel.setLayout(new BoxLayout(pastMovesPanel, BoxLayout.Y_AXIS));
		pastMovesPanel.setPreferredSize(new Dimension(280, 500));
		pastMovesPanel.setMaximumSize(new Dimension(280, 500));

		// Past moves label
		JLabel labelPastMoves = new JLabel("<html><center><font color='white'>Past Moves</font></html>",
				SwingConstants.CENTER);
		labelPastMoves.setFont(new Font("Calibri", Font.BOLD, 23));
		labelPastMoves.setAlignmentX(Component.CENTER_ALIGNMENT);

		// This text pane will hold all the past moves
		moves = new JTextPane();
		moves.setFont(new Font("Consolas", Font.BOLD, 16));
		moves.setPreferredSize(new Dimension(120, 250));
		moves.setMaximumSize(new Dimension(120, 250));
		moves.setEditable(false);

		// Set the text of the past moves to align in the centre
		StyledDocument alignPastMoves = moves.getStyledDocument();
		SimpleAttributeSet alignCentre = new SimpleAttributeSet();
		StyleConstants.setAlignment(alignCentre, StyleConstants.ALIGN_CENTER);
		alignPastMoves.setParagraphAttributes(0, alignPastMoves.getLength(), alignCentre, false);

		// Scroll panel for the past moves
		movesScroll = new JScrollPane(moves);
		movesScroll.setPreferredSize(new Dimension(150, 250));
		movesScroll.setMaximumSize(new Dimension(150, 250));

		// Turn info label
		turnInfo = new JLabel("Empty", SwingConstants.CENTER);
		turnInfo.setFont(new Font("Calibri", Font.BOLD, 20));
		turnInfo.setPreferredSize(new Dimension(120, 30));
		turnInfo.setAlignmentX(Component.CENTER_ALIGNMENT);
		
		// Button to go back to main menu
        resign = new JButton("Resign");
        resign.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                javax.swing.SwingUtilities.invokeLater(new Runnable() {
                    public void run() {
                        resign();
                    }
                });

            }
        });
        
        // Setting up the main menu button
        resign.setMaximumSize(new Dimension(120, 25));
        resign.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Button to go back to main menu
		JButton goBack = new JButton("Main Menu");
		goBack.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						StartWindow.createAndShowStartWindow();
						// Close the window and remove it from memory
						newGameWindow.dispose();
					}
				});

			}
		});

		// Setting up the main menu button
		goBack.setMaximumSize(new Dimension(120, 25));
		goBack.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Button to save the game
		save = new JButton("Save Game");
		save.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
			    
			    boardReference.pauseGame();
			    
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						// This will create the file chooser
						JFileChooser chooser = new JFileChooser();
						// Only directories can be chosen
						chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);

						// This just tell the user to choose a directory
						FileNameExtensionFilter filter = new FileNameExtensionFilter(
								"Choose a direcory for the save file", "csv");
						chooser.setFileFilter(filter);

						// For making a nice file name
						DateFormat df = new SimpleDateFormat(" 'at' hh-mm a 'on' MMM-dd");

						int returnVal = chooser.showSaveDialog(newGameWindow);
						if (returnVal == JFileChooser.APPROVE_OPTION) {
							try {

								File file = new File(chooser.getSelectedFile() + "/Checkers Save"
										+ df.format(new Date()) + ".csv");
								BufferedWriter writer = new BufferedWriter(new FileWriter(file));

								System.out.println("Saving to: " + file.getAbsolutePath());

								for (int y = 0; y < 8; y++) {
									for (int x = 0; x < 8; x++) {

										writer.append(boardReference.model.getPiece(x, 7 - y).name());
										writer.append(',');

									}
									writer.append('\n');
								}
								writer.append(boardReference.model.getTurn().name());
								writer.append('\n');
								writer.append(boardReference.model.getWhitePlayer().name());
                                writer.append('\n');
                                writer.append(boardReference.model.getBlackPlayer().name());
                                writer.append('\n');
								writer.append(boardReference.model.getPastMoveListString());
								// Closing the file writer
								writer.close();

							} catch (IOException e) {
								e.printStackTrace();
							}

						} else {
							System.out.println("Saving cancelled by user");
						}
						
						boardReference.resumeGame();
					}

				});

			}
		});

		// Setting up the save button
		save.setMaximumSize(new Dimension(120, 25));
		save.setAlignmentX(Component.CENTER_ALIGNMENT);
		// save.setEnabled(false);

		// Numerical label for rows
		JLabel rowLabel = new JLabel("<html><p style=\"padding:3px; \">8</p>" + "<p style=\"padding:3px; \">7</p>"
				+ "<p style=\"padding:3px; \">6</p>" + "<p style=\"padding:3px; \">5</p>"
				+ "<p style=\"padding:3px; \">4</p>" + "<p style=\"padding:3px; \">3</p>"
				+ "<p style=\"padding:3px; \">2</p>" + "<p style=\"padding:3px; \">1</p></html>", SwingConstants.CENTER);
		rowLabel.setForeground(new Color(140, 140, 140));
		rowLabel.setFont(new Font("Calibri", Font.PLAIN, 35));
		rowLabel.setPreferredSize(new Dimension(20, 400));

		// Alphabetical label for columns
		JLabel coloumnLabel = new JLabel(
				"<html>&nbsp;A&nbsp;&nbsp;&nbsp;&nbsp;B&nbsp;&nbsp;&nbsp;C&nbsp;&nbsp;&nbsp; "
						+ "D&nbsp;&nbsp;&nbsp;&nbsp;E&nbsp;&nbsp;&nbsp;&nbsp;F&nbsp;&nbsp;&nbsp;&nbsp;G&nbsp;&nbsp;&nbsp;H</html>",
				SwingConstants.LEFT);
		coloumnLabel.setForeground(new Color(140, 140, 140));
		coloumnLabel.setFont(new Font("Calibri", Font.PLAIN, 35));
		coloumnLabel.setPreferredSize(new Dimension(200, 40));

		// Adding all the components to the window
		rowLabelPanel.add(rowLabel);

		pastMovesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		pastMovesPanel.add(labelPastMoves);
		pastMovesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		pastMovesPanel.add(movesScroll);
		pastMovesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		pastMovesPanel.add(turnInfo);
		pastMovesPanel.add(Box.createRigidArea(new Dimension(0, 20)));
		pastMovesPanel.add(save);
		pastMovesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
        pastMovesPanel.add(resign);
		pastMovesPanel.add(Box.createRigidArea(new Dimension(0, 10)));
		pastMovesPanel.add(goBack);
		pastMovesPanel.add(Box.createRigidArea(new Dimension(20, 0)));

		gamePanel.add(Box.createRigidArea(new Dimension(0, 10)));
		gamePanel.add(board);
		gamePanel.add(Box.createRigidArea(new Dimension(0, 1)));
		gamePanel.add(coloumnLabel, BorderLayout.WEST);
		gamePanel.add(Box.createRigidArea(new Dimension(0, 20)));

		outerPanel.add(rowLabelPanel);
		outerPanel.add(gamePanel);
		outerPanel.add(pastMovesPanel);
		newGameWindow.add(outerPanel);

		// Update the window
		update();

		newGameWindow.setVisible(true);
		
		// Start the game
		boardReference.startGame();

	}

	public static void disableResign() {
		resign.setEnabled(false);
		// Repaint the information right now to avoid graphics glitches
        resign.paintImmediately(resign.getVisibleRect());
	}

	public static void enableResign() {
	    resign.setEnabled(true);
        // Repaint the information right now to avoid graphics glitches
        resign.paintImmediately(resign.getVisibleRect());
	}
	
	public static void disableSave() {
        save.setEnabled(false);
        // Repaint the information right now to avoid graphics glitches
        save.paintImmediately(save.getVisibleRect());
    }

    public static void enableSave() {
        save.setEnabled(true);
        // Repaint the information right now to avoid graphics glitches
        save.paintImmediately(save.getVisibleRect());
    }

	public static void update() {
		// Update past moves list text
		moves.setText(boardReference.model.getPastMoveListString());

		// Update turn info label
		if (boardReference.model.isFinished()) {
			if (boardReference.model.getTurn() == Turn.BLACK_TO_PLAY) {
				turnInfo.setText("<html><font color=BC9C0E> Victory!</font> <font color=#D7D7D7>White Wins! </font></html>");
			} else {
				turnInfo.setText("<html><font color=BC9C0E> Victory!</font> <font color='gray'>Black Wins!  </font></html>");
			}
		} else if (boardReference.model.isCapture()) {

			if (boardReference.model.getTurn() == Turn.BLACK_TO_PLAY) {
				turnInfo.setText("<html><font color='gray'>Black Must Capture! </html>");
			} else {
				turnInfo.setText("<html><font color='D7D7D7'> White Must Capture! </html>");
			}

		} else {

			if (boardReference.model.getTurn() == Turn.BLACK_TO_PLAY) {
				turnInfo.setText("<html><font color='gray'> Black's Turn </font></html>");
			} else {
				turnInfo.setText("<html><font color=#D7D7D7> White's Turn </font></html>");
			}

		}
		
		// Repaint the information right now to avoid graphics glitches
		moves.paintImmediately(moves.getVisibleRect());
		turnInfo.paintImmediately(turnInfo.getVisibleRect());
	}
	
	public static void resign() {
	    // Resign the game
	    disableResign();
	    disableSave();
	    boardReference.resign();
	    
	    if (boardReference.model.getTurn() == Turn.BLACK_TO_PLAY) {
            turnInfo.setText("<html><font color=#D7D7D7>White Wins! Black Resigned.</font></html>");
        } else {
            turnInfo.setText("<html><font color='gray'>Black Wins! White Resigned. </font></html>");
        }
	}

	private static class GradientPanel extends JPanel {

		private static final long serialVersionUID = 1L;

		// Fills the panel with a blue/black gradient
		protected void paintComponent(Graphics background) {
			Graphics2D g2d = (Graphics2D) background;
			g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

			Color color1 = new Color(5, 19, 43);
			Color color2 = new Color(11, 42, 91);
			GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight() - 100, color2);
			g2d.setPaint(gp);
			g2d.fillRect(0, 0, getWidth(), getHeight());

			super.paintComponent(background);
		}

	}

}
