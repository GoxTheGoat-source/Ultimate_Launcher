package Main;

import java.awt.BorderLayout;
import java.awt.CardLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Desktop;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class Launcher {
   private static JFrame mainFrame;
   private static JTextArea logArea;
   private static JComboBox versionList;
   private static JComboBox ramList;
   private static JButton btnLaunch;
   private static JButton btnDir;
   private static JButton btnClean;
   private static JPanel cardPanel;
   private static CardLayout cardLayout;
   private static JTextField userField;
   private static boolean saveUsername = false;
   private static String finalUsername = "Solahdine";
   private static final String GAME_DIR;
   private static final String VERSION_DIR;
   private static final String ASSETS_DIR;
   private static final String LIB_DIR;
   private static final String NATIVES_DIR;
   private static final String CONFIG_FILE;
   private static final String RESOURCESPACKS_DIR;
   private static final String[] SPLASH_TEXTS;
   private static final Map VERSIONS;

   static {
      GAME_DIR = System.getenv("APPDATA") + File.separator + ".minecraft";
      VERSION_DIR = GAME_DIR + File.separator + "versions";
      ASSETS_DIR = GAME_DIR + File.separator + "assets";
      LIB_DIR = GAME_DIR + File.separator + "libraries";
      NATIVES_DIR = GAME_DIR + File.separator + "bin" + File.separator + "natives";
      CONFIG_FILE = String.valueOf(GAME_DIR) + File.separator + "launcher_config.txt";
      RESOURCESPACKS_DIR = GAME_DIR + File.separator + "resourcepacks";
      SPLASH_TEXTS = new String[]{"~aigm,~cr,~s,Sneak:4", "More of 1000 Lines of Code!! [:-O]", "A Problem, Not Go To minecraftforum.net. [:-)]", "Use You My TexturesPacks? [>:-(]", "Who Read That?? [?_?]", "Are You French? [:-)]", "Made By Goxibot [OK]", "Not A Good Day, A Super Day! [:-)]", "Ben Soul - SoulMan [-o-]", "Open source[;)]", "150126", "161121", "150426", "300813", "ERROR 422: The game is playing YOU [???]", "Something is watching from the logs [0_0]", "Don't look at the sky... [X_X]", "File corruption detected... Just kidding! [Maybe]", "He is the glitch [0x422]", "The world is folding [X_X]", "Entity 422 found in cache [!]", "Don't look behind you [0_0]", "Your PC is 32-bit, but your soul is 64-bit [;)]", "01101000 01100101 01101100 01110000 [SOS]", "Searching for the lost chunk [?]", "Memory leak in progress... [99%]", "Is it a feature or a bug? [o_O]", "Unexpected corruption detected [ERR]", "System.corrupt(true) [!!!]", "A Java developer doesn't drink water, he drinks coffee [Logo_Not_Found]", "Hardware: the part you kick. Software: the part you curse [!]", "Toc Toc... Qui est là ? (long silence) ... Java ! [;)]", "I don't sleep, I compile [zZz]", "99 little bugs in the code... fix one... 127 little bugs [:'(]", "Real programmers count from 0 [0,1,2...]", "Ma femme me dit : 'Prends du lait'. Je suis toujours au magasin [LOGIC]", "It works on my machine! [\\o/]", "Dark mode is the only way [B-)]", "404: My humor was not found [?]", "It's not a bug, it's an undocumented feature [B-)]", "Keyboard not found: Press F1 to continue [?]", "Coffee + Java = This Launcher [Logo_Not_Found]", "Alpha 1.2.6 was better [v]", "Ssssssssss... BOOM! [TNT]", "Square world, round mind [[]]", "If you see Herobrine, tell him he owes me $10 [xD]", "Creeper just wants a hug... a bit too much [BOOM]", "Digging straight down: Best idea ever [RIP]", "Endermen aren't shy, they just want your eyes [O_O]", "Legacy versions are the best [old]", "Bring back the old cobble! [gray]", "Why did the chicken cross the road? To load the next chunk [o_o]", "GoxTheGoat: The true GOAT [B-)]", "Running on 32-bit architecture [x86]", "Solahdine is coding... [Zzz]", "AES-256-GCM Encryption active [SECURE]", "Assets.zip.001 found [LOAD]", "Merging files like a boss [JOIN]", "FatJarAutoForge power [JAR]", "Gield Binary is watching [X]", "Eclipse Neon (je suis 32bit) [i386]", "100% Gluten Free [OK]", "Minecraft Launcher Edition: Gox [v1]"};
      VERSIONS = new LinkedHashMap();
      VERSIONS.put("1.12.2", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.12.2.jar", "net.minecraft.client.main.Main", "1.12"));
      VERSIONS.put("1.12", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.12.jar", "net.minecraft.client.main.Main", "1.12"));
      VERSIONS.put("1.9.4", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.9.4.jar", "net.minecraft.client.main.Main", "1.9"));
      VERSIONS.put("1.8.9", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.8.9.jar", "net.minecraft.client.main.Main", "1.8"));
      VERSIONS.put("1.7.10", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.7.10.jar", "net.minecraft.client.main.Main", "1.7.10"));
      VERSIONS.put("1.7.2", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.7.2.jar", "net.minecraft.client.main.Main", "1.7.10"));
      VERSIONS.put("1.6.4", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.6.4.jar", "net.minecraft.client.main.Main", "legacy"));
      VERSIONS.put("1.5.2", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.5.2.jar", "net.minecraft.client.Minecraft", "pre-1.6"));
      VERSIONS.put("1.5", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.5.jar", "net.minecraft.client.Minecraft", "pre-1.6"));
      VERSIONS.put("1.4.7", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.4.7.jar", "net.minecraft.client.Minecraft", "pre-1.6"));
      VERSIONS.put("1.0", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.0.jar", "net.minecraft.client.Minecraft", "pre-1.6"));
      VERSIONS.put("b1.7.3", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/b1.7.3.jar", "net.minecraft.client.Minecraft", "pre-1.6"));
      VERSIONS.put("a1.2.6", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/a1.2.6.jar", "net.minecraft.client.Minecraft", "pre-1.6"));
      VERSIONS.put("a1.1.1", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/a1.1.1.jar", "net.minecraft.client.Minecraft", "pre-1.6"));
      VERSIONS.put("UNKNOWN", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/ERROR422.jar", "com.jdotsoft.jarloader.ERR422Loader", "legacy"));
   }

   public static void main(String[] args) {
      System.setProperty("https.protocols", "TLSv1.2");
      (new File(VERSION_DIR)).mkdirs();
      (new File(LIB_DIR)).mkdirs();
      (new File(NATIVES_DIR)).mkdirs();
      loadConfig();
      initMainLauncher();
   }

   private static void styleRGBButton(final JButton btn, final Color primaryColor) {
      btn.setFont(new Font("Consolas", 1, 14));
      btn.setForeground(primaryColor);
      btn.setBackground(new Color(20, 20, 20));
      btn.setFocusPainted(false);
      btn.setBorder(BorderFactory.createLineBorder(primaryColor, 2));
      btn.setContentAreaFilled(false);
      btn.setOpaque(true);
      btn.setPreferredSize(new Dimension(150, 40));
      btn.addMouseListener(new MouseAdapter() {
         public void mouseEntered(MouseEvent e) {
            btn.setBackground(primaryColor);
            btn.setForeground(Color.BLACK);
         }

         public void mouseExited(MouseEvent e) {
            btn.setBackground(new Color(20, 20, 20));
            btn.setForeground(primaryColor);
         }
      });
   }

   private static void loadConfig() {
      File f = new File(CONFIG_FILE);
      if (f.exists()) {
         try {
            Throwable var1 = null;
            Object var2 = null;

            try {
               BufferedReader br = new BufferedReader(new FileReader(f));

               try {
                  String savedName = br.readLine();
                  String savedCheck = br.readLine();
                  if (savedName != null && !savedName.isEmpty()) {
                     finalUsername = savedName;
                     if (userField != null) {
                        userField.setText(finalUsername);
                     }
                  }

                  if (savedCheck != null) {
                     saveUsername = Boolean.parseBoolean(savedCheck);
                  }
               } finally {
                  if (br != null) {
                     br.close();
                  }

               }
            } catch (Throwable var13) {
               if (var1 == null) {
                  var1 = var13;
               } else if (var1 != var13) {
                  var1.addSuppressed(var13);
               }

               throw var1;
            }
         } catch (Exception var14) {
         }
      }

   }

   private static void initMainLauncher() {
      mainFrame = new JFrame("Ultimate Launcher - v1.1(x86)");
      mainFrame.setSize(900, 550);
      mainFrame.setDefaultCloseOperation(3);
      mainFrame.setLocationRelativeTo((Component)null);
      cardLayout = new CardLayout();
      cardPanel = new JPanel(cardLayout);
      JPanel loginPanel = new JPanel(new GridBagLayout());
      loginPanel.setBackground(new Color(15, 15, 15));
      GridBagConstraints gbc = new GridBagConstraints();
      gbc.insets = new Insets(10, 10, 10, 10);
      JLabel welcomeLabel = new JLabel("UL CORE LOGIN SYSTEM");
      welcomeLabel.setForeground(Color.GREEN);
      welcomeLabel.setFont(new Font("Monospaced", 1, 24));
      gbc.gridx = 0;
      gbc.gridy = 0;
      gbc.gridwidth = 2;
      loginPanel.add(welcomeLabel, gbc);
      userField = new JTextField(finalUsername, 15);
      userField.setBackground(Color.BLACK);
      userField.setForeground(Color.GREEN);
      userField.setCaretColor(Color.GREEN);
      gbc.gridy = 1;
      gbc.gridwidth = 2;
      loginPanel.add(userField, gbc);
      JButton btnPlay = new JButton("PLAY");
      styleRGBButton(btnPlay, Color.GREEN);
      JButton btnOptions = new JButton("Options");
      styleRGBButton(btnOptions, new Color(0, 191, 255));
      gbc.gridy = 2;
      gbc.gridwidth = 1;
      loginPanel.add(btnPlay, gbc);
      gbc.gridx = 1;
      loginPanel.add(btnOptions, gbc);
      JPanel consolePanel = new JPanel(new BorderLayout(5, 5));
      consolePanel.setBackground(new Color(15, 15, 15));
      consolePanel.setBorder(new EmptyBorder(10, 10, 10, 10));
      JLabel titleLabel = new JLabel("UL CORE  SYSTEM  V1.1");
      titleLabel.setForeground(new Color(0, 255, 0));
      titleLabel.setFont(new Font("Monospaced", 1, 22));
      consolePanel.add(titleLabel, "North");
      logArea = new JTextArea();
      logArea.setEditable(false);
      logArea.setBackground(Color.BLACK);
      logArea.setForeground(new Color(0, 255, 0));
      logArea.setFont(new Font("Verdana", 0, 12));
      ((DefaultCaret)logArea.getCaret()).setUpdatePolicy(2);
      consolePanel.add(new JScrollPane(logArea), "Center");
      JPanel southPanel = new JPanel(new BorderLayout());
      southPanel.setOpaque(false);
      JPanel leftButtons = new JPanel(new FlowLayout(0));
      leftButtons.setOpaque(false);
      btnDir = new JButton("Open Folder");
      styleRGBButton(btnDir, Color.YELLOW);
      btnClean = new JButton("Clean Cache");
      styleRGBButton(btnClean, Color.RED);
      leftButtons.add(btnDir);
      leftButtons.add(btnClean);
      JPanel rightButtons = new JPanel(new FlowLayout(2));
      rightButtons.setOpaque(false);
      versionList = new JComboBox((String[])VERSIONS.keySet().toArray(new String[0]));
      ramList = new JComboBox(new String[]{"512M", "1G"});
      btnLaunch = new JButton("LAUNCH GAME");
      styleRGBButton(btnLaunch, Color.GREEN);
      rightButtons.add(versionList);
      rightButtons.add(ramList);
      rightButtons.add(btnLaunch);
      southPanel.add(leftButtons, "West");
      southPanel.add(rightButtons, "East");
      consolePanel.add(southPanel, "South");
      cardPanel.add(loginPanel, "LOGIN");
      cardPanel.add(consolePanel, "CONSOLE");
      mainFrame.add(cardPanel);
      btnPlay.addActionListener((e) -> {
         finalUsername = userField.getText().trim();
         if (finalUsername.isEmpty()) {
            JOptionPane.showMessageDialog(mainFrame, "Enter a valid username!");
         } else {
            if (saveUsername) {
               saveConfig();
            }

            cardLayout.show(cardPanel, "CONSOLE");
         }
      });
      btnOptions.addActionListener((e) -> {
         showOptionsDialog();
      });
      btnLaunch.addActionListener((e) -> {
         (new Thread(() -> {
            logArea.setText("");
            File nDir = new File(NATIVES_DIR);
            log("INFO", "Ready System");
            deleteDir(nDir);
            if (!nDir.exists()) {
               nDir.mkdirs();
            }

            SwingUtilities.invokeLater(() -> {
               btnLaunch.setEnabled(false);
               btnClean.setEnabled(false);
               btnDir.setEnabled(false);
            });
            log("SYSTEM", "Starting New Session...");

            try {
               preparerEtLancer();
            } catch (IOException var5) {
               log("ERROR", var5.getMessage());
               var5.printStackTrace();
            } finally {
               SwingUtilities.invokeLater(() -> {
                  btnLaunch.setEnabled(true);
                  btnClean.setEnabled(true);
                  btnDir.setEnabled(true);
               });
            }

         })).start();
      });
      btnClean.addActionListener((e) -> {
         cleanCache();
      });
      btnDir.addActionListener((e) -> {
         try {
            Desktop.getDesktop().open(new File(GAME_DIR));
         } catch (Exception var2) {
            log("ERROR", var2.getMessage());
         }

      });
      mainFrame.setVisible(true);
      loadConfig();
      log("INFO", "Ready System");
      Random rand = new Random();
      String splash = SPLASH_TEXTS[rand.nextInt(SPLASH_TEXTS.length)];
      log("SPLASH", "----------------------------------------------------------------------------------------------------");
      log("SPLASH", ">>  " + splash + "  <<");
      log("SPLASH", "----------------------------------------------------------------------------------------------------");
   }

   private static void saveConfig() {
      try {
         Throwable var0 = null;
         Object var1 = null;

         try {
            PrintWriter pw = new PrintWriter(new File(CONFIG_FILE));

            try {
               pw.println(userField.getText());
               pw.println(saveUsername);
            } finally {
               if (pw != null) {
                  pw.close();
               }

            }
         } catch (Throwable var10) {
            if (var0 == null) {
               var0 = var10;
            } else if (var0 != var10) {
               var0.addSuppressed(var10);
            }

            throw var0;
         }
      } catch (Exception var11) {
      }

   }

   private static void showOptionsDialog() {
      JDialog dialog = new JDialog(mainFrame, "System Options", true);
      dialog.setLayout(new FlowLayout());
      dialog.setSize(250, 150);
      dialog.setLocationRelativeTo(mainFrame);
      dialog.getContentPane().setBackground(new Color(30, 30, 30));
      JCheckBox checkSave = new JCheckBox("Save Username", saveUsername);
      checkSave.setForeground(Color.GREEN);
      checkSave.setOpaque(false);
      JButton btnOk = new JButton("OK");
      btnOk.addActionListener((e) -> {
         saveUsername = checkSave.isSelected();
         dialog.dispose();
      });
      dialog.add(checkSave);
      dialog.add(btnOk);
      dialog.setVisible(true);
   }

   private static void cleanCache() {
      int res = JOptionPane.showConfirmDialog(mainFrame, "Delete libs, natives and assets?", "System", 0);
      if (res == 0) {
         deleteDir(new File(LIB_DIR));
         deleteDir(new File(NATIVES_DIR));
         deleteDir(new File(ASSETS_DIR));
         (new File(LIB_DIR)).mkdirs();
         (new File(NATIVES_DIR)).mkdirs();
         (new File(ASSETS_DIR)).mkdirs();
         log("SYSTEM", "Cache cleared.");
      }

   }

   private static void deleteDir(File dir) {
      File[] files = dir.listFiles();
      if (files != null) {
         File[] arrayOfFile = files;
         int i = files.length;

         for(byte b = 0; b < i; ++b) {
            File f = arrayOfFile[b];
            if (f.isDirectory()) {
               deleteDir(f);
            } else {
               f.delete();
            }
         }
      }

      dir.delete();
   }

   private static void preparerEtLancer() throws IOException {
      String sel = (String)versionList.getSelectedItem();
      GameVersion cfg = (GameVersion)VERSIONS.get(sel);
      File gDir = new File(GAME_DIR);
      File RPacks = new File(RESOURCESPACKS_DIR);
      File AssetFile = new File(gDir, "Assets.zip");
      if (!AssetFile.exists()) {
         try {
            downloadAndMergeAssets(AssetFile);
         } catch (IOException var28) {
            log("ERROR", "Asset Error: " + var28.getMessage());
         }
      }

      String rawUrl = "https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/NewDefault+v1.80[MC1.6.1-1.8.9].zip";
      String encodedUrl = rawUrl.replace("[", ".").replace("]", "").replace("+", "+");
      String rawUrl2 = "https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/NewDefault+v1.80[MC1.9-1.12.2].zip";
      String encodedUrl2 = rawUrl2.replace("[", ".").replace("]", "").replace("+", "+");
      File AssetDir;
      File jar;
      if (!RPacks.exists()) {
         RPacks.mkdirs();
         AssetDir = new File(RPacks, "NewDefaultPlusv1.80_MC1.6.1-1.8.9_.zip");
         downloadFile(encodedUrl, AssetDir);
         jar = new File(RPacks, "NewDefaultPlusv1.80_MC1.9-1.12.2_.zip");
         downloadFile(encodedUrl2, jar);
      }

      AssetDir = new File(ASSETS_DIR);
      extractAssets(AssetFile, AssetDir);
      jar = new File(VERSION_DIR + File.separator + (sel.equals("UNKNOWN") ? "ERROR422" : sel) + ".jar");
      if (!jar.exists()) {
         downloadFile(cfg.url, jar);
      }

      String mainClass = cfg.mainClass;
      if (!sel.startsWith("a") && !sel.startsWith("b") && !sel.matches("1\\.[0-5](\\..*)?")) {
         if (sel.equals("UNKNOWN")) {
            mainClass = "com.jdotsoft.jarloader.ERR422Loader";
         } else {
            mainClass = "net.minecraft.client.main.Main";
         }
      } else {
         mainClass = "net.minecraft.client.Minecraft";
      }

      File lDir = new File(LIB_DIR);
      File vDir = new File(VERSION_DIR);
      File nDir = new File(NATIVES_DIR);
      vDir.mkdirs();
      nDir.mkdirs();
      log("SYSTEM", "Checking libraries for " + sel);
      String[][] req = new String[][]{{"authlib-1.5.25.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar", "commons-codec-1.10.jar", "commons-compress-1.8.1.jar", "commons-io-2.5.jar", "commons-lang3-3.5.jar", "commons-logging-1.1.3.jar", "fastutil-7.1.0.jar", "gson-2.8.0.jar", "guava-21.0.jar", "httpclient-4.3.3.jar", "httpcore-4.3.2.jar", "icu4j-core-mojang-51.2.jar", "java-objc-bridge-1.0.0.jar", "java-objc-bridge-1.0.0-natives-osx.jar", "jinput-2.0.5.jar", "jinput-platform-2.0.5-natives-linux.jar", "jinput-platform-2.0.5-natives-osx.jar", "jinput-platform-2.0.5-natives-windows.jar", "jna-4.4.0.jar", "jopt-simple-5.0.3.jar", "jutils-1.0.0.jar", "libraryjavasound-20101123.jar", "librarylwjglopenal-20100824.jar", "log4j-api-2.8.1.jar", "log4j-core-2.8.1.jar", "lwjgl_util-2.9.2-nightly-20140822.jar", "lwjgl_util-2.9.4-nightly-20150209.jar", "lwjgl-2.9.2-nightly-20140822.jar", "lwjgl-2.9.4-nightly-20150209.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-linux.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-osx.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-windows.jar", "lwjgl-platform-2.9.4-nightly-20150209.jar", "lwjgl-platform-2.9.4-nightly-20150209-natives-linux.jar", "lwjgl-platform-2.9.4-nightly-20150209-natives-osx.jar", "lwjgl-platform-2.9.4-nightly-20150209-natives-windows.jar", "netty-all-4.1.9.Final.jar", "oshi-core-1.1.jar", "patchy-1.3.9.jar", "platform-3.4.0.jar", "realms-1.10.22.jar", "soundsystem-20120107.jar", "text2speech-1.10.3.jar", "text2speech-1.10.3-natives-linux.jar", "text2speech-1.10.3-natives-windows.jar"}, {"authlib-1.5.25.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar", "commons-codec-1.10.jar", "commons-compress-1.8.1.jar", "commons-io-2.5.jar", "commons-lang3-3.5.jar", "commons-logging-1.1.3.jar", "fastutil-7.1.0.jar", "gson-2.8.0.jar", "guava-21.0.jar", "httpclient-4.3.3.jar", "httpcore-4.3.2.jar", "icu4j-core-mojang-51.2.jar", "java-objc-bridge-1.0.0.jar", "java-objc-bridge-1.0.0-natives-osx.jar", "jinput-2.0.5.jar", "jinput-platform-2.0.5-natives-linux.jar", "jinput-platform-2.0.5-natives-osx.jar", "jinput-platform-2.0.5-natives-windows.jar", "jna-4.4.0.jar", "jopt-simple-5.0.3.jar", "jutils-1.0.0.jar", "libraryjavasound-20101123.jar", "librarylwjglopenal-20100824.jar", "log4j-api-2.8.1.jar", "log4j-core-2.8.1.jar", "lwjgl_util-2.9.2-nightly-20140822.jar", "lwjgl_util-2.9.4-nightly-20150209.jar", "lwjgl-2.9.2-nightly-20140822.jar", "lwjgl-2.9.4-nightly-20150209.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-linux.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-osx.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-windows.jar", "lwjgl-platform-2.9.4-nightly-20150209.jar", "lwjgl-platform-2.9.4-nightly-20150209-natives-linux.jar", "lwjgl-platform-2.9.4-nightly-20150209-natives-osx.jar", "lwjgl-platform-2.9.4-nightly-20150209-natives-windows.jar", "netty-all-4.1.9.Final.jar", "oshi-core-1.1.jar", "patchy-1.3.9.jar", "platform-3.4.0.jar", "realms-1.10.17.jar", "soundsystem-20120107.jar", "text2speech-1.10.3.jar", "text2speech-1.10.3-natives-linux.jar", "text2speech-1.10.3-natives-windows.jar"}, {"authlib-1.5.22.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar", "commons-codec-1.9.jar", "commons-compress-1.8.1.jar", "commons-io-2.4.jar", "commons-lang3-3.3.2.jar", "commons-logging-1.1.3.jar", "fastutil-7.0.12_mojang.jar", "gson-2.2.4.jar", "guava-17.0.jar", "httpclient-4.3.3.jar", "httpcore-4.3.2.jar", "icu4j-core-mojang-51.2.jar", "jinput-2.0.5.jar", "jinput-platform-2.0.5-natives-linux.jar", "jinput-platform-2.0.5-natives-osx.jar", "jinput-platform-2.0.5-natives-windows.jar", "jna-3.4.0.jar", "jopt-simple-4.6.jar", "jutils-1.0.0.jar", "libraryjavasound-20101123.jar", "librarylwjglopenal-20100824.jar", "log4j-api-2.0-beta9.jar", "log4j-core-2.0-beta9.jar", "lwjgl_util-2.9.2-nightly-20140822.jar", "lwjgl_util-2.9.4-nightly-20150209.jar", "lwjgl-2.9.2-nightly-20140822.jar", "lwjgl-2.9.4-nightly-20150209.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-linux.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-osx.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-windows.jar", "lwjgl-platform-2.9.4-nightly-20150209.jar", "lwjgl-platform-2.9.4-nightly-20150209-natives-linux.jar", "lwjgl-platform-2.9.4-nightly-20150209-natives-osx.jar", "lwjgl-platform-2.9.4-nightly-20150209-natives-windows.jar", "netty-1.8.8.jar", "netty-all-4.0.23.Final.jar", "oshi-core-1.1.jar", "platform-3.4.0.jar", "realms-1.8.19.jar", "soundsystem-20120107.jar"}, {"authlib-1.5.22.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar", "commons-codec-1.9.jar", "commons-compress-1.8.1.jar", "commons-io-2.4.jar", "commons-lang3-3.3.2.jar", "commons-logging-1.1.3.jar", "gson-2.2.4.jar", "guava-17.0.jar", "httpclient-4.3.3.jar", "httpcore-4.3.2.jar", "icu4j-core-mojang-51.2.jar", "jinput-2.0.5.jar", "jinput-platform-2.0.5-natives-linux.jar", "jinput-platform-2.0.5-natives-osx.jar", "jinput-platform-2.0.5-natives-windows.jar", "jna-3.4.0.jar", "jopt-simple-4.6.jar", "jutils-1.0.0.jar", "libraryjavasound-20101123.jar", "librarylwjglopenal-20100824.jar", "log4j-api-2.0-beta9.jar", "log4j-core-2.0-beta9.jar", "lwjgl_util-2.9.2-nightly-20140822.jar", "lwjgl_util-2.9.4-nightly-20150209.jar", "lwjgl-2.9.2-nightly-20140822.jar", "lwjgl-2.9.4-nightly-20150209.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-linux.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-osx.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-windows.jar", "lwjgl-platform-2.9.4-nightly-20150209.jar", "lwjgl-platform-2.9.4-nightly-20150209-natives-linux.jar", "lwjgl-platform-2.9.4-nightly-20150209-natives-osx.jar", "lwjgl-platform-2.9.4-nightly-20150209-natives-windows.jar", "netty-1.8.8.jar", "netty-all-4.0.23.Final.jar", "oshi-core-1.1.jar", "platform-3.4.0.jar", "realms-1.8.7.jar", "soundsystem-20120107.jar"}, {"authlib-1.5.21.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar", "commons-codec-1.9.jar", "commons-compress-1.8.1.jar", "commons-io-2.4.jar", "commons-lang3-3.3.2.jar", "commons-logging-1.1.3.jar", "gson-2.2.4.jar", "guava-17.0.jar", "httpclient-4.3.3.jar", "httpcore-4.3.2.jar", "icu4j-core-mojang-51.2.jar", "jinput-2.0.5.jar", "jinput-platform-2.0.5-natives-linux.jar", "jinput-platform-2.0.5-natives-osx.jar", "jinput-platform-2.0.5-natives-windows.jar", "jna-3.4.0.jar", "jopt-simple-4.6.jar", "jutils-1.0.0.jar", "libraryjavasound-20101123.jar", "librarylwjglopenal-20100824.jar", "log4j-api-2.0-beta9.jar", "log4j-core-2.0-beta9.jar", "lwjgl_util-2.9.2-nightly-20140822.jar", "lwjgl_util-2.9.4-nightly-20150209.jar", "lwjgl-2.9.2-nightly-20140822.jar", "lwjgl-2.9.4-nightly-20150209.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-linux.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-osx.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-windows.jar", "lwjgl-platform-2.9.4-nightly-20150209.jar", "lwjgl-platform-2.9.4-nightly-20150209-natives-linux.jar", "lwjgl-platform-2.9.4-nightly-20150209-natives-osx.jar", "lwjgl-platform-2.9.4-nightly-20150209-natives-windows.jar", "netty-1.8.8.jar", "netty-all-4.0.23.Final.jar", "oshi-core-1.1.jar", "platform-3.4.0.jar", "realms-1.7.59.jar", "soundsystem-20120107.jar", "twitch-6.5.jar", "twitch-external-platform-4.5-natives-windows-32.jar", "twitch-external-platform-4.5-natives-windows-64.jar", "twitch-platform-6.5-natives-osx.jar", "twitch-platform-6.5-natives-windows-32.jar", "twitch-platform-6.5-natives-windows-64.jar"}, {"authlib-1.5.21.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar", "commons-codec-1.9.jar", "commons-compress-1.8.1.jar", "commons-io-2.4.jar", "commons-lang3-3.3.2.jar", "commons-logging-1.1.3.jar", "gson-2.2.4.jar", "guava-17.0.jar", "httpclient-4.3.3.jar", "httpcore-4.3.2.jar", "icu4j-core-mojang-51.2.jar", "jinput-2.0.5.jar", "jinput-platform-2.0.5-natives-linux.jar", "jinput-platform-2.0.5-natives-osx.jar", "jinput-platform-2.0.5-natives-windows.jar", "jopt-simple-4.6.jar", "jutils-1.0.0.jar", "libraryjavasound-20101123.jar", "librarylwjglopenal-20100824.jar", "log4j-api-2.0-beta9.jar", "log4j-core-2.0-beta9.jar", "lwjgl_util-2.9.1.jar", "lwjgl_util-2.9.2-nightly-20140822.jar", "lwjgl-2.9.1.jar", "lwjgl-2.9.2-nightly-20140822.jar", "lwjgl-platform-2.9.1-natives-linux.jar", "lwjgl-platform-2.9.1-natives-osx.jar", "lwjgl-platform-2.9.1-natives-windows.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-linux.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-osx.jar", "lwjgl-platform-2.9.2-nightly-20140822-natives-windows.jar", "netty-1.8.8.jar", "netty-all-4.0.15.Final.jar", "realms-1.6.1.jar", "soundsystem-20120107.jar", "trove4j-3.0.3.jar", "twitch-6.5.jar", "twitch-external-platform-4.5-natives-windows-32.jar", "twitch-external-platform-4.5-natives-windows-64.jar", "twitch-platform-6.5-natives-osx.jar", "twitch-platform-6.5-natives-windows-32.jar", "twitch-platform-6.5-natives-windows-64.jar", "vecmath-1.5.2.jar"}, {"authlib-1.5.21.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar", "commons-codec-1.9.jar", "commons-compress-1.8.1.jar", "commons-io-2.4.jar", "commons-lang3-3.1.jar", "commons-logging-1.1.3.jar", "gson-2.2.4.jar", "guava-15.0.jar", "httpclient-4.3.3.jar", "httpcore-4.3.2.jar", "icu4j-core-mojang-51.2.jar", "jinput-2.0.5.jar", "jinput-platform-2.0.5-natives-linux.jar", "jinput-platform-2.0.5-natives-osx.jar", "jinput-platform-2.0.5-natives-windows.jar", "jopt-simple-4.5.jar", "jutils-1.0.0.jar", "libraryjavasound-20101123.jar", "librarylwjglopenal-20100824.jar", "log4j-api-2.0-beta9.jar", "log4j-core-2.0-beta9.jar", "lwjgl_util-2.9.1.jar", "lwjgl-2.9.1.jar", "lwjgl-platform-2.9.1-natives-linux.jar", "lwjgl-platform-2.9.1-natives-osx.jar", "lwjgl-platform-2.9.1-natives-windows.jar", "netty-1.8.8.jar", "netty-all-4.0.10.Final.jar", "realms-1.3.5.jar", "soundsystem-20120107.jar", "trove4j-3.0.3.jar", "twitch-5.16.jar", "twitch-external-platform-4.5-natives-windows-32.jar", "twitch-external-platform-4.5-natives-windows-64.jar", "twitch-platform-5.16-natives-osx.jar", "twitch-platform-5.16-natives-windows-32.jar", "twitch-platform-5.16-natives-windows-64.jar", "vecmath-1.3.1.jar"}, {"authlib-1.3.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar", "commons-io-2.4.jar", "commons-lang3-3.1.jar", "gson-2.2.4.jar", "guava-15.0.jar", "icu4j-core-mojang-51.2.jar", "jinput-2.0.5.jar", "jinput-platform-2.0.5-natives-linux.jar", "jinput-platform-2.0.5-natives-osx.jar", "jinput-platform-2.0.5-natives-windows.jar", "jopt-simple-4.5.jar", "jutils-1.0.0.jar", "libraryjavasound-20101123.jar", "librarylwjglopenal-20100824.jar", "log4j-api-2.0-beta9.jar", "log4j-core-2.0-beta9.jar", "lwjgl_util-2.9.0.jar", "lwjgl_util-2.9.1-nightly-20131017.jar", "lwjgl-2.9.0.jar", "lwjgl-2.9.1-nightly-20131017.jar", "lwjgl-platform-2.9.0-natives-linux.jar", "lwjgl-platform-2.9.0-natives-osx.jar", "lwjgl-platform-2.9.0-natives-windows.jar", "lwjgl-platform-2.9.1-nightly-20131017-natives-osx.jar", "netty-1.8.8.jar", "netty-all-4.0.10.Final.jar", "soundsystem-20120107.jar", "trove4j-3.0.3.jar", "vecmath-1.3.1.jar"}, {"argo-2.25_fixed.jar", "bcprov-jdk15on-1.47.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar", "commons-io-2.4.jar", "commons-lang3-3.1.jar", "gson-2.2.2.jar", "guava-14.0.jar", "jinput-2.0.5.jar", "jinput-platform-2.0.5-natives-linux.jar", "jinput-platform-2.0.5-natives-osx.jar", "jinput-platform-2.0.5-natives-windows.jar", "jopt-simple-4.5.jar", "jutils-1.0.0.jar", "libraryjavasound-20101123.jar", "librarylwjglopenal-20100824.jar", "lwjgl_util-2.9.0.jar", "lwjgl_util-2.9.1-nightly-20130708-debug3.jar", "lwjgl-2.9.0.jar", "lwjgl-2.9.1-nightly-20130708-debug3.jar", "lwjgl-platform-2.9.0-natives-linux.jar", "lwjgl-platform-2.9.0-natives-osx.jar", "lwjgl-platform-2.9.0-natives-windows.jar", "lwjgl-platform-2.9.1-nightly-20130708-debug3-natives-osx.jar", "soundsystem-20120107.jar"}, {"asm-all-4.1.jar", "jinput-2.0.5.jar", "jinput-platform-2.0.5-natives-linux.jar", "jinput-platform-2.0.5-natives-osx.jar", "jinput-platform-2.0.5-natives-windows.jar", "jopt-simple-4.5.jar", "jutils-1.0.0.jar", "launchwrapper-1.5.jar", "lwjgl_util-2.9.0.jar", "lwjgl_util-2.9.1-nightly-20130708-debug3.jar", "lwjgl-2.9.0.jar", "lwjgl-2.9.1-nightly-20130708-debug3.jar", "lwjgl-platform-2.9.0-natives-linux.jar", "lwjgl-platform-2.9.0-natives-osx.jar", "lwjgl-platform-2.9.0-natives-windows.jar", "lwjgl-platform-2.9.1-nightly-20130708-debug3-natives-osx.jar"}, {"exe4jlib.jar"}};
      String[] currentLibs = null;
      if (sel.equals("1.12.2")) {
         currentLibs = req[0];
      } else if (sel.equals("1.12")) {
         currentLibs = req[1];
      } else if (sel.equals("1.9.4")) {
         currentLibs = req[2];
      } else if (sel.equals("1.9")) {
         currentLibs = req[3];
      } else if (sel.equals("1.8.9")) {
         currentLibs = req[4];
      } else if (sel.equals("1.8")) {
         currentLibs = req[5];
      } else if (sel.equals("1.7.10")) {
         currentLibs = req[6];
      } else if (sel.equals("1.7.2")) {
         currentLibs = req[7];
      } else if (sel.equals("1.6.4")) {
         currentLibs = req[8];
      } else if (!sel.startsWith("a") && !sel.startsWith("b") && !sel.matches("1\\.[0-5](\\..*)?")) {
         currentLibs = req[9];
      } else {
         currentLibs = req[10];
      }

      String[] var20 = currentLibs;
      int var19 = currentLibs.length;

      String os;
      File assetFolder;
      for(int var18 = 0; var18 < var19; ++var18) {
         os = var20[var18];
         assetFolder = new File(lDir, os);
         if (assetFolder.getParentFile() != null) {
            assetFolder.getParentFile().mkdirs();
         }

         if (!assetFolder.exists() || assetFolder.length() == 0L) {
            downloadFile("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/" + os, assetFolder);
         }
      }

      os = System.getProperty("os.name").toLowerCase().contains("win") ? "windows" : "linux";
      log("SYSTEM", "Checking natives for " + os);
      File libFolder = new File(LIB_DIR);
      int var34;
      if (libFolder.exists() && libFolder.listFiles() != null) {
         File[] var22;
         var34 = (var22 = libFolder.listFiles()).length;

         for(int var32 = 0; var32 < var34; ++var32) {
            File lib = var22[var32];
            if (lib.getName().contains("natives-" + os) && lib.getName().endsWith(".jar")) {
               log("NATIVES", "Extracted : " + lib.getName());

               try {
                  extraireNatives(lib, nDir);
               } catch (IOException var27) {
                  log("ERROR", var27.getMessage());
                  var27.printStackTrace();
               }
            }
         }
      }

      SwingUtilities.invokeLater(() -> {
         btnLaunch.setEnabled(false);
      });
      log("SYSTEM", "Starting " + sel);
      StringBuilder cp = new StringBuilder();
      cp.append(jar.getAbsolutePath()).append(";");
      String[] var23 = currentLibs;
      int var35 = currentLibs.length;

      String finalCp;
      for(var34 = 0; var34 < var35; ++var34) {
         finalCp = var23[var34];
         File f = new File(LIB_DIR, finalCp);
         if (f.exists()) {
            cp.append(f.getAbsolutePath()).append(";");
         } else {
            log("ERROR", "Missing library: " + finalCp);
         }
      }

      finalCp = cp.toString();
      if (finalCp.endsWith(";")) {
         finalCp = finalCp.substring(0, finalCp.length() - 1);
      }

      assetFolder = new File(ASSETS_DIR);
      if (!assetFolder.exists()) {
         assetFolder.mkdirs();
      }

      try {
         List cmd = new ArrayList();
         cmd.add("java");
         cmd.add("-Xmx" + ramList.getSelectedItem());
         cmd.add("-Djava.library.path=" + nDir.getAbsolutePath());
         cmd.add("-cp");
         cmd.add(finalCp);
         cmd.add(mainClass);
         if (mainClass.equals("net.minecraft.client.main.Main")) {
            cmd.add("--username");
            cmd.add(finalUsername);
            cmd.add("--accessToken");
            cmd.add("0");
            cmd.add("--version");
            cmd.add(sel);
            cmd.add("--gameDir");
            cmd.add(GAME_DIR);
            cmd.add("--assetsDir");
            cmd.add(assetFolder.getAbsolutePath());
            cmd.add("--assetIndex");
            cmd.add(cfg.assetIdx);
            cmd.add("--userProperties");
            cmd.add("{}");
         } else {
            cmd.add(finalUsername);
            cmd.add("0");
         }

         log("DEBUG", "mainClass : " + mainClass);
         log("SYSTEM", "Command: " + cmd.toString());

         try {
            ProcessBuilder pb = new ProcessBuilder(cmd);
            pb.directory(new File(GAME_DIR));
            pb.redirectErrorStream(true);
            Process p = pb.start();
            (new Thread(() -> {
               try {
                  BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));

                  String line;
                  while((line = r.readLine()) != null) {
                     log("GAME_OUTPUT", line);
                  }
               } catch (IOException var6) {
                  log("ERROR", "Crash: " + var6.getMessage());
                  var6.printStackTrace();
               } finally {
                  SwingUtilities.invokeLater(() -> {
                     btnLaunch.setEnabled(true);
                     btnClean.setEnabled(true);
                     btnDir.setEnabled(true);
                  });
               }

            })).start();
         } catch (Exception var25) {
            log("ERROR", var25.getMessage());
            var25.printStackTrace();
         }
      } catch (Throwable var26) {
         log("ERROR", "Coupable trouvé : " + var26.toString());
         var26.printStackTrace();
      }

   }

   private static void log(String level, String msg) {
      String time = (new SimpleDateFormat("HH:mm:ss")).format(new Date());
      SwingUtilities.invokeLater(() -> {
         logArea.append("[" + time + " " + level + "] > " + msg + "\n");
      });
   }

   private static void extractAssets(File zipFile, File targetDir) throws IOException {
      log("SYSTEM", "Extracting assets...");
      Throwable var2 = null;
      Object var3 = null;

      try {
         ZipInputStream zis = new ZipInputStream(new FileInputStream(zipFile));

         ZipEntry entry;
         try {
            for(; (entry = zis.getNextEntry()) != null; zis.closeEntry()) {
               File newFile = new File(targetDir, entry.getName());
               if (entry.isDirectory()) {
                  newFile.mkdirs();
               } else {
                  newFile.getParentFile().mkdirs();
                  Throwable var7 = null;
                  Object var8 = null;

                  try {
                     FileOutputStream fos = new FileOutputStream(newFile);

                     try {
                        byte[] buffer = new byte[8192];

                        int len;
                        while((len = zis.read(buffer)) > 0) {
                           fos.write(buffer, 0, len);
                        }
                     } finally {
                        if (fos != null) {
                           fos.close();
                        }

                     }
                  } catch (Throwable var29) {
                     if (var7 == null) {
                        var7 = var29;
                     } else if (var7 != var29) {
                        var7.addSuppressed(var29);
                     }

                     throw var7;
                  }
               }
            }
         } finally {
            if (zis != null) {
               zis.close();
            }

         }
      } catch (Throwable var31) {
         if (var2 == null) {
            var2 = var31;
         } else if (var2 != var31) {
            var2.addSuppressed(var31);
         }

         throw var2;
      }

      log("SUCCESS", "Asset succefully extracted");
   }

   private static boolean downloadFile(String urlStr, File target) {
      try {
         URL url = new URL(urlStr);
         HttpURLConnection conn = (HttpURLConnection)url.openConnection();
         conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
         if (conn.getResponseCode() != 200) {
            return false;
         } else {
            long fileSize = conn.getContentLengthLong();
            log("DOWNLOAD", "Syncing: " + target.getName() + " (Size: " + fileSize / 1024L + " KB)");
            Throwable var6 = null;
            Object var7 = null;

            try {
               InputStream in = conn.getInputStream();

               try {
                  FileOutputStream out = new FileOutputStream(target);

                  try {
                     byte[] buf = new byte[8192];
                     long downloaded = 0L;
                     int lastPercent = -1;

                     int n;
                     while((n = in.read(buf)) != -1) {
                        out.write(buf, 0, n);
                        downloaded += (long)n;
                        if (fileSize > 0L) {
                           int percent = (int)(downloaded * 100L / fileSize);
                           if (percent != lastPercent) {
                              updateLastLogLine("PROGRESS > " + target.getName() + "[" + percent + "%]");
                              lastPercent = percent;
                           }
                        }
                     }
                  } finally {
                     if (out != null) {
                        out.close();
                     }

                  }
               } catch (Throwable var29) {
                  if (var6 == null) {
                     var6 = var29;
                  } else if (var6 != var29) {
                     var6.addSuppressed(var29);
                  }

                  if (in != null) {
                     in.close();
                  }

                  throw var6;
               }

               if (in != null) {
                  in.close();
               }
            } catch (Throwable var30) {
               if (var6 == null) {
                  var6 = var30;
               } else if (var6 != var30) {
                  var6.addSuppressed(var30);
               }

               throw var6;
            }

            log("SYSTEM", "Download complete: " + target.getName());
            return true;
         }
      } catch (Exception var31) {
         log("DEBUG", "Download failed for: " + urlStr + " : " + var31.getMessage());
         var31.printStackTrace();
         return false;
      }
   }

   private static void updateLastLogLine(String newText) {
      SwingUtilities.invokeLater(() -> {
         try {
            int lineCount = logArea.getLineCount();
            if (lineCount > 0) {
               int start = logArea.getLineStartOffset(lineCount - 1);
               int end = logArea.getLineEndOffset(lineCount - 1);
               logArea.replaceRange("[" + (new SimpleDateFormat("HH:mm:ss")).format(new Date()) + " INFO] > " + newText, start, end);
            }
         } catch (Exception var4) {
            log("INFO", newText);
         }

      });
   }

   private static void downloadAndMergeAssets(File targetFile) throws IOException {
      String baseUrl = "https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/Assets.zip.";
      List parts = new ArrayList();
      int partNum = 1;
      log("SYSTEM", "Downloading Assets's Parts...");

      while(true) {
         String ext = String.format("%03d", partNum);
         File partFile = new File(targetFile.getParentFile(), "Assets.zip." + ext);
         String url = baseUrl + ext;
         boolean success = downloadFileSafe(url, partFile);
         if (!success) {
            if (partNum == 1) {
               throw new IOException("Impossible de trouver la partie " + ext + " sur GitHub.");
            } else {
               log("SYSTEM", "Fusion of " + parts.size() + " parts...");
               Throwable var34 = null;
               partFile = null;

               try {
                  FileOutputStream fos = new FileOutputStream(targetFile);

                  File part;
                  try {
                     for(Iterator var8 = parts.iterator(); var8.hasNext(); part.delete()) {
                        part = (File)var8.next();
                        Throwable var9 = null;
                        Object var10 = null;

                        try {
                           FileInputStream fis = new FileInputStream(part);

                           try {
                              byte[] buffer = new byte[8192];

                              int read;
                              while((read = fis.read(buffer)) > 0) {
                                 fos.write(buffer, 0, read);
                              }
                           } finally {
                              if (fis != null) {
                                 fis.close();
                              }

                           }
                        } catch (Throwable var31) {
                           if (var9 == null) {
                              var9 = var31;
                           } else if (var9 != var31) {
                              var9.addSuppressed(var31);
                           }

                           throw var9;
                        }
                     }
                  } finally {
                     if (fos != null) {
                        fos.close();
                     }

                  }
               } catch (Throwable var33) {
                  if (var34 == null) {
                     var34 = var33;
                  } else if (var34 != var33) {
                     var34.addSuppressed(var33);
                  }

                  throw var34;
               }

               log("SUCCESS", "Finish succefully Parts's fusion !");
               return;
            }
         }

         parts.add(partFile);
         ++partNum;
      }
   }

   private static boolean downloadFileSafe(String urlStr, File target) {
      try {
         URL url = new URL(urlStr);
         HttpURLConnection conn = (HttpURLConnection)url.openConnection();
         conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
         conn.setConnectTimeout(10000);
         conn.setReadTimeout(10000);
         if (conn.getResponseCode() != 200) {
            return false;
         } else {
            long fileSize = conn.getContentLengthLong();
            log("DOWNLOAD", "Syncing: " + target.getName() + " (Size: " + fileSize / 1024L + " KB)");
            Throwable var6 = null;
            Object var7 = null;

            try {
               InputStream in = conn.getInputStream();

               try {
                  FileOutputStream out = new FileOutputStream(target);

                  try {
                     byte[] buf = new byte[8192];
                     long downloaded = 0L;
                     int lastPercent = -1;

                     int n;
                     while((n = in.read(buf)) != -1) {
                        out.write(buf, 0, n);
                        downloaded += (long)n;
                        if (fileSize > 0L) {
                           int percent = (int)(downloaded * 100L / fileSize);
                           if (percent != lastPercent) {
                              updateLastLogLine("PROGRESS > " + target.getName() + "[" + percent + "%]");
                              lastPercent = percent;
                           }
                        }
                     }
                  } finally {
                     if (out != null) {
                        out.close();
                     }

                  }
               } catch (Throwable var29) {
                  if (var6 == null) {
                     var6 = var29;
                  } else if (var6 != var29) {
                     var6.addSuppressed(var29);
                  }

                  if (in != null) {
                     in.close();
                  }

                  throw var6;
               }

               if (in != null) {
                  in.close();
               }
            } catch (Throwable var30) {
               if (var6 == null) {
                  var6 = var30;
               } else if (var6 != var30) {
                  var6.addSuppressed(var30);
               }

               throw var6;
            }

            log("SYSTEM", "Download complete: " + target.getName());
            return true;
         }
      } catch (Exception var31) {
         log("DEBUG", "Download failed for: " + urlStr + " : " + var31.getMessage());
         var31.printStackTrace();
         return false;
      }
   }

   private static void extraireNatives(File jarFile, File destinationDir) throws IOException {
      Throwable var2 = null;
      Object var3 = null;

      try {
         ZipInputStream zis = new ZipInputStream(new FileInputStream(jarFile));

         ZipEntry entry;
         try {
            for(; (entry = zis.getNextEntry()) != null; zis.closeEntry()) {
               String name = entry.getName();
               if (name.endsWith(".dll") || name.endsWith(".so") || name.endsWith(".dylib")) {
                  File f = new File(destinationDir, name);
                  if (!f.exists()) {
                     Throwable var8 = null;
                     Object var9 = null;

                     try {
                        FileOutputStream fos = new FileOutputStream(f);

                        try {
                           byte[] buf = new byte[8192];

                           int len;
                           while((len = zis.read(buf)) > 0) {
                              fos.write(buf, 0, len);
                           }
                        } finally {
                           if (fos != null) {
                              fos.close();
                           }

                        }
                     } catch (Throwable var30) {
                        if (var8 == null) {
                           var8 = var30;
                        } else if (var8 != var30) {
                           var8.addSuppressed(var30);
                        }

                        throw var8;
                     }
                  }
               }
            }
         } finally {
            if (zis != null) {
               zis.close();
            }

         }

      } catch (Throwable var32) {
         if (var2 == null) {
            var2 = var32;
         } else if (var2 != var32) {
            var2.addSuppressed(var32);
         }

         throw var2;
      }
   }

   static class GameVersion {
      String url;
      String mainClass;
      String assetIdx;

      GameVersion(String url, String mainClass, String assetIdx) {
         this.url = url;
         this.mainClass = mainClass;
         this.assetIdx = assetIdx;
      }
   }
}
