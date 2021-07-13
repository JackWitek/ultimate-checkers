package windows;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

//Class for pop up message frames
public class PopupFrame extends JFrame {

    private static final long serialVersionUID = 1L;

    PopupFrame(String title, String message) {
        super(title);

        // Store an internal copy of the frame itself
        final PopupFrame frame = this;

        JPanel popupPanel = new JPanel();
        popupPanel.setLayout(new BoxLayout(popupPanel, BoxLayout.Y_AXIS));

        JLabel tooMany = new JLabel(message);

        JButton okay = new JButton("Okay");

        // Setting up the components
        tooMany.setAlignmentX(Component.CENTER_ALIGNMENT);
        okay.setAlignmentX(Component.CENTER_ALIGNMENT);

        okay.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                frame.setVisible(false);
            }
        });

        // adding he components
        popupPanel.add(Box.createRigidArea(new Dimension(0, 20)));
        popupPanel.add(tooMany);
        popupPanel.add(Box.createRigidArea(new Dimension(0, 30)));
        popupPanel.add(okay);

        this.setMinimumSize(new Dimension(350, 200));

        // Centres the window with respect to the user's screen resolution
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
        this.add(popupPanel, BorderLayout.CENTER);
        this.setVisible(false);
    }
}