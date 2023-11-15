package persistence;
import edu.uga.miage.m1.polygons.gui.persistence.JSonVisitor;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JSonVisitorTest {

    @Test
    public void testVisitCircle() {
        Circle circle = new Circle(10, 20);
        JSonVisitor jsonVisitor = new JSonVisitor();
        jsonVisitor.visit(circle);
        assertEquals("{\"type\": \"circle\", \"x\": -15, \"y\": -5}", jsonVisitor.getRepresentation());
    }

    @Test
    public void testVisitSquare() {
        Square square = new Square(5, 5);
        JSonVisitor jsonVisitor = new JSonVisitor();
        jsonVisitor.visit(square);
        assertEquals("{\"type\": \"square\", \"x\": -20, \"y\": -20}", jsonVisitor.getRepresentation());
    }

    @Test
    public void testVisitTriangle() {
        Triangle triangle = new Triangle(0, 0);
        JSonVisitor jsonVisitor = new JSonVisitor();
        jsonVisitor.visit(triangle);
        assertEquals("{\"type\": \"triangle\", \"x\": -25, \"y\": -25}", jsonVisitor.getRepresentation());
    }
}
