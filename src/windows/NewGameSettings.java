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
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import models.CheckersModel;
import models.GameBoard;
import models.CheckersEnum.Player;

public class NewGameSettings {
    
    

	public static void createAndShowGameSettings() {

		final JFrame playerMenu = new JFrame("New Game");

		playerMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// Some dimensions used in this window:
		Dimension buttonSize = new Dimension(180, 35);
		Dimension dropDownSize = new Dimension(80, 23);

		Border matte = BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY.brighter());

		// panelMain will contain info label, 3 settings panels, goBack button
		JPanel panelMain = new JPanel() {

			private static final long serialVersionUID = 1L;

			// Fills the panel with a red/black gradient
			protected void paintComponent(Graphics grphcs) {
				Graphics2D g2d = (Graphics2D) grphcs;
				g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

				Color color1 = new Color(119, 29, 29);
				Color color2 = new Color(48, 9, 9);
				GradientPaint gp = new GradientPaint(0, 0, color1, 0, getHeight() - 100, color2);
				g2d.setPaint(gp);
				g2d.fillRect(0, 0, getWidth(), getHeight());

				super.paintComponent(grphcs);
			}
		};

		panelMain.setOpaque(false);
		panelMain.setLayout(new BoxLayout(panelMain, BoxLayout.Y_AXIS));

	    Font customFont = null;
	    

		try {
		     customFont = Font.createFont(Font.TRUETYPE_FONT, ClassLoader.getSystemClassLoader().getResourceAsStream("data/AlegreyaSC-Medium.ttf")).deriveFont(22f);

		     GraphicsEnvironment ge =  GraphicsEnvironment.getLocalGraphicsEnvironment();
		     ge.registerFont(Font.createFont(Font.TRUETYPE_FONT, ClassLoader.getSystemClassLoader().getResourceAsStream("data/AlegreyaSC-Medium.ttf")));
		

		} catch (IOException|FontFormatException e) {
		     //Handle exception
		}
		
		
		// Info text at the top of the settings window
		JLabel playerNote = new JLabel("<html><center> <font color='white'>Choose your settings </font></html>",
				SwingConstants.CENTER);

		playerNote.setFont(customFont);
		playerNote.setPreferredSize(new Dimension(200, 25));
		playerNote.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Now there will be 3 panels to hold options for the 3 modes

		Color backgroundCol = new Color(47, 47, 47);

		
		// Human vs Human --------------------------------------------
		JPanel panelHvH = new JPanel();
		panelHvH.setBackground(backgroundCol);
		panelHvH.setAlignmentX(JComponent.CENTER_ALIGNMENT);
		panelHvH.setLayout(new BoxLayout(panelHvH, BoxLayout.Y_AXIS));
		panelHvH.setBorder(matte);
		panelHvH.setMaximumSize(new Dimension(300, 80));

		// // Option to flip board each turn in HvH (Not enabled yet)
		// JCheckBox flipBoardHvH = new JCheckBox("Flip board each turn");
		// flipBoardHvH.setBackground(backgroundCol);
		// flipBoardHvH.setForeground(Color.WHITE);
		// flipBoardHvH.setEnabled(false);
		// flipBoardHvH.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Button to read settings and start HvH game
		JButton buttonHvH = new JButton("Human vs Human");

		buttonHvH.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						// Create standard game board setup for checkers
						GameBoard board = new GameBoard(true, CheckersModel.DEFAULT_TURN,
						        Player.HUMAN, Player.HUMAN);
						// Start the game
						NewGameWindow.createAndShowNewGame(board);
						// Close the start menu window and remove it from memory
						playerMenu.dispose();
					}
				});
			}

		});

		// Setting up buttonHvH
		buttonHvH.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonHvH.setMaximumSize(buttonSize);

		// Setting up panelHvH
		panelHvH.add(Box.createRigidArea(new Dimension(0, 15)));
		panelHvH.add(buttonHvH);
		panelHvH.add(Box.createRigidArea(new Dimension(0, 15)));
		// panelHvH.add(flipBoardHvH);


		
		// Human vs Computer --------------------------------------------
		JPanel panelHvC = new JPanel();
		panelHvC.setBackground(backgroundCol);
		panelHvC.setLayout(new BoxLayout(panelHvC, BoxLayout.Y_AXIS));
		panelHvC.setBorder(matte);
		panelHvC.setMaximumSize(new Dimension(300, 170));

		// Panel to hold difficulty selection
		JPanel diffHolderHvC = new JPanel();
		diffHolderHvC.setBackground(backgroundCol);
		diffHolderHvC.setForeground(Color.WHITE);
		diffHolderHvC.setLayout(new BoxLayout(diffHolderHvC, BoxLayout.X_AXIS));
		// diffHolderHvC.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		diffHolderHvC.setMaximumSize(new Dimension(250, 50));

		
		// Difficulty info label
		JLabel diffLabel1 = new JLabel("Comp Difficulty: ");
		diffLabel1.setBackground(backgroundCol);
		diffLabel1.setForeground(Color.WHITE);

		// Get player options
        Player[] playerOptions = Player.values();
        
        // Option to select difficulty of computer
		final JComboBox<Player> compDifficultyHvC = new JComboBox<Player>(playerOptions);
		// Remove Human from the list
        compDifficultyHvC.removeItemAt(0);
		compDifficultyHvC.setAlignmentX(Component.CENTER_ALIGNMENT);
		compDifficultyHvC.setMaximumSize(dropDownSize);
		compDifficultyHvC.setSelectedIndex(2);
		
		// Option to select if human plays black HvC
		final JCheckBox humanColorHvC = new JCheckBox("Human plays white  ");
		humanColorHvC.setAlignmentX(Component.CENTER_ALIGNMENT);
		humanColorHvC.setBackground(backgroundCol);
		humanColorHvC.setForeground(Color.WHITE);
		humanColorHvC.setSelected(true);

		// Button to read settings and start HvC game
		JButton buttonHvC = new JButton("Human vs Computer");

		buttonHvC.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						Player difficulty = (Player) compDifficultyHvC.getSelectedItem();

						// Create standard game board setup for checkers
						if (humanColorHvC.isSelected()) {
							GameBoard board = new GameBoard(true, CheckersModel.DEFAULT_TURN,
							        difficulty, Player.HUMAN);
							// Start the game
							NewGameWindow.createAndShowNewGame(board);
						} else {
							GameBoard board = new GameBoard(true, CheckersModel.DEFAULT_TURN,
							        Player.HUMAN, difficulty);
							// Start the game
							NewGameWindow.createAndShowNewGame(board);
						}

						// Close the start menu window and remove it from memory
						playerMenu.dispose();
					}
				});
			}

		});

		// Setting up buttonHvC
		buttonHvC.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonHvC.setMaximumSize(buttonSize);

		// Setting up diffHolderHvC
		diffHolderHvC.add(Box.createRigidArea(new Dimension(36, 0)));
		diffHolderHvC.add(diffLabel1);
		diffHolderHvC.add(Box.createRigidArea(new Dimension(10, 0)));
		diffHolderHvC.add(compDifficultyHvC);

		// Setting up panelHvC
		panelHvC.add(Box.createRigidArea(new Dimension(0, 15)));
		panelHvC.add(buttonHvC);
		panelHvC.add(Box.createRigidArea(new Dimension(0, 15)));
		panelHvC.add(diffHolderHvC);
		panelHvC.add(Box.createRigidArea(new Dimension(0, 8)));
		panelHvC.add(humanColorHvC);

		// Computer vs Computer --------------------------------------------
		JPanel panelCvC = new JPanel();
		panelCvC.setBackground(backgroundCol);
		panelCvC.setLayout(new BoxLayout(panelCvC, BoxLayout.Y_AXIS));
		panelCvC.setBorder(matte);
		panelCvC.setMaximumSize(new Dimension(300, 200));

		// Panel to hold difficulty selection
		JPanel diffWHolderCvC = new JPanel();
		diffWHolderCvC.setBackground(backgroundCol);

		diffWHolderCvC.setLayout(new BoxLayout(diffWHolderCvC, BoxLayout.X_AXIS));
		// diffWHolderCvC.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		diffWHolderCvC.setMaximumSize(new Dimension(250, 30));

		// Difficulty info label
		JLabel diffLabel2 = new JLabel("White Difficulty: ");
		diffLabel2.setBackground(backgroundCol);
		diffLabel2.setForeground(Color.WHITE);
		
		// Option to select difficulty of computer (White)
        final JComboBox<Player> compWDifficultyCvC = new JComboBox<Player>(playerOptions);
        // Remove Human from the list
        compWDifficultyCvC.removeItemAt(0);
        compWDifficultyCvC.setMaximumSize(dropDownSize);
        compWDifficultyCvC.setAlignmentX(Component.CENTER_ALIGNMENT);
		compWDifficultyCvC.setSelectedIndex(2);

		// Panel to hold difficulty selection
		JPanel diffBHolderCvC = new JPanel();
		diffBHolderCvC.setBackground(backgroundCol);

		diffBHolderCvC.setLayout(new BoxLayout(diffBHolderCvC, BoxLayout.X_AXIS));
		// diffBHolderCvC.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		diffBHolderCvC.setMaximumSize(new Dimension(250, 30));

		// Difficulty info label
		JLabel diffLabel3 = new JLabel("Black Difficulty: ");
		diffLabel3.setBackground(backgroundCol);
		diffLabel3.setForeground(Color.WHITE);

		// Option to select difficulty of computer (Black)
		final JComboBox<Player> compBDifficultyCvC = new JComboBox<Player>(playerOptions);
        // Remove Human from the list
        compBDifficultyCvC.removeItemAt(0);
        compBDifficultyCvC.setMaximumSize(dropDownSize);
        compBDifficultyCvC.setAlignmentX(Component.CENTER_ALIGNMENT);
        compBDifficultyCvC.setSelectedIndex(2);

		// Panel to hold delay selection
		JPanel delayPanel = new JPanel();
		delayPanel.setBackground(backgroundCol);
		delayPanel.setLayout(new BoxLayout(delayPanel, BoxLayout.X_AXIS));
		// delayPanel.setBorder(BorderFactory.createEtchedBorder(EtchedBorder.LOWERED));
		delayPanel.setMaximumSize(new Dimension(250, 30));

		// Delay info label
		JLabel delayLabel = new JLabel("Delay between moves (in ms): ");
		delayLabel.setBackground(backgroundCol);
		delayLabel.setForeground(Color.WHITE);

		// Option to chose delay between moves
		final JTextField delayField = new JTextField("" + GameBoard.DEFAULT_WAIT_TIME);
		delayField.setMaximumSize(new Dimension(50, 24));
		delayField.setAlignmentX(Component.CENTER_ALIGNMENT);

		delayField.setToolTipText("Please enter a number");
		// Button to read settings and start CvC game
		JButton buttonCvC = new JButton("Computer vs Computer");

		buttonCvC.addActionListener(new ActionListener() {

		
			
			public void actionPerformed(ActionEvent e) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {

						Player selectedWhitePlayer = (Player) compWDifficultyCvC.getSelectedItem();
						Player selectedBlackPlayer = (Player) compBDifficultyCvC.getSelectedItem();

						// Create standard game board setup for checkers
						GameBoard board = new GameBoard(true, CheckersModel.DEFAULT_TURN,
						        selectedBlackPlayer, selectedWhitePlayer);
						try {
							board.setTime(Integer.parseInt(delayField.getText()));
						}
						catch (NumberFormatException e) {
							delayField.setText("200");
							
						}
						// Start the game
						NewGameWindow.createAndShowNewGame(board);
						// Close the start menu window and remove it from memory
						playerMenu.dispose();
					}
				});
			}

		});

		// Setting up buttonCvC
		buttonCvC.setAlignmentX(Component.CENTER_ALIGNMENT);
		buttonCvC.setMaximumSize(buttonSize);

		// Setting up diffWHolderCvC
		diffWHolderCvC.add(Box.createRigidArea(new Dimension(35, 0)));
		diffWHolderCvC.add(diffLabel2);
		diffWHolderCvC.add(Box.createRigidArea(new Dimension(10, 0)));
		diffWHolderCvC.add(compWDifficultyCvC);

		// Setting up diffBHolderCvC
		diffBHolderCvC.add(Box.createRigidArea(new Dimension(36, 0)));
		diffBHolderCvC.add(diffLabel3);
		diffBHolderCvC.add(Box.createRigidArea(new Dimension(10, 0)));
		diffBHolderCvC.add(compBDifficultyCvC);

		// Setting up delayPanel
		delayPanel.add(Box.createRigidArea(new Dimension(5, 0)));
		delayPanel.add(delayLabel);
		delayPanel.add(Box.createRigidArea(new Dimension(10, 0)));
		delayPanel.add(delayField);

		// Setting up panelCvC
		panelCvC.add(Box.createRigidArea(new Dimension(0, 15)));
		panelCvC.add(buttonCvC);
		panelCvC.add(Box.createRigidArea(new Dimension(0, 25)));
		panelCvC.add(diffWHolderCvC);
		panelCvC.add(Box.createRigidArea(new Dimension(0, 5)));
		panelCvC.add(diffBHolderCvC);
		panelCvC.add(Box.createRigidArea(new Dimension(0, 10)));
		panelCvC.add(delayPanel);
		panelCvC.add(Box.createRigidArea(new Dimension(0, 5)));

		// Bottom back button
		JButton buttonCancel = new JButton("Cancel");

		// Setting up buttonCancel
		buttonCancel.setMaximumSize(new Dimension(100, 30));
		buttonCancel.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						StartWindow.createAndShowStartWindow();
						// Close the window and remove it from memory
						playerMenu.dispose();
					}
				});

			}
		});

		// Panel to hold cancel Button
		JPanel cancelPanel = new JPanel();
		cancelPanel.setOpaque(false);
		cancelPanel.setLayout(new BoxLayout(cancelPanel, BoxLayout.X_AXIS));
		cancelPanel.setMaximumSize(new Dimension(350, 30));

		cancelPanel.add((Box.createRigidArea(new Dimension(250, 0))));
		cancelPanel.add(buttonCancel);

		// Adding everything to playerMenu
		playerMenu.add(panelMain, BorderLayout.CENTER);

		// The 3 panels are added to the panel and aligned
		panelMain.add(Box.createRigidArea(new Dimension(0, 20)));
		panelMain.add(playerNote);
		panelMain.add(Box.createRigidArea(new Dimension(0, 30)));
		panelMain.add(panelHvH);
		panelMain.add(Box.createRigidArea(new Dimension(0, 20)));
		panelMain.add(panelHvC);
		panelMain.add(Box.createRigidArea(new Dimension(0, 20)));
		panelMain.add(panelCvC);
		panelMain.add(Box.createRigidArea(new Dimension(0, 17)));
		panelMain.add(cancelPanel);

		// Setting up playerMenu
		playerMenu.setSize(400, 660);
		playerMenu.setResizable(false);

		// Centers the window with respect to the user's screen resolution
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int x = (int) ((dimension.getWidth() - playerMenu.getWidth()) / 2);
		int y = (int) ((dimension.getHeight() - playerMenu.getHeight()) / 2);
		playerMenu.setLocation(x, y);

		playerMenu.setVisible(true);
	}

}
