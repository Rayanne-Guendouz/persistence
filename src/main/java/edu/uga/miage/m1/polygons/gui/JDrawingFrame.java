package edu.uga.miage.m1.polygons.gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
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
import javax.swing.plaf.nimbus.State;

import java.awt.*;

import edu.uga.miage.m1.polygons.gui.commands.GoBack;
import edu.uga.miage.m1.polygons.gui.persistence.JSonVisitor;
import edu.uga.miage.m1.polygons.gui.persistence.Visitable;
import edu.uga.miage.m1.polygons.gui.persistence.XMLVisitor;
import edu.uga.miage.m1.polygons.gui.shapes.Circle;
import edu.uga.miage.m1.polygons.gui.shapes.ComplexShape;
import edu.uga.miage.m1.polygons.gui.shapes.Cube;
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;
import edu.uga.singleshape.CubePanel;


/**
 * This class represents the main application class, which is a JFrame subclass
 * that manages a toolbar of shapes and a drawing canvas.
 *
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */
public class JDrawingFrame extends JFrame implements MouseListener, MouseMotionListener,KeyListener {

    public enum Shapes {

        SQUARE, TRIANGLE, CIRCLE, CUBE
    }
    // Pour la sauvegarde
    private class stateOfPaint {
        private ArrayList<Visitable> shapesList;
        public ArrayList<Visitable> getShapesList() {
            return shapesList;
        }
        public void setShapesList(ArrayList<Visitable> shapesList) {
            this.shapesList = shapesList;
        }
    }

    private ArrayList<stateOfPaint> stateOfPaintList = new ArrayList<stateOfPaint>();

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

    // Controller Command
    private RemoteControl remoteControl = new RemoteControl();


    private long lastCtrlZPressTime = 0;
    private static final long CTRL_Z_DELAY = 500; // Délai d'attente en millisecondes (0.5 seconde ici)

    // Pour le déplacement de la forme
    private SimpleShape selectedShape;
    private boolean isShapeSelected = false;
    private int xShape;
    private int yShape;
    private boolean isInGroupSelection = false;

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
        ArrayList<Visitable> shapesList = new ArrayList<Visitable>();
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
        // Ajout du bouton de regroupement
        JButton regroupeButton = new JButton("Regrouper");
        m_toolbar.add(regroupeButton);
        // ActionListener pour le bouton Regrouper
        regroupeButton.addActionListener(new RegroupeActionListener());
        setPreferredSize(new Dimension(400, 400));
        // Ajout du KeyListener pour dÃ©tecter "Ctrl+Z"
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(true);
        // Ajoutez un gestionnaire de focus pour gérer le focus entre les composants
        KeyboardFocusManager.getCurrentKeyboardFocusManager().addKeyEventDispatcher(new KeyEventController());
        addStateOfPaint(shapesList);
    }
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Getters et Setters
    public SimpleShape getSelectedShape() {
        return selectedShape;
    }

    public boolean isShapeSelected() {
        return isShapeSelected;
    }

    public Shapes getSelected() {
        return m_selected;
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //Pour le controle des Etats et de la liste des formes
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

    private void addStateOfPaint(ArrayList<Visitable> shapesList) {
        stateOfPaint stateOfPaint = new stateOfPaint();
        stateOfPaint.setShapesList(shapesList);
        stateOfPaintList.add(stateOfPaint);
        printStates();
    }

    private ArrayList<Visitable> buildShapeList(int index){
        ArrayList<Visitable> shapesList = new ArrayList<Visitable>();
        for (Visitable shape : stateOfPaintList.get(index).getShapesList()) {
            shapesList.add(shape);
        }
        return shapesList;
    }

    private void paintSimpleShapes(Graphics2D g2) {
        ArrayList<Visitable> shapesList = buildShapeList(stateOfPaintList.size()-1);
        Visitable visitable = (Visitable) selectedShape;
        shapesList.remove(visitable);
        for (Visitable shape : shapesList) {
            SimpleShape simpleShape = (SimpleShape) shape;
            simpleShape.draw(g2);
        }
    }

    

    
    public void printStates(){
        System.out.println("--------------------------------------------------");
        // Print du dernier état de la liste et de la liste des formes
        System.out.println("Nombre de formes Etat courant : " + stateOfPaintList.get(stateOfPaintList.size()-1).getShapesList().size());
        System.out.println("Nombre d'états : " + stateOfPaintList.size());
        for(stateOfPaint stateOfPaint : stateOfPaintList) {
            System.out.println("Etat n°" + stateOfPaintList.indexOf(stateOfPaint) + " :");
            System.out.println("Nombre de formes : " + stateOfPaint.getShapesList().size());
            for(Visitable shape : stateOfPaint.getShapesList()) {
                SimpleShape simpleShape = (SimpleShape) shape;
                System.out.println(simpleShape.getClass().getName() + ":" + simpleShape.getX() + "," + simpleShape.getY());

            }
            System.out.println("--------------------------------------------------");

        }
        System.out.println("--------------------------------------------------");
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /// Pour le dessin des formes
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///Pour Ajouter une forme
    /**
     * Implements method for the <tt>MouseListener</tt> interface to
     * draw the selected shape into the drawing canvas when we add a shape.
     * @param evt The associated mouse event.
     */
    public void drawShape(MouseEvent evt){
        Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
        ArrayList<Visitable> shapesList = buildShapeList(stateOfPaintList.size()-1);
        SimpleShape simpleShape = null;
        switch(m_selected) {
            case CIRCLE:
                simpleShape = new Circle(evt.getX(), evt.getY());
                break;
            case TRIANGLE:
                simpleShape = new Triangle(evt.getX(), evt.getY());
                break;
            case SQUARE:
                simpleShape = new Square(evt.getX(), evt.getY());
                break;
            case CUBE:
                simpleShape = new Cube(evt.getX(),evt.getY());
                break;
            default:
                System.out.println("No shape named " + m_selected);
        }
        simpleShape.draw(g2);
        // Ajout de l'objet dans la liste
        Visitable visitable = (Visitable) simpleShape;
        shapesList.add(visitable);
        // Ajout de l'etat dans la liste
        addStateOfPaint(shapesList);
        
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Pour le deplacement de la forme
    /**
     * Implements method for the <tt>MouseListener</tt> interface to
     * draw the selected shape into the drawing canvas when we select a shape.
     * @param shape The associated shape.
     */
    public void selectShape(SimpleShape shape) {
        isShapeSelected = true;
        selectedShape = shape.clone();
        ArrayList<Visitable> shapesList = buildShapeList(stateOfPaintList.size() - 1);
        shapesList.remove((Visitable) shape);
        // Ajout de la forme sélectionnée à la liste
        Visitable visitable = (Visitable) selectedShape;
        shapesList.add(visitable);
        // Ajout de l'état dans la liste
        addStateOfPaint(shapesList);
    
        // Déplacement de la forme
        Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, m_panel.getWidth(), m_panel.getHeight());
    
        // Redessine toutes les formes restantes
        paintSimpleShapes(g2);
        printStates();
    }

    /**
     * Implements method for the <tt>MouseListener</tt> interface to
     * draw the selected shape into the drawing canvas when we move a shape.
     * @param evt The associated mouse event.
     */

     public void moveShapeMouse(MouseEvent evt) {
        int newX = evt.getX() - xShape;
        int newY = evt.getY() - yShape;
    
        BufferedImage buffer = new BufferedImage(m_panel.getWidth(), m_panel.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = buffer.createGraphics();
        g2.setColor(Color.WHITE);
        g2.fillRect(0, 0, m_panel.getWidth(), m_panel.getHeight());
    
        ArrayList<Visitable> shapesList = buildShapeList(stateOfPaintList.size() - 1);
        Visitable visitable = (Visitable) selectedShape;
        shapesList.remove(visitable);
    
        if (selectedShape instanceof ComplexShape) {
            moveComplexShape(newX, newY, (ComplexShape) selectedShape, g2, shapesList);
        } else {
            moveSimpleShape(newX, newY, (SimpleShape) selectedShape, g2, shapesList);
        }
    
        m_panel.getGraphics().drawImage(buffer, 0, 0, m_panel);
        g2.dispose();
    }

    private void moveSimpleShape(int newX, int newY, SimpleShape shape, Graphics2D g2, ArrayList<Visitable> shapesList) {
        shape.setX(newX);
        shape.setY(newY);
        shape.draw(g2);
        paintSimpleShapes(g2);
        shapesList.add((Visitable) shape);
    }

    private void moveComplexShape(int newX, int newY, ComplexShape complexShape, Graphics2D g2, ArrayList<Visitable> shapesList) {
        SimpleShape[] shapes = complexShape.getShapes();
        for (SimpleShape shape : shapes) {
            shape.move(newX, newY);
            shape.draw(g2);
            shapesList.add((Visitable) shape);
        }
    }

    /**
     * Implements method for the <tt>MouseListener</tt> interface to
     * draw the selected shape into the drawing canvas when we release a shape.
     * @param evt The associated mouse event.
     */
    public void releaseShape(MouseEvent evt) {
        int newX = evt.getX() - xShape;
        int newY = evt.getY() - yShape;
    
        if (isShapeSelected && selectedShape != null) {
            if (selectedShape instanceof ComplexShape) {
                releaseComplexShape(newX, newY, (ComplexShape) selectedShape);
            } else {
                releaseSimpleShape(newX, newY, (SimpleShape) selectedShape);
            }
        }
    }

    private void releaseSimpleShape(int newX, int newY, SimpleShape shape) {
        shape.setX(newX);
        shape.setY(newY);
    }

    private void releaseComplexShape(int newX, int newY, ComplexShape complexShape) {
        SimpleShape[] shapes = complexShape.getShapes();
        for (SimpleShape shape : shapes) {
            shape.move(newX, newY);
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // pour le retour arrière
    /**
     * Implements method for the <tt>MouseListener</tt> interface to
     * draw the selected shape into the drawing canvas when we release a shape.
     * @param evt The associated mouse event.
     */
    public void goBack() {
        isShapeSelected = false;
        if (!stateOfPaintList.isEmpty() && stateOfPaintList.size() > 1) {
            stateOfPaintList.remove(stateOfPaintList.size()-1); // Supprime le dernier état
            ArrayList<Visitable>shapesList = buildShapeList(stateOfPaintList.size()-1); // Récupère la liste des formes
            // Efface le panneau
            Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
            g2.setColor(Color.WHITE);
            g2.fillRect(0, 0, m_panel.getWidth(), m_panel.getHeight());
            // Redessine toutes les formes restantes
            for (Visitable shape : shapesList) {
                SimpleShape simpleShape = (SimpleShape) shape;
                simpleShape.draw(g2);
            }
            printStates();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // pour la sauvegarde
    /**
     * Implements method for the <tt>MouseListener</tt> interface to
     * draw the selected shape into the drawing canvas when we release a shape.
     * @param evt The associated mouse event.
     */
    public void save(){
        String jsonData = "{\n  \"shapes\": [\n";
        String xmlData = "<?xml version=\"1.0\" encoding=\"UTF-8\" ?>\r\n" + //
                "<root>\r\n" + //
                "<shapes>\n";
        // ItÃ¨re sur tous les objets
        ArrayList<Visitable> shapesList = buildShapeList(stateOfPaintList.size()-1);
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

    public ComplexShape regroup() {
        isInGroupSelection = true;
        ArrayList<Visitable> shapesList = buildShapeList(stateOfPaintList.size() - 1);

        ArrayList<SimpleShape> selectedShapes = new ArrayList<>();
        for (Visitable shape : shapesList) {
            SimpleShape simpleShape = (SimpleShape) shape;
            if (simpleShape.isSelected()) {
                selectedShapes.add(simpleShape);
            }
        }
        ComplexShape complexShape = new ComplexShape(selectedShapes.toArray(new SimpleShape[0]));
        System.out.println("Shapes ajoutés au ComplexShape :");
        SimpleShape[] addedShapes = complexShape.getShapes();
        for (SimpleShape shape : addedShapes) {
            System.out.println(shape.getClass().getName() + ": x=" + shape.getX() + ", y=" + shape.getY());
        }
    
        isInGroupSelection = false;
        return complexShape;
    }
    

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Controlleur de l'utilisateur
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Controlleur de la souris
    /**
     * Implements method for the <tt>MouseListener</tt> interface to
     * draw the selected shape into the drawing canvas.
     * @param evt The associated mouse event.
     */
    public void mouseClicked(MouseEvent evt) {
        System.out.println("je clique");
        if (m_panel.contains(evt.getX(), evt.getY()) && !isShapeSelected) {
            drawShape(evt);

        } else if (m_panel.contains(evt.getX(), evt.getY())) {
            ArrayList<Visitable> shapesList = buildShapeList(stateOfPaintList.size() - 1);

            for (Visitable shape : shapesList) {
                SimpleShape simpleShape = (SimpleShape) shape;
                simpleShape.draw(m_panel.getGraphics());

                if (simpleShape.contains(evt.getPoint())) {
                    // Inverser l'état de sélection
                    simpleShape.setSelected(!simpleShape.isSelected());

                    // Redessiner avec la sélection mise à jour
                    Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
                    g2.setColor(Color.WHITE);
                    g2.fillRect(0, 0, m_panel.getWidth(), m_panel.getHeight());
                    paintSimpleShapes(g2);
                    simpleShape.draw(g2);
    
                    // Mettre à jour l'état de peinture
                    addStateOfPaint(shapesList);
                }
            }
        }
        System.out.println("jai cliqué");
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
        System.out.println("je presse");
        ArrayList<Visitable> shapesList = buildShapeList(stateOfPaintList.size()-1);
        if (m_panel.contains(evt.getX(), evt.getY()) && !isInGroupSelection) {
            for (Visitable shape : shapesList) {
                SimpleShape simpleShape = (SimpleShape) shape;
                if (simpleShape.contains(evt.getPoint())) {
                    // Traitement du clic sur la forme
                    xShape = evt.getX() - simpleShape.getX();
                    yShape = evt.getY() - simpleShape.getY();
                    // Mettez à jour la propriété isSelected ici
                    simpleShape.setSelected(!simpleShape.isSelected());
                    // Redessiner avec la sélection mise à jour
                    Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
                    g2.setColor(Color.WHITE);
                    g2.fillRect(0, 0, m_panel.getWidth(), m_panel.getHeight());
                    paintSimpleShapes(g2);
                    simpleShape.draw(g2);
                    selectShape(simpleShape);
                    System.out.println(simpleShape.getClass().getName() + " sélectionné");
                }
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

    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Controlleur du clavier
    @Override
    public void keyPressed(KeyEvent e) {
    }
    @Override
    public void keyReleased(KeyEvent e) {
    }
    @Override
    public void keyTyped(KeyEvent e) {
    }
    
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Les listeners
    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Les listener pour les boutons
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

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Les listener pour le bouton de sauvegarde
    // La fonction de sauvegarde
    public class SauvegardeActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            save();
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Les listener pour le bouton de regroupement
    // La fonction de regroupement
    public class RegroupeActionListener implements ActionListener {
        public void actionPerformed(ActionEvent e) {
            regroup(); 
        }
    }

    ///////////////////////////////////////////////////////////////////////////////////////////////////////////////
    // Le listener pour le clavier
    private class KeyEventController implements KeyEventDispatcher {
        @Override
        public boolean dispatchKeyEvent(KeyEvent e) {
            if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_Z) {
                long currentTime = System.currentTimeMillis();
                if (currentTime - lastCtrlZPressTime >= CTRL_Z_DELAY) {
                    remoteControl.addCommand(new GoBack(JDrawingFrame.this));
                    lastCtrlZPressTime = currentTime;
                }
            }
            remoteControl.play();
            remoteControl.reset();
            return false;
        }
    }
}
