import edu.uga.miage.m1.polygons.gui.StateOfPaint;
import edu.uga.miage.m1.polygons.gui.persistence.Visitable;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StateOfPaintTest {

    @Test
    void testGetShapesList() {
        ArrayList<Visitable> shapesList = new ArrayList<>();
        StateOfPaint stateOfPaint = new StateOfPaint();
        stateOfPaint.setShapesList(shapesList);

        assertEquals(shapesList, stateOfPaint.getShapesList());
    }

    @Test
    void testSetShapesList() {
        ArrayList<Visitable> shapesList = new ArrayList<>();
        StateOfPaint stateOfPaint = new StateOfPaint();
        stateOfPaint.setShapesList(shapesList);

        assertEquals(shapesList, stateOfPaint.getShapesList());
    }
}
