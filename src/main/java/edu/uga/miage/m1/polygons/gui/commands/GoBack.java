package edu.uga.miage.m1.polygons.gui.commands;

import edu.uga.miage.m1.polygons.gui.JDrawingFrame;

public class GoBack implements Command {

    private final JDrawingFrame frame;

    public GoBack(JDrawingFrame frame) {
        this.frame = frame;
    }

    @Override
    public void execute() {
        frame.goBack();
    }
}