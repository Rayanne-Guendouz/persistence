package edu.uga.miage.m1.polygons.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JToolBar;

import java.awt.*;

import edu.uga.miage.m1.polygons.gui.commands.GoBack;
import edu.uga.miage.m1.polygons.gui.persistence.JSonVisitor;
import edu.uga.miage.m1.polygons.gui.persistence.Visitable;
import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.Cube;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;
import edu.uga.singleshape.CubePanel;

import java.util.List;
import java.util.ArrayList;

/**
 * This class represents the main application class, which is a JFrame subclass
 * that manages a toolbar of shapes and a drawing canvas.
 *
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */
public class JDrawingFrame extends JFrame implements MouseListener, MouseMotionListener,KeyListener {

    public enum Shapes {

        SQUARE, TRIANGLE, CIRCLE,CUBE
    }
    private enum Actions {
        PLACESHAPE, MOVESHAPE
    }
    private static final long serialVersionUID = 1L;

    private JToolBar m_toolbar;

    private Shapes m_selected;

    public JPanel m_panel;

    private JLabel m_label;

    private ActionListener m_reusableActionListener = new ShapeActionListener();


    /**
     * Tracks buttons to manage the background.
     */
    public Map<Shapes, JButton> m_buttons = new HashMap<>();

    /**
     * Default constructor that populates the main window.
     * @param frameName
     */
    // Liste des objets visitable
    public ArrayList<Visitable> shapesList = new ArrayList<Visitable>();

    // Controller Command
    private RemoteControl remoteControl = new RemoteControl();

    // Liste des objets SimpleSHape 
    private ArrayList<SimpleShape> simpleShapes = new ArrayList<SimpleShape>();

    private long lastCtrlZPressTime = 0;
    private static final long CTRL_Z_DELAY = 500; // Délai d'attente en millisecondes (0.5 seconde ici)

    // Liste des dernière actions de l'utilisateur
    private ArrayList<Actions> actionsList = new ArrayList<Actions>();
    // Pour le déplacement de la forme
    private ArrayList<Point> position = new ArrayList<Point>();
    private SimpleShape selectedShape;
    private boolean isShapeSelected = false;
    private int xShape;
    private int yShape;

    public JDrawingFrame(String frameName) {
        super(frameName);
        // Instantiates components
        m_toolbar = new JToolBar("Toolbar");
        m_panel = new JPanel();
        m_panel.setBackground(Color.WHITE);
        m_panel.setLayout(null);
        m_panel.setMinimumSize(new Dimension(400, 400));
        m_panel.addMouseListener(this);
        m_panel.addMouseMotionListener(this);
        m_label = new JLabel(" ", JLabel.LEFT);
        // Initialisation de shapesList
        shapesList = new ArrayList<Visitable>();
        // Fills the panel
        setLayout(new BorderLayout());
        add(m_toolbar, BorderLayout.NORTH);
        add(m_panel, BorderLayout.CENTER);
        add(m_label, BorderLayout.SOUTH);
        // Add shapes in the menu
        addShape(Shapes.SQUARE, new ImageIcon(getClass().getResource("images/square.png")));
        addShape(Shapes.TRIANGLE, new ImageIcon(getClass().getResource("images/triangle.png")));
        addShape(Shapes.CIRCLE, new ImageIcon(getClass().getResource("images/circle.png")));
        addShape(Shapes.CUBE, new ImageIcon(getClass().getResource("images/underc.png")));
        // Ajout du bouton Sauvegarder
        JButton sauvegarderButton = new JButton("Sauvegarder");
        m_toolbar.add(sauvegarderButton);
        // ActionListener pour le bouton Sauvegarder
        sauvegarderButton.addActionListener(new SauvegardeActionListener());
        setPreferredSize(new Dimension(400, 400));
        // Ajout du KeyListener pour dÃ©tecter "Ctrl+Z"
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        // Ajoutez un gestionnaire de focus pour gérer le focus entre les composants
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventController());
    }

    /**
     * Injects an available <tt>SimpleShape</tt> into the drawing frame.
     * @param name The name of the injected <tt>SimpleShape</tt>.
     * @param icon The icon associated with the injected <tt>SimpleShape</tt>.
     */
    public void addShape(Shapes shape, ImageIcon icon) {
        JButton button = new JButton(icon);
        button.setBorderPainted(false);
        m_buttons.put(shape, button);
        button.setActionCommand(shape.toString());
        button.addActionListener(m_reusableActionListener);
        if (m_selected == null) {
            button.doClick();
        }
        m_toolbar.add(button);
        m_toolbar.validate();
        repaint();
    }

    public  ArrayList<Visitable> getShapesList() {
        return shapesList;
    }
    
    public ArrayList<Actions> getActionsList() {
        return actionsList;
    }

    public ArrayList<Point> getPosition() {
        return position;
    }

    public SimpleShape getSelectedShape() {
        return selectedShape;
    }

    public boolean isShapeSelected() {
        return isShapeSelected;
    }

    public ArrayList<SimpleShape> getSimpleShapes() {
        return simpleShapes;
    }

    public Shapes getSelected() {
        return m_selected;
    }

    public void selectShape(SimpleShape shape){
        isShapeSelected = true;
        selectedShape = shape;
        //Destruction de la forme
        int xShape = shape.getX();
        int yShape = shape.getY();
        position.add(new Point(xShape, yShape));
        simpleShapes.remove(shape);
        Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, m_panel.getWidth(), m_panel.getHeight());
        // Redessine toutes les formes restantes
        for (SimpleShape shape2 : simpleShapes) {
            shape2.draw(g2);
        }
    }

    public void moveShapeMouse(MouseEvent evt) {
        // Mettez à jour les coordonnées de la forme en fonction de la position de la souris
        int newX = evt.getX() - xShape;
        int newY = evt.getY() - yShape;
        // Dessinez la forme à la nouvelle position
        Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, m_panel.getWidth(), m_panel.getHeight());
        // Dessinez toutes les formes restantes
        for (SimpleShape shape : simpleShapes) {
            shape.draw(g2);
        }
        // Dessinez la forme actuellement déplacée à la nouvelle position
        selectedShape.setX(newX);
        selectedShape.setY(newY);
        selectedShape.draw(g2);
    }

    public void releaseShape(MouseEvent evt) {
        // Mettez à jour les coordonnées de la forme en fonction de la position de la souris
        int newX = evt.getX() - xShape;
        int newY = evt.getY() - yShape;
        // Dessinez la forme à la nouvelle position
        Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, m_panel.getWidth(), m_panel.getHeight());
        // Dessinez toutes les formes restantes
        for (SimpleShape shape : simpleShapes) {
            shape.draw(g2);
        }
        // Dessinez la forme actuellement déplacée à la nouvelle position
        selectedShape.setX(newX);
        selectedShape.setY(newY);
        selectedShape.draw(g2);
        // Ajout de la forme dans la liste
        simpleShapes.add(selectedShape);
        isShapeSelected = false;
    }

    /**
     * Implements method for the <tt>MouseListener</tt> interface to
     * draw the selected shape into the drawing canvas.
     * @param evt The associated mouse event.
     */
    public void mouseClicked(MouseEvent evt) {
        if (m_panel.contains(evt.getX(), evt.getY()) && !isShapeSelected) {
            Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
            actionsList.add(Actions.PLACESHAPE);
            switch(m_selected) {
                case CIRCLE:
                    Circle circle = new Circle(evt.getX(), evt.getY());
                    circle.draw(g2);
                    // Ajout de l'objet dans la liste
                    shapesList.add(circle);
                    simpleShapes.add(circle);
                    break;
                case TRIANGLE:
                    Triangle triangle = new Triangle(evt.getX(), evt.getY());
                    triangle.draw(g2);
                    // Ajout de l'objet dans la liste
                    shapesList.add(triangle);
                    simpleShapes.add(triangle);
                    break;
                case SQUARE:
                    Square square = new Square(evt.getX(), evt.getY());
                    square.draw(g2);
                    // Ajout de l'objet dans la liste
                    shapesList.add(square);
                    simpleShapes.add(square);
                    break;
                case CUBE:
                    Cube cube = new Cube(evt.getX(),evt.getY());
                    shapesList.add(cube);
                    simpleShapes.add(cube);
                    Graphics2D g2d = (Graphics2D) g2;
                    CubePanel c = new CubePanel(100, 100, 100);
                    c.paintComponent(g2d);
                    break;
                default:
                    System.out.println("No shape named " + m_selected);
            }
        }
        
    }

    /**
     * Implements an empty method for the <tt>MouseListener</tt> interface.
     * @param evt The associated mouse event.
     */
    public void mouseEntered(MouseEvent evt) {
    }

    /**
     * Implements an empty method for the <tt>MouseListener</tt> interface.
     * @param evt The associated mouse event.
     */
    public void mouseExited(MouseEvent evt) {
        m_label.setText(" ");
        m_label.repaint();
    }

    /**
     * Implements method for the <tt>MouseListener</tt> interface to initiate
     * shape dragging.
     * @param evt The associated mouse event.
     */
    public void mousePressed(MouseEvent evt) {
        isShapeSelected = false;
        for (SimpleShape shape : simpleShapes) {
            if (shape.contains(evt.getPoint())) {
                actionsList.add(Actions.MOVESHAPE);
                // Traitement du clic sur la forme
                System.out.println("Clicked on shape: " + shape);
                xShape = evt.getX() - shape.getX();
                yShape = evt.getY() - shape.getY();
                selectShape(shape);
            }
        }
    }

    /**
     * Implements method for the <tt>MouseListener</tt> interface to complete
     * shape dragging.
     * @param evt The associated mouse event.
     */
    public void mouseReleased(MouseEvent evt) {
        if (isShapeSelected && selectedShape != null) {
            releaseShape(evt);
        }
    }

    /**
     * Implements method for the <tt>MouseMotionListener</tt> interface to
     * move a dragged shape.
     * @param evt The associated mouse event.
     */
    public void mouseDragged(MouseEvent evt) {
        if (isShapeSelected && selectedShape != null) {
            moveShapeMouse(evt);
        }
    }


    /**
     * Implements an empty method for the <tt>MouseMotionListener</tt>
     * interface.
     * @param evt The associated mouse event.
     */
    public void mouseMoved(MouseEvent evt) {
        modifyLabel(evt);
    }

    private void modifyLabel(MouseEvent evt) {
        m_label.setText("(" + evt.getX() + "," + evt.getY() + ")");
    }

    /**
     * Simple action listener for shape tool bar buttons that sets
     * the drawing frame's currently selected shape when receiving
     * an action event.
     */
    private class ShapeActionListener implements ActionListener {

        public void actionPerformed(ActionEvent evt) {
            // ItÃ¨re sur tous les boutons
            Iterator<Shapes> keys = m_buttons.keySet().iterator();
            while (keys.hasNext()) {
                Shapes shape = keys.next();
                JButton btn = m_buttons.get(shape);
                if (evt.getActionCommand().equals(shape.toString())) {
                    btn.setBorderPainted(true);
                    m_selected = shape;
                } else {
                    btn.setBorderPainted(false);
                }
                btn.repaint();
            }
        }
    }
    
    // La fonction de sauvegarde
    public class SauvegardeActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
                String jsonData = "{\n  \"shapes\": [\n";
                String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + //
                        "<root>\r\n" + //
                        "<shapes>\n";
                // ItÃ¨re sur tous les objets
                for (Visitable shape : shapesList) {
                    // DonnÃ©e de sauvegarde XML
                    XMLVisitor xmlVisitor = new XMLVisitor();
                    // DonnÃ©e de sauvegarde JSON
                    JSonVisitor jsonVisitor = new JSonVisitor();
                    // Sauvegarde en JSON
                    shape.accept(jsonVisitor);
                    jsonData += "   " + jsonVisitor.getRepresentation();
                    if (shapesList.indexOf(shape) < shapesList.size() - 1) {
                        jsonData += ",";
                    }
                    jsonData += "\n";
                    // Sauvegarde en XML
                    shape.accept(xmlVisitor);
                    xmlData += "    " + xmlVisitor.getRepresentation() + "\n";
                }
                // Imprime le JSON
                jsonData += "  ]\n}";
                // Imprime le XML
                xmlData += "</shapes>\r\n</root>";

                String cheminFichierJSON = "src/main/java/edu/uga/miage/m1/polygons/gui/save/Save.json"; 
                String cheminFichierXML = "src/main/java/edu/uga/miage/m1/polygons/gui/save/Save.xml"; 

                try (BufferedWriter jsonWriter = new BufferedWriter(new FileWriter(new File(cheminFichierJSON)));
                    BufferedWriter xmlWriter = new BufferedWriter(new FileWriter(new File(cheminFichierXML)))) {
                    jsonWriter.write(jsonData);
                    xmlWriter.write(xmlData);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
    }
    
    // La classe qui gère les entree clavier
    private class KeyEventController implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastCtrlZPressTime >= CTRL_Z_DELAY && !shapesList.isEmpty() && !actionsList.isEmpty() && actionsList.get(actionsList.size()-1) == Actions.PLACESHAPE) {
                    remoteControl.addCommand(new GoBack(JDrawingFrame.this));
                    lastCtrlZPressTime = currentTime;
                }
                if(currentTime - lastCtrlZPressTime >= CTRL_Z_DELAY && !shapesList.isEmpty() && !actionsList.isEmpty() && actionsList.get(actionsList.size()-1) == Actions.MOVESHAPE) {
                    // Ecrire la fonction qui remet à sa place la forme
                    remoteControl.addCommand(new GoBack(JDrawingFrame.this));
                    lastCtrlZPressTime = currentTime;
                }
            }
            remoteControl.play();
            remoteControl.reset();
            return false;
        }
    }

    // La fonction pour revenir en arrière
    public void goBack() {
        if (!shapesList.isEmpty()) {
            shapesList.remove(shapesList.size() - 1); // Supprime la derniÃ¨re forme
            simpleShapes.remove(simpleShapes.size()-1); // SUpprime la dernière forme
            // Efface le panneau
            Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, m_panel.getWidth(), m_panel.getHeight());
            // Redessine toutes les formes restantes
            for (SimpleShape shape : simpleShapes) {
                shape.draw(g2);
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }
}
