import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.ConvolveOp;
import java.awt.image.Kernel;
import java.util.Random;
import javax.swing.*;

public class GamePanel2 extends JPanel implements Runnable {
    static final int GAME_WIDTH = 1000;
    static final int GAME_HEIGHT = (int) (GAME_WIDTH * 0.55555);
    static final Dimension SCREEN_SIZE = new Dimension(GAME_WIDTH, GAME_HEIGHT);
    static final int BALL_DIAMETER = 20;
    static final int PADDLE_WIDTH = 25;
    static final int PADDLE_HEIGHT = 100;

    boolean isPaused = false;

    Thread gameThread;
    Image image;
    Graphics graphics;
    Random random;
    Paddle paddle1;
    Paddle paddle2;
    Ball ball;
    Score score;

    BufferedImage bufferedImage; // For rendering with blur effect

    GamePanel2() {
        newPaddle();
        newBall();
        score = new Score(GAME_WIDTH, GAME_HEIGHT);
        this.setFocusable(true);
        this.addKeyListener(new AL());
        this.setPreferredSize(SCREEN_SIZE);

        gameThread = new Thread(this);
        gameThread.start();
    }

    public void newBall() {
        random = new Random();
        ball = new Ball((GAME_WIDTH / 2) - (BALL_DIAMETER / 2), random.nextInt(GAME_HEIGHT - BALL_DIAMETER), BALL_DIAMETER, BALL_DIAMETER);
    }

    public void newPaddle() {
        paddle1 = new Paddle(0, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 1);
        paddle2 = new Paddle(GAME_WIDTH - PADDLE_WIDTH, (GAME_HEIGHT / 2) - (PADDLE_HEIGHT / 2), PADDLE_WIDTH, PADDLE_HEIGHT, 2);
    }

    public void paint(Graphics g) {
        if (bufferedImage == null) {
            bufferedImage = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_ARGB);
        }

        Graphics2D g2d = (Graphics2D) bufferedImage.getGraphics();
        g2d.setColor(Color.BLACK);
        g2d.fillRect(0, 0, getWidth(), getHeight());

        draw(g2d); // Draw game components on buffered image

        if (isPaused) {
            BufferedImage blurredImage = applyBlur(bufferedImage);
            g.drawImage(blurredImage, 0, 0, this);

            // Draw pause message
            g.setColor(new Color(255, 255, 255, 200)); // Semi-transparent white
            g.setFont(new Font("Arial", Font.BOLD, 48));
            String message = "Game Paused";
            FontMetrics fm = g.getFontMetrics();
            int x = (GAME_WIDTH - fm.stringWidth(message)) / 2;
            int y = GAME_HEIGHT / 2;
            g.drawString(message, x, y);
        } else {
            g.drawImage(bufferedImage, 0, 0, this); // Draw normal image
        }

        g2d.dispose();
    }

    public BufferedImage applyBlur(BufferedImage img) {
        // Larger kernel for increased blur
        float[] matrix = new float[49];
        for (int i = 0; i < matrix.length; i++) {
            matrix[i] = 1.0f / 49.0f; // Uniform weight for Gaussian blur
        }
        Kernel kernel = new Kernel(7, 7, matrix); // 7x7 kernel for stronger blur
        ConvolveOp op = new ConvolveOp(kernel, ConvolveOp.EDGE_NO_OP, null);
        return op.filter(img, null);
    }

    public void draw(Graphics g) {
        paddle1.draw(g);
        paddle2.draw(g);
        ball.draw(g);
        score.draw(g);
    }

    public void move() {
        paddle1.move();
        paddle2.move();
        ball.move();
    }

    public void checkCollision() {
        // Bounce ball off top and bottom window edges
        if (ball.y <= 0) {
            ball.setYDirection(-ball.yVelocity);
        }
        if (ball.y >= GAME_HEIGHT - BALL_DIAMETER) {
            ball.setYDirection(-ball.yVelocity);
        }
        // Bounce ball off paddles when collides
        if (ball.intersects(paddle1)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;
            if (ball.yVelocity > 0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
            ball.setXDirection(ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }
        if (ball.intersects(paddle2)) {
            ball.xVelocity = Math.abs(ball.xVelocity);
            ball.xVelocity++;
            if (ball.yVelocity > 0)
                ball.yVelocity++;
            else
                ball.yVelocity--;
            ball.setXDirection(-ball.xVelocity);
            ball.setYDirection(ball.yVelocity);
        }

        // Stop paddles at window edges
        if (paddle1.y <= 0)
            paddle1.y = 0;
        if (paddle1.y >= GAME_HEIGHT - PADDLE_HEIGHT)
            paddle1.y = GAME_HEIGHT - PADDLE_HEIGHT;
        if (paddle2.y <= 0)
            paddle2.y = 0;
        if (paddle2.y >= GAME_HEIGHT - PADDLE_HEIGHT)
            paddle2.y = GAME_HEIGHT - PADDLE_HEIGHT;

        // Give a player point if ball hits and create new ball
        if (ball.x <= 0) {
            score.Player2++;
            newPaddle();
            newBall();
        }
        if (ball.x >= GAME_WIDTH - BALL_DIAMETER) {
            score.Player1++;
            newPaddle();
            newBall();
        }
    }

    public void run() {
        long lastTime = System.nanoTime();
        double amountOfTicks = 60.0;
        double ns = 1000000000 / amountOfTicks;
        double delta = 0;

        while (true) {
            long now = System.nanoTime();
            delta += (now - lastTime) / ns;
            lastTime = now;
            if (delta >= 1) {
                if (!isPaused) {
                    move();
                }
                checkCollision();
                repaint();
                delta--;
            }
        }
    }

    public class AL extends KeyAdapter {
        public void keyPressed(KeyEvent e) {
            paddle1.keyPressed(e);
            paddle2.keyPressed(e);

            if (e.getKeyCode() == KeyEvent.VK_SPACE) {
                isPaused = !isPaused;
                repaint(); // Trigger repaint for blur and message
            }
        }

        public void keyReleased(KeyEvent e) {
            paddle1.keyReleased(e);
            paddle2.keyReleased(e);
        }
    }
}
