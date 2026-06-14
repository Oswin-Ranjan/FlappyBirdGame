import javax.swing.*;

public class App {
    public static void main(String[] args) throws Exception {
        int boardWidth=360;
        int boardHeight=640;

        JFrame frame=new JFrame("Flappy Bird");
        frame.setVisible(true); // Make the window visible
        frame.setSize(boardWidth, boardHeight); // Set the size of the windowS
        frame.setLocationRelativeTo(null); // Center the window on the screen
        frame.setResizable(false); // Disable window resizing
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // Exit the application when the window is closed

        FlappyBird flappyBird=new FlappyBird(); // Create an instance of the FlappyBird class
        frame.add(flappyBird); // Add the FlappyBird panel to the frame
        frame.pack(); // Adjust the frame size to fit the preferred size of the FlappyBird panel
        flappyBird.requestFocus(); // Request focus for the FlappyBird panel to receive keyboard input
        frame.setVisible(true); // Make the window visible after adding the panel
    }
}
