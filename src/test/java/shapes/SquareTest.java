package shapes;

import org.junit.jupiter.api.Test;

import edu.uga.miage.m1.polygons.gui.shapes.Square;

import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class SquareTest {

    @Test
    public void testDraw() {
        // Crée une image pour les tests
        int width = 100;
        int height = 100;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Crée un objet Square
        Square square = new Square(50, 50);

        // Obtient un objet Graphics2D à partir de l'image
        Graphics2D g2 = image.createGraphics();

        // Appelle la méthode draw de Square
        square.draw(g2);

        // Vérifie que le pixel au centre de l'image est bleu
        Color centerColor = new Color(image.getRGB(50, 50));
        Color expectedColor = new Color(127, 127,255);
        assertEquals(expectedColor, centerColor);

        // Vérifie que le carré a bien été dessiné avec une bordure noire
        BasicStroke stroke = (BasicStroke) g2.getStroke();
        assertEquals(2.0f, stroke.getLineWidth());
        Color borderColor = new Color(image.getRGB(25, 30));
        expectedColor = Color.BLACK;
        assertEquals(expectedColor, borderColor);

        // Nettoie
        g2.dispose();
    }

    @Test
    public void testGetX() {
        Square square = new Square(30, 40);
        int expectedX = 5; // Le constructeur soustrait 25 de la valeur donnée (30 - 25)
        int actualX = square.getX();
        assertEquals(expectedX, actualX);
    }

    @Test
    public void testGetY() {
        Square square = new Square(30, 40);
        int expectedY = 15; // Le constructeur soustrait 25 de la valeur donnée (40 - 25)
        int actualY = square.getY();
        assertEquals(expectedY, actualY);
    }
    @Test
    public void testExtremeCoordinates() {
        Square square = new Square(Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertTrue(square.getX() > 0);
        assertTrue(square.getY() > 0);
    }
    @Test
    public void testNegativeCoordinates() {
        Square square = new Square(-10, -10);
        assertEquals(-35, square.getX());
        assertEquals(-35, square.getY());
    }
}

