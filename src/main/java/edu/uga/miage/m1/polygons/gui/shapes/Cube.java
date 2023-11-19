package edu.uga.miage.m1.polygons.gui.shapes;

import java.awt.Graphics2D;
import java.awt.Point;

import edu.uga.miage.m1.polygons.gui.persistence.Visitable;
import edu.uga.miage.m1.polygons.gui.persistence.Visitor;
import edu.uga.singleshape.CubePanel;

public class Cube implements SimpleShape, Visitable {

<<<<<<< HEAD
    int x;
=======
    boolean isSelected = false;

    int m_x;
>>>>>>> 9e42618 (composable 1er version)

    int y;

    public Cube(int x, int y) {
        setXY(x-25, y-25);
    }
    public Cube(SimpleShape shape) {
        setXY(shape.getX(), shape.getY());
    }

    /**
     * Implements the <tt>SimpleShape.draw()</tt> method for painting
     * the shape.
     * @param g2 The graphics object used for painting.
     */
    @Override
    public void draw(Graphics2D g2) {
        Graphics2D g2d = (Graphics2D) g2;
        CubePanel c = new CubePanel(100, 100, 100);
        c.paintComponent(g2d);
    }

    @Override
    public boolean isSelected() {
        return isSelected;
    }

    @Override
    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }


    public int getX() {
        return x;
    }


    public int getY() {
        return y;
    }


    public void setX(int x) {
        this.x = x;
    }


    public void setY(int y) {
        this.y = y;
    }

    @Override
    public boolean contains(Point point) {
        boolean res = false ;
        if (point.getX() >= x && point.getX() <= x + 50 && point.getY() >= y && point.getY() <= y + 50) {
            res = true ;
        }
        return res;
    }

    @Override
    public String typeOfShape() {
        return "Cube";
    }
    
    @Override
    public void setXY(int x, int y) {
        setX(x);
        setY(y);
    }

    @Override
    public void move(int x, int y) {
        m_x = x - 25;
        m_y = y - 25;
    }
    
}
