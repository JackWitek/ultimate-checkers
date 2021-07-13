package windows;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.plaf.basic.BasicComboBoxUI;
import javax.swing.plaf.basic.BasicComboPopup;
import javax.swing.plaf.basic.ComboPopup;

import models.CheckersEnum.Piece;
import models.CheckersEnum.Player;
import models.CheckersEnum.Turn;
import models.CheckersModel;
import models.GameBoard;

public class NewCustomWindow {

	private static JCheckBox changePlayer;

	// This method creates the custom board input window
	public static void createAndShowCustomGame() {

		// Create and set up the window.
		final JFrame setup = new JFrame("Custom Game Setup");
		setup.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		// A panel to hold a grid panel and buttons panel
		JPanel panel = new JPanel();
		panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));

		// Grid panel will have an 8x8 checkers grid
		JPanel grid = new JPanel();

		int space = 1;
		grid.setLayout(new GridLayout(8, 8, space, space));

		// 8x8 array of Combo boxes
		@SuppressWarnings("unchecked")
		final JComboBox<Piece>[][] boxs = new JComboBox[8][8];

		// Create temporary variables to use in the loop
		JComboBox<Piece> gridbox;
		JButton blank;

		// Build each row of the grid starting with the top row
		for (int row = 7; row >= 0; row--) {
			// Loop through the boxes in the current row
			for (int column = 0; column < 8; column++) {
				// Check if the square is forbidden
				if (((column + row) % 2) == 1) {
					// Create a blank button
					blank = new JButton();
					blank.setBackground(Color.RED);
					blank.setEnabled(false);
					grid.add(blank);
				} else {
					// Load Default Choices
					gridbox = new JComboBox<Piece>(CheckersModel.VALID_PIECES);
					gridbox.setBackground(Color.BLACK);
					gridbox.setForeground(Color.WHITE);
					gridbox.setFocusable(false);
					gridbox.setRenderer(new ComboBoxRenderer());

					// Set the modified UI for each ComboBox
					gridbox.setUI(new ModifiedComboBoxUI());

					// Create a ComboBox
					switch (row) {
					case 0:
						// Remove "Black Piece" from the list of options
						// (Must be king at that point)
						gridbox.removeItemAt(2);
						break;

					case 7:
						// Remove "White Piece" from the list of options
						gridbox.removeItemAt(1);
						break;

					default:
						// Use the default choices
						break;
					}

					// Store the reference to the ComboBox in an array to check
					// later
					boxs[row][column] = gridbox;
					// Display the ComboBox on the Grid
					grid.add(gridbox);
				}
			}

		}
		panel.add(grid); // Grid panel is added to main panel

		// A pop-up that will be made visible if too many pieces
		final JFrame popupTooManyPieces = new PopupFrame("Too Many Pieces!", "<html><center>"
				+ "There are too many pieces on the board! <br><br>"
				+ "Please make sure that there is a maximum of 12 pieces on each team." + "</center></html>");
		// A pop-up that will be made visible if zero pieces are on the board
		final JFrame popupNotEnoughPieces = new PopupFrame("Not Enough Pieces!", "<html><center>"
				+ "There are not enough pieces on the board! <br><br>"
				+ "Please make sure that there is at least one piece of each team on the board before starting."
				+ "</center></html>");

		// Panel to hold player type selection (WHITE) -------------------
		JPanel diffWHolderCvC = new JPanel();

		diffWHolderCvC.setLayout(new BoxLayout(diffWHolderCvC, BoxLayout.X_AXIS));
		diffWHolderCvC.setPreferredSize(new Dimension(100, 30));

		// Info label
		JLabel diffLabel2 = new JLabel("White Player: ");

		// Get player options
		Player[] playerOptions = Player.values();
		
		// Option to select player type (White)
		final JComboBox<Player> whitePlayer = new JComboBox<Player>(playerOptions);
		whitePlayer.setMaximumSize(new Dimension(80, 23));
		whitePlayer.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Panel to hold player type selection (BLACK) -------------------
		JPanel diffBHolderCvC = new JPanel();

		diffBHolderCvC.setLayout(new BoxLayout(diffBHolderCvC, BoxLayout.X_AXIS));
		diffBHolderCvC.setPreferredSize(new Dimension(100, 30));

		// Info label
		JLabel diffLabel3 = new JLabel("Black Player: ");

		// Option to to select player type (Black)
		final JComboBox<Player> blackPlayer = new JComboBox<Player>(playerOptions);
		// final JComboBox<Player> playerBDropDown = new JComboBox();
		blackPlayer.setMaximumSize(new Dimension(80, 23));
		whitePlayer.setAlignmentX(Component.CENTER_ALIGNMENT);

		// Setting up diffWHolderCvC
		diffWHolderCvC.add(diffLabel2);
		diffWHolderCvC.add(Box.createRigidArea(new Dimension(10, 0)));
		diffWHolderCvC.add(whitePlayer);
		diffWHolderCvC.add(Box.createRigidArea(new Dimension(10, 0)));

		// Setting up diffBHolderCvC
		diffBHolderCvC.add(diffLabel3);
		diffBHolderCvC.add(Box.createRigidArea(new Dimension(10, 0)));
		diffBHolderCvC.add(blackPlayer);
		diffBHolderCvC.add(Box.createRigidArea(new Dimension(10, 0)));

		whitePlayer.setFocusable(false);
		blackPlayer.setFocusable(false);

		// There will be two buttons (start and exit)
		JButton start = new JButton("Start Playing");
		start.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {

				Turn toPlay = changePlayer.isSelected() ? Turn.WHITE_TO_PLAY : Turn.BLACK_TO_PLAY;

				Player selectedWhitePlayer = (Player) whitePlayer.getSelectedItem();
				Player selectedBlackPlayer = (Player) blackPlayer.getSelectedItem();

				// Create blank game board
				final GameBoard board = new GameBoard(false, toPlay,
				        selectedBlackPlayer, selectedWhitePlayer);

				Piece newPiece;
				JComboBox<Piece> gridbox;
				boolean success;

				for (int j = 0; j < 8; j++) {
					// Check the board setup row by row starting at the bottom
					// row
					for (int i = 0; i < 8; i++) {
						// Check the value of the ComboBox if the square is not
						// forbidden
						if (((i + j) % 2) != 1) {
							gridbox = boxs[j][i];
							newPiece = (Piece) gridbox.getSelectedItem();

							// Try to add the selected piece to the board
							success = board.model.setPiece(i, j, newPiece);

							// If we were unable to add the piece to the board
							// it must have 12 pieces on that team already
							if (!success) {
								// Cannot place move than 12 pieces on either
								// team
								popupTooManyPieces.setVisible(true);

								// Return without closing the window
								return;
							}

						}

					}

				}

				// Check if final setup of the board is valid
				if (board.model.validStartSetup()) {

					// The custom setup is complete
					board.model.setupComplete();

					// Start the game
					javax.swing.SwingUtilities.invokeLater(new Runnable() {
						public void run() {
							NewGameWindow.createAndShowNewGame(board);
							// Close the window and remove it from memory
							setup.dispose();
						}
					});

				}
				// There are not enough pieces on the board (need one on each
				// team)
				else {
					// Cannot start a game without any pieces
					popupNotEnoughPieces.setVisible(true);

					// Return without closing the window
					return;
				}
			}
		});

		JButton goBack = new JButton("Go Back");

		goBack.setAlignmentX(Component.CENTER_ALIGNMENT);
		goBack.addActionListener(new ActionListener() {

			public void actionPerformed(ActionEvent e) {
				javax.swing.SwingUtilities.invokeLater(new Runnable() {
					public void run() {
						StartWindow.createAndShowStartWindow();
						// Close the window and remove it from memory
						setup.dispose();
					}
				});

			}
		});

		// Restrict the size of the window so that is does not exceed the screen
		// resolution
		Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
		int setupSize = (dimension.getHeight() - 50 > 700) ? 700 : (int) dimension.getHeight() - 50;

		// Panel bottom will hold the two buttons and an info label
		JPanel upperBottom = new JPanel();
		upperBottom.setLayout(new BoxLayout(upperBottom, BoxLayout.X_AXIS));
		upperBottom.setPreferredSize(new Dimension(setupSize, 50));
		JLabel note = new JLabel("<html>Click on a Square to Change it</html>", SwingConstants.CENTER);
		note.setPreferredSize(new Dimension(setupSize / 2 - 75, 25));

		changePlayer = new JCheckBox("White To Play");
		changePlayer.setAlignmentX(Component.CENTER_ALIGNMENT);
		changePlayer.setMnemonic(KeyEvent.VK_W);
		changePlayer.setSelected(true);

		// Adding everything to the bottom panels
		upperBottom.add(diffWHolderCvC);
		upperBottom.add(Box.createRigidArea(new Dimension(30, 0)));
		upperBottom.add(diffBHolderCvC);
		upperBottom.add(Box.createRigidArea(new Dimension(100, 0)));
		upperBottom.add(changePlayer);
		upperBottom.add(Box.createRigidArea(new Dimension(30, 0)));

		JPanel lowerBottom = new JPanel();
		lowerBottom.setLayout(new BoxLayout(lowerBottom, BoxLayout.X_AXIS));
		lowerBottom.setPreferredSize(new Dimension(setupSize, 50));
		lowerBottom.add(note);
		lowerBottom.add(Box.createRigidArea(new Dimension(20, 0)));
		lowerBottom.add(start);
		lowerBottom.add(Box.createRigidArea(new Dimension(90, 0)));
		lowerBottom.add(goBack);
		lowerBottom.add(Box.createRigidArea(new Dimension(20, 0)));

		panel.add(upperBottom);
		panel.add(lowerBottom);
		setup.add(panel);

		// Set the size of the window
		setup.setSize(setupSize, setupSize);
		setup.setResizable(false);

		// Centres the window with respect to the user's screen resolution
		int x = (int) ((dimension.getWidth() - setupSize) / 2);
		int y = (int) ((dimension.getHeight() - setupSize - 50) / 2);
		setup.setLocation(x, y);

		// Make the configured window visible
		setup.setVisible(true);
	}

	// Modified UI for the ComboBoxs
	private static class ModifiedComboBoxUI extends BasicComboBoxUI {
		protected JButton createArrowButton() {
			return new JButton() {

				private static final long serialVersionUID = 1L;

				public int getWidth() {
					return 0;
				}
			};
		}

		protected ComboPopup createPopup() {
			BasicComboPopup bcp = (BasicComboPopup) super.createPopup();
			Border outer = BorderFactory.createRaisedBevelBorder();
			Border inner = BorderFactory.createLoweredBevelBorder();
			// Set the border around the pop-up
			bcp.setBorder(BorderFactory.createCompoundBorder(outer, inner));

			return bcp;
		}
	}

	// Custom renderer for for the ComboBoxs
	private static class ComboBoxRenderer extends JLabel implements ListCellRenderer<Object> {

		private static final long serialVersionUID = 1L;

		public ComboBoxRenderer() {
			setOpaque(true);
			setHorizontalAlignment(CENTER);
		}

		// Gives the drop down a white background
		public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected,
				boolean cellHasFocus) {

			setText(value.toString());

			Color background;
			Color foreground;

			if (isSelected) {
				background = list.getSelectionBackground();
				foreground = Color.BLACK;
			} else {
				background = Color.WHITE;
				foreground = Color.BLACK;
			}

			setBackground(background);
			setForeground(foreground);

			return this;
		}
	}

}
