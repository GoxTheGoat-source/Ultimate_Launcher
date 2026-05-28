package Main;

import java.awt.*;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.List;
import java.util.zip.*;
import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Launcher {
    private static JFrame mainFrame;
    private static JTextArea logArea;
    private static JComboBox<String> versionList, ramList;
    private static JButton btnLaunch;
    private static String finalUsername = "Solahdine"; 
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm:ss");
    
    // --- CHEMINS SYSTÈME ---
    private static final String GAME_DIR = System.getenv("APPDATA") + File.separator + ".minecraft";
    private static final String LIB_DIR = GAME_DIR + File.separator + "libraries";
    private static final String VERSION_DIR = GAME_DIR + File.separator + "versions";
    private static final String NATIVES_DIR = GAME_DIR + File.separator + "bin" + File.separator + "natives";
    private static final String CONFIG_FILE = GAME_DIR + File.separator + "launcher_config.txt";

    static class GameVersion {
        String url, mainClass, assetIdx;
        GameVersion(String url, String mainClass, String assetIdx) {
            this.url = url; this.mainClass = mainClass; this.assetIdx = assetIdx;
        }
    }

    private static final Map<String, GameVersion> VERSIONS = new LinkedHashMap<>();
    static {
        VERSIONS.put("1.12.2", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.12.2.jar","net.minecraft.client.main.Main", "1.12"));
        VERSIONS.put("1.9.4", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.9.4.jar","net.minecraft.client.main.Main","1.9"));
        VERSIONS.put("1.8.9", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.8.9.jar","net.minecraft.client.main.Main","1.8"));
        VERSIONS.put("1.7.10", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.7.10.jar","net.minecraft.client.main.Main","1.7.10"));
        VERSIONS.put("1.7.2", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.7.2.jar","net.minecraft.client.main.Main","1.7.2"));
        VERSIONS.put("1.0", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.0.jar","net.minecraft.client.Minecraft","legacy"));
        VERSIONS.put("b1.7.3", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/b1.7.3.jar","net.minecraft.client.Minecraft","legacy"));
        VERSIONS.put("a1.2.6", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/a1.2.6.jar","net.minecraft.client.Minecraft","legacy"));
        VERSIONS.put("UNKNOWN", new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/ERROR422.jar","com.jdotsoft.jarloader.ERR422Loader","422"));
    }

    public static void main(String[] args) {
        new File(VERSION_DIR).mkdirs();
        new File(LIB_DIR).mkdirs();
        new File(NATIVES_DIR).mkdirs();
        loadConfig();
        initMainLauncher();
    }

    private static void loadConfig() {
        File f = new File(CONFIG_FILE);
        if(f.exists()) {
            try (BufferedReader r = new BufferedReader(new FileReader(f))) {
                finalUsername = r.readLine();
            } catch (Exception e) {}
        }
    }

    private static void initMainLauncher() {
        mainFrame = new JFrame("Ultimate Launcher - v1.5");
        mainFrame.setSize(900, 550);
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBackground(new Color(15, 15, 15));
        mainPanel.setBorder(new EmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel("UL CORE SYSTEM V1.5");
        titleLabel.setForeground(new Color(0, 255, 65));
        titleLabel.setFont(new Font("Monospaced", Font.BOLD, 20));

        logArea = new JTextArea();
        logArea.setEditable(false);
        logArea.setBackground(Color.BLACK);
        logArea.setForeground(new Color(0, 255, 120));
        logArea.setFont(new Font("Verdana", Font.PLAIN, 12));
        JScrollPane scroll = new JScrollPane(logArea);

        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);

        JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
        leftButtons.setOpaque(false);
        JButton btnDir = new JButton("Open Folder");
        JButton btnClean = new JButton("Clean Cache");
        btnDir.addActionListener(e -> { try { Desktop.getDesktop().open(new File(GAME_DIR)); } catch(Exception ex){} });
        btnClean.addActionListener(e -> cleanCache());
        leftButtons.add(btnDir); leftButtons.add(btnClean);

        JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        rightButtons.setOpaque(false);
        versionList = new JComboBox<>(VERSIONS.keySet().toArray(new String[0]));
        ramList = new JComboBox<>(new String[]{"1G", "2G", "4G", "8G"});
        btnLaunch = new JButton("LAUNCH GAME");
        btnLaunch.addActionListener(e -> new Thread(Launcher::preparerEtLancer).start());
        rightButtons.add(versionList); rightButtons.add(ramList); rightButtons.add(btnLaunch);

        footer.add(leftButtons, BorderLayout.WEST); footer.add(rightButtons, BorderLayout.EAST);
        mainPanel.add(titleLabel, BorderLayout.NORTH);
        mainPanel.add(scroll, BorderLayout.CENTER);
        mainPanel.add(footer, BorderLayout.SOUTH);

        mainFrame.add(mainPanel);
        mainFrame.setVisible(true);
        log("SYSTEM", "CORE ONLINE.");
    }

    private static void cleanCache() {
        int res = JOptionPane.showConfirmDialog(mainFrame, "Delete libs/natives?", "System", JOptionPane.YES_NO_OPTION);
        if(res == JOptionPane.YES_OPTION) {
            deleteDir(new File(LIB_DIR)); deleteDir(new File(NATIVES_DIR));
            new File(LIB_DIR).mkdirs(); new File(NATIVES_DIR).mkdirs();
            log("SYSTEM", "Cache cleared.");
        }
    }

    private static void deleteDir(File dir) {
        File[] files = dir.listFiles();
        if(files != null) for(File f : files) { if(f.isDirectory()) deleteDir(f); else f.delete(); }
        dir.delete();
    }

    private static void preparerEtLancer() {
        String sel = (String) versionList.getSelectedItem();
        GameVersion cfg = VERSIONS.get(sel);
        try {
            btnLaunch.setEnabled(false);
            log("SYSTEM", "Starting " + sel);

            File jar = new File(VERSION_DIR + File.separator + (sel.equals("UNKNOWN") ? "ERROR422" : sel) + ".jar");
            if(!jar.exists()) downloadFile(cfg.url, jar);

            String[] req = {
                "lwjgl-2.9.4.jar", "lwjgl_util-2.9.4.jar", "jinput-2.0.5.jar", "natives.jar",
                "authlib-1.5.25.jar", "netty-all-4.1.9.Final.jar", "trove4j-3.0.3.jar",
                "log4j-core-2.8.1.jar", "log4j-api-2.8.1.jar", "gson-2.8.0.jar", "guava-17.0.jar",
                "soundsystem-20120107.jar", "realms-1.10.22.jar", "text2speech-1.10.3.jar",
                "commons-io-2.5.jar", "commons-lang3-3.5.jar", "fastutil-7.1.0.jar","jopt-simple-5.0.3.jar"
            };
            
            for(String s : req) {
                File f = new File(LIB_DIR, s);
                if(!f.exists()) downloadFile("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/" + s, f);
            }

            extraireNatives(new File(LIB_DIR, "natives.jar"), new File(NATIVES_DIR));

            List<String> cmd = new ArrayList<>();
            cmd.add("java"); cmd.add("-Xmx" + ramList.getSelectedItem());
            cmd.add("-Djava.library.path=" + new File(NATIVES_DIR).getAbsolutePath());
            
            StringBuilder cp = new StringBuilder(jar.getAbsolutePath() + File.pathSeparator);
            File[] libs = new File(LIB_DIR).listFiles();
            if(libs != null) for(File f : libs) if(f.getName().endsWith(".jar")) cp.append(f.getAbsolutePath()).append(File.pathSeparator);
            
            cmd.add("-cp"); cmd.add(cp.toString());
            cmd.add(cfg.mainClass);

            if (cfg.mainClass.contains("main.Main")) {
                cmd.add("--username"); cmd.add(finalUsername);
                cmd.add("--version");  cmd.add(sel);
                cmd.add("--gameDir");  cmd.add(new File(GAME_DIR).getAbsolutePath());
                cmd.add("--assetsDir"); cmd.add(new File(GAME_DIR + "/assets").getAbsolutePath());
                cmd.add("--assetIndex"); cmd.add(cfg.assetIdx);
                cmd.add("--accessToken"); cmd.add("0");
            } else {
                cmd.add(finalUsername); cmd.add("0");
            }

            Process p = new ProcessBuilder(cmd).redirectErrorStream(true).start();
            try (BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()))) {
                String line; while ((line = r.readLine()) != null) log("GAME_OUTPUT", line);
            }
        } catch (Exception ex) { log("ERROR", ex.getMessage()); }
        finally { btnLaunch.setEnabled(true); }
    }

    private static void log(String prefix, String m) { 
        String time = TIME_FORMAT.format(new Date());
        SwingUtilities.invokeLater(() -> {
            logArea.append("[" + time + "] [" + prefix + "] > " + m + "\n");
            logArea.setCaretPosition(logArea.getDocument().getLength());
        }); 
    }

    private static void downloadFile(String u, File d) throws IOException {
        log("DOWNLOAD", "Syncing " + d.getName());
        HttpURLConnection c = (HttpURLConnection) new URL(u).openConnection();
        c.setRequestProperty("User-Agent", "Mozilla/5.0");
        try (InputStream in = c.getInputStream(); FileOutputStream out = new FileOutputStream(d)) {
            byte[] b = new byte[4096]; int r; while ((r = in.read(b)) != -1) out.write(b, 0, r);
        }
    }

    private static void extraireNatives(File j, File d) throws IOException {
        try (ZipInputStream z = new ZipInputStream(new FileInputStream(j))) {
            ZipEntry e; while((e = z.getNextEntry()) != null) {
                if(e.getName().contains(".dll")) {
                    File f = new File(d, new File(e.getName()).getName());
                    try(FileOutputStream o = new FileOutputStream(f)){
                        byte[] b = new byte[4096]; int r; while((r=z.read(b))!=-1) o.write(b,0,r);
                    }
                }
            }
        }
    }
}