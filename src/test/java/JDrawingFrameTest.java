import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.uga.miage.m1.polygons.gui.JDrawingFrame;

import static org.junit.jupiter.api.Assertions.*;

import java.awt.event.ActionEvent;
import java.awt.event.MouseEvent;
import java.io.File;

import javax.swing.ImageIcon;

public class JDrawingFrameTest {

    private JDrawingFrame drawingFrame;

    @BeforeEach
    public void setUp() {
        drawingFrame = new JDrawingFrame("Test Frame");
    }

    @Test
    public void testAddShape() {
        // Test adding a shape to the toolbar
        drawingFrame.addShape(JDrawingFrame.Shapes.SQUARE, new ImageIcon());
        // Ensure the shape is added to the toolbar
        assertNotNull(drawingFrame.m_buttons.get(JDrawingFrame.Shapes.SQUARE));
    }

    @Test
    public void testMouseClicked() {
        // Simulate a mouse click event
        MouseEvent event = new MouseEvent(drawingFrame.m_panel, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 50, 50, 1, false);
        drawingFrame.mouseClicked(event);

        // Test if a shape has been added to the shapes list
        // assertTrue(drawingFrame.shapesList.isEmpty());
    }

    @Test
    public void testSauvegardeActionListener() {
        // Simulate a "Sauvegarder" button click
        ActionEvent event = new ActionEvent(drawingFrame, ActionEvent.ACTION_PERFORMED, "Sauvegarder");
        drawingFrame.new SauvegardeActionListener().actionPerformed(event);

        // Test if the save files (JSON and XML) have been created
        File jsonFile = new File("src/main/java/edu/uga/miage/m1/polygons/gui/save/Save.json");
        File xmlFile = new File("src/main/java/edu/uga/miage/m1/polygons/gui/save/Save.xml");
        assertTrue(jsonFile.exists());
        assertTrue(xmlFile.exists());
    }

    @Test
    public void testGoBack() {
        // Simulate adding a shape and then going back
        MouseEvent event = new MouseEvent(drawingFrame.m_panel, MouseEvent.MOUSE_CLICKED, System.currentTimeMillis(), 0, 50, 50, 1, false);
        drawingFrame.mouseClicked(event);
        // int initialShapesCount = drawingFrame.shapesList.size();

        drawingFrame.goBack();

        // Ensure the last shape has been removed
        // assertEquals(initialShapesCount, drawingFrame.shapesList.size());
    }
}
