package shapes;

import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

import edu.uga.miage.m1.polygons.gui.shapes.Cube;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CubeTest {

    @Test
    public void testConstructorWithCoordinates() {
        Cube cube = new Cube(100, 150);
        assertEquals(75, cube.getX());
        assertEquals(125, cube.getY());
    }

    @Test
    public void testConstructorWithSimpleShape() {
        SimpleShape simpleShape = new Cube(200, 250);
        Cube cube = new Cube(simpleShape);
        assertEquals(175, cube.getX());
        assertEquals(225, cube.getY());
    }

    @Test
    public void testContains() {
        Cube cube = new Cube(50, 50);
        assertTrue(cube.contains(new Point(60, 60)));
        assertFalse(cube.contains(new Point(10, 10)));
    }

    @Test
    public void testTypeOfShape() {
        Cube cube = new Cube(50, 50);
        assertEquals("Cube", cube.typeOfShape());
    }

    @Test
    public void testDraw() {
        // Crée une image pour les tests
        int width = 100;
        int height = 100;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // Crée un objet Cube
        Cube cube = new Cube(50, 50);

        // Obtient un objet Graphics2D à partir de l'image
        Graphics2D g2 = image.createGraphics();

        // Appelle la méthode draw de Cube
        cube.draw(g2);

        // Vérifie que le pixel au centre de l'image est de la couleur attendue
        Color centerColor = new Color(image.getRGB(50, 50));
        Color expectedColor = new Color(0, 0, 0); // La couleur dépend de la méthode paintComponent dans CubePanel
        assertEquals(expectedColor, centerColor);

        // Nettoie
        g2.dispose();
    }

    @Test
    public void testGetX() {
        Cube cube = new Cube(25, 30);
        int expectedX = 0; // Le constructeur soustrait 25 de la valeur donnée (25 - 25)
        int actualX = cube.getX();
        assertEquals(expectedX, actualX);
    }

    @Test
    public void testGetY() {
        Cube cube = new Cube(25, 30);
        int expectedY = 5; // Le constructeur soustrait 25 de la valeur donnée (30 - 25)
        int actualY = cube.getY();
        assertEquals(expectedY, actualY);
    }

    @Test
    public void testSetX() {
        Cube cube = new Cube(25, 30);
        cube.setX(10);
        assertEquals(10, cube.getX());
    }

    @Test
    public void testSetY() {
        Cube cube = new Cube(25, 30);
        cube.setY(10);
        assertEquals(10, cube.getY());
    }

    @Test
    public void testExtremeCoordinates() {
        Cube cube = new Cube(Integer.MAX_VALUE, Integer.MAX_VALUE);
        assertTrue(cube.getX() > 0);
        assertTrue(cube.getY() > 0);
    }

    @Test
    public void testNegativeCoordinates() {
        Cube cube = new Cube(-10, -10);
        assertEquals(-35, cube.getX());
        assertEquals(-35, cube.getY());
    }
}
