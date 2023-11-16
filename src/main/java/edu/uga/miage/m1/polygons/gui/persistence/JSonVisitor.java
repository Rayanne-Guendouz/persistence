package edu.uga.miage.m1.polygons.gui.persistence;

import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

/**
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */
public class JSonVisitor implements Visitor {

    private String representation = null;
    private static final String MIDJSON = ", \"y\": ";
    private static final String ENDJSON = "}";

    public JSonVisitor() {
    }

    @Override
    public void visit(Circle circle) {
    	representation = "{\"type\": \"circle\", \"x\": " + circle.getX() + MIDJSON + circle.getY() + ENDJSON;
    }

    @Override
    public void visit(Square square) {
    	representation = "{\"type\": \"square\", \"x\": " + square.getX() + MIDJSON + square.getY() + ENDJSON;
    }

    @Override
    public void visit(Triangle triangle) {
    	representation = "{\"type\": \"triangle\", \"x\": " + triangle.getX() + MIDJSON + triangle.getY() + ENDJSON;
    }

    /**
     * @return the representation in JSon example for a Circle
     *
     *         <pre>
     * {@code
     *  {
     *     "shape": {
     *     	  "type": "circle",
     *        "x": -25,
     *        "y": -25
     *     }
     *  }
     * }
     *         </pre>
     */
    public String getRepresentation() {
        return representation;
    }
}