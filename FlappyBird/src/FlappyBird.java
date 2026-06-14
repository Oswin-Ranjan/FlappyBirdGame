import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class FlappyBird extends JPanel implements ActionListener, KeyListener {
  int boardWidth = 360;
  int boardHeight = 640;

  //Images for the game
  Image backgroundImg;
  Image birdImg;
  Image topPipeImg;
  Image bottomPipeImg;

  //Bird
  int birdX=boardWidth/8;
  int birdY=boardHeight/2;
  int birdWidth=34;
  int birdHeight=24;

  class Bird {
    int x=birdX;
    int y=birdY;
    int width=birdWidth;
    int height=birdHeight;
    Image img;

    Bird(Image img) {
      this.img=img;
    }
  }

  //Pipes
  int pipeX=boardWidth;
  int pipeY=0;
  int pipeWidth=64;
  int pipeHeight=512;

  class Pipe {
    int x=pipeX;
    int y=pipeY;
    int width=pipeWidth;
    int height=pipeHeight;
    Image img;
    boolean passed=false; // To track if the bird has passed the pipe for scoring

    Pipe(Image img) {
      this.img=img;
    }
  }

  //Game Logic
  Bird bird;
  int velocityX = -4; // Initial horizontal velocity for the pipes
  int velocityY = 0; // Initial upward velocity for the bird
  int gravity = 1;

  ArrayList<Pipe> pipes;
  Random random=new Random();

  Timer gameLoop;
  Timer placePipesTimer;

  boolean gameOver=false;
  double score=0;

  FlappyBird() {
    setPreferredSize(new Dimension(boardWidth, boardHeight));
    setFocusable(true); // Allow the panel to receive keyboard focus
    addKeyListener(this); // Add the key listener to the panel

    //Load images
    backgroundImg = new ImageIcon("flappybirdbg.png").getImage();
    birdImg = new ImageIcon("flappybird.png").getImage();
    topPipeImg = new ImageIcon("toppipe.png").getImage();
    bottomPipeImg = new ImageIcon("bottompipe.png").getImage();

    //bird
    bird = new Bird(birdImg);
    pipes = new ArrayList<>();

    //place pipes timer
    placePipesTimer = new Timer(1500, new ActionListener() {
      @Override
      public void actionPerformed(ActionEvent e) {
        placePipes(); // Call the method to place new pipes at regular intervals
      }
    });
    placePipesTimer.start(); // Start the timer to begin placing pipes

    //game timer
    gameLoop=new Timer(1000/60, this);
    gameLoop.start();
  }

  public void placePipes() {
    int randomPipeY=(int)(pipeY-pipeHeight/4-Math.random()*(pipeHeight/2)); // Randomize the vertical position of the pipes
    int openingSpace=boardHeight/4; // Space between the top and bottom pipes

    Pipe topPipe = new Pipe(topPipeImg);
    topPipe.y = randomPipeY; // Set the y-coordinate of the top pipe
    pipes.add(topPipe);

    Pipe bottomPipe = new Pipe(bottomPipeImg);
    bottomPipe.y = topPipe.y + pipeHeight + openingSpace; // Set the y-coordinate of the bottom pipe based on the top pipe's position and the opening space
    pipes.add(bottomPipe);
  }

  @Override
  public void paintComponent(Graphics g) {
    super.paintComponent(g); // Call the superclass method to ensure proper painting
    draw(g); // Call the draw method to render the game elements
  }

  public void draw(Graphics g) { 
    //background
    g.drawImage(backgroundImg, 0, 0,boardWidth, boardHeight, null);

    //bird
    g.drawImage(bird.img, bird.x, bird.y, bird.width, bird.height, null);

    //pipes
    for(int i=0; i<pipes.size(); i++) {
      Pipe pipe = pipes.get(i);
      g.drawImage(pipe.img, pipe.x, pipe.y, pipe.width, pipe.height, null);
    }

    //score
    g.setColor(Color.white);
    g.setFont(new Font("Arial", Font.PLAIN, 32));
    if(gameOver) {
      g.drawString("Game Over: " + String.valueOf((int) score), 10, 35); // Display the final score when the game is over
    }
    else {
      g.drawString("Score: " + String.valueOf((int) score), 10, 35); // Display the current score during the game
    }
  }

  public void move() {
    velocityY += gravity; // Apply gravity to the bird's vertical velocity
    bird.y += velocityY; // Move the bird up by decreasing its y-coordinate
    bird.y = Math.max(bird.y, 0); // Prevent the bird from going above the top of the screen

    for(int i = 0; i < pipes.size(); i++) {
      Pipe pipe = pipes.get(i);
      pipe.x += velocityX; // Move the pipe left by decreasing its x-coordinate

      if(!pipe.passed && pipe.x + pipe.width < bird.x) {
        score += 0.5; // Increment score by 0.5 for each pipe passed (top and bottom)
        pipe.passed = true; // Mark the pipe as passed to avoid multiple scoring
      }

      if(collision(bird, pipe)) {
        gameOver = true; // Set game over if there is a collision between the bird and a pipe
      }
    }

    if(bird.y > boardHeight) {
      gameOver = true; // Set game over if the bird falls below the bottom of the screen
    }
  }

  public boolean collision(Bird a, Pipe b) {
    return a.x < b.x + b.width && a.x + a.width > b.x && a.y < b.y + b.height && a.y + a.height > b.y; // Check for collision between the bird and a pipe
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    move(); // Update the game state (move the bird)
    repaint(); // Request a repaint to update the display
    if(gameOver) {
      gameLoop.stop(); // Stop the game loop timer
      placePipesTimer.stop(); // Stop the pipe placement timer
    }
  }

  @Override
  public void keyPressed(KeyEvent e) {
    if(e.getKeyCode() == KeyEvent.VK_SPACE) {
      velocityY = -9; // Set the bird's vertical velocity to a negative value to make it jump

      if(gameOver) {
        // Reset the game state for a new game
        bird.y = birdY; // Reset the bird's position
        velocityY = 0; // Reset the bird's vertical velocity
        pipes.clear(); // Clear all existing pipes
        score = 0; // Reset the score
        gameOver = false; // Reset the game over flag
        placePipesTimer.start(); // Restart the pipe placement timer
        gameLoop.start(); // Restart the game loop timer
      }
    }
  }

  @Override
  public void keyTyped(KeyEvent e) {}

  @Override
  public void keyReleased(KeyEvent e) {}

}
