/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package edu.uga.miage.m1.polygons.gui.shapes;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.RenderingHints;
import java.awt.geom.Ellipse2D;
import edu.uga.miage.m1.polygons.gui.persistence.Visitable;
import edu.uga.miage.m1.polygons.gui.persistence.Visitor;

public class Circle implements SimpleShape, Visitable, Cloneable {

    boolean isSelected;

    int x;

    int y;

    public Circle(int x, int y) {
<<<<<<< HEAD
        setXY(x-25, y-25);
    }

    public Circle(SimpleShape shape) {
        setXY(shape.getX(), shape.getY());
=======
        m_x = x - 25;
        m_y = y - 25;
        isSelected = false;
>>>>>>> 9e42618 (composable 1er version)
    }

    /**
     * Implements the <tt>SimpleShape.draw()</tt> method for painting
     * the shape.
     * @param g2 The graphics object used for painting.
     */
    @Override
    public void draw(Graphics2D g2) {
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        GradientPaint gradient = new GradientPaint(x, y, Color.RED, (float)x + 50, y, Color.WHITE);
        g2.setPaint(gradient);
        g2.fill(new Ellipse2D.Double(x, y, 50, 50));
        BasicStroke wideStroke = new BasicStroke(2.0f);
        if(isSelected){
            g2.setColor(Color.magenta);
        } else {
            g2.setColor(Color.black);
        }
        g2.setStroke(wideStroke);
        g2.draw(new Ellipse2D.Double(x, y, 50, 50));
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
    public int getX() {
        return x;
    }

    @Override
    public int getY() {
        return y;
    }

    @Override
    public void setX(int x) {
        this.x = x;
    }

    @Override
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
        return "Circle";
    }

    @Override
<<<<<<< HEAD
    public void setXY(int x, int y) {
        setX(x);
        setY(y);
    }
    
=======
    public void move(int x, int y) {
        m_x = x - 25;
        m_y = y - 25;
    }

>>>>>>> 9e42618 (composable 1er version)
}
