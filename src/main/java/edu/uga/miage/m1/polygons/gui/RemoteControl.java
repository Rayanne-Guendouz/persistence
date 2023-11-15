package edu.uga.miage.m1.polygons.gui;

import java.util.ArrayList;
import java.util.List;

import edu.uga.miage.m1.polygons.gui.commands.Command;

public class RemoteControl {
  protected List<Command> commands;

    public RemoteControl() {
        commands = new ArrayList<Command>();
    }

    public boolean addCommand(Command command) {
        return commands.add(command);
    }

    public void play()  {
        for (Command command : commands) {
            command.execute();
        }
    }

    public void reset() {
        commands.clear();
    }  
}
