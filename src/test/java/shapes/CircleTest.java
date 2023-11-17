package shapes;

import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.BasicStroke;

import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CircleTest {

    @Test
    public void testConstructorWithCoordinates() {
        Circle circle = new Circle(100, 150);
        assertEquals(75, circle.getX());
        assertEquals(125, circle.getY());
    }

    @Test
    public void testConstructorWithSimpleShape() {
        SimpleShape simpleShape = new Circle(200, 250);
        Circle circle = new Circle(simpleShape);
        assertEquals(175, circle.getX());
        assertEquals(225, circle.getY());
    }

    @Test
    public void testContains() {
        Circle circle = new Circle(50, 50);
        assertTrue(circle.contains(new Point(60, 60)));
        assertFalse(circle.contains(new Point(10, 10)));
    }

    @Test
    public void testTypeOfShape() {
        Circle circle = new Circle(50, 50);
        assertEquals("Circle", circle.typeOfShape());
    }

    @Test
    public void testDraw() {
        // Crée une image pour les tests
        int width = 100;
        int height = 100;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Crée un objet Circle
        Circle circle = new Circle(50, 50);

        // Obtient un objet Graphics2D à partir de l'image
        Graphics2D g2 = image.createGraphics();

        // Appelle la méthode draw de Circle
        circle.draw(g2);

        // Vérifie que le pixel au centre de l'image est rouge
        Color centerColor = new Color(image.getRGB(50, 50));
        Color expectedColor = new Color(255, 127,127);
        assertEquals(expectedColor, centerColor);


        // Vérifie que le cercle a bien été dessiné avec une bordure noire
        BasicStroke stroke = (BasicStroke) g2.getStroke();
        assertEquals(2.0f, stroke.getLineWidth());
        Color borderColor = new Color(image.getRGB(25, 25));
        expectedColor = Color.BLACK;
        assertEquals(expectedColor, borderColor);

        // Nettoie
        g2.dispose();
    }

    @Test
    public void testGetX() {
        Circle circle = new Circle(25, 30);
        int expectedX = 0; // Le constructeur soustrait 25 de la valeur donnée (25 - 25)
        int actualX = circle.getX();
        assertEquals(expectedX, actualX);
    }

    @Test
    public void testGetY() {
        Circle circle = new Circle(25, 30);
        int expectedY = 5; // Le constructeur soustrait 25 de la valeur donnée (30 - 25)
        int actualY = circle.getY();
        assertEquals(expectedY, actualY);
    }

    @Test
    public void testSetX() {
        Circle circle = new Circle(25, 30);
        circle.setX(10);
        assertEquals(10, circle.getX());
    }

    @Test
    public void testSetY() {
        Circle circle = new Circle(25, 30);
        circle.setY(10);
        assertEquals(10, circle.getY());
    }

    @Test
    public void testExtremeCoordinates() {
        Circle circle = new Circle(Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertTrue(circle.getX() > 0);
        assertTrue(circle.getY() > 0);
    }
    @Test
    public void testNegativeCoordinates() {
        Circle circle = new Circle(-10, -10);
        assertEquals(-35, circle.getX());
        assertEquals(-35, circle.getY());
    }
}
