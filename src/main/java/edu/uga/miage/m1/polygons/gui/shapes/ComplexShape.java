package edu.uga.miage.m1.polygons.gui.shapes;

import java.awt.Graphics2D;
import java.awt.Point;

import edu.uga.miage.m1.polygons.gui.persistence.Visitable;
import edu.uga.miage.m1.polygons.gui.persistence.Visitor;

import java.util.Arrays;

public class ComplexShape implements SimpleShape, Visitable, Cloneable{

    boolean isSelected = false; 

    private SimpleShape[] complexShape;
    
    public ComplexShape(SimpleShape[] complexShapes) {
        this.complexShape = complexShapes;
    }

    @Override
    public void draw(Graphics2D g2) {
        for (SimpleShape shape : complexShape) {
            shape.draw(g2);
        }
    }

    @Override
    public void move(int x, int y) {
        for (SimpleShape shape : complexShape) {
            shape.move(x, y);
        }
    }

    public void add(SimpleShape shape) {
        SimpleShape[] newComplexShape = Arrays.copyOf(complexShape, complexShape.length + 1);
        newComplexShape[complexShape.length] = shape;
        complexShape = newComplexShape;
    }

    public void remove(SimpleShape shape) {
        SimpleShape[] newComplexShape = new SimpleShape[complexShape.length - 1];
        for (int i = 0; i < complexShape.length; i++) {
            if (complexShape[i] != shape) {
                newComplexShape[i] = complexShape[i];
            }
        }
        complexShape = newComplexShape;
    }

    public SimpleShape[] getShapes() {
        return complexShape;
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

    @Override
    public boolean contains(Point point) {
        for (SimpleShape shape : complexShape) {
            if (shape.contains(point)) {
                return true;
            }
        }
        return false;
    }

    @Override
    public SimpleShape clone() {
        SimpleShape[] clonedShapes = new SimpleShape[complexShape.length];
        for (int i = 0; i < complexShape.length; i++) {
            clonedShapes[i] = complexShape[i].clone();
        }
        return new ComplexShape(clonedShapes);
    }

    @Override
    public int getX() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getX'");
    }

    @Override
    public int getY() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getY'");
    }

    @Override
    public void setX(int x) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setX'");
    }

    @Override
    public void setY(int y) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'setY'");
    }
    
}
