package shapes;

import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.BasicStroke;

import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TriangleTest {

    @Test
    public void testConstructorWithCoordinates() {
        Triangle triangle = new Triangle(100, 150);
        assertEquals(75, triangle.getX());
        assertEquals(125, triangle.getY());
    }

    @Test
    public void testConstructorWithSimpleShape() {
        SimpleShape simpleShape = new Triangle(200, 250);
        Triangle triangle = new Triangle(simpleShape);
        assertEquals(175, triangle.getX());
        assertEquals(225, triangle.getY());
    }

    @Test
    public void testContains() {
        Triangle triangle = new Triangle(50, 50);
        assertTrue(triangle.contains(new Point(60, 60)));
        assertFalse(triangle.contains(new Point(10, 10)));
    }

    @Test
    public void testTypeOfShape() {
        Triangle triangle = new Triangle(50, 50);
        assertEquals("Triangle", triangle.typeOfShape());
    }

    @Test
    public void testDraw() {
        // Crée une image pour les tests
        int width = 100;
        int height = 100;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Crée un objet Triangle
        Triangle triangle = new Triangle(50, 50);

        // Obtient un objet Graphics2D à partir de l'image
        Graphics2D g2 = image.createGraphics();

        // Appelle la méthode draw de Triangle
        triangle.draw(g2);

        // Vérifie que le pixel au centre de l'image est vert
        Color centerColor = new Color(image.getRGB(50, 50));
        Color expectedColor = new Color(127, 255, 127);
        assertEquals(expectedColor, centerColor);

        // Vérifie que le triangle a bien été dessiné avec une bordure noire
        BasicStroke stroke = (BasicStroke) g2.getStroke();
        assertEquals(2.0f, stroke.getLineWidth());
        Color borderColor = new Color(image.getRGB(35, 40));
        expectedColor = Color.BLACK;
        assertEquals(expectedColor, borderColor);

        // Nettoie
        g2.dispose();
    }

    @Test
    public void testGetX() {
        Triangle triangle = new Triangle(25, 30);
        int expectedX = 0; // Le constructeur soustrait 25 de la valeur donnée (25 - 25)
        int actualX = triangle.getX();
        assertEquals(expectedX, actualX);
    }

    @Test
    public void testGetY() {
        Triangle triangle = new Triangle(25, 30);
        int expectedY = 5; // Le constructeur soustrait 25 de la valeur donnée (30 - 25)
        int actualY = triangle.getY();
        assertEquals(expectedY, actualY);
    }

    @Test
    public void testSetX() {
        Triangle triangle = new Triangle(25, 30);
        triangle.setX(10);
        assertEquals(10, triangle.getX());
    }

    @Test
    public void testSetY() {
        Triangle triangle = new Triangle(25, 30);
        triangle.setY(10);
        assertEquals(10, triangle.getY());
    }

    @Test
    public void testExtremeCoordinates() {
        Triangle triangle = new Triangle(Integer.MAX_VALUE, Integer.MIN_VALUE);
        assertTrue(triangle.getX() > 0);
        assertTrue(triangle.getY() > 0);
    }

    @Test
    public void testNegativeCoordinates() {
        Triangle triangle = new Triangle(-10, -10);
        assertEquals(-35, triangle.getX());
        assertEquals(-35, triangle.getY());
    }
}
