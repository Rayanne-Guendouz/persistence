import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import edu.uga.miage.m1.polygons.gui.JDrawingFrame;

import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.*;

public class JDrawingFrameTest {

    private JDrawingFrame drawingFrame;

    @BeforeEach
    void setUp() {
        drawingFrame = new JDrawingFrame("TestFrame");
    }

    @Test
    void testMouseReleased() {
        // Add a shape to the drawing frame
        drawingFrame.addShape(JDrawingFrame.Shapes.SQUARE, null);

        // Simulate a mouse press event
        MouseEvent pressEvent = new MouseEvent(drawingFrame.getPanel(), MouseEvent.MOUSE_PRESSED,
                System.currentTimeMillis(), 0, 50, 50, 1, false);
        drawingFrame.mousePressed(pressEvent);

        // Simulate a mouse release event
        MouseEvent releaseEvent = new MouseEvent(drawingFrame.getPanel(), MouseEvent.MOUSE_RELEASED,
                System.currentTimeMillis(), 0, 60, 60, 1, false);
        drawingFrame.mouseReleased(releaseEvent);

        // Check if the shape is no longer selected
        assertFalse(drawingFrame.isShapeSelected());
        assertNull(drawingFrame.getSelectedShape());
    }



    @Test
    void testSave() {
        // Add a shape to the drawing frame
        drawingFrame.addShape(JDrawingFrame.Shapes.SQUARE, null);

        // Simulate a mouse click event
        MouseEvent mouseEvent = new MouseEvent(drawingFrame.getPanel(), MouseEvent.MOUSE_CLICKED,
                System.currentTimeMillis(), 0, 50, 50, 1, false);
        drawingFrame.mouseClicked(mouseEvent);

        // Save the current state
        drawingFrame.save();

        // Check if the save file exists
        Path jsonFilePath = Paths.get("src/main/java/edu/uga/miage/m1/polygons/gui/save/Save.json");
        Path xmlFilePath = Paths.get("src/main/java/edu/uga/miage/m1/polygons/gui/save/Save.xml");

        assertTrue(Files.exists(jsonFilePath));
        assertTrue(Files.exists(xmlFilePath));

        // Clean up: delete the save files
        try {
            Files.deleteIfExists(jsonFilePath);
            Files.deleteIfExists(xmlFilePath);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
