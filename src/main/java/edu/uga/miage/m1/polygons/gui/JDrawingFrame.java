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
import edu.uga.miage.m1.polygons.gui.shapes.SimpleShape;
import edu.uga.miage.m1.polygons.gui.shapes.Square;
import edu.uga.miage.m1.polygons.gui.shapes.Triangle;

/**
 * This class represents the main application class, which is a JFrame subclass
 * that manages a toolbar of shapes and a drawing canvas.
 *
 * @author <a href="mailto:christophe.saint-marcel@univ-grenoble-alpes.fr">Christophe</a>
 */
public class JDrawingFrame extends JFrame implements MouseListener, MouseMotionListener,KeyListener {

    public enum Shapes {

        SQUARE, TRIANGLE, CIRCLE
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
    private static final long CTRL_Z_DELAY = 500; // Délai d'attente en millisecondes (1 seconde ici)

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

    /**
     * Implements method for the <tt>MouseListener</tt> interface to
     * draw the selected shape into the drawing canvas.
     * @param evt The associated mouse event.
     */
    public void mouseClicked(MouseEvent evt) {
        if (m_panel.contains(evt.getX(), evt.getY())) {
            Graphics2D g2 = (Graphics2D) m_panel.getGraphics();
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
    }

    /**
     * Implements method for the <tt>MouseListener</tt> interface to complete
     * shape dragging.
     * @param evt The associated mouse event.
     */
    public void mouseReleased(MouseEvent evt) {
    }

    /**
     * Implements method for the <tt>MouseMotionListener</tt> interface to
     * move a dragged shape.
     * @param evt The associated mouse event.
     */
    public void mouseDragged(MouseEvent evt) {
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
