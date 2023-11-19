package edu.uga.miage.m1.polygons.gui.persistence;

import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.ComplexShape;
import edu.uga.miage.m1.polygons.gui.shapes.Cube;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

/**
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */
public class JSonVisitor implements Visitor {

    private StringBuilder representation;
    private static final String MIDJSON = ", \"y\": ";
    private static final String ENDJSON = "}";
    private static final String START_SHAPE = "{\"type\": \"%s\", \"x\": %d" + MIDJSON + "%d" + ENDJSON;
    private static final String START_COMPLEX = "{\"type\": \"complex\", \"shapes\": [";
    private static final String END_COMPLEX = "]}";

    public JSonVisitor() {
<<<<<<< HEAD
        // Nothing to do
        // Don't remove this method even if it is empty
        // Don't use for this moment
=======
        representation = new StringBuilder();
>>>>>>> 9e42618 (composable 1er version)
    }

    @Override
    public void visit(Circle circle) {
        appendRepresentation(String.format(START_SHAPE, "circle", circle.getX(), circle.getY()));
    }

    @Override
    public void visit(Square square) {
        appendRepresentation(String.format(START_SHAPE, "square", square.getX(), square.getY()));
    }

    @Override
    public void visit(Triangle triangle) {
        appendRepresentation(String.format(START_SHAPE, "triangle", triangle.getX(), triangle.getY()));
    }

    @Override
    public void visit(Cube cube) {
        appendRepresentation(String.format(START_SHAPE, "cube", cube.getX(), cube.getY()));
    }

    @Override
    public void visit(ComplexShape complexShape) {
        appendRepresentation(START_COMPLEX);
        SimpleShape[] shapes = complexShape.getShapes();
        for (int i = 0; i < shapes.length; i++) {
            shapes[i].accept(this);
            if (i < shapes.length - 1) {
                appendRepresentation(", ");
            }
        }
        appendRepresentation(END_COMPLEX);
    }

    private void appendRepresentation(String str) {
        representation.append(str);
    }

    /**
     * @return the representation in JSON example for a Circle
     *
     * <pre>
     * {@code
     *  {
     *     "shape": {
     *     	  "type": "circle",
     *        "x": -25,
     *        "y": -25
     *     }
     *  }
     * }
     * </pre>
     */
    public String getRepresentation() {
        return representation.toString();
    }
}
