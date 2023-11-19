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
public class XMLVisitor implements Visitor {

    private StringBuilder representation;
    private static final String START_SHAPE = "<shape><type>%s</type><x>%d</x><y>%d</y></shape>";
    private static final String START_COMPLEX = "<shape><type>complex</type><x>%d</x><y>%d</y><shapes>";
    private static final String END_COMPLEX = "</shapes></shape>";

    public XMLVisitor() {
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
        appendRepresentation(String.format(START_COMPLEX));
        SimpleShape[] shapes = complexShape.getShapes();
        for (SimpleShape shape : shapes) {
            shape.accept(this);
        }
        appendRepresentation(END_COMPLEX);
    }

    private void appendRepresentation(String str) {
        representation.append(str);
    }

    /**
     * @return the representation in XML for a Triangle:
     *
     * <pre>
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
        return representation.toString();
    }
}