package edu.uga.miage.m1.polygons.gui.persistence;

import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.Cube;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

/**
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */
public class XMLVisitor implements Visitor {

    private String representation = null;
    private static final String MIDXML = "</x><y>";
    private static final String ENDXML = "</y></shape>";
    

    public XMLVisitor() {
        // Nothing to do
        // Don't remove this method even if it is empty
        // Don't use for this moment
    }

    @Override
    public void visit(Circle circle) {
    	representation = "<shape><type>circle</type><x>" + circle.getX() + MIDXML + circle.getY() + ENDXML;
    }

    @Override
    public void visit(Square square) {
    	representation = "<shape><type>square</type><x>" + square.getX() + MIDXML + square.getY() + ENDXML;
    }

    @Override
    public void visit(Triangle triangle) {
    	representation = "<shape><type>triangle</type><x>" + triangle.getX() + MIDXML + triangle.getY() + ENDXML;
    }

     @Override
    public void visit(Cube cube) {
    	representation = "<shape><type>cube</type><x>" + cube.getX() + MIDXML + cube.getY() + ENDXML;
    }


    /**
     * @return the representation in JSon example for a Triangle:
     *
     *         <pre>
     * {@code
     *  <shape>
     *    <type>triangle</type>
     *    <x>-25</x>
     *    <y>-25</y>
     *  </shape>
     * }
     * </pre>
     */
    public String getRepresentation() {
        return representation;
    }
}
