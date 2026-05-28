package Main;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
/*     */ 
/*     */ public class Launcher {
/*     */   private static JTextArea logArea;
/*     */   
/*     */   public static void main(String[] args) {
/*     */     try {
/*  27 */       UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
/*  27 */     } catch (Exception exception) {}
/*  29 */     JFrame frame = new JFrame("ERROR 422 - Ultimate Launcher");
/*  30 */     frame.setDefaultCloseOperation(3);
/*  31 */     frame.setSize(650, 450);
/*  33 */     JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
/*  34 */     mainPanel.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));
/*  37 */     logArea = new JTextArea();
/*  38 */     logArea.setEditable(false);
/*  39 */     logArea.setBackground(new Color(10, 10, 10));
/*  40 */     logArea.setForeground(new Color(50, 255, 50));
/*  41 */     logArea.setFont(new Font("Consolas", 0, 12));
/*  42 */     JScrollPane scroll = new JScrollPane(logArea);
/*  45 */     JPanel buttonPanel = new JPanel(new GridLayout(1, 2, 10, 0));
/*  47 */     JButton btnLaunch = new JButton("DÉMARRER LE JEU");
/*  48 */     btnLaunch.setBackground(new Color(0, 100, 0));
/*  49 */     btnLaunch.setForeground(Color.BLACK);
/*  51 */     JButton btnClean = new JButton("RÉINITIALISER (FIX)");
/*  52 */     btnClean.setBackground(new Color(150, 0, 0));
/*  53 */     btnClean.setForeground(Color.BLACK);
/*  55 */     buttonPanel.add(btnLaunch);
/*  56 */     buttonPanel.add(btnClean);
/*  59 */     btnLaunch.addActionListener(e -> (new Thread(() -> lancerJeu())).start());
/*  60 */     btnClean.addActionListener(e -> nettoyerFichiers());
/*  62 */     mainPanel.add(new JLabel("Panel de Contrôle Error 422", 0), "North");
/*  63 */     mainPanel.add(scroll, "Center");
/*  64 */     mainPanel.add(buttonPanel, "South");
/*  66 */     frame.add(mainPanel);
/*  67 */     frame.setLocationRelativeTo((Component)null);
/*  68 */     frame.setVisible(true);
/*  70 */     log("[INFO] Système prêt.");
/*     */   }
/*     */   
/*     */   private static void log(String msg) {
/*  74 */     SwingUtilities.invokeLater(() -> logArea.append(msg + "\n"));
/*     */   }
/*     */   
/*     */   private static void nettoyerFichiers() {
/*  78 */     log("[CLEAN] Nettoyage des fichiers temporaires...");
/*  79 */     String[] cibles = { "options.txt", "usercache.json", "logs" };
/*     */     byte b;
/*     */     int i;
/*     */     String[] arrayOfString1;
/*  81 */     for (i = (arrayOfString1 = cibles).length, b = 0; b < i; ) {
/*  81 */       String nom = arrayOfString1[b];
/*  82 */       File f = new File(nom);
/*  83 */       if (f.exists()) {
/*  84 */         deleteFile(f);
/*  85 */         log(" > Supprimé : " + nom);
/*     */       } 
/*     */       b++;
/*     */     } 
/*  88 */     log("[CLEAN] Terminé. Le jeu est réinitialisé.");
/*     */   }
/*     */   
/*     */   private static void deleteFile(File file) {
/*  92 */     if (file.isDirectory()) {
/*  93 */       File[] contents = file.listFiles();
/*  94 */       if (contents != null) {
/*     */         byte b;
/*     */         int i;
/*     */         File[] arrayOfFile;
/*  95 */         for (i = (arrayOfFile = contents).length, b = 0; b < i; ) {
/*  95 */           File f = arrayOfFile[b];
/*  95 */           deleteFile(f);
/*  95 */           b++;
/*     */         } 
/*     */       } 
/*     */     } 
/*  98 */     file.delete();
/*     */   }
/*     */   
/*     */   private static void lancerJeu() {
/*     */     try {
/* 103 */       log("[LOAD] Chargement des JARs...");
/* 104 */       File jarFile = new File("ERROR422.jar");
/* 105 */       File libFile = new File("exe4jlib.jar");
/* 107 */       URL[] urls = { jarFile.toURI().toURL(), libFile.toURI().toURL() };
/* 108 */       URLClassLoader child = new URLClassLoader(urls, Launcher.class.getClassLoader());
/* 110 */       Class<?> classToLoad = Class.forName("com.jdotsoft.jarloader.ERR422Loader", true, child);
/* 111 */       Method method = classToLoad.getDeclaredMethod("main", new Class[] { String[].class });
/* 113 */       log("[RUN] Initialisation de l'instance...");
/* 114 */       method.invoke(null, new Object[] { new String[0] });
/* 115 */     } catch (Exception ex) {
/* 116 */       log("[ERREUR] " + ex.toString());
/*     */     } 
/*     */   }
/*     */ }