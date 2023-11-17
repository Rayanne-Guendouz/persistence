package edu.uga.miage.m1.polygons.gui;
import java.io.Serializable;
import java.util.ArrayList;

import edu.uga.miage.m1.polygons.gui.persistence.Visitable;

public class StateOfPaint implements Serializable {
    private ArrayList<Visitable> shapesList;
    public ArrayList<Visitable> getShapesList() {
        return shapesList;
    }
    public void setShapesList(ArrayList<Visitable> shapesList) {
        this.shapesList = shapesList;
    }
}