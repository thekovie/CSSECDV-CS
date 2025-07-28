package View;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.util.Random;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.JButton;
import javax.swing.BorderFactory;
import java.awt.FlowLayout;

public class CaptchaPanel extends JPanel {

    private String captchaText;
    private BufferedImage captchaImage;
    private JTextField captchaInputField;
    private JLabel captchaImageLabel;
    private JButton refreshButton;

    public CaptchaPanel() {
        setLayout(new FlowLayout());
        setBorder(BorderFactory.createTitledBorder("CAPTCHA Verification"));
        
        captchaImageLabel = new JLabel();
        add(captchaImageLabel);

        captchaInputField = new JTextField(10);
        add(captchaInputField);

        refreshButton = new JButton("Refresh");
        refreshButton.addActionListener(e -> generateCaptcha());
        add(refreshButton);

        generateCaptcha(); // Generate initial CAPTCHA
    }

    private void generateCaptcha() {
        int width = 150;
        int height = 50;
        captchaImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        Graphics g = captchaImage.getGraphics();
        Random rand = new Random();

        // Fill background with a light color
        g.setColor(new Color(230, 230, 230));
        g.fillRect(0, 0, width, height);

        // Generate random CAPTCHA text (e.g., 5-6 random characters)
        captchaText = generateRandomString(5 + rand.nextInt(2)); // 5 or 6 characters
        
        // Draw the CAPTCHA text
        g.setColor(Color.BLACK);
        g.setFont(new Font("Serif", Font.BOLD, 24));
        int x = 10;
        for (char c : captchaText.toCharArray()) {
            g.drawString(String.valueOf(c), x, 35 + rand.nextInt(10) - 5); // Slight random vertical offset
            x += 25;
        }

        // Add some noise (lines, circles)
        g.setColor(Color.GRAY);
        for (int i = 0; i < 10; i++) {
            g.drawLine(rand.nextInt(width), rand.nextInt(height),
                       rand.nextInt(width), rand.nextInt(height));
        }
        for (int i = 0; i < 5; i++) {
            g.drawOval(rand.nextInt(width - 20), rand.nextInt(height - 20), 10 + rand.nextInt(10), 10 + rand.nextInt(10));
        }

        g.dispose();
        captchaImageLabel.setIcon(new javax.swing.ImageIcon(captchaImage));
        captchaInputField.setText(""); // Clear input field
    }

    private String generateRandomString(int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder sb = new StringBuilder();
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    public boolean verifyCaptcha(String input) {
        return captchaText.equalsIgnoreCase(input);
    }

    public String getCaptchaInput() {
        return captchaInputField.getText();
    }
    
    public void resetCaptcha() {
        generateCaptcha();
    }
}
