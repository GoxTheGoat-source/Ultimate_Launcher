package Main;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Desktop;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.Graphics2D;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;
import javax.swing.text.DefaultCaret;

public class Launcher {
	private static JFrame mainFrame;

	private static JTextArea logArea;

	private static JComboBox<String> versionList, ramList;

	private static JButton btnLaunch, btnDir, btnClean, btnRP;

	private static JPanel cardPanel; // Pour switcher entre login et console

	private static java.awt.CardLayout cardLayout;

	private static javax.swing.JTextField userField;

	private static boolean saveUsername = false;

	private static String finalUsername = "Solahdine";

	private static String appData = System.getenv("APPDATA");

	private static final String GAME_DIR = (appData != null ? appData : System.getProperty("user.home"))
			+ File.separator + ".minecraft";

	private static final String VERSION_DIR = GAME_DIR + File.separator + "versions";

	private static final String ASSETS_DIR = GAME_DIR + File.separator + "assets";

	private static final String LIB_DIR = GAME_DIR + File.separator + "libraries";

	private static final String NATIVES_DIR = GAME_DIR + File.separator + "bin" + File.separator + "natives";

	private static final String CONFIG_FILE = GAME_DIR + File.separator + "launcher_config.txt";

	private static final String RESOURCESPACKS_DIR = GAME_DIR + File.separator + "resourcepacks";

	private static final String DOWNLOADS_DIR = GAME_DIR + File.separator + "downloads";

	private static final String[] SPLASH_TEXTS = {
			// --- Tes Classiques (L'âme du launcher) ---
			"~aigm,~cr,~s,Sneak:4", "More of 1000 Lines of Code!! [:-O]",
			"A Problem, Not Go To minecraftforum.net. [:-)]", "Use You My TexturesPacks(SeaOfPixel) ? [>:-(]",
			"Who Read That?? [?_?]", "Are You French? [:-)]", "Made By Goxibot [/(^_^)~]",
			"Not A Good Day, A Super Day! [:-)]", "Ben Soul - SoulMan [-o-]", "Open source[;)]", "150126",
			"yessoufou-161121", "150426", "300813",

			// --- Horreur & Mystère (Error 422 / Glitch) ---
			"ERROR 422: The game is playing YOU [???]", "Something is watching from the logs [0_0]",
			"Don't look at the sky... [X_X]", "File corruption detected... Just kidding! [Maybe]",
			"He is the glitch [0x422]", "The world is folding [X_X]", "Entity 422 found in cache [!]",
			"Don't look behind you [0_0]", "Your PC is 32-bit, but your soul is 64-bit [;)]",
			"01101000 01100101 01101100 01110000 [SOS]", "Searching for the lost chunk [?]",
			"Memory leak in progress... [99%]", "Is it a feature or a bug? [o_O]",
			"Unexpected corruption detected [ERR]", "System.corrupt(true) [!!!]",

			// --- Humour Développeur (Français & Anglais) ---
			"A Java developer doesn't drink water, he drinks coffee [Logo_Not_Found]",
			"Hardware: the part you kick. Software: the part you curse [!]",
			"Toc Toc... Qui est là ? (long silence) ... Java ! [;)]", "I don't sleep, I compile [zZz]",
			"99 little bugs in the code... fix one... 127 little bugs [:'(]",
			"Real programmers count from 0 [0,1,2...]",
			"Ma femme me dit : 'Prends du lait'. Je suis toujours au magasin [LOGIC]", "It works on my machine! [\\o/]",
			"Dark mode is the only way [B-)]", "404: My humor was not found [?]",
			"It's not a bug, it's an undocumented feature [B-)]", "Keyboard not found: Press F1 to continue [?]",
			"Coffee + Java = This Launcher [Logo_Not_Found]",

			// --- Minecraft & Nostalgie ---
			"Alpha 1.2.6 was better [v]", "Ssssssssss... BOOM! [TNT]", "Square world, round mind [[]]",
			"If you see Herobrine, tell him he owes me $10 [xD]", "Creeper just wants a hug... a bit too much [BOOM]",
			"Digging straight down: Best idea ever [RIP]", "Endermen aren't shy, they just want your eyes [O_O]",
			"Legacy versions are the best [old]", "Bring back the old cobble! [gray]",
			"Why did the chicken cross the road? To load the next chunk [o_o]",

			// --- Spécial Gox & Technique ---
			"GoxTheGoat: The true GOAT [B-)]", "Running on 32-bit architecture [x86]", "Solahdine is coding... [Zzz]",
			"AES-256-GCM Encryption active [SECURE]", "Assets.zip.001 found [LOAD]", "Merging files like a boss [JOIN]",
			"FatJarAutoForge power [JAR]", "Gield Binary is watching [X]", "Eclipse Neon (je suis 32bit) [i386]",
			"100% Gluten Free [OK]", "Minecraft Launcher Edition: Gox [v1]" };

	public static class GameVersion {
		public final String url;
		public final String mainClass;
		public final String assetIdx;
		public final String indexUrl;
		public final String assetHash;
		public final String assetPrefix;

		public GameVersion(String url, String mainClass, String assetIdx, String indexUrl, String assetHash) {
			this.url = url;
			this.mainClass = mainClass;
			this.assetIdx = assetIdx;
			this.indexUrl = indexUrl;
			this.assetHash = assetHash;
			this.assetPrefix = "https://resources.download.minecraft.net/";
		}

		public String getAssetUrl(String hash) {
			if (hash == null || hash.length() < 2)
				return null;

			// Extraction des 2 premiers caractères pour le dossier (ex: "4a")
			String folder = hash.substring(0, 2);

			// Structure officielle :
			// https://resources.download.minecraft.net/4a/4a2fac75...
			return this.assetPrefix + folder + "/" + hash;
		}
	}

	private static final Map<String, GameVersion> VERSIONS = new LinkedHashMap<>();

	static {
		VERSIONS.put("1.12.2",
				new GameVersion(
						"https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.12.2.jar",
						"net.minecraft.client.main.Main", "1.12",
						"https://launchermeta.mojang.com/mc/assets/1.12/4bdf1632edbb01b4fee0e1311b8f3821cbcff2fc/1.12.json",
						"4bdf1632edbb01b4fee0e1311b8f3821cbcff2fc"));
		VERSIONS.put("1.12",
				new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.12.jar",
						"net.minecraft.client.main.Main", "1.12",
						"https://launchermeta.mojang.com/mc/assets/1.12/4bdf1632edbb01b4fee0e1311b8f3821cbcff2fc/1.12.json",
						"4bdf1632edbb01b4fee0e1311b8f3821cbcff2fc"));
		VERSIONS.put("1.9.4",
				new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.9.4.jar",
						"net.minecraft.client.main.Main", "1.9",
						"https://launchermeta.mojang.com/mc/assets/1.9/8549e8aca91ee8b8d1620e9e8252d477f16a56d5/1.9.json",
						"8549e8aca91ee8b8d1620e9e8252d477f16a56d5"));
		VERSIONS.put("1.9",
				new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.9.jar",
						"net.minecraft.client.main.Main", "1.9",
						"https://launchermeta.mojang.com/mc/assets/1.9/8549e8aca91ee8b8d1620e9e8252d477f16a56d5/1.9.json",
						"8549e8aca91ee8b8d1620e9e8252d477f16a56d5"));
		VERSIONS.put("1.8.9",
				new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.8.9.jar",
						"net.minecraft.client.main.Main", "1.8",
						"https://launchermeta.mojang.com/mc/assets/1.8/e264980ad255aad2174cbe4d674c102474ae5202/1.8.json",
						"e264980ad255aad2174cbe4d674c102474ae5202"));
		VERSIONS.put("1.8",
				new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.8.jar",
						"net.minecraft.client.main.Main", "1.8",
						"https://launchermeta.mojang.com/mc/assets/1.8/e264980ad255aad2174cbe4d674c102474ae5202/1.8.json",
						"e264980ad255aad2174cbe4d674c102474ae5202"));
		VERSIONS.put("1.7.10",
				new GameVersion(
						"https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.7.10.jar",
						"net.minecraft.client.main.Main", "1.7.10",
						"https://launchermeta.mojang.com/mc/assets/1.7.10/f90ca2878ba3141d32d949ea7f665855dd073c65/1.7.10.json",
						"f90ca2878ba3141d32d949ea7f665855dd073c65"));
		VERSIONS.put("1.7.2",
				new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.7.2.jar",
						"net.minecraft.client.main.Main", "legacy",
						"https://launchermeta.mojang.com/mc/assets/legacy/c0fd82e8ce9fbc93119e40d96d5a4e62cfa3f729/legacy.json",
						"c0fd82e8ce9fbc93119e40d96d5a4e62cfa3f729"));
		VERSIONS.put("1.7",
				new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.7.jar",
						"net.minecraft.client.main.Main", "legacy",
						"https://launchermeta.mojang.com/mc/assets/legacy/c0fd82e8ce9fbc93119e40d96d5a4e62cfa3f729/legacy.json",
						"c0fd82e8ce9fbc93119e40d96d5a4e62cfa3f729"));
		VERSIONS.put("1.6.4",
				new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.6.4.jar",
						"net.minecraft.client.main.Main", "legacy",
						"https://launchermeta.mojang.com/mc/assets/legacy/c0fd82e8ce9fbc93119e40d96d5a4e62cfa3f729/legacy.json",
						"c0fd82e8ce9fbc93119e40d96d5a4e62cfa3f729"));
		VERSIONS.put("1.6",
				new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.6.jar",
						"net.minecraft.client.main.Main", "legacy",
						"https://launchermeta.mojang.com/mc/assets/legacy/c0fd82e8ce9fbc93119e40d96d5a4e62cfa3f729/legacy.json",
						"c0fd82e8ce9fbc93119e40d96d5a4e62cfa3f729"));
		VERSIONS.put("1.5.2",
				new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.5.2.jar",
						"net.minecraft.client.Minecraft", "legacy",
						"https://launchermeta.mojang.com/mc/assets/legacy/c0fd82e8ce9fbc93119e40d96d5a4e62cfa3f729/legacy.json",
						"c0fd82e8ce9fbc93119e40d96d5a4e62cfa3f729"));
		VERSIONS.put("1.5.1",
				new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.5.1.jar",
						"net.minecraft.client.Minecraft", "legacy",
						"https://launchermeta.mojang.com/mc/assets/legacy/c0fd82e8ce9fbc93119e40d96d5a4e62cfa3f729/legacy.json",
						"c0fd82e8ce9fbc93119e40d96d5a4e62cfa3f729"));
		VERSIONS.put("1.5",
				new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.5.jar",
						"net.minecraft.client.Minecraft", "pre-1.6",
						"https://launchermeta.mojang.com/v1/packages/3d8e55480977e32acd9844e545177e69a52f594b/pre-1.6.json",
						"3d8e55480977e32acd9844e545177e69a52f594b"));
		VERSIONS.put("1.4.7",
				new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.4.7.jar",
						"net.minecraft.client.Minecraft", "pre-1.6",
						"https://launchermeta.mojang.com/v1/packages/3d8e55480977e32acd9844e545177e69a52f594b/pre-1.6.json",
						"3d8e55480977e32acd9844e545177e69a52f594b"));
		VERSIONS.put("1.3.1",
				new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.3.1.jar",
						"net.minecraft.client.Minecraft", "pre-1.6",
						"https://launchermeta.mojang.com/v1/packages/3d8e55480977e32acd9844e545177e69a52f594b/pre-1.6.json",
						"3d8e55480977e32acd9844e545177e69a52f594b"));
		VERSIONS.put("1.3",
				new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.3.jar",
						"net.minecraft.client.Minecraft", "pre-1.6",
						"https://launchermeta.mojang.com/v1/packages/3d8e55480977e32acd9844e545177e69a52f594b/pre-1.6.json",
						"3d8e55480977e32acd9844e545177e69a52f594b"));
		VERSIONS.put("1.2.5",
				new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.2.5.jar",
						"net.minecraft.client.Minecraft", "pre-1.6",
						"https://launchermeta.mojang.com/v1/packages/3d8e55480977e32acd9844e545177e69a52f594b/pre-1.6.json",
						"3d8e55480977e32acd9844e545177e69a52f594b"));
		VERSIONS.put("1.0.0",
				new GameVersion("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/1.0.jar",
						"net.minecraft.client.Minecraft", "pre-1.6",
						"https://launchermeta.mojang.com/v1/packages/3d8e55480977e32acd9844e545177e69a52f594b/pre-1.6.json",
						"3d8e55480977e32acd9844e545177e69a52f594b"));
		VERSIONS.put("b1.7.3",
				new GameVersion(
						"https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/b1.7.3.jar",
						"net.minecraft.client.Minecraft", "pre-1.6",
						"https://launchermeta.mojang.com/v1/packages/3d8e55480977e32acd9844e545177e69a52f594b/pre-1.6.json",
						"3d8e55480977e32acd9844e545177e69a52f594b"));
		VERSIONS.put("a1.2.6",
				new GameVersion(
						"https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/a1.2.6.jar",
						"net.minecraft.client.Minecraft", "pre-1.6",
						"https://launchermeta.mojang.com/v1/packages/3d8e55480977e32acd9844e545177e69a52f594b/pre-1.6.json",
						"3d8e55480977e32acd9844e545177e69a52f594b"));
		VERSIONS.put("a1.1.1",
				new GameVersion(
						"https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/a1.1.1.jar",
						"net.minecraft.client.Minecraft", "pre-1.6",
						"https://launchermeta.mojang.com/v1/packages/3d8e55480977e32acd9844e545177e69a52f594b/pre-1.6.json",
						"3d8e55480977e32acd9844e545177e69a52f594b"));
		VERSIONS.put("UNKNOWN",
				new GameVersion(
						"https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/ERROR422.jar",
						"com.jdotsoft.jarloader.ERR422Loader", "legacy",
						"https://launchermeta.mojang.com/mc/assets/legacy/c0fd82e8ce9fbc93119e40d96d5a4e62cfa3f729/legacy.json",
						"c0fd82e8ce9fbc93119e40d96d5a4e62cfa3f729"));
	}

	public static void main(String[] args) {
		System.setProperty("https.protocols", "TLSv1.2");
		(new File(VERSION_DIR)).mkdirs();
		(new File(LIB_DIR)).mkdirs();
		(new File(NATIVES_DIR)).mkdirs();
		initMainLauncher();
	}

	private static void styleRGBButton(JButton btn, Color primaryColor) {
		btn.setFont(new Font("Consolas", Font.BOLD, 14));
		btn.setForeground(primaryColor);
		btn.setBackground(new Color(20, 20, 20));
		btn.setFocusPainted(false);
		btn.setBorder(javax.swing.BorderFactory.createLineBorder(primaryColor, 2));
		btn.setContentAreaFilled(false);
		btn.setOpaque(true);
		btn.setPreferredSize(new java.awt.Dimension(150, 40));

		btn.addMouseListener(new java.awt.event.MouseAdapter() {
			@Override
			public void mouseEntered(java.awt.event.MouseEvent e) {
				btn.setBackground(primaryColor);
				btn.setForeground(Color.BLACK);
			}

			@Override
			public void mouseExited(java.awt.event.MouseEvent e) {
				btn.setBackground(new Color(20, 20, 20));
				btn.setForeground(primaryColor);
			}
		});
	}

	private static void loadConfig() {
		File f = new File(CONFIG_FILE);
		if (f.exists()) {
			try (BufferedReader br = new BufferedReader(new FileReader(f))) {
				String savedName = br.readLine();
				String savedCheck = br.readLine();

				if (savedName != null && !savedName.isEmpty()) {
					finalUsername = savedName;
					if (userField != null)
						userField.setText(finalUsername);
				}

				if (savedCheck != null) {
					saveUsername = Boolean.parseBoolean(savedCheck);
				}
			} catch (Exception e) {
			}
		}
	}

	private static void initMainLauncher() {
		mainFrame = new JFrame("Ultimate Launcher v1.1.1");
		mainFrame.setSize(900, 550);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		mainFrame.setLocationRelativeTo(null);

		cardLayout = new java.awt.CardLayout();
		cardPanel = new JPanel(cardLayout);

		// --- PANEL 1 : LOGIN ---
		JPanel loginPanel = new JPanel(new java.awt.GridBagLayout());
		loginPanel.setBackground(new Color(15, 15, 15));
		java.awt.GridBagConstraints gbc = new java.awt.GridBagConstraints();
		gbc.insets = new java.awt.Insets(10, 10, 10, 10);

		JLabel welcomeLabel = new JLabel("UL CORE LOGIN SYSTEM");
		welcomeLabel.setForeground(Color.GREEN);
		welcomeLabel.setFont(new Font("Monospaced", Font.BOLD, 24));
		gbc.gridx = 0;
		gbc.gridy = 0;
		gbc.gridwidth = 2;
		loginPanel.add(welcomeLabel, gbc);

		userField = new javax.swing.JTextField(finalUsername, 15);
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

		// --- PANEL 2 : CONSOLE ---
		JPanel consolePanel = new JPanel(new BorderLayout(5, 5));
		consolePanel.setBackground(new Color(15, 15, 15));
		consolePanel.setBorder(new EmptyBorder(10, 10, 10, 10));

		ImageIcon logoIcon = null;
		try {
			URL imgURL = Launcher.class.getResource("/purified_logo.png");
			if (imgURL != null) {
				BufferedImage rawImg = ImageIO.read(imgURL);

				if (rawImg != null) {
					int width = rawImg.getWidth();
					int height = rawImg.getHeight();

					// On crée la nouvelle image en mode ARGB pour activer la
					// transparence
					BufferedImage transparentImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

					// Balayage pixel par pixel
					for (int y = 0; y < height; y++) {
						for (int x = 0; x < width; x++) {
							int rgb = rawImg.getRGB(x, y);

							// On extrait les valeurs Rouge, Verte, Bleue
							int r = (rgb >> 16) & 0xFF;
							int g = (rgb >> 8) & 0xFF;
							int b = rgb & 0xFF;

							// Si le pixel est blanc ou très proche du blanc
							// (anti-aliasing)
							if (r >= 245 && g >= 245 && b >= 245) {
								// ON APPLIQUE LA TRANSPARENCE TOTALE
								// (0x00ffffff)
								transparentImg.setRGB(x, y, 0x00FFFFFF);
							} else {
								// On garde le pixel d'origine (ton logo
								// purifié)
								transparentImg.setRGB(x, y, rgb);
							}
						}
					}
					logoIcon = new ImageIcon(transparentImg);
				}
			}
		} catch (Exception e) {
			System.err.println("[Launcher] Reading Logo Error : " + e.getMessage());
			e.printStackTrace();
		}

		// Ton bloc d'affichage reste propre et inchangé
		JLabel titleLabel = new JLabel("UL CORE SYSTEM V1.1.1 ", logoIcon, JLabel.CENTER);
		titleLabel.setHorizontalTextPosition(JLabel.LEFT);
		titleLabel.setForeground(new Color(0, 255, 0));
		titleLabel.setFont(new Font("Monospaced", Font.BOLD, 22));

		JPanel titlePanel = new JPanel(new BorderLayout());
		titlePanel.setOpaque(false);
		titlePanel.add(titleLabel, BorderLayout.WEST);

		btnRP = new JButton("Download RP");
		styleRGBButton(btnRP, new Color(0, 191, 255));
		btnRP.setPreferredSize(new java.awt.Dimension(150, 28));
		titlePanel.add(btnRP, BorderLayout.EAST);
		consolePanel.add(titlePanel, BorderLayout.NORTH);

		// --- COLLAPSIBLE LOGS STRUCTURAL CHANGE ---
		JPanel middlePanel = new JPanel(new GridLayout(1, 2));
		middlePanel.setOpaque(false);

		logArea = new JTextArea();
		logArea.setEditable(false);
		logArea.setBackground(Color.BLACK);
		logArea.setForeground(new Color(0, 255, 0));
		logArea.setFont(new Font("Verdana", Font.PLAIN, 12));
		((DefaultCaret) logArea.getCaret()).setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);

		JScrollPane scrollPane = new JScrollPane(logArea);

		middlePanel.add(scrollPane, BorderLayout.CENTER);

		consolePanel.add(middlePanel, BorderLayout.CENTER);

		// --- BOTTOM BUTTON BAR ---
		JPanel southPanel = new JPanel(new BorderLayout());
		southPanel.setOpaque(false);

		JPanel leftButtons = new JPanel(new FlowLayout(FlowLayout.LEFT));
		leftButtons.setOpaque(false);
		btnDir = new JButton("Open Folder");
		styleRGBButton(btnDir, Color.YELLOW);
		btnClean = new JButton("Clean Cache");
		styleRGBButton(btnClean, Color.RED);
		leftButtons.add(btnDir);
		leftButtons.add(btnClean);

		JPanel rightButtons = new JPanel(new FlowLayout(FlowLayout.RIGHT));
		rightButtons.setOpaque(false);
		versionList = new JComboBox<>((String[]) VERSIONS.keySet().toArray(new String[0]));
		ramList = new JComboBox<>(new String[] { "512M", "800M", "1G", "1500M" });
		btnLaunch = new JButton("LAUNCH GAME");
		styleRGBButton(btnLaunch, Color.GREEN);
		rightButtons.add(versionList);
		rightButtons.add(ramList);
		rightButtons.add(btnLaunch);

		southPanel.add(leftButtons, BorderLayout.WEST);
		southPanel.add(rightButtons, BorderLayout.EAST);
		consolePanel.add(southPanel, BorderLayout.SOUTH);

		cardPanel.add(loginPanel, "LOGIN");
		cardPanel.add(consolePanel, "CONSOLE");
		mainFrame.add(cardPanel);

		btnPlay.addActionListener(e -> {
			finalUsername = userField.getText().trim();
			if (finalUsername.isEmpty()) {
				JOptionPane.showMessageDialog(mainFrame, "Enter a valid username!");
				return;
			}
			if (saveUsername)
				saveConfig();
			cardLayout.show(cardPanel, "CONSOLE");
		});

		btnOptions.addActionListener(e -> showOptionsDialog());

		btnLaunch.addActionListener(e -> new Thread(() -> {
			SwingUtilities.invokeLater(() -> {
				logArea.setText("");
			});
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
			} catch (IOException e1) {
				log("ERROR", e1.getMessage());
				e1.printStackTrace();
			} finally {
				SwingUtilities.invokeLater(() -> {
					btnLaunch.setEnabled(true);
					btnClean.setEnabled(true);
					btnDir.setEnabled(true);
				});
			}
		}).start());

		btnClean.addActionListener(e -> cleanCache());
		btnRP.addActionListener(e -> new Thread(() -> {
			SwingUtilities.invokeLater(() -> btnRP.setEnabled(false));
			soon();
			SwingUtilities.invokeLater(() -> btnRP.setEnabled(true));
		}).start());
		btnDir.addActionListener(e -> {
			try {
				Desktop.getDesktop().open(new File(GAME_DIR));
			} catch (Exception ex) {
				log("ERROR", ex.getMessage());
			}
		});

		mainFrame.setVisible(true);
		loadConfig();
		log("INFO", "Ready System");

		Random rand = new Random();
		String splash = SPLASH_TEXTS[rand.nextInt(SPLASH_TEXTS.length)];

		log("SPLASH",
				"----------------------------------------------------------------------------------------------------");
		log("SPLASH", ">>  " + splash + "  <<");
		log("SPLASH",
				"----------------------------------------------------------------------------------------------------");
	}

	private static void saveConfig() {
		try (java.io.PrintWriter pw = new java.io.PrintWriter(new File(CONFIG_FILE))) {
			pw.println(userField.getText());
			pw.println(saveUsername);
		} catch (Exception e) {
		}
	}

	private static void showOptionsDialog() {
		javax.swing.JDialog dialog = new javax.swing.JDialog(mainFrame, "System Options", true);
		dialog.setLayout(new FlowLayout());
		dialog.setSize(250, 150);
		dialog.setLocationRelativeTo(mainFrame);
		dialog.getContentPane().setBackground(new Color(30, 30, 30));

		javax.swing.JCheckBox checkSave = new javax.swing.JCheckBox("Save Username", saveUsername);
		checkSave.setForeground(Color.GREEN);
		checkSave.setOpaque(false);

		JButton btnOk = new JButton("OK");
		btnOk.addActionListener(e -> {
			saveUsername = checkSave.isSelected();
			dialog.dispose();
		});

		dialog.add(checkSave);
		dialog.add(btnOk);
		dialog.setVisible(true);
	}

	private static void cleanCache() {
		int res = JOptionPane.showConfirmDialog(mainFrame, "Delete libs, natives, downloads and assets?", "System", 0);
		if (res == 0) {
			deleteDir(new File(LIB_DIR));
			deleteDir(new File(NATIVES_DIR));
			deleteDir(new File(ASSETS_DIR));
			deleteDir(new File(RESOURCESPACKS_DIR));
			deleteDir(new File(DOWNLOADS_DIR));
			(new File(LIB_DIR)).mkdirs();
			(new File(NATIVES_DIR)).mkdirs();
			(new File(ASSETS_DIR)).mkdirs();
			(new File(DOWNLOADS_DIR)).mkdirs();
			(new File(RESOURCESPACKS_DIR)).mkdirs();
			log("SYSTEM", "Cache cleared.");
		}
	}

	private static void deleteDir(File dir) {
		File[] files = dir.listFiles();
		if (files != null) {
			for (File f : files) {
				if (f.isDirectory())
					deleteDir(f);
				else
					f.delete();
			}
		}
		dir.delete();
	}

	private static void preparerEtLancer() throws IOException {

		String sel = (String) versionList.getSelectedItem();
		GameVersion cfg = VERSIONS.get(sel);

		File gDir = new File(GAME_DIR);
		File AssetDir = new File(ASSETS_DIR);

		// =========================================================================
		// SECURED ASSETS INDEX AND VIRTUAL/LEGACY ASSETS MANAGEMENT
		// =========================================================================
		File indexesDir = new File(AssetDir, "indexes");
		indexesDir.mkdirs();

		// Determine local JSON index file
		File indexJson = new File(indexesDir, cfg.assetIdx + ".json");

		boolean indexAvailable = true;

		// Download asset index if missing
		if (!indexJson.exists() || indexJson.length() == 0) {

			log("SYSTEM", "Downloading asset index from Mojang: " + cfg.assetIdx);

			try {

				boolean success = downloadFileSafe(cfg.indexUrl, indexJson);

				if (!success) {
					log("ERROR", "Download failed (Method returned false).");
					indexAvailable = false;
				}

			} catch (Exception e) {

				log("ERROR", "Failed to retrieve the asset index due to a network error: " + e.getMessage());

				indexAvailable = false;
			}
		}

		// Use local cache if available
		if (!indexAvailable && indexJson.exists() && indexJson.length() > 0) {

			log("SYSTEM", "Network unreachable. Using cached local asset index backup.");

			indexAvailable = true;
		}

		File jar = new File(VERSION_DIR + File.separator + (sel.equals("UNKNOWN") ? "ERROR422" : sel) + ".jar");

		String mainClass = cfg.mainClass;

		File lDir = new File(LIB_DIR);
		File vDir = new File(VERSION_DIR);
		File nDir = new File(NATIVES_DIR);

		vDir.mkdirs();
		nDir.mkdirs();

		log("SYSTEM", "Checking libraries for " + sel);

		// =========================================================================
		// LIBRARIES TABLE
		// =========================================================================
		String[][] req = {
				{ "authlib-1.5.25.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar", "commons-codec-1.10.jar",
						"commons-compress-1.8.1.jar", "commons-io-2.5.jar", "commons-lang3-3.5.jar",
						"commons-logging-1.1.3.jar", "fastutil-7.1.0.jar", "gson-2.8.0.jar", "guava-21.0.jar",
						"httpclient-4.3.3.jar", "httpcore-4.3.2.jar", "icu4j-core-mojang-51.2.jar",
						"java-objc-bridge-1.0.0.jar", "java-objc-bridge-1.0.0-natives-osx.jar", "jinput-2.0.5.jar",
						"jinput-platform-2.0.5-natives-linux.jar", "jinput-platform-2.0.5-natives-osx.jar",
						"jinput-platform-2.0.5-natives-windows.jar", "jna-4.4.0.jar", "jopt-simple-5.0.3.jar",
						"jutils-1.0.0.jar", "libraryjavasound-20101123.jar", "librarylwjglopenal-20100824.jar",
						"log4j-api-2.8.1.jar", "log4j-core-2.8.1.jar", "lwjgl_util-2.9.4-nightly-20150209.jar",
						"lwjgl-2.9.4-nightly-20150209.jar", "lwjgl-platform-2.9.4-nightly-20150209.jar",
						"lwjgl-platform-2.9.4-nightly-20150209-natives-linux.jar",
						"lwjgl-platform-2.9.4-nightly-20150209-natives-osx.jar",
						"lwjgl-platform-2.9.4-nightly-20150209-natives-windows.jar", "netty-all-4.1.9.Final.jar",
						"oshi-core-1.1.jar", "patchy-1.3.9.jar", "platform-3.4.0.jar", "realms-1.10.22.jar",
						"soundsystem-20120107.jar", "text2speech-1.10.3.jar", "text2speech-1.10.3-natives-linux.jar",
						"text2speech-1.10.3-natives-windows.jar" },
				{ "authlib-1.5.25.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar", "commons-codec-1.10.jar",
						"commons-compress-1.8.1.jar", "commons-io-2.5.jar", "commons-lang3-3.5.jar",
						"commons-logging-1.1.3.jar", "fastutil-7.1.0.jar", "gson-2.8.0.jar", "guava-21.0.jar",
						"httpclient-4.3.3.jar", "httpcore-4.3.2.jar", "icu4j-core-mojang-51.2.jar",
						"java-objc-bridge-1.0.0.jar", "java-objc-bridge-1.0.0-natives-osx.jar", "jinput-2.0.5.jar",
						"jinput-platform-2.0.5-natives-linux.jar", "jinput-platform-2.0.5-natives-osx.jar",
						"jinput-platform-2.0.5-natives-windows.jar", "jna-4.4.0.jar", "jopt-simple-5.0.3.jar",
						"jutils-1.0.0.jar", "libraryjavasound-20101123.jar", "librarylwjglopenal-20100824.jar",
						"log4j-api-2.8.1.jar", "log4j-core-2.8.1.jar", "lwjgl_util-2.9.4-nightly-20150209.jar",
						"lwjgl-2.9.4-nightly-20150209.jar", "lwjgl-platform-2.9.4-nightly-20150209.jar",
						"lwjgl-platform-2.9.4-nightly-20150209-natives-linux.jar",
						"lwjgl-platform-2.9.4-nightly-20150209-natives-osx.jar",
						"lwjgl-platform-2.9.4-nightly-20150209-natives-windows.jar", "netty-all-4.1.9.Final.jar",
						"oshi-core-1.1.jar", "patchy-1.3.9.jar", "platform-3.4.0.jar", "realms-1.10.17.jar",
						"soundsystem-20120107.jar", "text2speech-1.10.3.jar", "text2speech-1.10.3-natives-linux.jar",
						"text2speech-1.10.3-natives-windows.jar" },
				{ "authlib-1.5.22.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar", "commons-codec-1.9.jar",
						"commons-compress-1.8.1.jar", "commons-io-2.4.jar", "commons-lang3-3.3.2.jar",
						"commons-logging-1.1.3.jar", "fastutil-7.0.12_mojang.jar", "gson-2.2.4.jar", "guava-17.0.jar",
						"httpclient-4.3.3.jar", "httpcore-4.3.2.jar", "icu4j-core-mojang-51.2.jar", "jinput-2.0.5.jar",
						"jinput-platform-2.0.5-natives-linux.jar", "jinput-platform-2.0.5-natives-osx.jar",
						"jinput-platform-2.0.5-natives-windows.jar", "jna-3.4.0.jar", "jopt-simple-4.6.jar",
						"jutils-1.0.0.jar", "libraryjavasound-20101123.jar", "librarylwjglopenal-20100824.jar",
						"log4j-api-2.0-beta9.jar", "log4j-core-2.0-beta9.jar", "lwjgl_util-2.9.4-nightly-20150209.jar",
						"lwjgl-2.9.4-nightly-20150209.jar", "lwjgl-platform-2.9.4-nightly-20150209.jar",
						"lwjgl-platform-2.9.4-nightly-20150209-natives-linux.jar",
						"lwjgl-platform-2.9.4-nightly-20150209-natives-osx.jar",
						"lwjgl-platform-2.9.4-nightly-20150209-natives-windows.jar", "netty-1.8.8.jar",
						"netty-all-4.0.23.Final.jar", "oshi-core-1.1.jar", "platform-3.4.0.jar", "realms-1.8.19.jar",
						"soundsystem-20120107.jar" },
				{ "authlib-1.5.22.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar", "commons-codec-1.9.jar",
						"commons-compress-1.8.1.jar", "commons-io-2.4.jar", "commons-lang3-3.3.2.jar",
						"commons-logging-1.1.3.jar", "gson-2.2.4.jar", "guava-17.0.jar", "httpclient-4.3.3.jar",
						"httpcore-4.3.2.jar", "icu4j-core-mojang-51.2.jar", "jinput-2.0.5.jar",
						"jinput-platform-2.0.5-natives-linux.jar", "jinput-platform-2.0.5-natives-osx.jar",
						"jinput-platform-2.0.5-natives-windows.jar", "jna-3.4.0.jar", "jopt-simple-4.6.jar",
						"jutils-1.0.0.jar", "libraryjavasound-20101123.jar", "librarylwjglopenal-20100824.jar",
						"log4j-api-2.0-beta9.jar", "log4j-core-2.0-beta9.jar", "lwjgl_util-2.9.4-nightly-20150209.jar",
						"lwjgl-2.9.4-nightly-20150209.jar", "lwjgl-platform-2.9.4-nightly-20150209.jar",
						"lwjgl-platform-2.9.4-nightly-20150209-natives-linux.jar",
						"lwjgl-platform-2.9.4-nightly-20150209-natives-osx.jar",
						"lwjgl-platform-2.9.4-nightly-20150209-natives-windows.jar", "netty-1.8.8.jar",
						"netty-all-4.0.23.Final.jar", "oshi-core-1.1.jar", "platform-3.4.0.jar", "realms-1.8.7.jar",
						"soundsystem-20120107.jar" },
				{ "authlib-1.5.21.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar", "commons-codec-1.9.jar",
						"commons-compress-1.8.1.jar", "commons-io-2.4.jar", "commons-lang3-3.3.2.jar",
						"commons-logging-1.1.3.jar", "gson-2.2.4.jar", "guava-17.0.jar", "httpclient-4.3.3.jar",
						"httpcore-4.3.2.jar", "icu4j-core-mojang-51.2.jar", "jinput-2.0.5.jar",
						"jinput-platform-2.0.5-natives-linux.jar", "jinput-platform-2.0.5-natives-osx.jar",
						"jinput-platform-2.0.5-natives-windows.jar", "jna-3.4.0.jar", "jopt-simple-4.6.jar",
						"jutils-1.0.0.jar", "libraryjavasound-20101123.jar", "librarylwjglopenal-20100824.jar",
						"log4j-api-2.0-beta9.jar", "log4j-core-2.0-beta9.jar", "lwjgl_util-2.9.4-nightly-20150209.jar",
						"lwjgl-2.9.4-nightly-20150209.jar", "lwjgl-platform-2.9.4-nightly-20150209.jar",
						"lwjgl-platform-2.9.4-nightly-20150209-natives-linux.jar",
						"lwjgl-platform-2.9.4-nightly-20150209-natives-osx.jar",
						"lwjgl-platform-2.9.4-nightly-20150209-natives-windows.jar", "netty-1.8.8.jar",
						"netty-all-4.0.23.Final.jar", "oshi-core-1.1.jar", "platform-3.4.0.jar", "realms-1.7.59.jar",
						"soundsystem-20120107.jar", "twitch-6.5.jar",
						"twitch-external-platform-4.5-natives-windows-32.jar",
						"twitch-external-platform-4.5-natives-windows-64.jar", "twitch-platform-6.5-natives-osx.jar",
						"twitch-platform-6.5-natives-windows-32.jar", "twitch-platform-6.5-natives-windows-64.jar" },
				{ "authlib-1.5.21.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar", "commons-codec-1.9.jar",
						"commons-compress-1.8.1.jar", "commons-io-2.4.jar", "commons-lang3-3.3.2.jar",
						"commons-logging-1.1.3.jar", "gson-2.2.4.jar", "guava-17.0.jar", "httpclient-4.3.3.jar",
						"httpcore-4.3.2.jar", "icu4j-core-mojang-51.2.jar", "jinput-2.0.5.jar",
						"jinput-platform-2.0.5-natives-linux.jar", "jinput-platform-2.0.5-natives-osx.jar",
						"jinput-platform-2.0.5-natives-windows.jar", "jopt-simple-4.6.jar", "jutils-1.0.0.jar",
						"libraryjavasound-20101123.jar", "librarylwjglopenal-20100824.jar", "log4j-api-2.0-beta9.jar",
						"log4j-core-2.0-beta9.jar", "lwjgl_util-2.9.1.jar", "lwjgl-2.9.1.jar",
						"lwjgl-platform-2.9.1-natives-linux.jar", "lwjgl-platform-2.9.1-natives-osx.jar",
						"lwjgl-platform-2.9.1-natives-windows.jar", "netty-1.8.8.jar", "netty-all-4.0.23.Final.jar",
						"realms-1.6.1.jar", "soundsystem-20120107.jar", "trove4j-3.0.3.jar", "twitch-6.5.jar",
						"twitch-external-platform-4.5-natives-windows-32.jar",
						"twitch-external-platform-4.5-natives-windows-64.jar", "twitch-platform-6.5-natives-osx.jar",
						"twitch-platform-6.5-natives-windows-32.jar", "twitch-platform-6.5-natives-windows-64.jar",
						"vecmath-1.5.2.jar" },
				{ "authlib-1.5.21.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar", "commons-codec-1.9.jar",
						"commons-compress-1.8.1.jar", "commons-io-2.4.jar", "commons-lang3-3.1.jar",
						"commons-logging-1.1.3.jar", "gson-2.2.4.jar", "guava-15.0.jar", "httpclient-4.3.3.jar",
						"httpcore-4.3.2.jar", "icu4j-core-mojang-51.2.jar", "jinput-2.0.5.jar",
						"jinput-platform-2.0.5-natives-linux.jar", "jinput-platform-2.0.5-natives-osx.jar",
						"jinput-platform-2.0.5-natives-windows.jar", "jopt-simple-4.5.jar", "jutils-1.0.0.jar",
						"libraryjavasound-20101123.jar", "librarylwjglopenal-20100824.jar", "log4j-api-2.0-beta9.jar",
						"log4j-core-2.0-beta9.jar", "lwjgl_util-2.9.1.jar", "lwjgl-2.9.1.jar",
						"lwjgl-platform-2.9.1-natives-linux.jar", "lwjgl-platform-2.9.1-natives-osx.jar",
						"lwjgl-platform-2.9.1-natives-windows.jar", "netty-1.8.8.jar", "netty-all-4.0.10.Final.jar",
						"realms-1.6.1.jar", "soundsystem-20120107.jar", "trove4j-3.0.3.jar", "twitch-5.16.jar",
						"twitch-external-platform-4.5-natives-windows-32.jar",
						"twitch-external-platform-4.5-natives-windows-64.jar", "twitch-platform-5.16-natives-osx.jar",
						"twitch-platform-5.16-natives-windows-32.jar", "twitch-platform-5.16-natives-windows-64.jar",
						"vecmath-1.5.2.jar" },
				{ "authlib-1.3.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar", "commons-io-2.4.jar",
						"commons-lang3-3.1.jar", "gson-2.2.4.jar", "guava-15.0.jar", "icu4j-core-mojang-51.2.jar",
						"jinput-2.0.5.jar", "jinput-platform-2.0.5-natives-linux.jar",
						"jinput-platform-2.0.5-natives-osx.jar", "jinput-platform-2.0.5-natives-windows.jar",
						"jopt-simple-4.5.jar", "jutils-1.0.0.jar", "libraryjavasound-20101123.jar",
						"librarylwjglopenal-20100824.jar", "log4j-api-2.0-beta9.jar", "log4j-core-2.0-beta9.jar",
						"lwjgl_util-2.9.0.jar", "lwjgl_util-2.9.1-nightly-20131017.jar", "lwjgl-2.9.0.jar",
						"lwjgl-2.9.1-nightly-20131017.jar", "lwjgl-platform-2.9.0-natives-linux.jar",
						"lwjgl-platform-2.9.0-natives-osx.jar", "lwjgl-platform-2.9.0-natives-windows.jar",
						"lwjgl-platform-2.9.1-nightly-20131017-natives-osx.jar", "netty-1.8.8.jar",
						"netty-all-4.0.10.Final.jar", "soundsystem-20120107.jar", "trove4j-3.0.3.jar",
						"vecmath-1.3.1.jar" },
				{ "argo-2.25_fixed.jar", "bcprov-jdk15on-1.47.jar", "codecjorbis-20101023.jar", "codecwav-20101023.jar",
						"commons-io-2.4.jar", "commons-lang3-3.1.jar", "gson-2.2.2.jar", "guava-14.0.jar",
						"jinput-2.0.5.jar", "jinput-platform-2.0.5-natives-linux.jar",
						"jinput-platform-2.0.5-natives-osx.jar", "jinput-platform-2.0.5-natives-windows.jar",
						"jopt-simple-4.5.jar", "jutils-1.0.0.jar", "libraryjavasound-20101123.jar",
						"librarylwjglopenal-20100824.jar", "lwjgl_util-2.9.0.jar",
						"lwjgl_util-2.9.1-nightly-20130708-debug3.jar", "lwjgl-2.9.0.jar",
						"lwjgl-2.9.1-nightly-20130708-debug3.jar", "lwjgl-platform-2.9.0-natives-linux.jar",
						"lwjgl-platform-2.9.0-natives-osx.jar", "lwjgl-platform-2.9.0-natives-windows.jar",
						"lwjgl-platform-2.9.1-nightly-20130708-debug3-natives-osx.jar", "soundsystem-20120107.jar" },
				{ "asm-all-4.1.jar", "jinput-2.0.5.jar", "jinput-platform-2.0.5-natives-linux.jar",
						"jinput-platform-2.0.5-natives-osx.jar", "jinput-platform-2.0.5-natives-windows.jar",
						"jopt-simple-4.5.jar", "jutils-1.0.0.jar", "launchwrapper-1.5.jar", "lwjgl_util-2.9.0.jar",
						"lwjgl_util-2.9.1-nightly-20130708-debug3.jar", "lwjgl-2.9.0.jar",
						"lwjgl-2.9.1-nightly-20130708-debug3.jar", "lwjgl-platform-2.9.0-natives-linux.jar",
						"lwjgl-platform-2.9.0-natives-osx.jar", "lwjgl-platform-2.9.0-natives-windows.jar",
						"lwjgl-platform-2.9.1-nightly-20130708-debug3-natives-osx.jar" },
				{ "exe4jlib.jar" } };

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
		} else if (sel.equals("1.7") || sel.equals("1.7.1") || sel.equals("1.7.2")) {

			currentLibs = req[7];

		} else if (sel.equals("1.6") || sel.equals("1.6.4")) {

			currentLibs = req[8];

		} else if (sel.startsWith("a") || sel.startsWith("b") || sel.matches("1\\.[0-5](\\..*)?")) {

			currentLibs = req[9];

		} else {

			currentLibs = req[10];
		}

		// Count assets
		if (indexJson.exists() && indexJson.length() > 0) {

			try {

				com.google.gson.JsonObject root = com.google.gson.JsonParser
						.parseReader(new java.io.InputStreamReader(new java.io.FileInputStream(indexJson),
								java.nio.charset.StandardCharsets.UTF_8))
						.getAsJsonObject();

				com.google.gson.JsonObject objects = root.getAsJsonObject("objects");

				if (objects == null) {
					log("ERROR", "Key 'objects' not found in : " + indexJson.getName());
					return;
				}

				for (Map.Entry<String, com.google.gson.JsonElement> entry : objects.entrySet()) {

					String assetPath = entry.getKey();
					String hash = entry.getValue().getAsJsonObject().get("hash").getAsString();

					if (assetPath.contains("/lang/") || assetPath.endsWith(".lang")) {
						continue;
					}

					if (hash == null || hash.length() < 2) {
						log("WARN", "Hash invalide pour : " + assetPath);
						continue;
					}

					if (objects != null) {

						File obj = new File(new File(ASSETS_DIR, "objects"),
								hash.substring(0, 2) + File.separator + hash);

						if (!obj.exists() || obj.length() == 0) {
						}
					}
				}

			} catch (Exception ignored) {
			}
		}

		// =========================================================================
		// CLEAN OLD LEGACY ASSETS
		// =========================================================================
		if (!cfg.assetIdx.equals("legacy") && !cfg.assetIdx.equals("1.7.10") && !cfg.assetIdx.equals("pre-1.6")
				&& !cfg.assetIdx.equals("1.8")) {

			File virtualLegacy = new File(ASSETS_DIR, "virtual" + File.separator + "legacy");

			if (virtualLegacy.exists()) {

				deleteDir(virtualLegacy);

				log("SYSTEM", "Purged stale virtual/legacy for modern version.");
			}
		}

		downloadAssets(indexJson, cfg, sel);

		// =========================================================================
		// DOWNLOAD VERSION JAR
		// =========================================================================
		if (!jar.exists()) {

			try {
				downloadFile(cfg.url, jar);
			} catch (IOException e) {
				log("ERROR", e.getMessage());
				e.printStackTrace();
			}
		}

		for (String s : currentLibs) {
			File f = new File(lDir, s);
			if (f.getParentFile() != null) {
				f.getParentFile().mkdirs();
			}
			if (!f.exists() || f.length() == 0) {
				try {
					downloadFile("https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/" + s, f);
				} catch (Exception e) {
					log("ERROR", e.getMessage());
				}
			}
		}

		String os = System.getProperty("os.name").toLowerCase().contains("win") ? "windows" : "linux";
		log("SYSTEM", "Checking natives for " + os);
		File libFolder = new File(LIB_DIR);
		if (libFolder.exists() && libFolder.listFiles() != null) {
			for (File lib : libFolder.listFiles()) {
				if (lib.getName().contains("natives-" + os)
						|| lib.getName().contains("nightly") && lib.getName().endsWith(".jar")) {
					log("NATIVES", "Extracted : " + lib.getName());
					try {
						extraireNatives(lib, nDir);
					} catch (IOException e) {
						log("ERROR", e.getMessage());
						e.printStackTrace();
					}
				}
			}
		}

		log("SYSTEM", "Starting " + sel);
		StringBuilder cp = new StringBuilder();
		cp.append(jar.getAbsolutePath()).append(File.pathSeparator);
		for (String lib : currentLibs) {
			File f = new File(LIB_DIR, lib);
			if (f.exists()) {
				cp.append(f.getAbsolutePath()).append(File.pathSeparator);
			} else {
				log("ERROR", "Missing library: " + lib);
			}
		}

		File assetFolder = new File(ASSETS_DIR);
		if (!assetFolder.exists()) {
			assetFolder.mkdirs();
		}

		List<String> cmd = new ArrayList<>();

		cmd.add("java");
		cmd.add("-Xmx" + ramList.getSelectedItem());
		cmd.add("-XX:+UnlockExperimentalVMOptions");
		cmd.add("-XX:G1NewSizePercent=20");
		cmd.add("-XX:G1ReservePercent=20");
		cmd.add("-XX:MaxGCPauseMillis=50");
		cmd.add("-XX:+UseConcMarkSweepGC");
		cmd.add("-XX:+CMSIncrementalMode");
		cmd.add("-Djava.library.path=" + nDir.getAbsolutePath());
		cmd.add("-cp");
		cmd.add(cp.toString());
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
		log("DEBUG", "Command: " + cmd.toString());

		try {
			ProcessBuilder pb = new ProcessBuilder(cmd);
			pb.directory(gDir);
			pb.redirectErrorStream(true);

			Process p = pb.start();

			new Thread(() -> {
				try {
					BufferedReader r = new BufferedReader(new InputStreamReader(p.getInputStream()));
					String line;
					while ((line = r.readLine()) != null) {
						log("GAME_OUTPUT", line);
					}
				} catch (Exception ex) {
					log("ERROR", "Crash: " + ex.getMessage());
					ex.printStackTrace();
				}
			}).start();
		} catch (Exception e) {
			log("ERROR", e.getMessage());
			e.printStackTrace();
		}
	}

	private static void soon() {
		File RPacks = new File(RESOURCESPACKS_DIR);
		File RPack1 = new File(RPacks, "NewDefaultPlusv1.80_MC1.6.1-1.8.9_.zip");
		File RPack2 = new File(RPacks, "NewDefaultPlusv1.80_MC1.9-1.12.2_.zip");
		File DDir = new File(DOWNLOADS_DIR);

		String rawUrl = "https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/NewDefault+v1.80[MC1.6.1-1.8.9].zip";
		String encodedUrl = rawUrl.replace("[", ".").replace("]", "").replace("+", "+");
		String rawUrl2 = "https://github.com/GoxTheGoat-source/my_versions/releases/download/versions/NewDefault+v1.80[MC1.9-1.12.2].zip";
		String encodedUrl2 = rawUrl2.replace("[", ".").replace("]", "").replace("+", "+");

		if (!RPack1.exists() || !RPack2.exists()) {
			DDir.mkdirs();
			RPacks.mkdirs();
			File download1 = new File(DDir, "NewDefaultPlusv1.80_MC1.6.1-1.8.9_.zip");
			try {
				downloadFile(encodedUrl, download1);
			} catch (IOException e) {
				log("ERROR", e.getMessage());
			}
			File download2 = new File(DDir, "NewDefaultPlusv1.80_MC1.9-1.12.2_.zip");
			try {
				downloadFile(encodedUrl2, download2);
			} catch (IOException e) {
				log("ERROR", e.getMessage());
			}
			try {
				Files.copy(download1.toPath(), RPack1.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				log("ERROR", e.getMessage());
			}
			try {
				Files.copy(download2.toPath(), RPack2.toPath(), java.nio.file.StandardCopyOption.REPLACE_EXISTING);
			} catch (IOException e) {
				log("ERROR", e.getMessage());
			}
		}
	}

	private static void downloadAssets(File indexJson, GameVersion cfg, String sel) throws IOException {
		if (!indexJson.exists() || indexJson.length() == 0) {
			log("WARN", "Asset index missing or empty, skipping.");
			return;
		}

		File objectsDir = new File(ASSETS_DIR, "objects");
		objectsDir.mkdirs();

		boolean isPre16 = cfg.assetIdx.equals("pre-1.6");
		boolean isLegacy = cfg.assetIdx.equals("legacy");
		boolean is17x = cfg.assetIdx.equals("1.7.10");
		boolean is18x = cfg.assetIdx.equals("1.8");
		boolean needsVirtual = isLegacy || is17x || is18x;

		File targetDir = null;
		if (isPre16) {
			targetDir = new File(GAME_DIR, "resources");
			targetDir.mkdirs();
		} else if (needsVirtual) {
			targetDir = new File(ASSETS_DIR, "virtual" + File.separator + "legacy");
			targetDir.mkdirs();
		}

		com.google.gson.JsonObject root;
		try {
			root = com.google.gson.JsonParser
					.parseReader(new java.io.InputStreamReader(new java.io.FileInputStream(indexJson),
							java.nio.charset.StandardCharsets.UTF_8))
					.getAsJsonObject();
		} catch (Exception e) {
			log("ERROR", "Failed to parse asset index: " + e.getMessage());
			return;
		}

		com.google.gson.JsonObject objects = root.getAsJsonObject("objects");
		if (objects == null) {
			log("ERROR", "Key 'objects' not found in: " + indexJson.getName());
			return;
		}

		for (Map.Entry<String, com.google.gson.JsonElement> entry : objects.entrySet()) {
			String assetPath = entry.getKey();
			String hash = entry.getValue().getAsJsonObject().get("hash").getAsString();

			boolean isLangFile = assetPath.contains("/lang/") || assetPath.endsWith(".lang");
			if (isLangFile && (isPre16 || needsVirtual)) {
				continue;
			}

			if (hash == null || hash.length() < 2) {
				log("WARN", "Invalid hash for: " + assetPath);
				continue;
			}

			File targetObject = new File(objectsDir, hash.substring(0, 2) + File.separator + hash);

			if (!targetObject.exists() || targetObject.length() == 0) {
				targetObject.getParentFile().mkdirs();
				boolean ok = downloadFileSafe(cfg.getAssetUrl(hash), targetObject);
				if (!ok) {
					log("ERROR", "Asset not found on server: " + hash);
					continue;
				}
			}

			if (targetDir != null && targetObject.exists() && targetObject.length() > 0) {
				File dest = null;

				if (isPre16) {
					if (assetPath.startsWith("minecraft/sounds/") || assetPath.startsWith("minecraft/music/")
							|| assetPath.startsWith("minecraft/records/")) {
						String cleanPath = assetPath.substring("minecraft/".length());
						dest = new File(targetDir, cleanPath.replace("/", File.separator));
					}
				} else {
					if (!assetPath.startsWith("minecraft/icons/") && !isLangFile) {
						dest = new File(targetDir, assetPath.replace("/", File.separator));
					}
				}

				if (dest != null && (!dest.exists() || dest.length() == 0)) {
					dest.getParentFile().mkdirs();
					try (FileInputStream in = new FileInputStream(targetObject);
							FileOutputStream out = new FileOutputStream(dest)) {
						byte[] buf = new byte[65536];
						int len;
						while ((len = in.read(buf)) > 0)
							out.write(buf, 0, len);
					} catch (Exception e) {
						log("ERROR", "Failed to copy asset: " + dest.getName());
					}
				}
			}
		}
	}

	private static void log(String level, String msg) {
		String time = new SimpleDateFormat("HH:mm:ss").format(new Date());
		SwingUtilities.invokeLater(() -> logArea.append("[" + time + " " + level + "] > " + msg + "\n"));
	}

	private static void downloadFile(String urlStr, File target) throws IOException {
		URL url = new URL(urlStr);
		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		conn.setRequestProperty("User-Agent", "Mozilla/5.0");
		conn.setConnectTimeout(10000);
		conn.setReadTimeout(30000);

		if (conn.getResponseCode() != 200) {
			return;
		}

		long fileSize = conn.getContentLengthLong();
		log("DOWNLOAD", "Syncing: " + target.getName() + " (Size: " + (fileSize / 1024) + " KB)");

		try (InputStream in = conn.getInputStream(); FileOutputStream out = new FileOutputStream(target)) {
			byte[] buf = new byte[65536];
			int n;
			long downloaded = 0;
			int lastPercent = -1;

			while ((n = in.read(buf)) != -1) {
				out.write(buf, 0, n);
				downloaded += n;

				if (fileSize > 0) {
					int percent = (int) ((downloaded * 100) / fileSize);
					if (percent != lastPercent) {
						updateLastLogLine("PROGRESS > " + target.getName() + " [" + percent + "%]");
						lastPercent = percent;
					}
				}
			}
		} catch (Exception e) {
			log("ERROR", e.getMessage());
			if (target.exists())
				target.delete();
			return;
		} finally {
			conn.disconnect();
		}
		log("SYSTEM", "Download complete: " + target.getName());
	}

	private static void updateLastLogLine(String newText) {
		SwingUtilities.invokeLater(() -> {
			try {
				int lineCount = logArea.getLineCount();
				if (lineCount > 0) {
					int start = logArea.getLineStartOffset(lineCount - 1);
					int end = logArea.getLineEndOffset(lineCount - 1);
					logArea.replaceRange(
							"[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + " INFO] > " + newText, start,
							end);
				}
			} catch (Exception e) {
				log("INFO", newText);
			}
		});
	}

	private static boolean downloadFileSafe(String urlStr, File target) throws IOException {
		try {
			URL url = new URL(urlStr);
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64)");
			conn.setConnectTimeout(10000);
			conn.setReadTimeout(10000);

			if (conn.getResponseCode() != 200) {
				return false;
			}

			long fileSize = conn.getContentLengthLong();
			log("DOWNLOAD", "Syncing: " + target.getName() + " (Size: " + (fileSize / 1024) + " KB)");

			try (InputStream in = conn.getInputStream(); FileOutputStream out = new FileOutputStream(target)) {
				byte[] buffer = new byte[65536];
				int read;
				long downloaded = 0L;
				int lastPercent = -1;

				while ((read = in.read(buffer)) != -1) {
					out.write(buffer, 0, read);
					downloaded += read;

					if (fileSize > 0) {
						int percent = (int) ((downloaded * 100) / fileSize);
						if (percent != lastPercent) {
							updateLastLogLine("PROGRESS > " + target.getName() + " [" + percent + "%]");
							lastPercent = percent;
						}
					}
				}
			} catch (Exception e) {
				log("DEBUG", "Download failed for : " + urlStr + " : " + e.getMessage());
				e.printStackTrace();
				return false;
			} finally {
				conn.disconnect();
			}
			log("SYSTEM", "Download complete: " + target.getName());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			log("ERROR", e.getMessage());
			return false;
		}
	}

	private static void extraireNatives(File jarFile, File destinationDir) throws IOException {
		try (ZipInputStream zis = new ZipInputStream(new FileInputStream(jarFile))) {
			ZipEntry entry;
			while ((entry = zis.getNextEntry()) != null) {
				String name = entry.getName();
				if (name.endsWith(".dll") || name.endsWith(".so") || name.endsWith(".dylib")) {
					File f = new File(destinationDir, name);
					if (!f.exists()) {
						try (FileOutputStream fos = new FileOutputStream(f)) {
							byte[] buf = new byte[8192];
							int len;
							while ((len = zis.read(buf)) > 0)
								fos.write(buf, 0, len);
						}
					}
				}
				zis.closeEntry();
			}
		}
	}
}