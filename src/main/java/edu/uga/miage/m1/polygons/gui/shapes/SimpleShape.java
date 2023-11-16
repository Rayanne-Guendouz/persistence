package edu.uga.miage.m1.polygons.gui.shapes;

import java.awt.Graphics2D;
import java.awt.Point;

/**
 * This interface defines the <tt>SimpleShape</tt> extension. This extension
 * is used to draw shapes.
 *
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */
public interface SimpleShape {

    /**
     * Method to draw the shape of the extension.
     * @param g2 The graphics object used for painting.
     */
    void draw(Graphics2D g2);

    int getX();

    int getY();

    void setX(int x);
     
    void setY(int y);

    public abstract boolean contains(Point point);

    public abstract SimpleShape clone();

    
}
