package analizador;

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.Color;
import java.awt.Desktop;
import java.awt.Font;
import java.awt.MouseInfo;
import java.awt.Point;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.logging.Level;
import java.util.logging.Logger;
import java_cup.runtime.Symbol;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;
import javax.swing.JTextPane;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

/**
 *
 * @author GPlus
 */
public class FCompiler extends javax.swing.JFrame {

    int x, y, op, puntero;
    String nombreArchivo = "src/analizador/code.txt", pathAbrir = "", textoSeleccionado = "", code = "", filename;
    ArrayList<String> undo = new ArrayList<String>(), redo = new ArrayList<String>();
    Color color1, color2, color3, color4, cRes, cRes2, cData, cClass, cComent, cCad, cParam, cOP;
    TextLineNumber tln;
    static TSimbolos ts = new TSimbolos();
    ManejadorErrores manejadorE;
    private String textoCopiado = "";
    static String codigointermedio, codigoobjeto;
    ArrayList<String> palabras;
    ArrayList<Color> colores;
    ArrayList<Error1> manejadorErrores;
    ArrayList<Error1> manejadorErrores_intermedio;
    ArrayList<entradaTS> tablaSimbolos;
    CodigoIntermedio ci;
    CodigoObjeto co;
    GPDialog gpd_save, gpd_open, gpd_new, gpd_exit;
    String CodigoOriginal;
    TraductorObjeto tr;
    /**
     * Creates new form FCompiler
     */
    public FCompiler() {
        this.setUndecorated(true);
        initComponents();
        this.setLocationRelativeTo(null);
        //generarLex que genera la clase AnalizadorLexico desde el archivo alexico.flex
        //generarLex(); //Se eliminó 

        MasmChecker();
        codigointermedio = "";
        codigoobjeto = "";

        gpd_save = new GPDialog(this, false, true, false);
        gpd_open = new GPDialog(this, false, true, false);
        gpd_new = new GPDialog(this, true, true, true);
        gpd_exit = new GPDialog(this, true, true, true);
        tablaSimbolos = new ArrayList<entradaTS>();
        manejadorErrores = new ArrayList<Error1>();
        manejadorErrores_intermedio = new ArrayList<Error1>();
        //setIconImage(new ImageIcon(getClass().getResource("../imagenes/brillabrillacomodiamantenelcielo.png")).getImage()); ------Eliminado por imagen no relativa--------
        manejadorE = new ManejadorErrores();

        jTextPane_Code.setCaretPosition(0);

        palabras = new ArrayList<String>();
        colores = new ArrayList<Color>();

        /**
         * *********Ajuste de colores*******************
         */
        color1 = new Color(41, 85, 72);
        color2 = new Color(58, 121, 104);
        color3 = new Color(255, 255, 255);
        op = 1;
        tln = new TextLineNumber(jTextPane_Code);
        jScrollPane1.setRowHeaderView(tln);

        cRes = new Color(249, 38, 114);
        cRes2 = new Color(128, 0, 255);
        cData = new Color(72, 192, 239);
        cClass = new Color(103, 223, 116);
        cComent = new Color(104, 113, 94);
        cCad = new Color(230, 219, 116);
        cParam = new Color(253, 151, 32);
        cOP = new Color(245, 3, 75);
        buscar(jTextPane_Code.getText().toUpperCase());
        undo.add(jTextPane_Code.getText());
        ci = new CodigoIntermedio(this, false);
        co = new CodigoObjeto(this, false);
        if (!jTextPane_Code.getText().equals("")) {
            CodigoOriginal = jTextPane_Code.getText();
        }

        jTextPane_Code.getDocument().addDocumentListener(new DocumentListener() {

            @Override
            public void removeUpdate(DocumentEvent e) {
                //System.out.println("volvio a estar como antes");
                if (!jLabel8.getText().equals("")) {
                    if (!CodigoOriginal.equals(jTextPane_Code.getText())) {
                        if (!jLabel8.getText().contains("*")) {
                            //jLabel8.setForeground(Color.red);
                            jLabel8.setFont(new Font("Tahoma", Font.BOLD, 12));
                            jLabel8.setText(jLabel8.getText() + "*");
                        }
                    } else {
                        //jLabel8.setForeground(Color.WHITE);
                        jLabel8.setFont(new Font("Tahoma", Font.PLAIN, 12));
                        jLabel8.setText(jLabel8.getText().replace("*", ""));
                    }
                }
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                //System.out.println("Inserto texto");
                if (!jLabel8.getText().equals("")) {
                    if (!CodigoOriginal.equals(jTextPane_Code.getText())) {
                        if (!jLabel8.getText().contains("*")) {
                            //jLabel8.setForeground(Color.red);
                            jLabel8.setFont(new Font("Tahoma", Font.BOLD, 12));
                            jLabel8.setText(jLabel8.getText() + "*");
                        }
                    } else {
                        //jLabel8.setForeground(Color.black);
                        jLabel8.setFont(new Font("Tahoma", Font.PLAIN, 12));
                        jLabel8.setText(jLabel8.getText().replace("*", ""));
                    }
                }
            }

            @Override
            public void changedUpdate(DocumentEvent arg0) {
                //System.out.println("changedUpdate");
            }

        });

    }

    public void generarLex() {
        File JF = new File("src/analizador/alexico.flex");
        File file = new File(JF.getAbsolutePath() + "");
        jflex.Main.generate(file);
    }

    //Revision de MASM32
    public void MasmChecker() {
        String ruta = "C:\\masm32\\bin\\bldallc.bat";
        File directorio = new File(ruta);
        if (!directorio.exists()) {
            GPDialog dp = new GPDialog(this, true, false, false);
            dp.codigo("MASM32 is not installed on your computer or has \nbeen removed.\n\nPlease check MASM32 is installed on C:\\MASM32", "Error!");
            dp.setVisible(true);
        }
        //path+="\\ensamblador.asm";
        ruta = "C:\\masm32\\GPlus";
        directorio = new File(ruta);
        if (!directorio.exists()) {
            directorio.mkdirs();
        }
        String GPlusML = "@echo off\n\ncd \\masm32\\GPlus \nif not exist rsrc.rc goto over1\n\\masm32\\bin\\rc /v rsrc.rc\n\\masm32\\bin\\cvtres /machine:ix86 rsrc.res\n:over1\n"
                + "\nif exist %1.obj del %1.obj\nif exist %1.exe del %1.exe\n\n\\masm32\\bin\\ml /c /coff %1.asm\nif errorlevel 1 goto errasm\n"
                + "\nif not exist rsrc.obj goto nores\n\n\\masm32\\bin\\Link /SUBSYSTEM:CONSOLE /OPT:NOREF %1.obj rsrc.obj\nif errorlevel 1 goto errlink\n"
                + "\ndir %1.*\ngoto TheEnd\n\n:nores\n\\masm32\\bin\\Link /SUBSYSTEM:CONSOLE /OPT:NOREF %1.obj\nif errorlevel 1 goto errlink\n"
                + "dir %1.*\ngoto TheEnd\n\n:errlink\necho _\necho Link error\ngoto TheEnd\n\n:errasm\necho _\necho Assembly Error\n"
                + "goto TheEnd\n\n:TheEnd";
        saveFile("C:\\masm32\\GPlus\\GPlusML.bat", GPlusML);
    }

    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jPanel1 = new javax.swing.JPanel();
        tituloPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jScrollPane1 = new javax.swing.JScrollPane();
        jTextPane_Code = new javax.swing.JTextPane();
        cintaPanel = new javax.swing.JPanel();
        item5 = new javax.swing.JLabel();
        item1 = new javax.swing.JLabel();
        item2 = new javax.swing.JLabel();
        item3 = new javax.swing.JLabel();
        item4 = new javax.swing.JLabel();
        item6 = new javax.swing.JLabel();
        cintillaPanel = new javax.swing.JPanel();
        filePanel = new javax.swing.JPanel();
        jLabel3 = new javax.swing.JLabel();
        editPanel = new javax.swing.JPanel();
        jLabel4 = new javax.swing.JLabel();
        compilePanel = new javax.swing.JPanel();
        jLabel5 = new javax.swing.JLabel();
        appearPanel = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        helpPanel = new javax.swing.JPanel();
        jLabel7 = new javax.swing.JLabel();
        outPanel = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        out2Panel = new javax.swing.JPanel();
        jScrollPane3 = new javax.swing.JScrollPane();
        jTextPane_Output = new javax.swing.JTextPane();

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                formWindowClosing(evt);
            }
        });

        jPanel1.setBackground(new java.awt.Color(255, 255, 255));
        jPanel1.setLayout(null);

        tituloPanel.setBackground(new java.awt.Color(41, 85, 72));
        tituloPanel.setForeground(new java.awt.Color(51, 51, 51));
        tituloPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                tituloPanelMouseDragged(evt);
            }
        });
        tituloPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                tituloPanelMousePressed(evt);
            }
        });
        tituloPanel.setLayout(null);

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cruz.png"))); // NOI18N
        jLabel1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel1MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel1MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel1MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel1MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel1MouseReleased(evt);
            }
        });
        tituloPanel.add(jLabel1);
        jLabel1.setBounds(1050, 0, 47, 33);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/minimizar.png"))); // NOI18N
        jLabel2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel2MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                jLabel2MouseEntered(evt);
            }
            public void mouseExited(java.awt.event.MouseEvent evt) {
                jLabel2MouseExited(evt);
            }
            public void mousePressed(java.awt.event.MouseEvent evt) {
                jLabel2MousePressed(evt);
            }
            public void mouseReleased(java.awt.event.MouseEvent evt) {
                jLabel2MouseReleased(evt);
            }
        });
        tituloPanel.add(jLabel2);
        jLabel2.setBounds(1000, 0, 47, 33);

        jLabel15.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/brillabrillacomodiamantenelcielo.png"))); // NOI18N
        tituloPanel.add(jLabel15);
        jLabel15.setBounds(10, 0, 30, 30);

        jLabel14.setFont(new java.awt.Font("Caviar Dreams", 0, 14)); // NOI18N
        jLabel14.setForeground(new java.awt.Color(236, 236, 236));
        jLabel14.setText("GPlus Compiler");
        jLabel14.setToolTipText("");
        tituloPanel.add(jLabel14);
        jLabel14.setBounds(40, 0, 120, 30);

        jLabel8.setForeground(new java.awt.Color(204, 204, 204));
        jLabel8.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel8.setText(" ");
        tituloPanel.add(jLabel8);
        jLabel8.setBounds(140, 10, 840, 14);

        jPanel1.add(tituloPanel);
        tituloPanel.setBounds(0, 0, 1097, 40);

        jTextPane_Code.setFont(new java.awt.Font("Verdana", 0, 14)); // NOI18N
        jTextPane_Code.setText("/*SAMPLE PROGRAM*/\nDECLARE {\n\tINT IDI1;\n\tINT IDI2;\n\tint idi3;\n\tSTRING IDS1;\n\tSTRING IDS2;\n\tSTRING IDS3;\n\tString Saludo;\n\tBOOLEAN IDB1 ;\n}\n\nMAIN {\n\tIDI1=4+(3+4)-6+(5-5);\n\tIDS1 = \"MI\"; \n\tIDS2 = \" PRIMER\";\n\tIDS3= \"HOLA\"+\"MUNDO\";\n\tIDB1 = TRUE ;\n\tSaludo = IDS1 + IDS2 + IDS3;\n\tARRAYSTRING ID5 = NEW [ \"ING\",\"ARQ\",\"BIO\",\"CIVIL\",\"MECATRONICA\"] ;\n\tFEATURES ID4 = NEW FEATURES ( 9,2,3,4,5,6,4,8,9,10,1,2,3,4,5,6,1,8,9,2,2) ;\n\tFEATURES ID6 = NEW FEATURES ( 9,2,3,4,5,6,4,8,9,10,1,2,3,4,5,6,1,8,9,2,2) ;\n\tGROUP ID2 = NEW GROUP (ID4, MAX, ID5);\n\tNODE S = NEW NODE (ID4,\"Mariela\",\"ING\");\n\tNODE id = NEW NODE (ID6,\"Cesar\",\"BIO\");\n\tNODE id1 = NEW NODE (ID4,\"Ernesto\",\"CIVIL\");\n\tIDI1 = GETFEATURES (S.ANXIETY);\n\tGENERATEGROUP (ID2);\n\tIF (10<((5 - 4)+(6+2))){\n\t\tPRINTGRAPH (ID2);\t\n\t\tPRINTLN (\"Parte\"+\"if\"+\"verdadero\");\n\t\tIF (TRUE && IDB1 %% (5<2) ){\n\t\t\tIDI2 = GETSIMILITUDE(s,ID);\n\t\t}\n\t\t\n\t\tELSE {\n\t\t\tIDI2 = GETSIMILITUDE(ID,ID1);\n\t\t}\n\t       PRINTLN (\"Parte\"+\"fuera\"+\"del\"+\"if\");\n\t}ELSE {\n\t\tPRINTLN (\"A\");\n\t\tID31 = GETFEATURES (S.ANXIETY);\n\t}\n\tLOOP ((10 < (5-2)) && TRUE ){\n\tARRAYSTRING ID10 = NEW [ \"PROGRAMADOR\",\"ADMINISTRADOR\",\"ING REDES\",\"LIDER\",\"DISENADOR\"] ;\n\tFEATURES ID7 = NEW FEATURES ( 9,2,3,4,5,6,4,8,9,10,1,2,3,4,5,6,1,8,9,2,2) ;\n\tGROUP ID22 = NEW GROUP (ID7, MAX, ID10);\n\t}\n\tPRINTLN (\"FIN\"+\"DE\"+\"PROGRAMA\");\n}");
        jTextPane_Code.addFocusListener(new java.awt.event.FocusAdapter() {
            public void focusGained(java.awt.event.FocusEvent evt) {
                jTextPane_CodeFocusGained(evt);
            }
        });
        jTextPane_Code.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                jTextPane_CodePropertyChange(evt);
            }
        });
        jTextPane_Code.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                jTextPane_CodeKeyReleased(evt);
            }
        });
        jScrollPane1.setViewportView(jTextPane_Code);

        jPanel1.add(jScrollPane1);
        jScrollPane1.setBounds(10, 160, 1060, 360);

        cintaPanel.setBackground(new java.awt.Color(58, 121, 104));
        cintaPanel.setLayout(null);

        item5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/trans.png"))); // NOI18N
        item5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                item5MouseClicked(evt);
            }
        });
        cintaPanel.add(item5);
        item5.setBounds(300, 10, 70, 60);

        item1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/new.png"))); // NOI18N
        item1.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                item1MouseClicked(evt);
            }
        });
        cintaPanel.add(item1);
        item1.setBounds(10, 10, 70, 60);

        item2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/open.png"))); // NOI18N
        item2.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                item2MouseClicked(evt);
            }
        });
        cintaPanel.add(item2);
        item2.setBounds(80, 10, 70, 60);

        item3.setIcon(new javax.swing.ImageIcon(getClass().getResource("/saveas.png"))); // NOI18N
        item3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                item3MouseClicked(evt);
            }
        });
        cintaPanel.add(item3);
        item3.setBounds(150, 10, 70, 60);

        item4.setIcon(new javax.swing.ImageIcon(getClass().getResource("/save.png"))); // NOI18N
        item4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                item4MouseClicked(evt);
            }
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                item4MouseEntered(evt);
            }
        });
        cintaPanel.add(item4);
        item4.setBounds(220, 10, 70, 60);

        item6.setIcon(new javax.swing.ImageIcon(getClass().getResource("/trans.png"))); // NOI18N
        item6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                item6MouseClicked(evt);
            }
        });
        cintaPanel.add(item6);
        item6.setBounds(370, 10, 70, 60);

        jPanel1.add(cintaPanel);
        cintaPanel.setBounds(0, 70, 1100, 80);

        cintillaPanel.setBackground(new java.awt.Color(41, 85, 72));
        cintillaPanel.addMouseMotionListener(new java.awt.event.MouseMotionAdapter() {
            public void mouseDragged(java.awt.event.MouseEvent evt) {
                cintillaPanelMouseDragged(evt);
            }
        });
        cintillaPanel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mousePressed(java.awt.event.MouseEvent evt) {
                cintillaPanelMousePressed(evt);
            }
        });
        cintillaPanel.setLayout(null);

        filePanel.setBackground(new java.awt.Color(58, 121, 104));
        filePanel.setLayout(null);

        jLabel3.setFont(new java.awt.Font("Roboto", 0, 15)); // NOI18N
        jLabel3.setForeground(new java.awt.Color(255, 255, 255));
        jLabel3.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel3.setText("File");
        jLabel3.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel3MouseClicked(evt);
            }
        });
        filePanel.add(jLabel3);
        jLabel3.setBounds(0, 0, 90, 30);

        cintillaPanel.add(filePanel);
        filePanel.setBounds(0, 0, 90, 30);

        editPanel.setBackground(new java.awt.Color(41, 85, 72));
        editPanel.setLayout(null);

        jLabel4.setFont(new java.awt.Font("Roboto", 0, 15)); // NOI18N
        jLabel4.setForeground(new java.awt.Color(255, 255, 255));
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel4.setText("Edit");
        jLabel4.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel4MouseClicked(evt);
            }
        });
        editPanel.add(jLabel4);
        jLabel4.setBounds(0, 0, 90, 30);

        cintillaPanel.add(editPanel);
        editPanel.setBounds(90, 0, 90, 30);

        compilePanel.setBackground(new java.awt.Color(41, 85, 72));

        jLabel5.setFont(new java.awt.Font("Roboto", 0, 15)); // NOI18N
        jLabel5.setForeground(new java.awt.Color(255, 255, 255));
        jLabel5.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel5.setText("Run");
        jLabel5.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel5MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout compilePanelLayout = new javax.swing.GroupLayout(compilePanel);
        compilePanel.setLayout(compilePanelLayout);
        compilePanelLayout.setHorizontalGroup(
            compilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        compilePanelLayout.setVerticalGroup(
            compilePanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        cintillaPanel.add(compilePanel);
        compilePanel.setBounds(180, 0, 90, 30);

        appearPanel.setBackground(new java.awt.Color(41, 85, 72));
        appearPanel.setLayout(null);

        jLabel6.setFont(new java.awt.Font("Roboto", 0, 15)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(255, 255, 255));
        jLabel6.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel6.setText("Appearance");
        jLabel6.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel6MouseClicked(evt);
            }
        });
        appearPanel.add(jLabel6);
        jLabel6.setBounds(5, 0, 90, 30);

        cintillaPanel.add(appearPanel);
        appearPanel.setBounds(270, 0, 100, 30);

        helpPanel.setBackground(new java.awt.Color(41, 85, 72));

        jLabel7.setFont(new java.awt.Font("Roboto", 0, 15)); // NOI18N
        jLabel7.setForeground(new java.awt.Color(255, 255, 255));
        jLabel7.setHorizontalAlignment(javax.swing.SwingConstants.CENTER);
        jLabel7.setText("Help");
        jLabel7.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jLabel7MouseClicked(evt);
            }
        });

        javax.swing.GroupLayout helpPanelLayout = new javax.swing.GroupLayout(helpPanel);
        helpPanel.setLayout(helpPanelLayout);
        helpPanelLayout.setHorizontalGroup(
            helpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 100, Short.MAX_VALUE)
        );
        helpPanelLayout.setVerticalGroup(
            helpPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        cintillaPanel.add(helpPanel);
        helpPanel.setBounds(370, 0, 100, 30);

        jPanel1.add(cintillaPanel);
        cintillaPanel.setBounds(0, 40, 1100, 30);

        outPanel.setBackground(new java.awt.Color(58, 121, 104));

        jLabel13.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/text.png"))); // NOI18N

        javax.swing.GroupLayout outPanelLayout = new javax.swing.GroupLayout(outPanel);
        outPanel.setLayout(outPanelLayout);
        outPanelLayout.setHorizontalGroup(
            outPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        outPanelLayout.setVerticalGroup(
            outPanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(outPanelLayout.createSequentialGroup()
                .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                .addContainerGap())
        );

        jPanel1.add(outPanel);
        outPanel.setBounds(20, 530, 100, 30);

        out2Panel.setBackground(new java.awt.Color(58, 121, 104));

        jTextPane_Output.setEditable(false);
        jTextPane_Output.setFont(new java.awt.Font("Verdana", 0, 12)); // NOI18N
        jTextPane_Output.setForeground(new java.awt.Color(204, 0, 51));
        jTextPane_Output.setFocusable(false);
        jTextPane_Output.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                jTextPane_OutputMouseClicked(evt);
            }
        });
        jScrollPane3.setViewportView(jTextPane_Output);

        javax.swing.GroupLayout out2PanelLayout = new javax.swing.GroupLayout(out2Panel);
        out2Panel.setLayout(out2PanelLayout);
        out2PanelLayout.setHorizontalGroup(
            out2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(out2PanelLayout.createSequentialGroup()
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 1060, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(0, 0, Short.MAX_VALUE))
        );
        out2PanelLayout.setVerticalGroup(
            out2PanelLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, out2PanelLayout.createSequentialGroup()
                .addGap(0, 10, Short.MAX_VALUE)
                .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 120, javax.swing.GroupLayout.PREFERRED_SIZE))
        );

        jPanel1.add(out2Panel);
        out2Panel.setBounds(10, 550, 1060, 130);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.DEFAULT_SIZE, 1097, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 700, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void jLabel1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseClicked
        cerrar();
    }//GEN-LAST:event_jLabel1MouseClicked

    private void jLabel1MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseEntered
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cruz2.png")));
    }//GEN-LAST:event_jLabel1MouseEntered

    private void jLabel1MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseExited
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cruz.png")));
    }//GEN-LAST:event_jLabel1MouseExited

    private void jLabel1MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MousePressed
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cruz.png")));
    }//GEN-LAST:event_jLabel1MousePressed

    private void jLabel1MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel1MouseReleased
        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/cruz2.png")));
    }//GEN-LAST:event_jLabel1MouseReleased

    private void jLabel2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseClicked
        this.setExtendedState(ICONIFIED);
    }//GEN-LAST:event_jLabel2MouseClicked

    private void jLabel2MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseEntered
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/minimizar2.png")));
    }//GEN-LAST:event_jLabel2MouseEntered

    private void jLabel2MouseExited(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseExited
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/minimizar.png")));
    }//GEN-LAST:event_jLabel2MouseExited

    private void jLabel2MousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MousePressed
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/minimizar.png")));
    }//GEN-LAST:event_jLabel2MousePressed

    private void jLabel2MouseReleased(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel2MouseReleased
        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/imagenes/minimizar2.png")));
    }//GEN-LAST:event_jLabel2MouseReleased

    private void tituloPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tituloPanelMousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_tituloPanelMousePressed

    private void tituloPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_tituloPanelMouseDragged
        Point p = MouseInfo.getPointerInfo().getLocation();
        this.setLocation(p.x - x, p.y - y);
    }//GEN-LAST:event_tituloPanelMouseDragged

    private void cintillaPanelMouseDragged(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cintillaPanelMouseDragged
        Point p = MouseInfo.getPointerInfo().getLocation();
        this.setLocation(p.x - x, p.y - y);
    }//GEN-LAST:event_cintillaPanelMouseDragged

    private void cintillaPanelMousePressed(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_cintillaPanelMousePressed
        x = evt.getX();
        y = evt.getY();
    }//GEN-LAST:event_cintillaPanelMousePressed

    private void jLabel4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel4MouseClicked
        op = 2;

        filePanel.setBackground(color1);
        editPanel.setBackground(color2);
        compilePanel.setBackground(color1);
        appearPanel.setBackground(color1);
        helpPanel.setBackground(color1);

        item1.setIcon(new ImageIcon(getClass().getResource("/undo.png")));
        item2.setIcon(new ImageIcon(getClass().getResource("/redo.png")));
        item4.setIcon(new ImageIcon(getClass().getResource("/cut.png")));
        item3.setIcon(new ImageIcon(getClass().getResource("/copy.png")));
        item5.setIcon(new ImageIcon(getClass().getResource("/paste.png")));
        item6.setIcon(new ImageIcon(getClass().getResource("/trans.png")));


    }//GEN-LAST:event_jLabel4MouseClicked

    private void jLabel5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel5MouseClicked
        op = 3;

        filePanel.setBackground(color1);
        editPanel.setBackground(color1);
        compilePanel.setBackground(color2);
        appearPanel.setBackground(color1);
        helpPanel.setBackground(color1);

        item1.setIcon(new ImageIcon(getClass().getResource("/intermedio.png")));
        item2.setIcon(new ImageIcon(getClass().getResource("/obj.png")));
        item3.setIcon(new ImageIcon(getClass().getResource("/run.png")));
        item4.setIcon(new ImageIcon(getClass().getResource("/play.png")));
        item5.setIcon(new ImageIcon(getClass().getResource("/trans.png")));
        item6.setIcon(new ImageIcon(getClass().getResource("/trans.png")));
    }//GEN-LAST:event_jLabel5MouseClicked

    private void jLabel6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel6MouseClicked
        op = 4;

        filePanel.setBackground(color1);
        editPanel.setBackground(color1);
        compilePanel.setBackground(color1);
        appearPanel.setBackground(color2);
        helpPanel.setBackground(color1);

        item1.setIcon(new ImageIcon(getClass().getResource("/tema1.png")));
        item2.setIcon(new ImageIcon(getClass().getResource("/tema2.png")));
        item3.setIcon(new ImageIcon(getClass().getResource("/tema3.png")));
        item4.setIcon(new ImageIcon(getClass().getResource("/tema4.png")));
        item5.setIcon(new ImageIcon(getClass().getResource("/tema5.png")));
        item6.setIcon(new ImageIcon(getClass().getResource("/colores.png")));
    }//GEN-LAST:event_jLabel6MouseClicked

    private void jLabel7MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel7MouseClicked
        op = 5;

        filePanel.setBackground(color1);
        editPanel.setBackground(color1);
        compilePanel.setBackground(color1);
        appearPanel.setBackground(color1);
        helpPanel.setBackground(color2);

        item1.setIcon(new ImageIcon(getClass().getResource("/about.png")));
        item2.setIcon(new ImageIcon(getClass().getResource("/help1.png")));
        item3.setIcon(new ImageIcon(getClass().getResource("/trans.png")));
        item4.setIcon(new ImageIcon(getClass().getResource("/trans.png")));
        item5.setIcon(new ImageIcon(getClass().getResource("/trans.png")));
        item6.setIcon(new ImageIcon(getClass().getResource("/trans.png")));
    }//GEN-LAST:event_jLabel7MouseClicked

    private void jLabel3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jLabel3MouseClicked
        op = 1;

        filePanel.setBackground(color2);
        editPanel.setBackground(color1);
        compilePanel.setBackground(color1);
        appearPanel.setBackground(color1);
        helpPanel.setBackground(color1);

        item1.setIcon(new ImageIcon(getClass().getResource("/new.png")));
        item2.setIcon(new ImageIcon(getClass().getResource("/open.png")));
        item4.setIcon(new ImageIcon(getClass().getResource("/save.png")));
        item3.setIcon(new ImageIcon(getClass().getResource("/saveas.png")));
        item5.setIcon(new ImageIcon(getClass().getResource("/trans.png")));
        item6.setIcon(new ImageIcon(getClass().getResource("/trans.png")));
    }//GEN-LAST:event_jLabel3MouseClicked

    private void item5MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_item5MouseClicked
        switch (op) {
            case 1:
                break;
            case 2:
                int inicio = jTextPane_Code.getSelectionStart();
                String inicioText = jTextPane_Code.getText().substring(0, inicio);
                String finText = jTextPane_Code.getText().substring(inicio);
                String res = inicioText + textoCopiado + finText;
                int caretPos = jTextPane_Code.getCaretPosition();
                jTextPane_Code.setText(res);
                jTextPane_Code.setCaretPosition(caretPos + textoCopiado.length());
                break;
            case 3:
                break;
            case 4:
                color1 = new Color(230, 47, 75);
                color2 = new Color(255, 127, 105);
                color3 = new Color(241, 246, 246);
                color4 = Color.WHITE;
                filePanel.setBackground(color1);
                editPanel.setBackground(color1);
                compilePanel.setBackground(color1);
                appearPanel.setBackground(color2);
                helpPanel.setBackground(color1);

                cintillaPanel.setBackground(color1);
                tituloPanel.setBackground(color1);
                cintaPanel.setBackground(color2);
                outPanel.setBackground(color2);
                out2Panel.setBackground(color2);
                jScrollPane3.setBackground(color2);

                tln.setBackground(color1);
                tln.setForeground(Color.DARK_GRAY);
                tln.setCurrentLineForeground(color4);
                jScrollPane1.setRowHeaderView(tln);
                ci.cambiarColores(color1, color2);
                co.cambiarColores(color1, color2);
                //jPanel1.setBackground(color3);
                break;
            case 5:

                break;
        }
    }//GEN-LAST:event_item5MouseClicked

    private void item1MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_item1MouseClicked

        switch (op) {
            case 1:
                nuevo();
                break;
            case 2:
                if (undo.size() > 1) {

                    redo.add(undo.get(undo.size() - 1));
                    undo.remove(undo.size() - 1);
                    int i = jTextPane_Code.getCaretPosition();
                    jTextPane_Code.setText(undo.get(undo.size() - 1));
                    jTextPane_Code.setCaretPosition(i);

                    buscar(jTextPane_Code.getText().toUpperCase());
                }
                break;
            case 3:
                ci.cambiarColores(tituloPanel.getBackground(), cintaPanel.getBackground());
                if (codigointermedio.length() != 0) {
                    ci.codigo(codigointermedio);
                    ci.posicionInicial();
                }
                ci.setVisible(true);
                break;
            case 4:
                color1 = new Color(19, 19, 19);
                color4 = Color.WHITE;
                color2 = new Color(202, 32, 33);

                filePanel.setBackground(color1);
                editPanel.setBackground(color1);
                compilePanel.setBackground(color1);
                appearPanel.setBackground(color2);
                helpPanel.setBackground(color1);

                cintillaPanel.setBackground(color1);
                tituloPanel.setBackground(color1);
                cintaPanel.setBackground(color2);
                outPanel.setBackground(color2);
                out2Panel.setBackground(color2);
                jScrollPane3.setBackground(color2);

                tln.setBackground(color1);
                tln.setForeground(Color.gray);
                jScrollPane1.setRowHeaderView(tln);
                tln.setCurrentLineForeground(color4);
                ci.cambiarColores(color1, color2);
                co.cambiarColores(color1, color2);
                break;
            case 5:
                AcercaD v = new AcercaD(this, true);
                v.setVisible(true);
                break;
        }

    }//GEN-LAST:event_item1MouseClicked

    private void item2MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_item2MouseClicked
        switch (op) {
            case 1:
                try {
                    abrir();
                } catch (IOException ex) {
                    Logger.getLogger(FCompiler.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case 2:
                if (redo.size() > 1) {

                    undo.add(redo.get(redo.size() - 1));
                    redo.remove(redo.size() - 1);
                    int i = jTextPane_Code.getCaretPosition();
                    jTextPane_Code.setText(redo.get(redo.size() - 1));
                    jTextPane_Code.setCaretPosition(i);

                    buscar(jTextPane_Code.getText().toUpperCase());
                }
                break;

            case 3:
                //TODO: Aquí se abre ventana de código objeto, cambiar if
                co.cambiarColores(tituloPanel.getBackground(), cintaPanel.getBackground());
                if (codigoobjeto.length() != 0) {
                    co.codigo(codigoobjeto);
                    co.posicionInicial();
                }
                co.setVisible(true);

                break;
            case 4:
                color1 = new Color(41, 53, 65);
                color4 = Color.WHITE;
                color2 = new Color(250, 98, 92);

                filePanel.setBackground(color1);
                editPanel.setBackground(color1);
                compilePanel.setBackground(color1);
                appearPanel.setBackground(color2);
                helpPanel.setBackground(color1);

                cintillaPanel.setBackground(color1);
                tituloPanel.setBackground(color1);
                cintaPanel.setBackground(color2);
                outPanel.setBackground(color2);
                out2Panel.setBackground(color2);
                jScrollPane3.setBackground(color2);

                tln.setBackground(color1);
                tln.setForeground(Color.gray);
                tln.setCurrentLineForeground(color4);
                jScrollPane1.setRowHeaderView(tln);
                ci.cambiarColores(color1, color2);
                co.cambiarColores(color1, color2);
                break;
            case 5:
                enlace("http://gplus.pe.hu/");
                break;
        }

    }//GEN-LAST:event_item2MouseClicked

    private void item3MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_item3MouseClicked
        switch (op) {
            case 1:
                guardarArchivo(jTextPane_Code);
                break;
            case 2:
                int inicio = jTextPane_Code.getSelectionStart();
                int fin = jTextPane_Code.getSelectionEnd();
                String s = jTextPane_Code.getText();
                textoCopiado = s.substring(inicio, fin);
                break;
            case 3:
                jTextPane_Output.setText("");
//                jTextPane_Tokens.setText("");

                if (!manejadorE.empty()) {
                    manejadorE.clear();
                }

                try {
                    analizar();

                } catch (IOException ex) {
                    Logger.getLogger(FCompiler.class.getName()).log(Level.SEVERE, null, ex);
                }
                break;
            case 4:
                color1 = new Color(41, 85, 72);
                color2 = new Color(58, 121, 104);
                color4 = Color.white;

                filePanel.setBackground(color1);
                editPanel.setBackground(color1);
                compilePanel.setBackground(color1);
                appearPanel.setBackground(color2);
                helpPanel.setBackground(color1);

                cintillaPanel.setBackground(color1);
                tituloPanel.setBackground(color1);
                cintaPanel.setBackground(color2);
                outPanel.setBackground(color2);
                out2Panel.setBackground(color2);
                jScrollPane3.setBackground(color2);

                tln.setBackground(color1);
                tln.setForeground(Color.gray);
                tln.setCurrentLineForeground(color4);
                jScrollPane1.setRowHeaderView(tln);
                ci.cambiarColores(color1, color2);
                co.cambiarColores(color1, color2);

                break;
            case 5:
                break;
        }
    }//GEN-LAST:event_item3MouseClicked

    private void item4MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_item4MouseClicked
        switch (op) {
            case 1:
                sobrescribirArchivo(jTextPane_Code);
                break;
            case 2:
                int inicio = jTextPane_Code.getSelectionStart();
                int fin = jTextPane_Code.getSelectionEnd();
                String s = jTextPane_Code.getText();
                textoCopiado = s.substring(inicio, fin);
                String inicioText = jTextPane_Code.getText().substring(0, inicio);
                String finText = jTextPane_Code.getText().substring(fin, jTextPane_Code.getText().length());
                int caretPos = jTextPane_Code.getCaretPosition();
                jTextPane_Code.setText(inicioText + finText);
                jTextPane_Code.setCaretPosition(caretPos - textoCopiado.length());
                break;
            case 3:
                break;
            case 4:
                color1 = new Color(69, 110, 141);
                color2 = new Color(254, 185, 66);
                color4 = Color.WHITE;

                filePanel.setBackground(color1);
                editPanel.setBackground(color1);
                compilePanel.setBackground(color1);
                appearPanel.setBackground(color2);
                helpPanel.setBackground(color1);

                cintillaPanel.setBackground(color1);
                tituloPanel.setBackground(color1);
                cintaPanel.setBackground(color2);
                outPanel.setBackground(color2);
                out2Panel.setBackground(color2);
                jScrollPane3.setBackground(color2);

                tln.setBackground(color1);
                tln.setForeground(Color.DARK_GRAY);
                tln.setCurrentLineForeground(color4);
                jScrollPane1.setRowHeaderView(tln);
                ci.cambiarColores(color1, color2);
                co.cambiarColores(color1, color2);

                break;
            case 5:
                break;

        }
    }//GEN-LAST:event_item4MouseClicked

    private void jTextPane_CodeKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_jTextPane_CodeKeyReleased

        redo.removeAll(redo);

        undo.add(jTextPane_Code.getText());

        buscar(jTextPane_Code.getText().toUpperCase());
    }//GEN-LAST:event_jTextPane_CodeKeyReleased

    private void item6MouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_item6MouseClicked

        switch (op) {
            case 1:
                break;
            case 2:
                break;
            case 3:
                break;
            case 4:
                ElegirColor v = new ElegirColor(this, true);
                v.cambiarColores(tituloPanel.getBackground(), cintaPanel.getBackground(), tln.getForeground(), tln.getCurrentLineForeground());
                v.setVisible(true);
                break;
            case 5:
                break;
        }
    }//GEN-LAST:event_item6MouseClicked

    private void jTextPane_CodeFocusGained(java.awt.event.FocusEvent evt) {//GEN-FIRST:event_jTextPane_CodeFocusGained
        LinePainter painter = new LinePainter(jTextPane_Code);
    }//GEN-LAST:event_jTextPane_CodeFocusGained

    private void jTextPane_OutputMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_jTextPane_OutputMouseClicked
        String textini = "";
        if (!jTextPane_Output.getText().equals("")) {
            if (!jTextPane_Output.getText().trim().equals("")) {
                int position = jTextPane_Output.viewToModel(jTextPane_Output.getMousePosition());
                String[] text = jTextPane_Output.getText().split("\n");
                int[] charNum = new int[text.length];
                for (int i = 0; i < text.length; i++) {
                    charNum[i] = text[i].length() + 1;
                }
                int sum = 0;
                for (int i = 0; i < charNum.length; i++) {
                    sum += charNum[i];
                    if (sum >= position) {
                        //String []textini=text[i].split("Line:", 2);
                        int position2 = text[i].indexOf("Line:");
                        textini = text[i].substring(position2 + 3);
                        int position3 = textini.indexOf(":");
                        textini = textini.substring(position3);
                        textini = textini.replace(".", "");
                        textini = textini.replace(":", "");
                        String temp[] = textini.split(", column");
                        textini = temp[0].trim();
                        //System.out.println("PRUEBAS DE LA LINEA "+textini);
                        //jTextPane_Code.replaceSelection(text[i] + "\n");
                        break;
                    }
                }
            }

            //LinePainter painter = new LinePainter(jTextPane_Output);
            int sumacarateres = 0;
            String lineas[] = jTextPane_Code.getText().split("\n");
            for (int i = 0; i < Integer.parseInt(textini); i++) {
                sumacarateres += lineas[i].length();
            }
            //System.out.println("NUMERO DE CARACTERES "+sumacarateres);
            jTextPane_Code.setCaretPosition(sumacarateres + Integer.parseInt(textini) - 8);
            jTextPane_Code.setFocusable(true);
        }
    }//GEN-LAST:event_jTextPane_OutputMouseClicked

    private void jTextPane_CodePropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_jTextPane_CodePropertyChange
        //System.out.println("CAMBIO EL TEXTO");
    }//GEN-LAST:event_jTextPane_CodePropertyChange

    private void item4MouseEntered(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_item4MouseEntered
        // TODO add your handling code here:
    }//GEN-LAST:event_item4MouseEntered

    private void formWindowClosing(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_formWindowClosing
        cerrar();
    }//GEN-LAST:event_formWindowClosing

    //Guardar
    public void guardar() {
        //sirve para guardar el código que se encuentra en el JPanel_Code
        String codigo = jTextPane_Code.getText();
        //Convertimos el codigo a mayúscula para que esté uniforme
        codigo = codigo;

        //Se guarda el codigo en el archivo code.txt
        File archivo = new File(nombreArchivo);
        try {
            FileWriter salida = new FileWriter(archivo);
            BufferedWriter bw = new BufferedWriter(salida);
            bw.write(codigo);
            bw.close();
        } catch (IOException e) {

            gpd_save.codigo("The file could not be saved. An internal \nerror has occurred.", "Error!");
            gpd_save.setVisible(true);
        }
    }

    //Guardar
    public void sobrescribirArchivo(JTextPane panell) {
        //Checo si hay algun archivo abierto, o si no a guardado el archivo actual que esta siendo usado
        //para uso uso la variable pathAbrir ya que ese tendra el ultimo path 
        if (pathAbrir.equals("")) {
            guardarArchivo(panell);
        } else {

            FileWriter fw;
            try {
                fw = new FileWriter(pathAbrir);
            } catch (IOException io) {
                return;
            }

            //Escribimos
            try {
                fw.write(panell.getText());

            } catch (IOException io) {
                gpd_save.codigo("The file could not be saved. An internal \nerror has occurred", "Error!");
                gpd_save.setVisible(true);
            }

            //cerramos el fichero
            try {
                fw.close();
                CodigoOriginal = jTextPane_Code.getText();
                jLabel8.setFont(new Font("Tahoma", Font.PLAIN, 12));
                jLabel8.setText(jLabel8.getText().replace("*", ""));
            } catch (IOException io) {
                gpd_save.codigo("The file could not be closed. An internal \nerror has occurred", "Error!");
                gpd_save.setVisible(true);
            }

        }
    }

    //Guardar Archivo
    public void guardarArchivo(JTextPane panell) {

        String texto = panell.getText();
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.addChoosableFileFilter(new FileNameExtensionFilter("All files *.gp", "gp", "gp"));
        int seleccion = fileChooser.showSaveDialog(null);
        try {
            if (seleccion == JFileChooser.APPROVE_OPTION) {
                File JFC = fileChooser.getSelectedFile();
                String PATH = JFC.getAbsolutePath();
                pathAbrir = PATH + ".gp";
                filename = JFC.getAbsoluteFile().getName();
                jLabel8.setText(JFC.getAbsoluteFile().getName() + ".gp");

                PrintWriter printwriter = new PrintWriter(JFC);
                printwriter.print(panell.getText());
                printwriter.close();
                if (!(PATH.endsWith(".gp"))) {
                    File temp = new File(PATH + ".gp");
                    JFC.renameTo(temp);
                }
            }
        } catch (Exception e) {
            gpd_save.codigo("The file could not be closed. An internal \nerror has occurred", "Error!");
            gpd_save.setVisible(true);
        }
    }

    //Guardar gral
    public void saveFile(String path, String cad) {

        File archivo = new File(path);
        try {
            FileWriter salida = new FileWriter(archivo);
            BufferedWriter bw = new BufferedWriter(salida);
            bw.write(cad);
            bw.close();
        } catch (IOException e) {
            gpd_save.codigo("The file could not be closed. An internal \nerror has occurred", "Error!");
            gpd_save.setVisible(true);
        }
    }

    //Cambio de colores
    public void CambiarC() {
        filePanel.setBackground(color1);
        editPanel.setBackground(color1);
        compilePanel.setBackground(color1);
        appearPanel.setBackground(color2);
        helpPanel.setBackground(color1);

        cintillaPanel.setBackground(color1);
        tituloPanel.setBackground(color1);
        cintaPanel.setBackground(color2);
        outPanel.setBackground(color2);
        out2Panel.setBackground(color2);
        jScrollPane3.setBackground(color2);

        tln.setBackground(color1);
        tln.setForeground(color3);
        tln.setCurrentLineForeground(color4);
        jScrollPane1.setRowHeaderView(tln);
    }

    //Abrir documento
    public void abrir() throws IOException {
        String textu = "";
        JFileChooser JFC = new JFileChooser();
        JFC.setFileFilter(new FileNameExtensionFilter("All files *.gp", "gp", "gp"));
        JFC.setAcceptAllFileFilterUsed(false);
        int abrir = JFC.showDialog(null, "Open");
        if (abrir == JFileChooser.APPROVE_OPTION) {
            FileReader FR = null;
            BufferedReader BR = null;
            try {
                File archivo = JFC.getSelectedFile();

                jLabel8.setText(archivo.getName());

                String PATH = JFC.getSelectedFile().getAbsolutePath();
                if (PATH.endsWith(".gp")) {
                    FR = new FileReader(archivo);
                    BR = new BufferedReader(FR);
                    String linea;
                    if (pathAbrir.compareTo(archivo.getAbsolutePath()) == 0) {
                        gpd_save.codigo("The file is open.", "Error!");
                        gpd_save.setVisible(true);
                    } else {
                        pathAbrir = archivo.getAbsolutePath();

                        jTextPane_Code.setText(null);
                        while ((linea = BR.readLine()) != null) {
                            textu = textu + linea + "\n";
                            jTextPane_Code.setText(linea + "\n");
                        }
                        jTextPane_Code.setText(textu);
                        CodigoOriginal = jTextPane_Code.getText();
                        buscar(jTextPane_Code.getText().toUpperCase());
                    }

                }

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
                //Logger.getLogger(fileChooser.class.getName()).log(Level.SEVERE, null, ex);
                //cerramos el fichero, para asegurar que se cierra tanto
            } finally {
                try {
                    if (null != FR) {
                        FR.close();
                    }

                } catch (IOException ex) {
                    ex.printStackTrace();
                    //   Logger.getLogger(fileChooser.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        }

    }

    //Nuevo documento
    public void nuevo() {

        gpd_new.codigo("Are you sure you want to create a new file.\nUnsaved information will be lost.", "Warning!");
        gpd_new.setVisible(true);
        if (gpd_new.val == 1) {
            jTextPane_Output.setText("");
            jTextPane_Code.setText("/*Developed in G+ Compiler*/\n\nDECLARE {\n\t/*TO DO DECLARE*/\n}\n\nMAIN {\n\t/*TO DO MAIN*/\n}");
            pathAbrir = "";
            jLabel8.setText("");
        }

        buscar(jTextPane_Code.getText().toUpperCase());
    }

    /**
     * ***********Análisis************
     */
    public void analizar() throws IOException {
        sobrescribirArchivo(jTextPane_Code);

        jTextPane_Output.setText("");
        jTextPane_Output.setForeground(new Color(204, 0, 51));
        Generador.contadorTemp = 0;
        Generador.contadorEtiq = 0;
        tablaSimbolos.clear();
        manejadorErrores.clear();
        codigointermedio = "";
        manejadorErrores_intermedio.clear();
        ts.ts = new ArrayList<TSDatos>();

        int c = 0;
        code = jTextPane_Code.getText().toUpperCase();
        String cad = "";
        if ((jTextPane_Code.getText()).length() >= 1) {
            try {
                analizarCodigo();
                sintactico();
                System.out.println("Tamaño manejador errores: " + manejadorErrores.size());
                if (manejadorErrores.size() == 0) {
                    generacion();

                } else {
                    Collections.sort(manejadorErrores, new Comparator<Error1>() { //Ordenamiento a partir de numero de linea
                        @Override
                        public int compare(Error1 p1, Error1 p2) {
                            return new Integer(p1.getLinea()).compareTo(new Integer(p2.getLinea()));
                        }
                    });
                    String merrores = mostrarManejadorErrores();
                    append(new Color(240, 0, 0), merrores);
                    //jTextPane_Output.setText(jTextPane_Output.getText() + merrores);

                    //Error codigo intermedio
                    ci.cambiarForeground(new Color(240, 0, 0));
                    codigointermedio = "No intermediate code generated";
                    ci.codigo(codigointermedio);
                    ci.posicionInicial();

                    //Error codigo objeto
                    co.cambiarForeground(new Color(240, 0, 0));
                    codigoobjeto = "No object code generated";
                    co.codigo(codigoobjeto);
                    co.posicionInicial();
                }

            } catch (Exception ex) {
                Logger.getLogger(FCompiler.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
            jTextPane_Output.setForeground(new Color(221, 140, 32));
            jTextPane_Output.setText("NO CODE TO COMPILE");

            //Error codigo intermedio
            ci.cambiarForeground(new Color(221, 140, 32));
            codigointermedio = "No intermediate code to generate";
            ci.codigo(codigointermedio);
            ci.posicionInicial();

            //Error codigo objeto
            co.cambiarForeground(new Color(221, 140, 32));
            codigoobjeto = "No object code to generate";
            co.codigo(codigoobjeto);
            co.posicionInicial();
        }

    }

    public void analizarCodigo() throws Exception {
        AnalizadorLexico flex = new AnalizadorLexico(new StringReader(code));
        Symbol comp_lexico = flex.next_token();//separa por lexema

        while (comp_lexico.sym != 0) {
            comp_lexico = flex.next_token();
        }
        if (!flex.TablaSimbolos.isEmpty()) {
            this.tablaSimbolos.addAll(flex.TablaSimbolos);

        }
        if (!flex.ManejadorDeErrores.isEmpty()) {
            this.manejadorErrores.addAll(flex.ManejadorDeErrores);
        }
    }

    public void sintactico() {
        AnalizadorLexico flex = new AnalizadorLexico(new StringReader(code));

        Asintactico parser;
        ArrayList<Error1> m = new ArrayList<Error1>();
        //m.add(new errorsin("Prueba",1,2,"añsdhfoiasdhf"));
        parser = new Asintactico(flex, m, jTextPane_Code.getDocument().getDefaultRootElement().getElementCount());
        try {
            parser.parse();
        } catch (Exception ex) {
            Logger.getLogger(FCompiler.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!parser.ManejadorDeErrores.isEmpty()) {
            this.manejadorErrores.addAll(parser.ManejadorDeErrores);
        }
    }

    //Generacion de codigos
    private void generacion() {
        //TODO intermedio en Analizar
        intermedio();
        objeto();

        if (manejadorErrores_intermedio.size() == 0) {
            //jTextPane_Output.setForeground(new Color(162, 13, 97));
            //jTextPane_Output.setText("BUILD SUCCESSFUL");
            //jTextPane_Output.setForeground(new Color(102, 123, 57));
            append(new Color(102, 123, 57), "BUILD SUCCESSFUL");
            //jTextPane_Output.setText(jTextPane_Output.getText()+"BUILD SUCCESSFUL");

            //Codigo intermedio en ventana
            ci.cambiarForeground(new Color(51, 51, 51));
            ci.codigo(codigointermedio);
            ci.posicionInicial();

            //Codigo objeto en ventana                        
            co.cambiarForeground(new Color(51, 51, 51));
            co.codigo(codigoobjeto);
            co.posicionInicial();

            saveFile("C:\\masm32\\GPlus\\" + filename + ".cgp", codigointermedio);
        } else {
            String merrores = mostrarManejadorErrores2();

            //Codigo intermedio no generado
            ci.cambiarForeground(new Color(240, 0, 0));
            codigointermedio = "No intermediate code generated";
            ci.codigo(codigointermedio);
            ci.posicionInicial();
            jTextPane_Output.setText(jTextPane_Output.getText() + "\n" + codigointermedio);

            //Codigo objeto no generado
            co.cambiarForeground(new Color(240, 0, 0));
            codigoobjeto = "No object code generated";
            co.codigo(codigoobjeto);
            co.posicionInicial();
            jTextPane_Output.setText(jTextPane_Output.getText() + "\n" + codigoobjeto);
        }
    }

    public void intermedio() {
        AnalizadorLexico flex = new AnalizadorLexico(new StringReader(code));

        Intermedio parser;
        ArrayList<Error1> m = new ArrayList<Error1>();
        parser = new Intermedio(flex, m, jTextPane_Code.getDocument().getDefaultRootElement().getElementCount());
        try {
            parser.parse();
        } catch (Exception ex) {
            Logger.getLogger(FCompiler.class.getName()).log(Level.SEVERE, null, ex);
        }
        if (!parser.ManejadorDeErrores.isEmpty()) {
            this.manejadorErrores.addAll(parser.ManejadorDeErrores);
        }
    }

    //TODO función generación de cógigo objeto
    public void objeto() {
        String ruta = "C:\\masm32\\GPlus";
        File directorio = new File(ruta);
        if (!directorio.exists()) {
            directorio.mkdir();
        }
        
        //TODO HERE
        
        tr = new TraductorObjeto(filename,ts);
        
        codigoobjeto = tr.getCodigogenerado();
        
        String asmfile = "C:\\masm32\\GPlus\\" + filename + ".asm";
        saveFile(asmfile, codigoobjeto);
        cargaLiga(filename);

    }

    public void cargaLiga(String file) {

        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("cmd /c C:\\masm32\\GPlus\\GPlusML.bat " + file);
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is, "cp1252");
            BufferedReader br = new BufferedReader(isr);

            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
                jTextPane_Output.setForeground(new Color(162, 13, 97));
                //jTextPane_Output.setText("BUILD SUCCESSFUL");
                //jTextPane_Output.setForeground(new Color(102, 123, 57));
                if (line.contains(".asm") || line.contains(".cgp") || line.contains(".exe") || line.contains(".obj")) {
                    append(Color.blue, line + "\n");
                } else {
                    append(Color.black, line + "\n");
                }
                //jTextPane_Output.setText(jTextPane_Output.getText()+line+"\n");
            }
        } catch (IOException ex) {
            Logger.getLogger(FCompiler.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    //Manejador de errores
    public String mostrarManejadorErrores() {
        String errores = "";

        for (int i = 0; i <= manejadorErrores.size() - 1; i++) {
            String error = (manejadorErrores.get(i).toString() + "\n");
            if (!error.equals("\n")) {
                errores += error;
            }
        }

        return errores;
    }

    public String mostrarManejadorErrores2() {
        String errores = "";

        for (int i = 0; i <= manejadorErrores_intermedio.size() - 1; i++) {
            String error = (manejadorErrores_intermedio.get(i).toString() + "\n");
            if (!error.equals("\n")) {
                errores += error;
            }
        }
        return errores;
    }

    //Colorear palabras
    public void buscar(String cadena) {
        añadirPalabras();

        int posicion = cadena.indexOf("/*");
        while (posicion >= 0) {
            if (cadena.indexOf("/*", posicion) <= cadena.indexOf("*/", posicion + 2)) {

                palabras.add(cadena.substring(cadena.indexOf("/*", posicion), cadena.indexOf("*/", posicion + 2) + 2));
            } else {
                palabras.add(cadena.substring(cadena.indexOf("/*", posicion)));
            }
            colores.add(new Color(104, 113, 94));
            posicion = cadena.indexOf("/*", posicion + 1);
        }

        ArrayList<ArrayList<String>> palabrasColor = new ArrayList<ArrayList<String>>();
        for (int i = 0; i < palabras.size(); i++) {
            palabrasColor.add(new ArrayList<String>());
            if (palabras.get(i).contains("/*")) {
                //Comentarios
                palabrasColor.get(palabrasColor.size() - 1).add("" + palabras.get(i) + "");

            } else {
                palabrasColor.get(palabrasColor.size() - 1).add("" + palabras.get(i) + " ");//Primera palabra
                palabrasColor.get(palabrasColor.size() - 1).add(" " + palabras.get(i) + " ");
                palabrasColor.get(palabrasColor.size() - 1).add(" " + palabras.get(i) + "\n");
                palabrasColor.get(palabrasColor.size() - 1).add("\n" + palabras.get(i) + " ");
                palabrasColor.get(palabrasColor.size() - 1).add("\n" + palabras.get(i) + "\n");
            }
        }

        SimpleAttributeSet clean = new SimpleAttributeSet();
        jTextPane_Code.getStyledDocument().setCharacterAttributes(0, jTextPane_Code.getText().length(), clean, true);

        for (int i = 0; i < palabrasColor.size(); i++) {
            for (int y = 0; y < palabrasColor.get(i).size(); y++) {
                int index = cadena.indexOf(palabrasColor.get(i).get(y));

                while (index >= 0) {

                    SimpleAttributeSet set = new SimpleAttributeSet();
                    StyleConstants.setForeground(set, colores.get(i));
                    jTextPane_Code.getStyledDocument().setCharacterAttributes(index, palabrasColor.get(i).get(y).length(), set, true);
                    StyleConstants.setForeground(set, Color.black);
                    index = cadena.indexOf(palabrasColor.get(i).get(y), index + 1);
                }
            }
        }

    }

    public void añadirPalabras() {
        jTextPane_Code.setFont(new Font("Verdana", Font.PLAIN, 14));
        //Tipos de datos
        palabras.add("INT");
        colores.add(cData);
        palabras.add("STRING");
        colores.add(cData);
        palabras.add("BOOLEAN");
        colores.add(cData);

        //Operadores
        palabras.add("+");
        colores.add(cOP);
        palabras.add("=");
        colores.add(cParam);
        palabras.add("==");
        colores.add(cParam);
        palabras.add("<");
        colores.add(cParam);
        palabras.add(">");
        colores.add(cParam);
        palabras.add("<=");
        colores.add(cParam);
        palabras.add(">=");
        colores.add(cParam);
        palabras.add("!=");
        colores.add(cParam);
        palabras.add("-");
        colores.add(cOP);
        palabras.add("&&");
        colores.add(cParam);
        palabras.add("%%");
        colores.add(cParam);
        palabras.add("NOT");
        colores.add(cParam);
        palabras.add("//");
        colores.add(new Color(104, 113, 93));

        //Palabras reservadas primarias
        palabras.add("ELSE");
        colores.add(cRes);
        palabras.add("IF");
        colores.add(cRes);
        palabras.add("LOOP");
        colores.add(cRes);
        palabras.add("MAIN");
        colores.add(cRes);
        palabras.add("LOOP");
        colores.add(cRes);
        palabras.add("BUILD");
        colores.add(cRes);
        palabras.add("MAIN");
        colores.add(cRes);
        palabras.add("DECLARE");
        colores.add(cRes);

        //Palabras reservadas secundarias
        palabras.add("FALSE");
        colores.add(cRes2);
        palabras.add("TRUE");
        colores.add(cRes2);
        palabras.add("MAX");
        colores.add(cRes2);
        palabras.add("MIN");
        colores.add(cRes2);
        palabras.add("NEW");
        colores.add(cRes2);

        //Estructuras de datos
        palabras.add("FEATURES");
        colores.add(cParam);
        palabras.add("NODE");
        colores.add(cParam);
        palabras.add("ARRAYSTRING");
        colores.add(cParam);
        palabras.add("GROUP");
        colores.add(cParam);

        //Funciones
        palabras.add("GENERATEGROUP");
        colores.add(cClass);
        palabras.add("GETFEATURES");
        colores.add(cClass);
        palabras.add("PRINTGRAPH");
        colores.add(cClass);
        palabras.add("PRINTLN");
        colores.add(cClass);
        palabras.add("READFEATVAL");
        colores.add(cClass);
        palabras.add("READSTRING");
        colores.add(cClass);
        palabras.add("GETSIMILITUDE");
        colores.add(cClass);

    }

    //Abrir navegador para abrir página
    public void enlace(String enlaceAAceder) {
        Desktop enlace = Desktop.getDesktop();
        try {
            enlace.browse(new URI(enlaceAAceder));
        } catch (IOException | URISyntaxException e) {
            e.getMessage();
        }
    }

    //MANDAR TEXTO EN COLORES
    public void append(Color color, String texto) {
        StyledDocument doc = jTextPane_Output.getStyledDocument();
        Style syle = doc.addStyle("txt", null);
        StyleConstants.setForeground(syle, color);
        try {
            doc.insertString(doc.getLength(), texto, syle);
            jTextPane_Output.setCaretPosition(doc.getLength());
        } catch (BadLocationException ex) {
            ex.printStackTrace();
        }
    }

    //CERRAR VENTANA SIN HACERE NADA.
    public void cerrar() {
        Object[] opciones = {"Si", "No"};
        int eleccion = 0;
        if (!jLabel8.getText().equals(" ") && !jLabel8.getText().contains("*")) {
            System.exit(0);
        } else if (jLabel8.getText().contains("*")) {

            gpd_exit.codigo("Are you sure you want to exit?\n\nChanges will be lost.", "Warning!");
            gpd_exit.setVisible(true);
            eleccion = gpd_exit.val;

            if (eleccion == 1) {
                sobrescribirArchivo(jTextPane_Code);
                System.exit(0);
            } else {
                System.exit(0);
            }
        } else {
            gpd_exit.codigo("Are you sure you want to exit?\n\nChanges will be lost.", "Warning!");
            gpd_exit.setVisible(true);
            eleccion = gpd_exit.val;

            if (eleccion == 1) {
                guardarArchivo(jTextPane_Code);
                System.exit(0);
            } else {
                System.exit(0);
            }

        }
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FCompiler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FCompiler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FCompiler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FCompiler.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FCompiler().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JPanel appearPanel;
    private javax.swing.JPanel cintaPanel;
    private javax.swing.JPanel cintillaPanel;
    private javax.swing.JPanel compilePanel;
    private javax.swing.JPanel editPanel;
    private javax.swing.JPanel filePanel;
    private javax.swing.JPanel helpPanel;
    private javax.swing.JLabel item1;
    private javax.swing.JLabel item2;
    private javax.swing.JLabel item3;
    private javax.swing.JLabel item4;
    private javax.swing.JLabel item5;
    private javax.swing.JLabel item6;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JTextPane jTextPane_Code;
    private javax.swing.JTextPane jTextPane_Output;
    private javax.swing.JPanel out2Panel;
    private javax.swing.JPanel outPanel;
    private javax.swing.JPanel tituloPanel;
    // End of variables declaration//GEN-END:variables
}
