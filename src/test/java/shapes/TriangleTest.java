package shapes;

import org.junit.jupiter.api.Test;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.BasicStroke;

import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TriangleTest {

    @Test
    public void testDraw() {
        // CrÃ©e une image pour les tests
        int width = 100;
        int height = 100;
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // CrÃ©e un objet Triangle
        Triangle triangle = new Triangle(50, 50);
        
        Triangle triangleNeg = new Triangle(-10,-10);
        
        Triangle triangleMax = new Triangle(Integer.MAX_VALUE, Integer.MIN_VALUE);
       
        
        assertEquals(25, triangle.getX());
        assertEquals(25, triangle.getY());
        
        assertEquals(-35, triangleNeg.getX());
        assertEquals(-35, triangleNeg.getY());
        
        assertTrue(triangleMax.getX() > 0);
        assertTrue(triangleMax.getY() > 0);

        // Obtient un objet Graphics2D Ã  partir de l'image
        Graphics2D g2 = image.createGraphics();

        // Appelle la mÃ©thode draw de Triangle
        triangle.draw(g2);

        // VÃ©rifie que le pixel au centre de l'image est vert
        Color centerColor = new Color(image.getRGB(50, 50));
        Color expectedColor = new Color(127, 255, 127);
        assertEquals(expectedColor, centerColor);

        // VÃ©rifie que le triangle a bien Ã©tÃ© dessinÃ© avec une bordure noire
        BasicStroke stroke = (BasicStroke) g2.getStroke();
        assertEquals(2.0f, stroke.getLineWidth());
        Color borderColor = new Color(image.getRGB(35, 40));
        expectedColor = Color.BLACK;
        assertEquals(expectedColor, borderColor);

        // Nettoie
        g2.dispose();
    }
}
