package edu.uga.miage.m1.polygons.gui.shapes;

import java.awt.Graphics2D;
import edu.uga.miage.m1.polygons.gui.persistence.Visitable;
import edu.uga.miage.m1.polygons.gui.persistence.Visitor;
import edu.uga.singleshape.CubePanel;

public class Cube implements SimpleShape, Visitable {

    int m_x;

    int m_y;

    public Cube(int x, int y) {
        m_x = x - 25;
        m_y = y - 25;
    }

    /**
     * Implements the <tt>SimpleShape.draw()</tt> method for painting
     * the shape.
     * @param g2 The graphics object used for painting.
     */
    public void draw(Graphics2D g2) {
        Graphics2D g2d = (Graphics2D) g2;
        CubePanel c = new CubePanel(100, 100, 100);
        c.paintComponent(g2d);
    }

    @Override
    public void accept(Visitor visitor) {
        visitor.visit(this);
    }

    @Override
    public int getX() {
        return m_x;
    }

    @Override
    public int getY() {
        return m_y;
    }

    @Override
    public void setX(int x) {
        m_x = x;
    }

    @Override
    public void setY(int y) {
        m_y = y;
    }

    @Override
    public boolean contains(java.awt.Point point) {
        boolean res = false ;
        if (point.getX() >= m_x && point.getX() <= m_x + 50 && point.getY() >= m_y && point.getY() <= m_y + 50) {
            res = true ;
        }
        return res;
    }

    @Override
    public SimpleShape clone() {
        return new Cube(m_x, m_y);
    }
    
}
