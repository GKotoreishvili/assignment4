
import javax.swing.*;
import javax.swing.border.Border;
import java.io.*;
import java.util.concurrent.BlockingDeque;


public class JCount extends JPanel {

    private JTextField text;
    private JLabel label;
    private JButton start;
    private JButton stop;

    public JCount() {

        start = new JButton("Start");
        start = new JButton("Stop");
        label = new JLabel();
    }

    private static void createAndShowGUI() {

    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {

            @Override
            public void run() {
                createAndShowGUI();
            }
        });
    }
}

