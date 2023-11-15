package persistence;
import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class XMLVisitorTest {

    @Test
    public void testVisitCircle() {
        Circle circle = new Circle(10, 20);
        XMLVisitor xmlVisitor = new XMLVisitor();
        xmlVisitor.visit(circle);
        assertEquals("<shape><type>circle</type><x>-15</x><y>-5</y></shape>", xmlVisitor.getRepresentation());
    }

    @Test
    public void testVisitSquare() {
        Square square = new Square(5, 5);
        XMLVisitor xmlVisitor = new XMLVisitor();
        xmlVisitor.visit(square);
        assertEquals("<shape><type>square</type><x>-20</x><y>-20</y></shape>", xmlVisitor.getRepresentation());
    }

    @Test
    public void testVisitTriangle() {
        Triangle triangle = new Triangle(0, 0);
        XMLVisitor xmlVisitor = new XMLVisitor();
        xmlVisitor.visit(triangle);
        assertEquals("<shape><type>triangle</type><x>-25</x><y>-25</y></shape>", xmlVisitor.getRepresentation());
    }
}
