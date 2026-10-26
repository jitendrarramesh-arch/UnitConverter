import javax.swing.*;
import javax.swing.border.*;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import javax.imageio.ImageIO;
import java.math.BigDecimal;
import java.math.MathContext;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

/**
 * Basic Converter - Unit Converter Desktop Application
 * Built 100% with pure Java Swing and Java 2D vector graphics.
 */
public class BasicConverter {

    // Regex for standard Gmail format
    private static final Pattern GMAIL_PATTERN = Pattern.compile(
            "^[A-Za-z0-9._%+-]+@gmail\\.com$", Pattern.CASE_INSENSITIVE);

    // ================= DESIGN SYSTEM / PALETTE =================
    private static final Color CLR_BG_APP = new Color(0xF8FAFC);          // Slate 50
    private static final Color CLR_BG_CARD = Color.WHITE;
    private static final Color CLR_PRIMARY = new Color(0x4F46E5);         // Indigo 600
    private static final Color CLR_PRIMARY_HOVER = new Color(0x4338CA);   // Indigo 700
    private static final Color CLR_PRIMARY_LIGHT = new Color(0xEEF2FF);   // Indigo 50
    private static final Color CLR_ACCENT = new Color(0x7C3AED);          // Violet 600
    private static final Color CLR_TEXT_MAIN = new Color(0x0F172A);       // Slate 900
    private static final Color CLR_TEXT_MUTED = new Color(0x64748B);      // Slate 500
    private static final Color CLR_TEXT_LIGHT = new Color(0x94A3B8);      // Slate 400
    private static final Color CLR_BORDER = new Color(0xE2E8F0);          // Slate 200
    private static final Color CLR_FOCUS_RING = new Color(0x818CF8);      // Indigo 400
    private static final Color CLR_SUCCESS = new Color(0x10B981);         // Emerald 500
    private static final Color CLR_SUCCESS_BG = new Color(0xECFDF5);      // Emerald 50
    private static final Color CLR_ERROR = new Color(0xEF4444);           // Red 500
    private static final Color CLR_ERROR_BG = new Color(0xFEF2F2);        // Red 50

    // Font definitions
    private static final Font FONT_TITLE = new Font("Segoe UI", Font.BOLD, 22);
    private static final Font FONT_HEADING = new Font("Segoe UI", Font.BOLD, 16);
    private static final Font FONT_SUB = new Font("Segoe UI", Font.PLAIN, 13);
    private static final Font FONT_LABEL = new Font("Segoe UI", Font.BOLD, 12);
    private static final Font FONT_BODY = new Font("Segoe UI", Font.PLAIN, 14);
    private static final Font FONT_RESULT = new Font("Segoe UI", Font.BOLD, 26);
    private static final Font FONT_BADGE = new Font("Segoe UI", Font.BOLD, 11);

    // Session Data
    private static final List<HistoryEntry> HISTORY = new ArrayList<>();
    private static final SimpleDateFormat TIME_FMT = new SimpleDateFormat("hh:mm a");

    public static void main(String[] args) {
        // High-DPI text antialiasing
        System.setProperty("awt.useSystemAAFontSettings", "on");
        System.setProperty("swing.aatext", "true");

        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {}
            showLoginWindow();
        });
    }

    // =========================================================================
    // 1. MODERN PROFESSIONAL LOGIN WINDOW
    // =========================================================================
    public static void showLoginWindow() {
        JFrame frame = new JFrame("Unit Converter - Sign In");
        if (LOGO_ICON != null) frame.setIconImage(LOGO_ICON);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(CLR_BG_APP);
        frame.setLayout(new GridBagLayout());

        // Elevated Container Card
        ShadowCard card = new ShadowCard(420, 530);
        card.setLayout(new BoxLayout(card, BoxLayout.Y_AXIS));
        card.setBorder(new EmptyBorder(32, 34, 32, 34));

        // Header with Vector Logo
        JPanel header = new JPanel();
        header.setOpaque(false);
        header.setLayout(new BoxLayout(header, BoxLayout.Y_AXIS));

        JPanel logoRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 0));
        logoRow.setOpaque(false);
        logoRow.add(makeLogo(LOGO_FULL, 120, 120));

        JLabel title = new JLabel("Unit Converter");
        title.setFont(FONT_TITLE);
        title.setForeground(CLR_TEXT_MAIN);
        title.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel sub = new JLabel("Professional Multi-Unit Conversion Suite");
        sub.setFont(FONT_SUB);
        sub.setForeground(CLR_TEXT_MUTED);
        sub.setAlignmentX(Component.CENTER_ALIGNMENT);

        header.add(logoRow);
        header.add(Box.createVerticalStrut(10));
        header.add(title);
        header.add(Box.createVerticalStrut(4));
        header.add(sub);

        // Input Fields
        ModernIconTextField nameField = new ModernIconTextField("e.g. Rahul Sharma", VectorIcon.Type.USER);
        ModernIconTextField emailField = new ModernIconTextField("e.g. rahul@gmail.com", VectorIcon.Type.MAIL);
        ModernPasswordField passField = new ModernPasswordField("Enter your password");

        // Status Badge / Error Alert
        StatusBadge statusBadge = new StatusBadge();

        // Remember Me Checkbox
        JCheckBox rememberCheck = new JCheckBox("Remember this session");
        rememberCheck.setOpaque(false);
        rememberCheck.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        rememberCheck.setForeground(CLR_TEXT_MUTED);
        rememberCheck.setFocusPainted(false);
        rememberCheck.setSelected(true);

        // Sign In Button
        ModernButton loginBtn = new ModernButton("Sign In to Studio", CLR_PRIMARY, CLR_PRIMARY_HOVER, Color.WHITE);
        loginBtn.setPreferredSize(new Dimension(350, 44));

        // Real-time live email validation
        emailField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { checkEmail(); }
            public void removeUpdate(DocumentEvent e) { checkEmail(); }
            public void changedUpdate(DocumentEvent e) { checkEmail(); }
            private void checkEmail() {
                String email = emailField.getText().trim();
                if (!email.isEmpty() && !emailField.isPlaceholderActive()) {
                    if (GMAIL_PATTERN.matcher(email).matches()) {
                        statusBadge.showSuccess("Valid Gmail account recognized");
                    } else {
                        statusBadge.showNotice("Format: user@gmail.com");
                    }
                } else {
                    statusBadge.clear();
                }
            }
        });

        // Login Action
        ActionListener submitAction = e -> {
            String name = nameField.getText().trim();
            String email = emailField.getText().trim();
            char[] pass = passField.getPassword();

            if (name.isEmpty() || nameField.isPlaceholderActive() || name.length() < 2) {
                statusBadge.showError("Please enter your full name (at least 2 letters)");
                nameField.requestFocus();
            } else if (!GMAIL_PATTERN.matcher(email).matches()) {
                statusBadge.showError("Enter a valid Gmail address (name@gmail.com)");
                emailField.requestFocus();
            } else if (pass.length < 4) {
                statusBadge.showError("Password should be at least 4 characters");
                passField.requestFocus();
            } else {
                frame.dispose();
                showConverterWindow(name, email.toLowerCase());
            }
        };

        loginBtn.addActionListener(submitAction);
        passField.addActionListener(submitAction);
        emailField.addActionListener(submitAction);
        nameField.addActionListener(submitAction);

        // Form Assembly
        card.add(header);
        card.add(Box.createVerticalStrut(22));

        card.add(createFieldGroup("FULL NAME", nameField));
        card.add(Box.createVerticalStrut(14));

        card.add(createFieldGroup("GMAIL ADDRESS", emailField));
        card.add(Box.createVerticalStrut(14));

        card.add(createFieldGroup("PASSWORD", passField));
        card.add(Box.createVerticalStrut(10));

        JPanel optionsRow = new JPanel(new BorderLayout());
        optionsRow.setOpaque(false);
        optionsRow.setMaximumSize(new Dimension(350, 26));
        optionsRow.add(rememberCheck, BorderLayout.WEST);
        card.add(optionsRow);

        card.add(Box.createVerticalStrut(10));
        card.add(statusBadge);
        card.add(Box.createVerticalStrut(14));

        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(loginBtn);

        card.add(Box.createVerticalStrut(14));
        JLabel secureNote = new JLabel("Protected with Local Session Verification");
        secureNote.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        secureNote.setForeground(CLR_TEXT_LIGHT);
        secureNote.setAlignmentX(Component.CENTER_ALIGNMENT);
        card.add(secureNote);

        frame.add(card);
        frame.pack();
        frame.setSize(480, 720);
        frame.setResizable(false);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    private static JPanel createFieldGroup(String labelText, JComponent field) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setMaximumSize(new Dimension(350, 68));
        p.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel lbl = new JLabel(labelText);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(CLR_TEXT_MUTED);
        lbl.setAlignmentX(Component.LEFT_ALIGNMENT);

        field.setAlignmentX(Component.LEFT_ALIGNMENT);
        field.setMaximumSize(new Dimension(350, 42));

        p.add(lbl);
        p.add(Box.createVerticalStrut(6));
        p.add(field);
        return p;
    }

    // =========================================================================
    // 2. MODERN PROFESSIONAL CONVERTER STUDIO WINDOW
    // =========================================================================
    public static void showConverterWindow(String userName, String userEmail) {
        JFrame frame = new JFrame("Unit Converter - " + userName);
        if (LOGO_ICON != null) frame.setIconImage(LOGO_ICON);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.getContentPane().setBackground(CLR_BG_APP);
        frame.setLayout(new BorderLayout());

        // Top Navigation Bar
        JPanel topBar = createTopNavBar(frame, userName, userEmail);
        frame.add(topBar, BorderLayout.NORTH);

        // Main Workspace
        JPanel workspace = new JPanel(new BorderLayout(18, 18));
        workspace.setOpaque(false);
        workspace.setBorder(new EmptyBorder(18, 24, 24, 24));

        // Category Navigation Ribbon
        JPanel categoryRibbon = new JPanel(new FlowLayout(FlowLayout.LEFT, 8, 8));
        categoryRibbon.setOpaque(false);

        // Two-column layout: Converter Studio (Left) + History Drawer (Right)
        JPanel bodySplit = new JPanel(new GridBagLayout());
        bodySplit.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.fill = GridBagConstraints.BOTH;
        gbc.weighty = 1.0;

        // UI Controls for Conversion
        JComboBox<String> fromCombo = createStyledCombo();
        JComboBox<String> toCombo = createStyledCombo();
        ModernIconTextField inputField = new ModernIconTextField("Enter value to convert", null);
        inputField.setFont(new Font("Segoe UI", Font.BOLD, 16));

        JLabel resultNumber = new JLabel("0");
        resultNumber.setFont(FONT_RESULT);
        resultNumber.setForeground(CLR_PRIMARY);

        JLabel resultUnit = new JLabel("");
        resultUnit.setFont(new Font("Segoe UI", Font.BOLD, 15));
        resultUnit.setForeground(CLR_TEXT_MUTED);

        JLabel formulaText = new JLabel("Formula: -");
        formulaText.setFont(new Font("Segoe UI", Font.ITALIC, 13));
        formulaText.setForeground(CLR_TEXT_MUTED);

        JLabel copyStatus = new JLabel("");
        copyStatus.setFont(new Font("Segoe UI", Font.BOLD, 12));
        copyStatus.setForeground(CLR_SUCCESS);

        DefaultListModel<HistoryEntry> historyModel = new DefaultListModel<>();
        for (HistoryEntry h : HISTORY) historyModel.add(0, h);
        JList<HistoryEntry> historyList = new JList<>(historyModel);
        historyList.setCellRenderer(new HistoryCellRenderer());
        historyList.setBackground(CLR_BG_CARD);
        historyList.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Core Live Calculation Runnable
        final String[] activeCategory = {"Length"};
        Runnable executeConversion = () -> {
            String raw = inputField.getText().trim();
            if (raw.isEmpty() || inputField.isPlaceholderActive()) {
                resultNumber.setText("0");
                resultUnit.setText("");
                formulaText.setText("Formula: Select units & enter a value");
                copyStatus.setText("");
                return;
            }
            try {
                double val = Double.parseDouble(raw);
                String from = (String) fromCombo.getSelectedItem();
                String to = (String) toCombo.getSelectedItem();
                if (from == null || to == null) return;

                double converted = convert(activeCategory[0], from, to, val);
                String formatted = formatDecimal(converted);

                String toSym = unitSymbol(to);
                resultNumber.setText(formatted);
                resultUnit.setText(toSym);

                // Update formula explanation
                String formula = buildFormulaExplanation(activeCategory[0], from, to, val, converted);
                formulaText.setText(formula);
                copyStatus.setText("");
            } catch (NumberFormatException ex) {
                resultNumber.setText("Invalid input");
                resultUnit.setText("");
                formulaText.setText("Formula: Please enter a valid number");
            }
        };

        // Real-time calculation on typing!
        inputField.getDocument().addDocumentListener(new DocumentListener() {
            public void insertUpdate(DocumentEvent e) { executeConversion.run(); }
            public void removeUpdate(DocumentEvent e) { executeConversion.run(); }
            public void changedUpdate(DocumentEvent e) { executeConversion.run(); }
        });

        fromCombo.addActionListener(e -> executeConversion.run());
        toCombo.addActionListener(e -> executeConversion.run());

        // Per-category colour theme (filled in after the result card is built)
        final Runnable[] applyTheme = new Runnable[1];

        // Category Buttons
        ButtonGroup catGroup = new ButtonGroup();
        boolean first = true;
        for (String catName : UNITS.keySet()) {
            CategoryChip chip = new CategoryChip(catName, first, categoryColor(catName));
            catGroup.add(chip);
            chip.addActionListener(e -> {
                activeCategory[0] = catName;
                if (applyTheme[0] != null) applyTheme[0].run();
                loadUnitsForCategory(catName, fromCombo, toCombo);
                executeConversion.run();
            });
            categoryRibbon.add(chip);
            if (first) {
                first = false;
                loadUnitsForCategory(catName, fromCombo, toCombo);
            }
        }

        // Left Panel: Conversion Studio Card
        ShadowCard studioCard = new ShadowCard(480, 520);
        studioCard.setLayout(new BoxLayout(studioCard, BoxLayout.Y_AXIS));
        studioCard.setBorder(new EmptyBorder(24, 26, 24, 26));

        JLabel studioTitle = new JLabel("CONVERSION STUDIO");
        studioTitle.setFont(FONT_LABEL);
        studioTitle.setForeground(CLR_TEXT_LIGHT);
        studioTitle.setAlignmentX(Component.LEFT_ALIGNMENT);

        // Input Value Field Group
        JPanel inputGroup = createStudioFieldGroup("INPUT VALUE", inputField);

        // From / Swap / To Group
        JPanel selectorRow = new JPanel(new GridBagLayout());
        selectorRow.setOpaque(false);
        selectorRow.setAlignmentX(Component.LEFT_ALIGNMENT);
        GridBagConstraints sGbc = new GridBagConstraints();
        sGbc.fill = GridBagConstraints.HORIZONTAL;
        sGbc.weightx = 0.45;
        sGbc.gridx = 0;

        JPanel fromPanel = createComboGroup("FROM UNIT", fromCombo);
        selectorRow.add(fromPanel, sGbc);

        sGbc.weightx = 0.10;
        sGbc.gridx = 1;
        sGbc.insets = new Insets(16, 8, 0, 8);
        ModernIconButton swapBtn = new ModernIconButton(VectorIcon.Type.SWAP, "Swap Units");
        swapBtn.addActionListener(e -> {
            int fromIdx = fromCombo.getSelectedIndex();
            fromCombo.setSelectedIndex(toCombo.getSelectedIndex());
            toCombo.setSelectedIndex(fromIdx);
            executeConversion.run();
        });
        selectorRow.add(swapBtn, sGbc);

        sGbc.insets = new Insets(0, 0, 0, 0);
        sGbc.weightx = 0.45;
        sGbc.gridx = 2;
        JPanel toPanel = createComboGroup("TO UNIT", toCombo);
        selectorRow.add(toPanel, sGbc);

        // Result Card with Copy Action
        JPanel resultCard = new JPanel();
        resultCard.setOpaque(true);
        resultCard.setBackground(CLR_PRIMARY_LIGHT);
        resultCard.setBorder(new CompoundBorder(
                new RoundBorder(new Color(0xC7D2FE), 12),
                new EmptyBorder(16, 18, 16, 18)));
        resultCard.setLayout(new BorderLayout(12, 6));
        resultCard.setAlignmentX(Component.LEFT_ALIGNMENT);

        JPanel resLeft = new JPanel();
        resLeft.setOpaque(false);
        resLeft.setLayout(new BoxLayout(resLeft, BoxLayout.Y_AXIS));

        JLabel resTag = new JLabel("CONVERTED RESULT");
        resTag.setFont(FONT_BADGE);
        resTag.setForeground(CLR_PRIMARY);

        JPanel resValueRow = new FlowLayout(FlowLayout.LEFT, 6, 0) != null ? new JPanel(new FlowLayout(FlowLayout.LEFT, 6, 0)) : new JPanel();
        resValueRow.setOpaque(false);
        resValueRow.add(resultNumber);
        resValueRow.add(resultUnit);

        resLeft.add(resTag);
        resLeft.add(Box.createVerticalStrut(4));
        resLeft.add(resValueRow);
        resLeft.add(Box.createVerticalStrut(4));
        resLeft.add(formulaText);

        JPanel resRight = new JPanel(new FlowLayout(FlowLayout.RIGHT, 4, 8));
        resRight.setOpaque(false);

        ModernButton copyBtn = new ModernButton("Copy", CLR_PRIMARY, CLR_PRIMARY_HOVER, Color.WHITE);
        copyBtn.setIcon(new VectorIcon(VectorIcon.Type.COPY, 14, Color.WHITE));
        copyBtn.setPreferredSize(new Dimension(88, 36));
        copyBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));

        copyBtn.addActionListener(e -> {
            String val = resultNumber.getText() + " " + resultUnit.getText();
            Toolkit.getDefaultToolkit().getSystemClipboard().setContents(new StringSelection(val), null);
            copyStatus.setText("Copied to clipboard!");
            javax.swing.Timer timer = new javax.swing.Timer(2200, evt -> copyStatus.setText(""));
            timer.setRepeats(false);
            timer.start();
        });

        resRight.add(copyStatus);
        resRight.add(copyBtn);

        resultCard.add(resLeft, BorderLayout.CENTER);
        resultCard.add(resRight, BorderLayout.EAST);

        // Re-colour the result card to match the active category
        applyTheme[0] = () -> {
            Color c = categoryColor(activeCategory[0]);
            resultCard.setBackground(mix(c, Color.WHITE, 0.90f));
            resultCard.setBorder(new CompoundBorder(
                    new RoundBorder(mix(c, Color.WHITE, 0.55f), 12),
                    new EmptyBorder(16, 18, 16, 18)));
            resultNumber.setForeground(c.darker());
            resTag.setForeground(c.darker());
            resultCard.repaint();
        };
        applyTheme[0].run();

        // Action Buttons Row (Log to History)
        JPanel actionRow = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 0));
        actionRow.setOpaque(false);
        actionRow.setAlignmentX(Component.LEFT_ALIGNMENT);

        ModernButton logHistoryBtn = new ModernButton("Save to History", new Color(0x0284C7), new Color(0x0369A1), Color.WHITE);
        logHistoryBtn.setIcon(new VectorIcon(VectorIcon.Type.CLOCK, 14, Color.WHITE));
        logHistoryBtn.setPreferredSize(new Dimension(150, 40));
        logHistoryBtn.addActionListener(e -> {
            String inputVal = inputField.getText().trim();
            if (inputVal.isEmpty() || inputField.isPlaceholderActive()) return;
            try {
                double val = Double.parseDouble(inputVal);
                String from = (String) fromCombo.getSelectedItem();
                String to = (String) toCombo.getSelectedItem();
                double converted = convert(activeCategory[0], from, to, val);
                String resStr = formatDecimal(converted) + " " + unitSymbol(to);

                HistoryEntry entry = new HistoryEntry(
                        activeCategory[0], from, to, val, resStr, TIME_FMT.format(new Date()));
                HISTORY.add(0, entry);
                historyModel.add(0, entry);
            } catch (Exception ignored) {}
        });

        actionRow.add(logHistoryBtn);

        studioCard.add(studioTitle);
        studioCard.add(Box.createVerticalStrut(14));
        studioCard.add(inputGroup);
        studioCard.add(Box.createVerticalStrut(14));
        studioCard.add(selectorRow);
        studioCard.add(Box.createVerticalStrut(18));
        studioCard.add(resultCard);
        studioCard.add(Box.createVerticalStrut(16));
        studioCard.add(actionRow);

        // Right Panel: Recent Conversion History
        ShadowCard historyCard = new ShadowCard(320, 520);
        historyCard.setLayout(new BorderLayout(0, 12));
        historyCard.setBorder(new EmptyBorder(20, 20, 20, 20));

        JPanel histHeader = new JPanel(new BorderLayout());
        histHeader.setOpaque(false);

        JLabel histTitle = new JLabel("CONVERSION HISTORY");
        histTitle.setFont(FONT_LABEL);
        histTitle.setForeground(CLR_TEXT_LIGHT);

        JButton clearBtn = new JButton("Clear");
        clearBtn.setFont(new Font("Segoe UI", Font.BOLD, 11));
        clearBtn.setForeground(CLR_TEXT_MUTED);
        clearBtn.setContentAreaFilled(false);
        clearBtn.setBorderPainted(false);
        clearBtn.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
        clearBtn.addActionListener(e -> {
            HISTORY.clear();
            historyModel.clear();
        });

        histHeader.add(histTitle, BorderLayout.WEST);
        histHeader.add(clearBtn, BorderLayout.EAST);

        JScrollPane histScroll = new JScrollPane(historyList);
        histScroll.setBorder(BorderFactory.createEmptyBorder());
        histScroll.getViewport().setBackground(CLR_BG_CARD);

        // History item click restores to studio!
        historyList.addMouseListener(new MouseAdapter() {
            public void mouseClicked(MouseEvent e) {
                HistoryEntry sel = historyList.getSelectedValue();
                if (sel != null) {
                    for (Component c : categoryRibbon.getComponents()) {
                        if (c instanceof CategoryChip && ((CategoryChip) c).getText().equals(sel.category)) {
                            ((CategoryChip) c).setSelected(true);
                            activeCategory[0] = sel.category;
                            if (applyTheme[0] != null) applyTheme[0].run();
                            loadUnitsForCategory(sel.category, fromCombo, toCombo);
                            break;
                        }
                    }
                    fromCombo.setSelectedItem(sel.fromUnit);
                    toCombo.setSelectedItem(sel.toUnit);
                    inputField.setText(formatDecimal(sel.inputValue));
                    executeConversion.run();
                }
            }
        });

        JLabel histHint = new JLabel("Tip: Click any history item to restore");
        histHint.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        histHint.setForeground(CLR_TEXT_LIGHT);
        histHint.setHorizontalAlignment(SwingConstants.CENTER);

        historyCard.add(histHeader, BorderLayout.NORTH);
        historyCard.add(histScroll, BorderLayout.CENTER);
        historyCard.add(histHint, BorderLayout.SOUTH);

        // Assemble GridBag layout
        gbc.gridx = 0;
        gbc.weightx = 0.65;
        bodySplit.add(studioCard, gbc);

        gbc.gridx = 1;
        gbc.weightx = 0.35;
        gbc.insets = new Insets(0, 16, 0, 0);
        bodySplit.add(historyCard, gbc);

        workspace.add(categoryRibbon, BorderLayout.NORTH);
        workspace.add(bodySplit, BorderLayout.CENTER);

        frame.add(workspace, BorderLayout.CENTER);
        frame.pack();
        frame.setSize(940, 710);
        frame.setMinimumSize(new Dimension(860, 640));
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        // Initial default calculation
        inputField.setText("1");
        executeConversion.run();
    }

    private static JPanel createStudioFieldGroup(String label, JComponent comp) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));
        p.setAlignmentX(Component.LEFT_ALIGNMENT);

        JLabel lbl = new JLabel(label);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(CLR_TEXT_MUTED);

        p.add(lbl);
        p.add(Box.createVerticalStrut(6));
        p.add(comp);
        return p;
    }

    private static JPanel createComboGroup(String label, JComboBox<String> combo) {
        JPanel p = new JPanel();
        p.setOpaque(false);
        p.setLayout(new BoxLayout(p, BoxLayout.Y_AXIS));

        JLabel lbl = new JLabel(label);
        lbl.setFont(FONT_LABEL);
        lbl.setForeground(CLR_TEXT_MUTED);

        p.add(lbl);
        p.add(Box.createVerticalStrut(6));
        p.add(combo);
        return p;
    }

    private static JPanel createTopNavBar(JFrame frame, String userName, String userEmail) {
        JPanel bar = new JPanel(new BorderLayout());
        bar.setBackground(CLR_BG_CARD);
        bar.setBorder(new CompoundBorder(
                BorderFactory.createMatteBorder(0, 0, 1, 0, CLR_BORDER),
                new EmptyBorder(12, 24, 12, 24)));

        // App Logo & Title
        JPanel left = new JPanel(new FlowLayout(FlowLayout.LEFT, 12, 0));
        left.setOpaque(false);
        left.add(makeLogo(LOGO_ICON, 40, 40));

        JPanel titleBlock = new JPanel();
        titleBlock.setOpaque(false);
        titleBlock.setLayout(new BoxLayout(titleBlock, BoxLayout.Y_AXIS));

        JLabel title = new JLabel("Unit Converter");
        title.setFont(FONT_HEADING);
        title.setForeground(CLR_TEXT_MAIN);

        JLabel ver = new JLabel("v2.5 Enterprise Edition");
        ver.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        ver.setForeground(CLR_TEXT_LIGHT);

        titleBlock.add(title);
        titleBlock.add(ver);
        left.add(titleBlock);

        // Right: User Profile Chip + Sign Out Button
        JPanel right = new JPanel(new FlowLayout(FlowLayout.RIGHT, 14, 0));
        right.setOpaque(false);

        // User Avatar Circle with Initials
        String initials = getInitials(userName);
        AvatarPanel avatar = new AvatarPanel(initials);

        JPanel userMeta = new JPanel();
        userMeta.setOpaque(false);
        userMeta.setLayout(new BoxLayout(userMeta, BoxLayout.Y_AXIS));

        JLabel nameLbl = new JLabel(userName);
        nameLbl.setFont(new Font("Segoe UI", Font.BOLD, 13));
        nameLbl.setForeground(CLR_TEXT_MAIN);

        JLabel emailLbl = new JLabel(userEmail);
        emailLbl.setFont(new Font("Segoe UI", Font.PLAIN, 11));
        emailLbl.setForeground(CLR_TEXT_MUTED);

        userMeta.add(nameLbl);
        userMeta.add(emailLbl);

        ModernButton logoutBtn = new ModernButton("Sign Out", new Color(0xF1F5F9), new Color(0xE2E8F0), CLR_TEXT_MAIN);
        logoutBtn.setPreferredSize(new Dimension(92, 34));
        logoutBtn.setFont(new Font("Segoe UI", Font.BOLD, 12));
        logoutBtn.addActionListener(e -> {
            frame.dispose();
            showLoginWindow();
        });

        right.add(avatar);
        right.add(userMeta);
        right.add(Box.createHorizontalStrut(8));
        right.add(logoutBtn);

        bar.add(left, BorderLayout.WEST);
        bar.add(right, BorderLayout.EAST);
        return bar;
    }

    private static String getInitials(String name) {
        String[] parts = name.trim().split("\\s+");
        if (parts.length >= 2) {
            return ("" + parts[0].charAt(0) + parts[1].charAt(0)).toUpperCase();
        } else if (!parts[0].isEmpty()) {
            return ("" + parts[0].charAt(0)).toUpperCase();
        }
        return "U";
    }

    // =========================================================================
    // 3. CONVERSION ENGINE & EXTENDED DATA DEFINITIONS
    // =========================================================================
    private static final Map<String, String[]> UNITS = new LinkedHashMap<>();
    private static final Map<String, Double> FACTOR = new HashMap<>();

    static {
        // 1. Length (Base: Meter)
        UNITS.put("Length", new String[]{
                "Meter (m)", "Centimeter (cm)", "Millimeter (mm)", "Kilometer (km)",
                "Inch (in)", "Foot (ft)", "Yard (yd)", "Mile (mi)", "Nautical Mile (nmi)"});
        FACTOR.put("Meter", 1.0);
        FACTOR.put("Centimeter", 0.01);
        FACTOR.put("Millimeter", 0.001);
        FACTOR.put("Kilometer", 1000.0);
        FACTOR.put("Inch", 0.0254);
        FACTOR.put("Foot", 0.3048);
        FACTOR.put("Yard", 0.9144);
        FACTOR.put("Mile", 1609.344);
        FACTOR.put("Nautical Mile", 1852.0);

        // 2. Mass / Weight (Base: Kilogram)
        UNITS.put("Mass / Weight", new String[]{
                "Kilogram (kg)", "Gram (g)", "Milligram (mg)", "Metric Ton (t)",
                "Pound (lb)", "Ounce (oz)", "Stone (st)"});
        FACTOR.put("Kilogram", 1.0);
        FACTOR.put("Gram", 0.001);
        FACTOR.put("Milligram", 0.000001);
        FACTOR.put("Metric Ton", 1000.0);
        FACTOR.put("Pound", 0.45359237);
        FACTOR.put("Ounce", 0.028349523125);
        FACTOR.put("Stone", 6.35029318);

        // 3. Temperature (Special formula)
        UNITS.put("Temperature", new String[]{
                "Celsius (°C)", "Fahrenheit (°F)", "Kelvin (K)"});

        // 4. Volume (Base: Liter)
        UNITS.put("Volume", new String[]{
                "Liter (L)", "Milliliter (mL)", "Cubic meter (m³)",
                "US Gallon (gal)", "US Quart (qt)", "US Pint (pt)", "Fluid Ounce (fl oz)"});
        FACTOR.put("Liter", 1.0);
        FACTOR.put("Milliliter", 0.001);
        FACTOR.put("Cubic meter", 1000.0);
        FACTOR.put("US Gallon", 3.785411784);
        FACTOR.put("US Quart", 0.946352946);
        FACTOR.put("US Pint", 0.473176473);
        FACTOR.put("Fluid Ounce", 0.0295735295625);

        // 5. Area (Base: Square meter)
        UNITS.put("Area", new String[]{
                "Square meter (m²)", "Square kilometer (km²)", "Square foot (ft²)",
                "Square yard (yd²)", "Acre (ac)", "Hectare (ha)"});
        FACTOR.put("Square meter", 1.0);
        FACTOR.put("Square kilometer", 1000000.0);
        FACTOR.put("Square foot", 0.09290304);
        FACTOR.put("Square yard", 0.83612736);
        FACTOR.put("Acre", 4046.8564224);
        FACTOR.put("Hectare", 10000.0);

        // 6. Speed (Base: Meter per second)
        UNITS.put("Speed", new String[]{
                "Meter per second (m/s)", "Kilometer per hour (km/h)",
                "Mile per hour (mph)", "Knot (kn)"});
        FACTOR.put("Meter per second", 1.0);
        FACTOR.put("Kilometer per hour", 1.0 / 3.6);
        FACTOR.put("Mile per hour", 0.44704);
        FACTOR.put("Knot", 0.514444444);

        // 7. Time (Base: Second)
        UNITS.put("Time", new String[]{
                "Second (s)", "Minute (min)", "Hour (hr)", "Day (d)", "Week (wk)", "Year (yr)"});
        FACTOR.put("Second", 1.0);
        FACTOR.put("Minute", 60.0);
        FACTOR.put("Hour", 3600.0);
        FACTOR.put("Day", 86400.0);
        FACTOR.put("Week", 604800.0);
        FACTOR.put("Year", 31536000.0);

        // 8. Digital Storage (Base: Byte)
        UNITS.put("Digital Storage", new String[]{
                "Byte (B)", "Kilobyte (KB)", "Megabyte (MB)",
                "Gigabyte (GB)", "Terabyte (TB)", "Petabyte (PB)"});
        FACTOR.put("Byte", 1.0);
        FACTOR.put("Kilobyte", 1024.0);
        FACTOR.put("Megabyte", 1024.0 * 1024.0);
        FACTOR.put("Gigabyte", 1024.0 * 1024.0 * 1024.0);
        FACTOR.put("Terabyte", 1024.0 * 1024.0 * 1024.0 * 1024.0);
        FACTOR.put("Petabyte", 1024.0 * 1024.0 * 1024.0 * 1024.0 * 1024.0);

        // 9. Pressure (Base: Pascal)
        UNITS.put("Pressure", new String[]{
                "Pascal (Pa)", "Kilopascal (kPa)", "Bar (bar)", "PSI (psi)", "Atmosphere (atm)"});
        FACTOR.put("Pascal", 1.0);
        FACTOR.put("Kilopascal", 1000.0);
        FACTOR.put("Bar", 100000.0);
        FACTOR.put("PSI", 6894.757293168);
        FACTOR.put("Atmosphere", 101325.0);

        // 10. Energy (Base: Joule)
        UNITS.put("Energy", new String[]{
                "Joule (J)", "Kilojoule (kJ)", "Calorie (cal)",
                "Kilocalorie (kcal)", "Watt-hour (Wh)", "Kilowatt-hour (kWh)"});
        FACTOR.put("Joule", 1.0);
        FACTOR.put("Kilojoule", 1000.0);
        FACTOR.put("Calorie", 4.184);
        FACTOR.put("Kilocalorie", 4184.0);
        FACTOR.put("Watt-hour", 3600.0);
        FACTOR.put("Kilowatt-hour", 3600000.0);
    }

    private static void loadUnitsForCategory(String category, JComboBox<String> from, JComboBox<String> to) {
        String[] units = UNITS.get(category);
        if (units == null) return;
        from.setModel(new DefaultComboBoxModel<>(units));
        to.setModel(new DefaultComboBoxModel<>(units));
        if (units.length > 1) {
            to.setSelectedIndex(1);
        }
    }

    private static String unitName(String str) {
        int idx = str.indexOf(" (");
        return idx < 0 ? str : str.substring(0, idx);
    }

    private static String unitSymbol(String str) {
        int idx = str.lastIndexOf('(');
        return idx < 0 ? str : str.substring(idx + 1, str.length() - 1);
    }

    private static double convert(String category, String fromFull, String toFull, double value) {
        String f = unitName(fromFull);
        String t = unitName(toFull);

        if ("Temperature".equals(category)) {
            double c;
            switch (f) {
                case "Fahrenheit": c = (value - 32.0) * 5.0 / 9.0; break;
                case "Kelvin":     c = value - 273.15; break;
                default:           c = value; break; // Celsius
            }
            switch (t) {
                case "Fahrenheit": return c * 9.0 / 5.0 + 32.0;
                case "Kelvin":     return c + 273.15;
                default:           return c;         // Celsius
            }
        }

        Double factorFrom = FACTOR.get(f);
        Double factorTo = FACTOR.get(t);
        if (factorFrom == null || factorTo == null) return value;
        return (value * factorFrom) / factorTo;
    }

    private static String formatDecimal(double val) {
        if (Double.isNaN(val) || Double.isInfinite(val)) return "-";
        BigDecimal bd = new BigDecimal(val).round(new MathContext(9));
        return bd.stripTrailingZeros().toPlainString();
    }

    private static String buildFormulaExplanation(String cat, String fromFull, String toFull, double inVal, double outVal) {
        String fName = unitName(fromFull);
        String tName = unitName(toFull);

        if ("Temperature".equals(cat)) {
            if (fName.equals(tName)) return "Formula: Value remains unchanged (1:1)";
            if (fName.equals("Celsius") && tName.equals("Fahrenheit")) return "Formula: (°C × 9/5) + 32 = °F";
            if (fName.equals("Fahrenheit") && tName.equals("Celsius")) return "Formula: (°F − 32) × 5/9 = °C";
            if (fName.equals("Celsius") && tName.equals("Kelvin")) return "Formula: °C + 273.15 = K";
            if (fName.equals("Kelvin") && tName.equals("Celsius")) return "Formula: K − 273.15 = °C";
            return "Formula: Convert via Celsius intermediate";
        }

        double unitRatio = convert(cat, fromFull, toFull, 1.0);
        return "Formula: 1 " + unitSymbol(fromFull) + " = " + formatDecimal(unitRatio) + " " + unitSymbol(toFull);
    }

    // =========================================================================
    // CATEGORY COLOURS + LOGO IMAGE LOADING
    // =========================================================================
    private static final Map<String, Color> CATEGORY_COLORS = new HashMap<>();

    static {
        CATEGORY_COLORS.put("Length", new Color(0x06B6D4));          // Cyan
        CATEGORY_COLORS.put("Mass / Weight", new Color(0xF59E0B));   // Amber
        CATEGORY_COLORS.put("Temperature", new Color(0xEF4444));     // Red
        CATEGORY_COLORS.put("Volume", new Color(0x10B981));          // Emerald
        CATEGORY_COLORS.put("Area", new Color(0x65A30D));            // Lime
        CATEGORY_COLORS.put("Speed", new Color(0xF97316));           // Orange
        CATEGORY_COLORS.put("Time", new Color(0x8B5CF6));            // Violet
        CATEGORY_COLORS.put("Digital Storage", new Color(0x4F46E5)); // Indigo
        CATEGORY_COLORS.put("Pressure", new Color(0xEC4899));        // Pink
        CATEGORY_COLORS.put("Energy", new Color(0xCA8A04));          // Gold
    }

    private static Color categoryColor(String category) {
        Color c = CATEGORY_COLORS.get(category);
        return c != null ? c : CLR_PRIMARY;
    }

    /** Blend colour a towards b by t (0 = a, 1 = b). */
    private static Color mix(Color a, Color b, float t) {
        int r = Math.round(a.getRed() + (b.getRed() - a.getRed()) * t);
        int g = Math.round(a.getGreen() + (b.getGreen() - a.getGreen()) * t);
        int bl = Math.round(a.getBlue() + (b.getBlue() - a.getBlue()) * t);
        return new Color(r, g, bl);
    }

    // The logo is built into this file, so no extra files are needed.
    // (Optional: put logo_full.png / logo_icon.png next to the app to override it.)
    private static final BufferedImage LOGO_FULL = loadLogo("logo_full.png", LogoData.FULL);
    private static final BufferedImage LOGO_ICON = loadLogo("logo_icon.png", LogoData.ICON);

    private static BufferedImage loadLogo(String fileName, String embeddedBase64) {
        BufferedImage img = loadImage(fileName);
        if (img != null) return img;
        try {
            byte[] bytes = Base64.getDecoder().decode(embeddedBase64);
            return ImageIO.read(new ByteArrayInputStream(bytes));
        } catch (Exception e) {
            return null;
        }
    }

    private static BufferedImage loadImage(String name) {
        try {
            File f = new File(name);
            if (f.exists()) return ImageIO.read(f);
            java.net.URL url = BasicConverter.class.getResource("/" + name);
            if (url != null) return ImageIO.read(url);
        } catch (Exception ignored) {}
        return null;
    }

    /** Image logo if the file was found, otherwise the built-in vector logo. */
    private static JComponent makeLogo(BufferedImage img, int w, int h) {
        return img != null ? new LogoImagePanel(img, w, h) : new VectorLogoPanel(w, h);
    }

    private static BufferedImage drawScaled(BufferedImage src, int w, int h) {
        BufferedImage out = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2 = out.createGraphics();
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BICUBIC);
        g2.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);
        g2.drawImage(src, 0, 0, w, h, null);
        g2.dispose();
        return out;
    }

    /** Smooth down-scaling (halve repeatedly, then final resize). */
    private static BufferedImage scaleSmooth(BufferedImage src, int w, int h) {
        BufferedImage cur = src;
        int cw = src.getWidth(), ch = src.getHeight();
        while (cw / 2 >= w && ch / 2 >= h) {
            cw /= 2;
            ch /= 2;
            cur = drawScaled(cur, cw, ch);
        }
        return drawScaled(cur, w, h);
    }

    /**
     * Logo image drawn with rounded corners
     */
    static class LogoImagePanel extends JPanel {
        private final BufferedImage scaled;
        private final int w, h;

        LogoImagePanel(BufferedImage img, int w, int h) {
            this.w = w;
            this.h = h;
            this.scaled = scaleSmooth(img, w, h);
            Dimension d = new Dimension(w, h);
            setPreferredSize(d);
            setMinimumSize(d);
            setMaximumSize(d);
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            int arc = Math.max(10, w / 5);
            g2.setPaint(new TexturePaint(scaled, new Rectangle(0, 0, w, h)));
            g2.fill(new RoundRectangle2D.Float(0, 0, w, h, arc, arc));
            g2.dispose();
        }
    }

    // =========================================================================
    // 4. CUSTOM PROFESSIONAL SWING UI COMPONENTS & VECTOR GRAPHICS
    // =========================================================================

    /**
     * Elevated Shadow Card with rounded corners and clean borders
     */
    static class ShadowCard extends JPanel {
        private final int width, height;

        ShadowCard(int width, int height) {
            this.width = width;
            this.height = height;
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Soft multi-layer drop shadow
            for (int i = 4; i >= 1; i--) {
                g2.setColor(new Color(15, 23, 42, 4 * i));
                g2.fillRoundRect(i, i + 2, w - (i * 2), h - (i * 2), 24, 24);
            }

            // Card surface
            g2.setColor(CLR_BG_CARD);
            g2.fillRoundRect(2, 2, w - 5, h - 5, 20, 20);

            // Card border
            g2.setColor(CLR_BORDER);
            g2.drawRoundRect(2, 2, w - 5, h - 5, 20, 20);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Modern Text Field with embedded vector icons and focus highlight ring
     */
    static class ModernIconTextField extends JTextField {
        private final String placeholder;
        private final VectorIcon.Type iconType;
        private boolean isFocused = false;

        ModernIconTextField(String placeholder, VectorIcon.Type iconType) {
            this.placeholder = placeholder;
            this.iconType = iconType;
            setFont(FONT_BODY);
            setForeground(CLR_TEXT_MAIN);
            setBackground(Color.WHITE);
            setCaretColor(CLR_PRIMARY);
            setOpaque(false);

            int leftPad = (iconType != null) ? 40 : 14;
            setBorder(new EmptyBorder(10, leftPad, 10, 14));

            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) {
                    isFocused = true;
                    repaint();
                }
                public void focusLost(FocusEvent e) {
                    isFocused = false;
                    repaint();
                }
            });
        }

        public boolean isPlaceholderActive() {
            return getText().isEmpty() && !isFocused;
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            // Background Fill
            g2.setColor(Color.WHITE);
            g2.fillRoundRect(1, 1, w - 2, h - 2, 12, 12);

            // Border & Focus Glow
            if (isFocused) {
                g2.setColor(new Color(0x81, 0x8C, 0xF8, 90));
                g2.setStroke(new BasicStroke(3f));
                g2.drawRoundRect(1, 1, w - 3, h - 3, 12, 12);

                g2.setColor(CLR_PRIMARY);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, w - 3, h - 3, 12, 12);
            } else {
                g2.setColor(CLR_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, 12, 12);
            }

            // Draw leading vector icon
            if (iconType != null) {
                Color iconColor = isFocused ? CLR_PRIMARY : CLR_TEXT_LIGHT;
                VectorIcon.paintDirect(g2, iconType, 12, (h - 18) / 2, 18, 18, iconColor);
            }

            g2.dispose();
            super.paintComponent(g);

            // Draw placeholder text if empty and not focused
            if (isPlaceholderActive()) {
                Graphics2D gText = (Graphics2D) g.create();
                gText.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                gText.setFont(getFont());
                gText.setColor(CLR_TEXT_LIGHT);
                int left = (iconType != null) ? 42 : 16;
                FontMetrics fm = gText.getFontMetrics();
                int y = (h - fm.getHeight()) / 2 + fm.getAscent();
                gText.drawString(placeholder, left, y);
                gText.dispose();
            }
        }
    }

    /**
     * Modern Password Field with Show/Hide toggle button
     */
    static class ModernPasswordField extends JPasswordField {
        private final String placeholder;
        private boolean isFocused = false;
        private boolean showPass = false;

        ModernPasswordField(String placeholder) {
            this.placeholder = placeholder;
            setFont(FONT_BODY);
            setForeground(CLR_TEXT_MAIN);
            setCaretColor(CLR_PRIMARY);
            setOpaque(false);
            setBorder(new EmptyBorder(10, 40, 10, 42));

            addFocusListener(new FocusAdapter() {
                public void focusGained(FocusEvent e) { isFocused = true; repaint(); }
                public void focusLost(FocusEvent e) { isFocused = false; repaint(); }
            });

            // Toggle show/hide password on click near the right edge
            addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (e.getX() >= getWidth() - 36) {
                        showPass = !showPass;
                        setEchoChar(showPass ? (char) 0 : '•');
                        repaint();
                    }
                }
            });
            setCursor(Cursor.getPredefinedCursor(Cursor.TEXT_CURSOR));
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            int w = getWidth();
            int h = getHeight();

            g2.setColor(Color.WHITE);
            g2.fillRoundRect(1, 1, w - 2, h - 2, 12, 12);

            if (isFocused) {
                g2.setColor(new Color(0x81, 0x8C, 0xF8, 90));
                g2.setStroke(new BasicStroke(3f));
                g2.drawRoundRect(1, 1, w - 3, h - 3, 12, 12);

                g2.setColor(CLR_PRIMARY);
                g2.setStroke(new BasicStroke(1.5f));
                g2.drawRoundRect(1, 1, w - 3, h - 3, 12, 12);
            } else {
                g2.setColor(CLR_BORDER);
                g2.setStroke(new BasicStroke(1f));
                g2.drawRoundRect(1, 1, w - 2, h - 2, 12, 12);
            }

            // Left Lock Icon
            VectorIcon.paintDirect(g2, VectorIcon.Type.LOCK, 12, (h - 18) / 2, 18, 18, isFocused ? CLR_PRIMARY : CLR_TEXT_LIGHT);

            // Right Eye / Eye-off Icon
            VectorIcon.Type eyeIcon = showPass ? VectorIcon.Type.EYE_OPEN : VectorIcon.Type.EYE_CLOSED;
            VectorIcon.paintDirect(g2, eyeIcon, w - 32, (h - 16) / 2, 16, 16, showPass ? CLR_PRIMARY : CLR_TEXT_LIGHT);

            g2.dispose();
            super.paintComponent(g);

            if (getPassword().length == 0 && !isFocused) {
                Graphics2D gText = (Graphics2D) g.create();
                gText.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
                gText.setFont(getFont());
                gText.setColor(CLR_TEXT_LIGHT);
                FontMetrics fm = gText.getFontMetrics();
                int y = (h - fm.getHeight()) / 2 + fm.getAscent();
                gText.drawString(placeholder, 42, y);
                gText.dispose();
            }
        }
    }

    /**
     * Modern Button with animated hover state & rounded geometry
     */
    static class ModernButton extends JButton {
        private final Color baseColor, hoverColor, textColor;
        private boolean hover = false;

        ModernButton(String text, Color baseColor, Color hoverColor, Color textColor) {
            super(text);
            this.baseColor = baseColor;
            this.hoverColor = hoverColor;
            this.textColor = textColor;
            setFont(new Font("Segoe UI", Font.BOLD, 14));
            setForeground(textColor);
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(10, 18, 10, 18));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(hover ? hoverColor : baseColor);
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Category selector chip tab
     */
    static class CategoryChip extends JToggleButton {
        private boolean hover = false;
        private final Color chipColor;

        CategoryChip(String text, boolean selected, Color chipColor) {
            super(text, selected);
            this.chipColor = chipColor;
            setFont(new Font("Segoe UI", Font.BOLD, 12));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));
            setBorder(new EmptyBorder(8, 16, 8, 16));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            boolean isSel = isSelected();
            int w = getWidth();
            int h = getHeight();

            if (isSel) {
                g2.setPaint(new GradientPaint(0, 0, chipColor, w, h, chipColor.darker()));
                g2.fillRoundRect(0, 0, w, h, 14, 14);
                setForeground(Color.WHITE);
            } else {
                g2.setColor(mix(chipColor, Color.WHITE, hover ? 0.78f : 0.90f));
                g2.fillRoundRect(0, 0, w, h, 14, 14);
                g2.setColor(mix(chipColor, Color.WHITE, 0.45f));
                g2.drawRoundRect(0, 0, w - 1, h - 1, 14, 14);
                setForeground(CLR_TEXT_MAIN);
            }

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Square Modern Icon Button (e.g. for Swap)
     */
    static class ModernIconButton extends JButton {
        private final VectorIcon.Type type;
        private boolean hover = false;

        ModernIconButton(VectorIcon.Type type, String tooltip) {
            this.type = type;
            setToolTipText(tooltip);
            setPreferredSize(new Dimension(42, 42));
            setContentAreaFilled(false);
            setFocusPainted(false);
            setBorderPainted(false);
            setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

            addMouseListener(new MouseAdapter() {
                public void mouseEntered(MouseEvent e) { hover = true; repaint(); }
                public void mouseExited(MouseEvent e) { hover = false; repaint(); }
            });
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setColor(hover ? CLR_PRIMARY_LIGHT : new Color(0xF1F5F9));
            g2.fillRoundRect(0, 0, getWidth(), getHeight(), 12, 12);
            g2.setColor(hover ? CLR_FOCUS_RING : CLR_BORDER);
            g2.drawRoundRect(0, 0, getWidth() - 1, getHeight() - 1, 12, 12);

            VectorIcon.paintDirect(g2, type, (getWidth() - 20) / 2, (getHeight() - 20) / 2, 20, 20, hover ? CLR_PRIMARY : CLR_TEXT_MUTED);

            g2.dispose();
            super.paintComponent(g);
        }
    }

    /**
     * Status Alert Badge (Error, Success, Notice)
     */
    static class StatusBadge extends JPanel {
        private final JLabel msgLabel = new JLabel(" ");
        private Color bgColor = new Color(0, 0, 0, 0);

        StatusBadge() {
            setOpaque(false);
            setLayout(new FlowLayout(FlowLayout.CENTER, 8, 4));
            setMaximumSize(new Dimension(350, 32));
            msgLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
            add(msgLabel);
        }

        public void showError(String msg) {
            msgLabel.setText(msg);
            msgLabel.setForeground(CLR_ERROR);
            bgColor = CLR_ERROR_BG;
            repaint();
        }

        public void showSuccess(String msg) {
            msgLabel.setText(msg);
            msgLabel.setForeground(CLR_SUCCESS);
            bgColor = CLR_SUCCESS_BG;
            repaint();
        }

        public void showNotice(String msg) {
            msgLabel.setText(msg);
            msgLabel.setForeground(CLR_PRIMARY);
            bgColor = CLR_PRIMARY_LIGHT;
            repaint();
        }

        public void clear() {
            msgLabel.setText(" ");
            bgColor = new Color(0, 0, 0, 0);
            repaint();
        }

        @Override
        protected void paintComponent(Graphics g) {
            if (bgColor.getAlpha() > 0) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
                g2.setColor(bgColor);
                g2.fillRoundRect(0, 0, getWidth(), getHeight(), 10, 10);
                g2.dispose();
            }
            super.paintComponent(g);
        }
    }

    /**
     * Styled JComboBox for Unit Selection
     */
    private static JComboBox<String> createStyledCombo() {
        JComboBox<String> cb = new JComboBox<>();
        cb.setFont(FONT_BODY);
        cb.setBackground(Color.WHITE);
        cb.setForeground(CLR_TEXT_MAIN);
        cb.setPreferredSize(new Dimension(190, 42));
        cb.setRenderer(new DefaultListCellRenderer() {
            @Override
            public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean sel, boolean focus) {
                JLabel l = (JLabel) super.getListCellRendererComponent(list, value, index, sel, focus);
                l.setBorder(new EmptyBorder(8, 12, 8, 12));
                l.setFont(FONT_BODY);
                l.setBackground(sel ? CLR_PRIMARY : Color.WHITE);
                l.setForeground(sel ? Color.WHITE : CLR_TEXT_MAIN);
                return l;
            }
        });
        return cb;
    }

    /**
     * User avatar with initials
     */
    static class AvatarPanel extends JPanel {
        private final String initials;

        AvatarPanel(String initials) {
            this.initials = initials;
            setPreferredSize(new Dimension(36, 36));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);

            g2.setPaint(new GradientPaint(0, 0, CLR_PRIMARY, getWidth(), getHeight(), CLR_ACCENT));
            g2.fillOval(0, 0, getWidth(), getHeight());

            g2.setColor(Color.WHITE);
            g2.setFont(new Font("Segoe UI", Font.BOLD, 13));
            FontMetrics fm = g2.getFontMetrics();
            int x = (getWidth() - fm.stringWidth(initials)) / 2;
            int y = (getHeight() - fm.getHeight()) / 2 + fm.getAscent();
            g2.drawString(initials, x, y);

            g2.dispose();
        }
    }

    /**
     * Vector logo generator (clean hexagonal/cube gradient badge)
     */
    static class VectorLogoPanel extends JPanel {
        private final int w, h;

        VectorLogoPanel(int w, int h) {
            this.w = w;
            this.h = h;
            setPreferredSize(new Dimension(w, h));
            setOpaque(false);
        }

        @Override
        protected void paintComponent(Graphics g) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

            g2.setPaint(new GradientPaint(0, 0, CLR_PRIMARY, w, h, CLR_ACCENT));
            g2.fillRoundRect(0, 0, w, h, 14, 14);

            // Vector arrow loop inside logo
            g2.setColor(Color.WHITE);
            g2.setStroke(new BasicStroke(2.2f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
            int pad = w / 4;
            g2.drawArc(pad, pad, w - (pad * 2), h - (pad * 2), 40, 200);
            g2.drawArc(pad, pad, w - (pad * 2), h - (pad * 2), 220, 200);

            g2.dispose();
        }
    }

    /**
     * Vector Icons rendered procedurally via Java 2D Graphics
     */
    static class VectorIcon implements Icon {
        public enum Type { USER, MAIL, LOCK, EYE_OPEN, EYE_CLOSED, SWAP, COPY, CLOCK }

        private final Type type;
        private final int size;
        private final Color color;

        VectorIcon(Type type, int size, Color color) {
            this.type = type;
            this.size = size;
            this.color = color;
        }

        @Override public int getIconWidth() { return size; }
        @Override public int getIconHeight() { return size; }

        @Override
        public void paintIcon(Component c, Graphics g, int x, int y) {
            Graphics2D g2 = (Graphics2D) g.create();
            paintDirect(g2, type, x, y, size, size, color);
            g2.dispose();
        }

        public static void paintDirect(Graphics2D g2, Type type, int x, int y, int w, int h, Color col) {
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(col);
            g2.setStroke(new BasicStroke(1.8f, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));

            switch (type) {
                case USER:
                    g2.drawOval(x + w / 4, y + 2, w / 2, w / 2);
                    g2.drawArc(x + 2, y + h / 2, w - 4, h / 2, 0, 180);
                    break;

                case MAIL:
                    g2.drawRoundRect(x + 1, y + 3, w - 2, h - 6, 4, 4);
                    Path2D mailPath = new Path2D.Float();
                    mailPath.moveTo(x + 2, y + 4);
                    mailPath.lineTo(x + w / 2, y + h / 2 + 1);
                    mailPath.lineTo(x + w - 2, y + 4);
                    g2.draw(mailPath);
                    break;

                case LOCK:
                    g2.drawArc(x + w / 4, y + 1, w / 2, h / 2, 0, 180);
                    g2.drawRoundRect(x + 2, y + h / 3 + 2, w - 4, h / 2 + 2, 4, 4);
                    g2.fillOval(x + w / 2 - 1, y + h / 2 + 2, 3, 3);
                    break;

                case EYE_OPEN:
                    g2.drawArc(x + 1, y + 3, w - 2, h - 6, 30, 120);
                    g2.drawArc(x + 1, y + 3, w - 2, h - 6, 210, 120);
                    g2.fillOval(x + w / 2 - 2, y + h / 2 - 2, 5, 5);
                    break;

                case EYE_CLOSED:
                    g2.drawArc(x + 1, y + 4, w - 2, h - 8, 200, 140);
                    g2.drawLine(x + 2, y + 2, x + w - 2, y + h - 2);
                    break;

                case SWAP:
                    g2.drawLine(x + 2, y + h / 3, x + w - 4, y + h / 3);
                    g2.drawLine(x + w - 8, y + h / 3 - 4, x + w - 4, y + h / 3);
                    g2.drawLine(x + w - 8, y + h / 3 + 4, x + w - 4, y + h / 3);

                    g2.drawLine(x + 4, y + 2 * h / 3, x + w - 2, y + 2 * h / 3);
                    g2.drawLine(x + 8, y + 2 * h / 3 - 4, x + 4, y + 2 * h / 3);
                    g2.drawLine(x + 8, y + 2 * h / 3 + 4, x + 4, y + 2 * h / 3);
                    break;

                case COPY:
                    g2.drawRoundRect(x + 5, y + 1, w - 6, h - 6, 4, 4);
                    g2.setColor(col);
                    g2.drawRoundRect(x + 1, y + 5, w - 6, h - 6, 4, 4);
                    break;

                case CLOCK:
                    g2.drawOval(x + 1, y + 1, w - 2, h - 2);
                    g2.drawLine(x + w / 2, y + 4, x + w / 2, y + h / 2);
                    g2.drawLine(x + w / 2, y + h / 2, x + w / 2 + 4, y + h / 2);
                    break;
            }
        }
    }

    /**
     * History item record
     */
    static class HistoryEntry {
        final String category, fromUnit, toUnit, resultText, timeStr;
        final double inputValue;

        HistoryEntry(String cat, String from, String to, double inVal, String res, String time) {
            this.category = cat;
            this.fromUnit = from;
            this.toUnit = to;
            this.inputValue = inVal;
            this.resultText = res;
            this.timeStr = time;
        }
    }

    /**
     * Custom List Cell Renderer for conversion history items
     */
    static class HistoryCellRenderer extends JPanel implements ListCellRenderer<HistoryEntry> {
        private final JLabel inLabel = new JLabel();
        private final JLabel outLabel = new JLabel();
        private final JLabel metaLabel = new JLabel();

        HistoryCellRenderer() {
            setLayout(new BorderLayout(6, 4));
            setBorder(new CompoundBorder(
                    new EmptyBorder(4, 4, 4, 4),
                    new CompoundBorder(new RoundBorder(CLR_BORDER, 8), new EmptyBorder(8, 10, 8, 10))));
            setOpaque(false);

            inLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            inLabel.setForeground(CLR_TEXT_MAIN);

            outLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
            outLabel.setForeground(CLR_PRIMARY);

            metaLabel.setFont(new Font("Segoe UI", Font.PLAIN, 11));
            metaLabel.setForeground(CLR_TEXT_LIGHT);

            JPanel top = new JPanel(new BorderLayout());
            top.setOpaque(false);
            top.add(inLabel, BorderLayout.WEST);
            top.add(outLabel, BorderLayout.EAST);

            add(top, BorderLayout.CENTER);
            add(metaLabel, BorderLayout.SOUTH);
        }

        @Override
        public Component getListCellRendererComponent(JList<? extends HistoryEntry> list, HistoryEntry val, int idx, boolean isSel, boolean focus) {
            if (val != null) {
                inLabel.setText(formatDecimal(val.inputValue) + " " + unitSymbol(val.fromUnit));
                outLabel.setText("→ " + val.resultText);
                metaLabel.setText(val.category + "  •  " + val.timeStr);
                setBackground(isSel ? CLR_PRIMARY_LIGHT : CLR_BG_CARD);
            }
            return this;
        }
    }

    /**
     * Custom rounded border for general panels
     */
    static class RoundBorder extends AbstractBorder {
        private final Color color;
        private final int radius;

        RoundBorder(Color color, int radius) {
            this.color = color;
            this.radius = radius;
        }

        @Override
        public void paintBorder(Component c, Graphics g, int x, int y, int w, int h) {
            Graphics2D g2 = (Graphics2D) g.create();
            g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
            g2.setColor(color);
            g2.drawRoundRect(x, y, w - 1, h - 1, radius, radius);
            g2.dispose();
        }

        @Override
        public Insets getBorderInsets(Component c) { return new Insets(4, 4, 4, 4); }
    }

    // =========================================================================
    // EMBEDDED LOGO DATA (Base64 JPEG)
    // =========================================================================
    static class LogoData {
        static final String FULL =
                "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAQDAwMDAgQDAwMEBAQFBgoGBgUFBgwICQcKDgwPDg4MDQ0PERYTDxAVEQ0NExoTFRcYGRkZDxIbHR"
                        + "sYHRYYGRj/2wBDAQQEBAYFBgsGBgsYEA0QGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBj/wAARCAFA"
                        + "AUADASIAAhEBAxEB/8QAHQAAAgEFAQEAAAAAAAAAAAAAAAEHAgQFBggJA//EAFkQAAEDAwIDBQQDCgcLCQkAAAEAAgMEBREGBxIhMQgTQVFhFC"
                        + "JxgRUykRYjQlJicoKhsbIJM0Ois8HRGCQnNURUY5KVo7QlU1Vkc3SDwuEmNDY4ZWekw9L/xAAcAQACAgMBAQAAAAAAAAAAAAAAAQMEAgUGBwj/"
                        + "xABBEQABAwIEAwUGBAMGBgMAAAABAAIDBBEFEiExBkFRE2FxgZEUIqGxwdEVMlLwB0LhI2JygpLxFiQlMzQ1Q6LS/9oADAMBAAIRAxEAPwDhAI"
                        + "SCqW0VVBQl4I8U0k0IQiySXhzQmjxTQhCEIQhCEIQmkmjKEJYTwhNCSQTSTTQhCSOaEJoQjOU0ICPFJNCSE0in4IQkhHojoUIR8U0IQhBQhCEI"
                        + "QhCEI8EIQhCEIS5oQhCaSEKgISB5J+KjUiPFPCXqnnkhJCEIWSSAjxQhCEIQhCEIQjkhCFUkhCE0ISTSQjKEBCEeKaRTQhCEITQhNLKMoSTQgp"
                        + "IQmEICeChCSE0kIQhHgjwQhCEIQhPxQUk0ISQn4JIQjCMIQUIXyGEKkJqNSKrKEk0ITQkhNJNCEkJJoykhNCaEZQhCE0kZyhCYTVIQhCaEIQkm"
                        + "hJCaE0JZRlF0JpqnKaEIQEITSVQwglU5RlCE0IQhCEIR4IQhCEIQhNJNCEJJoQhIpKpUoQvgqlRlPKhUpCrzyTVKAmkqkJJ+KaEISTQkhCEJoR"
                        + "4oQmmkhLKE0IQhLBQhCfihJCV0JhCXVZvTuj9T6tqjDp2yVlwwcOkjZiNn50hw1vzKya0uOVouVHNNHCwySuDWjck2HqVhUw0lzWgEknAA6k+Q"
                        + "81P2lOzVJLwVOstQNhHU0NrAkf8AB0zhwj9Frvipx0pojRejA12nNPUlLUAY9skHfVB/8V+SP0cBbyl4drJtXjKO/f0/2XC4t/EXDKK7YLyu7t"
                        + "B/qP0BXLOlNiNyNVxx1DLN9EUT+Yq7s407XDzazBe75Nx6rHat2j19ozvJbpYpZ6Jn+XUP3+HHmS0Zb+kAu5BU8RLnuJcepJySvq2byOPDktwe"
                        + "F4gy2c5uvL0/quCP8VsRE+cws7P9Ot/9V9/K3cvN7IIyCCPMIyu69VbQbe6y45rjYoqWtf8A5bb/AO95c+Z4Rwu/SBUHas7L+o7f3lTpG6096g"
                        + "HMU1TinqAPIH6jvtb8Fo6rAqqDUDMO77Lu8I/iRhFdZkzjE/o7b/UNPWygVNZC9WC96cuLqC/WqsttSDju6qIsJ+BPIj4ZWO5rUEEGxXeRyMka"
                        + "HxkEHYjUFNNJCFkmhCEIQhHihCEJpJoQhJNGEIQfgkmUihCtU1TlGcKup7KsFVKhPPNZXSsqs+aYKpQE7pWVSapynnl0TSTQkOiMoQmhJGUITy"
                        + "hLKMoQnlCWeaEITHM4HipJ05slq+88E90ENipXgOD63LpXA+LYm8/9bhUaLYdP641TphzRaLvOyAH/AN2l++Qn9B3IfLCuULqUSj2sEt/u2+v3"
                        + "C1mKx1z4bUD2tf8A3hf05DzBXQunNm9C2IMlrKSS+VTefeXA/egfSFvu/wCsXKR46lsdPHTxMZHDGMMhjaGMYPRowB8goI0/v3Sy8MOprW+mf0"
                        + "9oosyM+JYfeHyJUl2jVNmv9P39nudPWNxkiJ/vN+LTzHzC9WwV2EyNy0JaD0/m+OpXieO4bi+fPiWZ3edW+VtB4aeC3JtaB4r6tr+fVawKz1VQ"
                        + "rceK35pLrmzQ3W3x148XK4ZW8+q0xtfjo5XMNy/KUD6JVn4cei3SOsB8VeR1II6rTYbjnxV3Le6Sgon1lfVw0tOwZdLPIGMb8zyVCalyC50C18"
                        + "mHuJsBqtkuFBa71bn2+72+lr6R4w6CqibIw/I9Pkoh1V2Z9F3kPn01VVGn6o8xGCainJ/MceJvyd8lZak7R+k7Kx8FhgnvtUOQdGe5gB9XuGXf"
                        + "oj5rSdI70a71xvfpq21tfHQ2ya4Ma+hoGd2x7cHk9xy5/wAzj0XH4jWYdI8ROAe4m2n3+y7LA+HeJaGJ9ZTPMLGguOY72F/ya3/zAeKjLXegrl"
                        + "oC/fRlyudornnJa6gqmyED8tn1oz6OC1VVTnNXMT1MjiT58yqFxLy0uJaLBe/0rJWQtbO/M+2pAtfyubeqaaXinlJTppIQhCaSEIQnlGUk0IQk"
                        + "UIQhWaaSFWVhVBNUhNNJVeKap8E/BZJJppBNMJI8EIykhJGUA559fVNvJwwcHIXqpuz2ONtN2LQ292mFuk9TywNea+3xDuKh/COc0Aw12fFzeF"
                        + "3mSo3yBhF1m1twvKrwTUnbt7Abl7L3R0er7I51sc/ggvNHmWjn8vfx7jj+K8A/FRh6YWQcDqFiRZNCSayQl4IBR4pJITysvpu11t2vfc2+sNJU"
                        + "xxOlZMC5pyCOWW8x16qvSej9T661VTab0jZKy8XSoP3umpWcTseLnHo1o8XOIA8Sui752cbrsZNpSo1PeIKu9X2lrXVFHStzDSNj7ktaHnm92X"
                        + "nJwBy5Z6rYYRDHUV0MEmziAqWJSugpJJm8gVE8W4GvtJzNp7/TivpwcCScc3D8mVvX5glbrZd1dNXctinqH22oPLu6vk0n0eOX24WfktFNVQOh"
                        + "liZJG4Ycx7Q5rh6g9Vo2oNn6Ksa6ewyewTn+Rfl0Lv62/rHovU30mM4Z71HJ2zB/K/fyOl/UeBXnAqMIr/dqY+yf+pu3mP6eaktlYHsa+N4e13"
                        + "NrmnIPwPisbeNa2DToxdbnFHLjIp2HvJT+gOY+eFCVNpTcy2XQ6ct9JdojUDpTSkQvb4u4weEDzOQVKOitgKCNzazWlYa2XIcaKleWx/pyfWd8"
                        + "sfEqMcT4hVjsqWkyvG5cTYH0BP70VaswvCsPHa1dSHNOwZYucPUgeenesdFuZrPWFxdaNuNL1E8p5GokZ3rmDzI+oz4uJUg6U7Nd41RXxXbdzW"
                        + "FVVkHiFuoZeLh9DIRwt+DG/NSlYaK12W3R2+00NNQ0rPqwU7Axo+Q6n1PNbZQ1XCQMrVVWHT1HvV0peemzR5BcRX8Zvp3ZMKhELf1fmef8x28B"
                        + "t1XDO/2mrFo/f676e01QMobZTQUndQNc5wBdTsc45cSSSSSST4pbC2yG5b8WWSesbTsonmsxjJlLeXAP9bPwCyHabeH9pm+OBzmnov8AhY18+z"
                        + "9Ewbm+1vDeJgjiZn8Z7/7Grj8MpBPiTYeWY/C5+i9mrah7uGzLm950Q13N3NA5+K0jWtjg0zuLe9P01xZcYaCtlp2VTBwiUNd1xzwfAjzBWDBU"
                        + "jb+UVPRdofURpGMbBVSR1rAwYB72JryR8SSVHHMdeXxWslbke5vQlb3DKg1NHDOd3NafUAqrKFSCD0IPwKYPPCxurtk0IQE0k0IyhCEJpIQhCE"
                        + "eKChCsk0k1VVhCqCpTWSSaaSfimEkwUyqQmmhNCSM80JJj6w+IXvFa/wDEdH/2Ef7oXg6PrD4r3itZ/wCQ6P8A7CP90KtUclLGq6+30F1ts9uu"
                        + "dFT1tHUMMc1NUxtkjlaerXNcCCPQrzW7bmwu3+09Vp7UuhaKe1svdRPFUW1snFTRljWuDogebM8Ry3JHkAvTDK4d/hIj/wCxm3//AH+s/oo1FE"
                        + "SHBZOFwvPZJBPkmrygQugthOyTr3eeSnvda1+nNIOPEbtUx5kqmg8xTRnHH5cZw0flYwufD9R35p/YvcbbeOOLZrSUUbGsYyy0bWtaMBoEDOQU"
                        + "Mzy3ZZsbdYva7Z/QWz2lRY9EWSOkDwPaayX75U1bh+FLIRl3oOTR4ALnztqSCPWe3ZP+b3Qf8Ouv1xr245O71Vty/P8AI3Mfqp1suGD/ANWp7/"
                        + "qCpYy3NQzN/ulQPSTg45rM05Y4c1pVLXNY0vc8BjRkuJwAB1JWuSan1Zr2/nSe2tPI7I+/3EHgDGdC7jP8Wz8r6x8PX3jF8Wp8OiD5Tdx2aNye"
                        + "4fVeNDCJKhziCGsbq5xNmtHUlSHf9xNK6RjdHcK8SVQ5+x0w7yX5jo39IhaxRbh7qa0kI290FL7M44FZURmRo9eNxbGPtKkTb7s/6O0u2K439j"
                        + "NRXnPG6Wqbmnjd+REfrH8p+T6BTMzgbG2NgDWNGGtAwGjyA8FxM9ViVabyO7JvRup83fZc3W8Q4Rh7iyjg9ocP533DfJg1I/xELnGn247Sd2aJ"
                        + "K/XVvswP8myr4S35Qxn9qvYdoO0FSffaLeWhdL4MlrqloPzdEQuhE1QfhDXauleT/iWubx/Vt09mht07MW+a4z1/sZv7WXeq1PfrFPqiZzGCau"
                        + "s80VcS1jQ1pdHF74w0DmWeC03b3VdFonU7JbxS1gEVbFLM2NnvsEfFlpaSDnJXoBHI+KVs0Ujo5GnLXsOHD4ELB620Rojc6gdTa8sjamrLeGK+"
                        + "0QbFcYD4HvMYmaPxJQ4eRHVa0YTVUEvtNG7M4X331Fj3H4LucM/iXh9fD+H4pD2TCLXZ+UDlpuAO6/guFtf61odXa7p79R2vu44qSGmfFWe+JT"
                        + "GCOIhpGAQW8s+Cv7Fuq2ylv+DjQFa0f5zaOJ3+tx5V1u7slqTae6RVE8rLvpyseWUF8pmFscpxnu5GnnFMBzLD16tLhzUZhcwZJBIXO0dzXqsO"
                        + "HUU1JHHH70VhlsSRblqCustt9yNmtxr1T6Z1Xtdpyy3OrcIaeSOkjdTzvPRgcGtdG4+GcgnlnOFlt1OyxYKjTdZe9u6eW3XKmjdMbWZHSQVIaM"
                        + "lrOIksfgHHMgnlgZyuXNB2G5an3KsNhtDHurKuuhZGWdWYeHOf6BrQXE+i9QNR3K3aZ0jdNRXSVsdFQU0lTK9xx7rWk4+JOB8StxTPbPE4TNGn"
                        + "O2q834gpp8Cr4n4XK4B27C4uG45EnQ7d1tD08nk+qrkeZZny8PCXuL+Hyyc4/WqfVadesowhJNCEITQhCEkykhCsueU/FJNVVYQmEk0whNNLwQ"
                        + "msU00kLIIVSSEvBCFUPrfNe7ttdizUYz/IR/uBeEI6he6dDNi1Ug/0Ef7gVao5KSNZXjGFw/8Awj54tFaAP/X6z+ijXanfeq4m/hGH8ehdAn/6"
                        + "hWD/AHMShj/MFm7ZefiEIV9QIP1Hfmn9i9ydvjw7SaWaeotFJ/QMXhqfqu/NP7F7iaGfwbXabbnpaaT+gYqtRyUka2njXF/bzc6O87cTj6vDc2"
                        + "/zaddjd96riT+EPr3UVp29qo/4zvLkxh8iWQc/61dwSpZTV8M8mzXAnyUVZGZYHxjchchvF511qun0Rps470/31MfqMa3m4vI6MaOZ8zgLqbRW"
                        + "lrHofTEVlscPCwYfPUPH3ypkxze8+fkOjRyHiTGOyWlI9NaFbeKqMi6XhrZ3ud9aODrGz5/XPxb5KVI6rHivV8PpZatxxKqHvv2H6W8gPmf918"
                        + "/8Y4mamQ4fTG0MZ1/vO5k9bbD18NliqfVXsU4IHNaxFV5PIqPNwd+LHoieS02yFt4vLOT4mv4Yac+UjxzLvyRz8yFJiEkNIztJnWC4ijwKqxGY"
                        + "U9Iwucfh3k7AeKnRrxhMyDzXC137QG6d2mc6PUbrZEekNuhbEB+lguPzK+Ns343VttS2X7rKitYDzhro2Tsd8cjP2ELlTxLTZrZTby+67Afwjx"
                        + "UszGWPN0u755V3WZR5pd8PNQTtv2hLXq2sisupKeGz3aQhsUjXn2eod+KCebHHwByD4HPJSxcr5brPa5rldq6GipIW8Uk87uFrf/X0HMroKWpg"
                        + "qY+1idpz7vFcRiPDtbh1R7LUxkPO3O/hbfy8N1nahluutjrbDfKCK52a4R9zWUE2eGZvgQRza9p5teObSAQuDdxtCUmld36vSOlbqNR07pWijd"
                        + "TDvJ/f+rBI1v8ALN+q4DkTzHXAmS4bg7g7x36XSG0Ftq6a3Z7uruz8xHgPLLpP5Fh8hl7vTopZ0Vt1tj2cNKnVurLzTzXktLHXKob7xcRzipYu"
                        + "bufiRlx8SByXJ4oYK2XNCNBu7kfv4r1ThWWs4ZpzDVuzOf8AkhGrgTzP6b/p1J8QVc9nTYmHbGzv1fqoRfdNVQEFjnAstsOMuZxdOMge+7oAOE"
                        + "cskwx2md/4NdTu0Jo2r49OU8ofWVjDgXCVp5Bv+iaeYP4TufQDOubz9pDUW5TZrBZGS2XS5OHUwd9/rB5zOH4P+jHLzLlB5OVqZpmhvZxbfNdr"
                        + "g+CVEk/4liZvKdm8m/1HIct9TsISQqa7BCaQTQhNCSEITSQhCFZJpJqqrCMp+KXimsghNCEIWKE0k00ICChCaENPvAeq9v6WqDaCnHF0hYP5oX"
                        + "iHGx75AGtc7x5DPJezTa7u2MZnmGNH80KvML2WbCto9sHmuNP4QuXvdA6EOelyrP6GJdS/SQAyXLkjt6VIqNvNFHPNtzqv6GNRtFiFkTouF8p5"
                        + "VKFbUSCfcd8D+xe2+kZwzbzT7c9LVSf0DF4kH6rvzT+xez2mqwN0PY256WykH+4Yq82pCkYty9qB8Vx3277YdRT7VWgH3am6VkTyPBhZBxH/AF"
                        + "QV1L7cM/WXNvarkZUam21kP8jPdJB6H2eID9qs4XSiprIYDs5zR6kKpic7oKSWVu7Wkjxtoo3bUtBAjAawDDWjo1o5AfIABfdlX6rXW1eD1X2Z"
                        + "VgnqvpE0oAsAvnR9IsJuxuBPpHRzae2Td3dLiXRQSDrCwfXkHqMgD1OfBcsOc57y57iXE5JJySVIG81ylrtz5KdzyY6OmihYM8hlvG79b1H2F4"
                        + "ZxPXOqsQkaT7rCWgeGh9SvauD8KjoMPY5o96QZifHYeQ+N+qE1k7Fpu/6nuTaDT9oq7lUE44KaMu4fVx6NHqSFO+k+zIKahF73O1HTWigjHFLT"
                        + "U0zQWjyknd7jf0eL4rUU9FNUf9tunXl6q9i3EWH4UP8AmpQHHZo1cfBo189u9QDa7Tc73dYrZZ7fU19bMcR09NGZHu+AH7V1bpzYfVWpbdR3rf"
                        + "XVklPabZA3gtbKlrSxoH1p5vqsOOpGXH8YKxrt89rNrbVJY9pdMwV9UBwvrcGOF583yn75N+oeRUC633P1ruFV95qa9SzwB3FHQxfeqaL82Mcs"
                        + "+pyfVXQIKUFpdnJ3A/L5nmuce/Fsdc18UXs0Y2e8Ay675R/JfnrfoV0nqztKaG290+3SWzVkoqnuAWtqWxGOiiPi5o5Ond+USAfMrlzVGr9Sa1"
                        + "1DJe9UXeouVa/l3kzuTG/isaOTG+gACwWc9SmFVlqny/m26clv8J4fpMMBdELvO7nauPn9rKvKeeSoTChut1ZP5p/qVKaYSVXghJCaSaEk0IQh"
                        + "CChCsvFCAhVVYT8U0k/BZBCaEIQkUIQhZJJ9Vu+3G2GoNyL4aW2NFNQQuHtdxmaTHAD4D8Z5HRo+JwOastu9E124Gv6LTlG8wskzJU1GMiCFv1"
                        + "n48T0AHiSF3Xb7HbtG6CNq03RNp6egpZHQRAZc54YXZcfwnOcBk+JW5wnC/aiZJNGD49y894440/BWtpKWxnftfZo2ue/oPM6aHk/dG5aa29t9"
                        + "XtfoGBrnloZfrvKA+eqcMO7gO/BaDguDcDOB4En0K+lo3RRvikyx8bHtPoWAj9RXkpPPNWVMlTVSOklmcZJHuOS5zjlxPrklegm12sxqbZfTN1"
                        + "77jnFAykqOfMSwDuXA+pDGu+DgtTUydtJmAsOQ6BdjhOHigpxGXFzzq5x3c7mT8gOQsBoFMjrt+UoG7V+na3V+xP0pQRGabT1WLhKxoyfZ3NMc"
                        + "rgPycxuPkA4+CkD6UcerkfTL43ZjLScEFr2hzXAjBBaeTgQSCDyIJBUPZrY5l5l9CQmusNedmPTl9rprtoG/0unp5SXyWe6NkNI13iIJ2BzmN8"
                        + "mSNOPxyFo9F2T9eTVLG1+pdHUMDnYMouTql2PMRxMc4/PCL9QnZRJovSly1zuBaNI2iJ0lVcqllO0gcmNJ9958mtaHOJ8A0r1kZcKeCGOmpHcN"
                        + "PCxsUWfxGtDW/qAXPm223Wh9lLZPNRVvtl4qYu7rL9cQ2nPd8iYoGE4ijOBxEkudgZIHuq6vO/22di4vbNY0U0jc/e6HiqXH09wEfaVgWk6lMG"
                        + "2gU+G5gD66557Tt2jF828jc733zXNoHnmCH+sLUantVMuU7qbQegL9qCbpxyDumfYwPOPjhaPq62b27x3q1XPUVJaNKw2vvDRNa93HH3hbxEgF"
                        + "znO90DnhbbBop2VkVRFGXBjgdB0N99lpcZxGihp5IqmZrMwI1Ouo6b+gVU1yp6SDvqqpigjH4crw0faVrlfufYqWUU9vNRdalxw2KjYTk/E9fl"
                        + "lbAzZDRlogF03D1dU1rxzcamoFLD8skuPyIX1j3P2y0ZG6i0Bpn6Qmb7vfUcAgjd+dPIOJ3yBXq1ZxHXPORjWxX6++/wAmjT5rzCF1HMbUcT6g"
                        + "9QMjPNx1+AWgnarcHcrVUt+msTNPU9QIwX3GQt5NaG5DMcZJAz0AW60+0W0u3kDK3cXUza+oA4hTSPMLHfmwsJkf8zha3qHcvczUtLNHb7hT2V"
                        + "jm4ZSW0ESSfkmZ3vZ+GMnkoUqZKmWrlkrHyvqC4iR0xJeT48RPPPxXBYkyOhk7WSEue+5zPsATfX3Rp5GxC6ikw/FsSYIpqkQRtAGSL81uV3nU"
                        + "eIuCuhb12kbZZLabLtnpWmoqZnusqKqIRsHq2BnX4uPyUKam1rqnWVf7XqW91dwfnLY5HYjj/MjGGt+QWAT8FoamvnqdJHadBoPRdFhXDWHYWc"
                        + "9PF753cdXHzOvpYIOfFNZnS2lL5rLUcNlsNGZ6iTm5x5Rws8XyO/BaPP5DJ5KYtyOzhV6a0PT6i0jXVF6FHTj6Xp3MxI0jm6eJo5mLzb9ZoGeY"
                        + "JxhHSTSRuma0lo3Kmq8doaSqjop5QJH7D79L8r78lAoTBSCYUIW2KaYQhCSaYSQslimmEk1ksUeKYSRlCE0ISwhCs0IQqqspoQEJhJVISTTWJQ"
                        + "jxQjwTQujeyZJQM1Bqdkhb7e6mgMQPUxB7uPH6RZn5LqMuwclecmntQ3jSuoqa+WKtfSV1O7LJG8wQeRa4Hk5pHIg8iujdNdqm2zU0cGr9P1FP"
                        + "OBh1TbSJI3Hz7txBb8iV12CYtTwwiCY5SOfI3XiHH/BOI1mIOxGjb2jXAXA3FgBoDuDa+mt76K53G7N9Jfb1UXnRdfT2uadxkloKlp7gvPMljm"
                        + "glmT+Dgjyx0WA0faN8dqbbW2e36Npb1Q1E4qMMqWyBjwOElnC4EcQ4cgj8EKTaff8A2rq2A/dI+nP4tTSSsI+xpC+km9W1xGfuxonf+HL/APwr"
                        + "c2G4VUHO2UNJ6OHyKrYZxHxZh8YgkpnSAaDNG6/qLX87rTnbl7ztPC7Zyfi6cnyYVD9eb7TNxS7V08Dj0dO93L7ZApBs+4ekNSNqHWC6m4inx3"
                        + "3s9PITHnoSC0YB6Z6LJR18lSc09rrXjzexsY/nFV/wbDG6uqdO4j+q6KLiriWbRlBY97Xj5kKKm1PaQuR4u507ZgfF3dkj9byk7QO8l4PFft2p"
                        + "KVp6x29rxy9OEMCmKnobzWPwKalp2+cs3EcfBo/rWeotI9+5oq7q4DxbTxBv63ZUbo8Cg3c5/wC/JXWP4wq/5Y4vQ/8A7UAQdn7T9RL7RqPUl+"
                        + "vUviZZgwH5niP61t1i2n0Bbpmx2nSdJWVA6GSN1ZJn4O4v2KfLdpDTEJa6Wh9rc3xqpHSD7On6ludC+mooRHRwRU8Y/BhYGD9SrOxyig/8SlF+"
                        + "rtf36q23hLE6v/2OIPI5hmg+3/1UQWjb3V1VGynotPihgOOE1Tm07SPRgyf1LlPebdjUlq3JuuktLajp22+2y+yvrbdHjvpWjEnDI7nwh2WgjG"
                        + "eHPiuj+0v2j6TRGm6vQ2jq4SatrGGKpqIXZFqjcOZJH8s4HDW/gg8RweELz5w4kk5J9VSnx6vqRYvyt6N0/qttQ8GYRRHM2IOd1d73wOnwWz22"
                        + "Z18llrLlNLW3Fpy+WqkdK9zSeTveJ+H2LZKOgJxkclHtDVT0FfHV07g2SM55jII8QR4gqV9P19De6M1FGOB7AO+pycui9fVvk77ea73gytpqlg"
                        + "pX2bKPLN39568+fWxjDX04zsHufLu8OnoryhtzeWQsjX6M0/qONv0pTyR1A5e20pDJSPJ2ch/zGfVfanYGLKU8mAvQ6jCaepiMM7A5veuKnrJm"
                        + "vzxOII5haHNsgx8hdRasiDM8m1NE9rv5jiFlLPsbaI52yXvUFRVsBz3NHCIQ70L3En7At3jn6c1dx1OMc1zo4Gwpj84jJ7i42+aim4ixUsydsf"
                        + "Rt/UBbDpqjs2mbWLbYbfBQ02Q5zYh70jvxnuPN59SfhhbpbL/JTyNeyUtc05BB5hRvDV48VfRV5BGDzWwfhUTWdmxoDRyGy4etoTUOL5CS48zq"
                        + "fVaTvPsnTV7KnWu31EGygOmuNlgby83TU7R4dS6MdOZbyyBzWPNdLaz3uh0vHJb9OzMrLyDjvQcxUrvMkfWcPxR8/Jc31dVUV1wnrauQy1FRI6"
                        + "WV5AHE5xyTgcuZPgvG+IaWkp6ospXX6gbA+P05L2bguTFHUeXEBdotlJ/MR3j5E6nv3XzzyQkhaFdkqkJZQsgsVUmllATCRT6oQhNJCaSMoQrJ"
                        + "PmkmqqsoTykE0wkmmqUx0TSTQhCYSQmhP5poSyU8n1QOiqGEwEiVf2O/3nTV6iu1iuM9DWRdJYj1Hi1wPJzT4tIIK6F0h2kbRUsjp9ZWt9tmAD"
                        + "TWW+MyQvPmY88TP0S4eQC5uaGeK+7GU5PvkqRkROxWDngbhd32HcXRN3hbLbtWWebPRhqmxvHxa8gj7FtsOprJTxh0t7tkYxnifWRAfvLzwiht"
                        + "byO+c7HwBWQio9NuGXSSA/mBbCLC3S7PaPNVX1bWfyn0XdN5352w01CXVur6GqlH+T24mrkJ+DMgfMhQTuL2ttR3qlltO3tJNYKSRpY+4zuDqx"
                        + "48eDHuxfEcTvIhQvHQabOAKmQD1AWSpLLpWQjvbkY/0Mq5Hw85x/O31VaTFmMF8rvQrRpBLPM+aZ75JHuLnveS5ziTkkk8ySfFLuD5KX7fpfbm"
                        + "Qj2vUksfwgz/AFrYodGbMuizLrOqa7yFJn/zK+3ht9tXj9+C003FcERsY3nwY77Ln7uT5L7Uk1bQVjKqhnkgnZ9WSM4I/wDT0U0V+lNrYsmj1V"
                        + "NJ+dTY/rWt1dn0hC49xdTIPzMLIcPPjIcJACO+ylh4ihqBYRuserSPmF8LRuEzgbFfKJ7ZOntNI3kfV0Z/8p+S26j1BZavBpbtSPOPquf3bvsd"
                        + "grSe400w/wAcXfJGdJN/jBI74YXbYZjFfTtDJp2PA/UdfUfW61lTQU0xzRsc3wGnofupJZWxEe7NGfg8FOS9W6lbxVNwpYgPx5Wj+tRhLPo9rf"
                        + "chmz8li6mpsYJ7iEj44WxqeKuzbp2d/wDET9FVjwBsh1zf6f6qSbjubp63scKeSavlHRsDMNP6RwP2qPdR7kajvsT6WGQW6jdyMVM48Tx5Of1P"
                        + "wGAsJPPSOPuNwrJ74z0C88xriSurQYzKA3o3T47/ABsuiw/AaOmIf2d3dXa/Db4K0wQjHmvo4hUZC40iy6MFLKEIWKaq8OaaoTQhMKpUphZBYl"
                        + "NNJCySTQkhCSs00JKqrSaElntF6QvOvde2vR+no4ZLpc5vZ6ds8ojYXYJ5uPTkCgmyVlg0ZUnbtbBbi7K01qqNd0dBTsurpWUvstY2o4jGGl2e"
                        + "Hp9dqjejpZq64U9FThplnkbEziOBxOIAyfDmQgOBF0WXyCamTc3svbsbR6Edq/WdBa4LW2oZTF9NXsnfxvzw+6B090rI1HZC3vg2uGvorBQ1dq"
                        + "NvZc2spa5ks74HMEmWxAZJ4TnhHPkeqQkb1RlKgpPKpPLmpqn7K+7tPsod1JLdahp36LF470XBhl9nLA8Hgxni4T0+SyLwN0rXUL5RkqZ9tey1"
                        + "u1uvoOPWGjqC1T2ySaSBrqi4MhfxxnDgWkeao207MG7G7Wk6jUmjqC1zUNPWSUMjqmvZC4SsDS4cJHT3hzS7VvVGUqHA5VB66U/uD+0Jj/FFh/"
                        + "2tH/YtK3P7MW6+0GiGar1nQWuC2vqmUYfTV7Jnd48OLRwgdPdPNMTN6pFhURB/qq2yHPVWwK3HR22OttdRmo0/ZnyUbXFrqyd4hgBHUB7up9Bl"
                        + "Wos8jssYue5U6upgpIzNUPDGjmSAPUrWmzOHivq2qcPwipKufZ03Pt1AaqO30FwwMmGiqw+T5NcG5+AUYPobhHdja5KKpZXCTuTSuicJePOOHg"
                        + "xniz4YViRs8BAkaR4qnRYnQYgC6lla+29iDbx6K49tkH4RVQr5fxz9qkS09nbdC60jaiW2UVtDhlsdfVCOT5taHEfPC1vWO12uNCsE9/szm0ZI"
                        + "AraZ4mgz5Fzfqn87Cmc2qjbnc0geBVaDGcKqJvZ4qhjn9A4E+XXyWve3Snq8/aqTWPP4SyGj9IXvXOpfoLT8cElZ3L5+GeURN4W4zzPjzHJXOt"
                        + "dAao2/uNPR6loo4TUxmSGWGUSxvAOCA4eI5ZHqPNRdrMWdpY5evJXDVUbakUZkb2pF8txe3W26wZqXH8Ir5mcnxWb0XofUGv77NaNORU8lTDAa"
                        + "h4nmETQwODTzPU5cOSz8ey+uJde1GjmRWw3aCkbXOiNa0AxOOMtd0JB6jwSaJ5AHNaSDp5rCfFKCmkdFNM1rmjMQSAQOp7loRlPmqTIs5rPRWo"
                        + "NB6hZZtRwQxVT4G1DO5lEjHMcSOTh6gghWOnbBc9VaootP2aFs1dWSd3Exzg1ucEkknoAAST6KB3aZshGvTmrcdVTvg9qa8GO1819Ldb9Fj+Pm"
                        + "kXLcda7X6q2/oaSq1IyghbVyOjhZDVNle4tGScDwGRz9QrvR2zuuNdabN9sNHRuou+dAH1NS2Iuc3GSAeoGcZ88p+zzF/Z5Dm6W19FWONYe2mF"
                        + "YZm9kTYOuLX6X25FaFlBWSksF0h1idLzwCK5NrPYXRvdgCXj4ME+WfHy5qTD2ad0x1orTkHH+MGf2JR000t8jCbdAisxvD6LL7TO1mYXFyBcdR"
                        + "1UQBPqpd/uad0uvsdp/2gz+xYG17N63vGsrzpehpqE3KzcHtbH1bWtbx/V4XdHLM0VQ0gFh120Krx8S4TK1zmVLCGi594aC4Fz5kDxK0HCalz+"
                        + "5r3S/zO0/7QZ/YtY1htXq/Q0tsi1BBRRuuUxgpu4qWyhzhw5zjoPeHND6OeNuZ7CB4FOm4jwuqkEMFSxzjsA4E6a/JaWnlS3/c1bpDINFaQQcY"
                        + "+kGf2LEai2M3K01aZLlW2FtTSxNLpH0E7agxtHUlrfewPMA4WRoqhozGM28CsIuJsImeI46phJ2GYfdR2E/FUgqpVwt2hCPFB6JpKzQhCqqyhS"
                        + "/2WP8A5xtvs/8ASzf3HqIFJ3Z2vVFp/tU6Cu1xlbHTRXmBskjjgNDyWAk+Ay4LB/5SgbrrT+EldxWHbg/6e4fuQLhPTH/xrZ/+/Qf0rV6FdvLQ"
                        + "+o9ZbUaf1BY6Cev+52rnNbTU7C97IZWMHe8I5lrXRgHHQOz0BXEOzO3Wp9xd4rJZbBbaiUMrYZauq7s91SQtkDnySO6NAAPI8ycAcyo2EBhCyO"
                        + "69C+384O7IM3peqT/9inTauRrdhdEOcQB9AW/GTjmaaPC5t7feo6FnZjprRLIBV3G+QmCLPMtiZI97seQ4mD9ILZtybpU0f8G/PU0dRLTVVNpK"
                        + "3SwzQvLXxva2nLXNI5ggjOVFbRZXXM/bS7OA2/1TLuho2jDdL3af+/qSFvu22qefADpFIckeDXZb0LV1Bdjn+CmLSMf4PYz/APitVrsFvVYO0d"
                        + "sfXaa1pR0lXeqem9iv1vkADayJ3uioYPAO8cfUeOWMtWw7qWik0n2FNV6Pt8809HadJTUEEk5HeOZHEGtLscicAZWRJNgUgsD2DiB2PKQed2rv"
                        + "3wuKds+1buPspp65aR0pbtP1FDLc561z7hTSSScbyGkZa9oxhg8PNdjdhurEHZEt7M83Xau/favMW6/4+rR/1iT98rJjQSbpEr2EZunqM9iAbu"
                        + "Gnt5v33LfTXdCN3s/fd1x44eLPDnwzn1XnNu92sdyN6dAM0fqq3aep6BtXHW8VBTSRycbA4AZdI4Y98+C7Yiqgf4Lr2f8A+3p/4ZeWCImjmhxW"
                        + "3ba6TGttzrXYJi4UsjzLVOacEQsHE/B8CQMD1K6q3M3StO0Wm7dabNaaeerliLaOgBMcNPE3lxOxzxnkAOZOTlc47FXiCz70UDp3BgqoZaRjjy"
                        + "w97fdHzLcfNbv2i9N3O5XOg1ZRQyVNNFTey1IjBcYcPLmuIH4J4iM+BHPquzw1r4cLlqace/exPMDT9/7LyriSkjxLiOmo8QP9gG3AvYF2u/jY"
                        + "D4c1Tae1TqiGac3vT1rq2OY7ufZeOExvweHOS4ObnGRyOPFbh2e9OC5xXDdLUpFZebjVSCnqZufdNBxJIM9CXZbnwa3A6rlqjoK6vlfHQUc9U+"
                        + "NjpHthjLy1rRkk46AALqrYy8U1x2LhtXe4fSvnpJmtPNoeS4H5tf8AqKeBSy11W1tQ7NlBLb9dP33KLjPBqTCsMk/DIxGZHNa+1/y6nroCbXta"
                        + "/Na7rPtQ3SDUU9Dou1299BBIWCsrWue6owccTWhwDWnwzk458uikXa7dm27s2Svst3tNPT18UH990hPeQVMLvdLmh3PGTgtOcZBB8uQdS6Zu2k"
                        + "7/ADWm70r4nscRHIR7kzfB7D0IIUy9nPTd0oL9WaurIpKajdSupacSNLe/LnNLnAHq0BvXzPoUYXW11RXiGS5uTmFth9LLDiPhPA6XBDPStDXN"
                        + "ALHg6uOnfrf4bi1lm9uNJRaD7YNysVI5zqEW2aelc85d3TwwtBPiRzbn0U07g6OtGv8ARNTYLk5rJP4ylqQMuppgPdePTwI8QT6KKKa/wV/bKq"
                        + "nU7gRRWV1HIQcgvGHOHy48fJfDWm5suhu0LR1NS577RW2yGKtibz4cSScMrR+M3PzGR5LbQNp6amkEg/s+0LfAf0XLYhRYjiWI09RC4iobCx46"
                        + "kjX1I9TpzWu9ne0XLTm/t9s13gNNV0ltliljPn3sfMHxBGCD4ghX+52rjo7thWjULHFkEVHTRVWPwoX8bZP1HPxAUuMp7PU6rp9ZUoY6rfRGlF"
                        + "REctnhcWvbnzxjkfIkLmztDT9/vHx+H0fAP3lWxLD5MMoBY3s+4PduFucFqG4/jzpZ2WD4Sxw79AbfRS12m9NsvG39FqiiY2Sa1TcEsrfwqeXA"
                        + "z6gP4T+kVqfZc0w2a/3XWFTHltIz2Klcf+cfzkI+DcD9Nbjttf6XX2wbbJdX966OnfaqsHqQG4Y748JYfiFc22eDaTYBzXGP2qhpnSuLTymqnn"
                        + "l8feLR8Gq0MNbNUsxP/wCPLmPiP38FrhWVVJhE3De8vaZB/hcbn1Ong5Q9v1qao1nvZ9CW4GaO2FtspmMOe8mLvfI+LyG/orp/TtNZ9vdD6d0x"
                        + "LURw5MdvjP8Az9S4Oc75ucHn7FyzsVZH33dV2oLhmZlsBq3Pfz46hxIZn1zxO+QU4bhaOuWuK+yz02qHWhlrkNRExtP3pM2QQ/PEOgaAPifNQ4"
                        + "PT1EsUuIsZmc82A7r6728PJWeK6emY6kwF0mSGFt3OsT7xBtoAT3/5io17RFjl0xuzate2yKPgrHMmcHNy32mAtPvD8poYfXDltW1G/Gr9dbo0"
                        + "9gvNHaIqSaGaVzqaF7HgsYXDBLjyytl3Xsv3YbUXC3kNfXwNFZTOA6ysBJA8uJvEPmFz/sLMIN7KGTP+S1H9GVBWU01FijGMJDZCD8dR++qt0E"
                        + "VNi/DMntTA6anY5rSdwLXaR8vJTjvZvFqbb3UtttlhprZNFVUZne6ric9wcJC3lhw5YAWD7Pmqa7Ve6GttTXRkEdbXQUz5GwNLWZDi3kCTjkB4"
                        + "rS+0pU+064sjvK3uH+9crns0VXst91G7PWmgH89yM8r8aFO46BxsP8qTcDpYeEHVEcYEr2gF3M/2g+wUj683H3hsmvrhb9K6JFxs8JZ3NUbdLL"
                        + "3mWNLvea4A4cSPkoc15rvXeq9T6ct2ubFFaZKSrZUU8QpXwPcHva0khxOR7nL5qUtYdoCm0lrStsD9Oy1ppuD782rbGHcTA76pacfWwoX1rr9u"
                        + "4e5tovbbc6hEIgpu6dMJc4mLs5AH436lDjEkQzxsqC52axbY23+it8JYTNH2U0tAxjQy4kBGY+7va/8AMDrpzXU282v7toDQbb3ZY6OSrfXsp8"
                        + "VbC9nC4PJ5Ajn7oWI2S3Wvu4dvupvlupYZqF8fd1dI1zY5OLiy0gk+8OEHkeh+3L6y1jpzTtkNdqZjZaJ1QIsOp+/AeeIj3cHwB5q21Hqw6Z21"
                        + "rb7pu0U1c2GEVEUEGI4y04++YaObQCHEDmQF0ktK9lS6Uy+60XLdztuvOqWljkwxtH7JeWR1myk25jTp3G553XK+7NrobLvXqS3W1jI6WOsLmR"
                        + "sGGs42teWgeABcQtNCubjcKy73iqulwndNV1UrpppHdXOcckq2C83lcHyOc0WBJX0jQwPgpooZHZnNaAT1IABPmmgpJqNWlaIQhV1YQE2lzXBz"
                        + "SQRzBHghLxQhdw7O9t62waapbDuzDXtrqWNsLL7RR98J2tGAZowQ4Px1c3Id1IBzmSbx2zdjrNa5qi0XO5XeoPvCloLa+EyO/KdIGtHxOV5rJY"
                        + "UfZhF1JW9m9Go97NeC/XmJlDQ0sZgt1sieXMpYycnmfrPccFzsDOAMAABdLa57S21V87HlVt9bb3XPvUunaa3Np3W+VrO+Y2IObxkYxljufRcQ"
                        + "JYQYwUXW17c7g6g2w3Jt+stNz8FXSPPHE4kMqYjyfDIB1a4cvTkRzAXa+5Pa12g1n2f9T2Ggulzgu13sktPFRTUEn3uZ7P4t0g93keXEOXLK8/"
                        + "sE+GUkyy5QCu2OzX2j9q9tdgaHS+qr1XUl0hr6md8UVvlmbwPeC08TeXQdFxhXzMqLtVVEZJZJM97SRjkXEhW2EEEFDW2QSu32dpPapvYuG3X0"
                        + "5X/Tp0kbT7P9Hy8HtHc8HDx44cZ/C6LiEIxyQm1tkiVVHJJDMyWJ7mSMcHNe04LSDkEHzyp30nvtSPt8dJq6KeOpY3hNbAzjZL6uaObT54yD6K"
                        + "B0ltMMxapw15fTnfcHUHxWpxbBaTFIwypbe2xGhHh+7Lpmp3s0JQUbpKOaeqkP8jT0pjLviXAD7cqGNPbg1eldd1l7sdE2C31chMtsdISwszkN"
                        + "zjkRk4OOWcdFp2EYKuV3EdZVvY82aWG4yi1j8T5bKjQcK0FFHJGAXB4scxvceVh4HcciulqPe3Q9wpmyV76ikkHPuammMuD6FoI/YsHqvfmkZQ"
                        + "PpdJQTSVThwtrKhnAyL1aw83EeGcAeqgRGPBW5uL8QkjMd2gnmBr8/kFSh4GwuKUSEFwHInT5X9T4rd9s9U0OnNwpL3fKqcMkp5WOlDDI5z3EH"
                        + "JxzOcHmqt1NU2vVusqe5WmWSSCOkbCTJGWEODnHGD8QtGTC0/wCJzexmi0yE5u+/jf6LefhEHtwr9c4GXut4W+qlzbLdiLT1nfYtRTzexQjio5"
                        + "mMMhjyecZA58PPI8uY8lqu52pLdqrXX0ra5ZJYPZo4uKRhYeJuc8j8VpqFLNjNTNRtoZLFjTcHn4X6a9FFBgNJBWur4gQ92/TXc2tufFSFtRrq"
                        + "m0bea2K5zyst1XEC7gYX8MjT7pwPMFw+xZXdfci26ss9BabFPM+nbKZ6gyRmPLgMMAB69XH7FFOOSAFmzG6plCcPFsh9d72vfZYycP0b68YiQe"
                        + "0Hpta9rb+fJTNtxr/RujNCilnqZzcZnuqKhrKdxy7o1nF0wAAM+ZKj+r3D1tV1stSdUXaLvHueI4qp7Wsyc4aAeQHRazhGDjOFjPjNTLDHTg5W"
                        + "sFhluL9511P9VlBgNJFPLUluZ0huc1jbw00H2Cmfbzd+O2WWqotX3SuqJGyiSnnka6dxaRzaT15EZHxK1nTuotNae3rnvlLUSizOMxhcIXcTBI"
                        + "3k3h68iSPko/xnoChSnHqksha8A9kbtJvfwJvqPsFCOHaRr53MuBKLOAtbxAtv9yt93V1ZatXahoKu0zySxwUxieZIyzB4yeh9CrjabWFm0jcb"
                        + "pNeJ5YW1MUbIzHEZMkOJOcdOqjrGOoTwoxjE4rvxCwz3vtptba/1UzsFpzQfh2vZ2t3736de5dIu3g0BI9z5KiVzndS6gcSfmQtC3J1vpvUc9h"
                        + "Nke7FHUulmPsxi908GMcuf1TyUVqoBbCt4oq6yF0MjWgHoNd79VrqHhKiopmzxOdcX3ItqLdO9THuluRp3VejG2y01U8s4rGTYkgcwcIDs8z8Q"
                        + "ntzutbLLo11g1NLOWU54aZzYjKHROzmNwHkc49DjwUNgJ4UZ4iq/bPbdM1rWtoR3i/1Wf/C1D7F7Ab5AcwNxcHuNvosnfmWhmo6sWGd8ttc/ig"
                        + "MjCxzWnnwkHy6Z8cBY7wSCYWle7O4uta/IbDwW/jZkYGXJsNzufHvQhCCVislaoSTVZWEIQhNCfghCELFCkG56RD+z3pTU1rsVRLVT3O7Q19bB"
                        + "FJIDHC2lMYeRlrQ3vJD4dTlR8svRap1PbLJPZrbqO70dtn4u+oqetljhk4hh3Exrg05HI5HMLFwOhCYKl3abTemKnYrUGpbpZbVWXKC8wUcEtw"
                        + "tVZcR3bqWWQxtZTPbwEua08buS1fZXSFBqbXlVd7/bKeu0/YaV9yr6OqrG0MVUc8ENKZ3ua2MySua3mQcB2Oi0qzap1Pp2KaLT+o7vaWTEGVtB"
                        + "Wy04kIGAXBjhnGT1WPZWVbKSelZVTNgnc100QeQyQtzwlw6EjiOCemT5rHIde9PMpYo9trZZO2bYtv7lHHc7BV3+jZC7j4o62gnkY6Nwew+8HR"
                        + "vAJaeucFUb22LS9ot+lKyxUVmZU3Gkqames09HUsttRGJiyIRioJeJmFkjZRyAPAMZyTFwudzbV0tU24VYnpA1tNKJncUAactDDnLQCSRjGPBU"
                        + "SVdZPRx0s1XPJBG98jIXyEsY52OJwaTgE8IyR1wM9E8hve6My6S1VtTt81mvLxp2ijibp/TEZrLTNOS+jrXspHRVkWTl0UoklBBzwPDgeTmLmY"
                        + "jDj8VePulzkqJ55LjWPlqI+5mkdM4ulZy9xxzlzfdbyPL3R5BWmE2MI3KRN1f2GGGo1XbKeoibLDJVxMfG7o5peAQVYvAErwBgBxAHlzVcE0tL"
                        + "VxVNPI6OWJ4kY9vVrgcg/avmckkk8zzUxIygc/8AZRBpzl19LD6pY54XS1n2+0bcuzHS1Y05SS3ubSVdeG1LaKrZP7RBVzDvTWcXs7WCKIjunN"
                        + "y7AAPE5pXNQCy33Tal+5r7nfuhu30P/wBHe2Sez9eL+K4uHrz6deahewm1lKDZbnYdFN1Dscy52bT9Tc7yzVUNDM+kifNI2nkp8sY5rc4Dntdg"
                        + "45kEZWWpNJ6YqO1xqrS0VtgnsNLNfRS0rHucxjYKWpfDwuByQ10bCOZ+rzyoytF/v2n55prDe7lapJ4zFK+hqpIDIw9WuLCMj0PJWdPU1NHU9/"
                        + "SVM0EvC5veRPLHYcC1wyOeCCQfMEhLszqnmC3rQ2labUG02v65lnluF0t1Nb5KJ0LHvfCZKtrJCGt65YSOYKr2fsVJcdza2jvGno7s+jst1rGW"
                        + "yqikLXzwUcskYexha84ewe6CM4wtOs2oL/pypkqNPXy52maRvA+SgqpKdzmg5wSwgkZ8Eqa+3yj1A6+0l5uNPdHPdIa6Kpeycudnid3gPFk5OT"
                        + "nnkp5DYhLMFvm5tht1LojSGqDpWPSV5u4q21dmh71kboonsEVWyOVznxtk4pGYJIJhLm4BwoyV3cbncrxcpLhd7hV19ZLjvKmrmdNI/AwMucST"
                        + "9qtcLNjSBYpE3WV05BT1OoWQ1ULZYjDO4sd0yIXlp+RAPyWJH1R8F96eeekqBNTyOjkAc3ib1wQWn7QSPmvjjyUpIygc7n6KEMIkLr6ED4X+4R"
                        + "5Loay7e0ldYrFpyx6MsFWy8aaN1Oo7xHXSOqaoxvfLHBNT5jpzAWlnC8AExnjJ4gFz1grKUmpNRUFhqLJQ3+6UtsqSTPQwVckcExPi+MO4XdPE"
                        + "KF7C7ZTNNlJ+xOl7DqC26onu1NpZ1XD9HQUVRqcVBo4Xz1Pdni7ghwLhy4jyHiR1XxtujtJXPtVXvTFJYbl9EUk9xdQafq3vgqauWCKR8VESTx"
                        + "tL5GBmM8eOQPEcqKI6mpipZqaOomZDPwiWJryGycJyOIdDg8xnovpUV9fV3I3Gqramesc8PNTLK50hcMYJeTnIwOefAJdmbk3RmCk3Wlmo6zZm"
                        + "l1jcND0ujb028m2xU9HFNTxXKDuXPe8QzOcQ6F4Y0vacO71oI4hk7rpzbPQ+otCWm/m1xsqNW2uPTtlp46gtEN+j70SSkE5w4wU4weWa7l05QJ"
                        + "eL9fNQ1za2/wB5uN1qWsEbZ6+pfUPDR0aHPJIHovhHX18UVPFFXVMbKeUzwNZK4CKQ4y9gz7rvdbzHP3R5BAjNt0Zgto3QttksW5VZpyxU8cUF"
                        + "nihts8jCSZ6qKNramUknqZu86csALTwq5HyTTPllkfJI9xc57ySXEnJJJ6lU4UrRYWWBN0BNCAVmEkI8EICaSOaCOSEkICtkITVVToQhATQmhC"
                        + "EwsU/BCEBZIRhNCEIQhP0QmkhCaE0ksJ4R4p4TshJCYTwiyEsIwFUhFkrqnCMBVeKMIsi6pARhVIwmi6WEY5ppoSulhGE8pIshGEYTwhOyLpYR"
                        + "4p+CEWQkmhCLIQhAwhCE/BJCMppIQjKWUk1b+CSEKspkJpJphCYKEkJpKpCSE7pJhNLKaaEJhJNNJCOiEJoTT8FTlNMJJhNIITSVXgj5JZRnmh"
                        + "JNCSEIVSWUEoQhNCSad0I6oQhCE/BCSMoSTSRnySRdCqVKaSE0JpISQhGUIyE0IykjKWUk7L4IQhVlMhCMoTSTQhATuhNCSeU0kJpJppIBTVKe"
                        + "U0KpGVSmhJNCSad0JpgqnxQndJVZQqU07oTymFR4qrKLoTRlLKMouhPKMpIymlZVISzySzyQiyqQkEZQiyaMqnKMpXRZVZS5JZRlO6LJoSKEro"
                        + "sjKEFGUXTQUkJJXQvj4oQEKupUwhCE0k0keKaaEZQkmmEJhCSE0kwU0soTSTRzQhCE0ckgmmhNCWUeCEk0I+aE0kIQjKd0ITSTQhGUJfNCEJpp"
                        + "IyhCEJJoQhCEsoQmEJfNBQhNCSEIRzR0QhF0IyhCSRTX/9k=";

        static final String ICON =
                "/9j/4AAQSkZJRgABAQAAAQABAAD/2wBDAAQDAwMDAgQDAwMEBAQFBgoGBgUFBgwICQcKDgwPDg4MDQ0PERYTDxAVEQ0NExoTFRcYGRkZDxIbHR"
                        + "sYHRYYGRj/2wBDAQQEBAYFBgsGBgsYEA0QGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBgYGBj/wAARCACA"
                        + "AIADASIAAhEBAxEB/8QAHQAAAQQDAQEAAAAAAAAAAAAAAAEGBwgCAwUJBP/EAEkQAAEDAwIDBQQFBwYPAAAAAAECAwQABREGBxIhMQhBUWFxEx"
                        + "QigRUyYpGxFiNCUnKSoTNTdqOztAkYJChDREVjZIKTssLh8P/EABsBAAEFAQEAAAAAAAAAAAAAAAADBAUGBwEC/8QAOhEAAQMDAQUGAggFBQAA"
                        + "AAAAAQACAwQFESEGEjFBURMiYXGBkbHBFCMyQmJyoaIHM5LS8FKy0eHx/9oADAMBAAIRAxEAPwCh9FFFSiaoooooQiiiihCKKKKEIooooQiiii"
                        + "hCKKKKEIooooQlpaTyorq4lozRRXVxFLmkpM0IWWaM1jnlmnFpvQ2rNWuD6BskmS1nBkqAbZT6uKwn7iTXpjHSO3WDJ8EjPPFTsMkzg1o5k4Hu"
                        + "U380ZGQCcE9POrFaT7NUQezk6z1ApzoTCtfwj0U8sZ/dT86mi17c7c2uxuWmJo+0+7OpCXfbsh5bv7Ti8qP3ipyn2drJRvPG6PHj7Kh3T+JFro"
                        + "zuQh0p/CMD3OM+gI8VQ6jNWw1T2ZtI3YLkaVuUmxSDzDDmZEcnwwTxp+RPpUHas2V3D0gHH5dlXcISP9dtuX2wPFQA4k/MUzqrVVU2r26dRqFL"
                        + "WjbW0XTDYpd15+67un05H0JTAzS5rHPP05Hypaj1aktHOijFCFqpc1iKKTSiypM1116T1MjQ7WsjZJp0+5IXEFzS2VMB5OCW1KHJKviGArGc8s"
                        + "1x891cDgV3GFlmkzSZNJXVxTHt5P2ajlr6QhuouYx+dvmHWuL7PCPZj/mGfOp8YurC4rS4zrbjAH5stKBRj7OOWPSqQZp26CXqJy8vRbBe37ct"
                        + "DJe4U/E2sggYUjpjn1xV02e2g7F7aX6OCTpluAT550PuFn+0ex7KreqjUHTk8lwHlzA9CrdJu+P0q+pq9Aciqq+p3J1Lp5xLOsrCSznAnwfqH1"
                        + "B5fLI9K7w3S0ki2e/G9o4OnsuBXtc+HBjPz6edX2O426UEPkDHDiH90j0Pyys+n2QqW43WbwPAt7wPt81O0e8JOPjr713+FAhqmz5rESOgZU+8"
                        + "4G0p9VEgVBdhue5Guin8kLC3Z7av/bF6yAR4ttj638RS70bUwtNbEO6ovGprtqPUQnRmRJlOezYaSvi4g2ynkkHA5nJ9Kr1zvEMcbpKZheBzOg"
                        + "9M6n0GPFR8OytNLWx0NTOGPecbo7zvXHdb6nPgstx9W7J6ntl5kRtPN366W+MJC5sHMLOXUNfywGV83AeaVDkedVkJSVkpSUpzyBOcD1p+7dWJ"
                        + "i86K3CedelJXGsftW0sNhYUUPId+Ik8h+aHTuJPdTBBqgV0z5i2V7QN4Z0GOZHyW07OWyG2NmooZHO3CB3iTjLWnQcBxPAeaUdaXNY5paZKyLT"
                        + "miiikUqvTrsDwYVz7H1xgXGIxLiP36Y27HkNhxtxJbZylSVAgjyNNPtFdiHRY0nfNf7ay06bet0R64SbO4C5DeQ2grUGv0mVEA4HNOcDCRzp5f"
                        + "4PtWOydL/pDL/s2anfd9X+b1rv8Ao9P/ALsumJcQ44S+MjVeJA5gHx50UgPwJ9BQfqK/ZP4U+zokFZnYHsca23bTF1Jqgv6X0g5hxMp1v/Kpyf"
                        + "8AcNq6JP8AOK5c8gKqfu0LtpobaXSmgdO6IsUe2RPaTi64BxPSVhtn43XD8S1evToAByq3WjAE7cWBIwMW2MP6lNVk7cT/ALvF0EvPWROH9W1U"
                        + "hszIXXenz/qCZ3iLeoZWjmCq/Mxoc1gtPNIWhYwpKhkKHgR301noW0mjtTe8fRiJt5WR7K2xG1SloV4pa5pQfX5YrRYG9RbhXp6xabmKtlqjEJ"
                        + "uV6AypBP8AomfFZHf3dcgdZ/0RorSehreI+nrY2y8ofnprnxyHz3lbh5/IYHlWp3atbWSblNG124ftuGQDz3RzxzOQM9Vhlyr2WYOjlkcXOH8t"
                        + "jt3T8btcZ6AE444ymPB1TurOSl2xbQzksEZS7eJ7cQkfsHmK4+6DO9ms9r39MydpikKkNSfeLXcm5ih7Mk8PskniOc91WBSoKGaUqSOZqAq6Ka"
                        + "qYWSTHXwbj9APiqpQ7WQUdUypZQsy05B3pM+5eR+3HgqP7b6it2hWNT2fVfvNskzIb0RyM/Fc9qCpvgCSgjIPxKPPHSmRYEaMKUflLJ1C3yAV9"
                        + "GssLx/1FjNX61no/Sm4lkNq1jbBMCUlMee3hMyGfFp3rj7CsoPeO8Uh3Q2uve1+rfo2esTLdIBct90bQUtymweeR+gtPIKQTyPeQQTVrpT1MAj"
                        + "ZKAWtGARnqTr4ra9ltoLdfXTSU7nRzPwXNJGRgYy041HXn4DnJem+z9pHcrSki7bZbiPyJUc8LsC8QQytpRGUhZQTwg4OFAKHI+FQbeLRcbBqC"
                        + "bZLvGVGnwnlR5DKuZQtJwRnv9e+rg9lvRw252t1Buprl/wCibfOjoUyJHwlMVslXtSOuVqUAgdTgY+sKqpr7U51rufftV+7+7puU1yQhk9W0E4"
                        + "Qk+YSE58800mawRtIGHHipCy1NW+vqYHS9pCzGHEDIPNuQADjXlyCauaTNFHU1Hq4L047Arwa7KUgE9dQS/wCzZqdN2ZIVsDrlOeun5/8AdnKr"
                        + "h2H5widlxSSQAq+SyP3Gamjc26pd2S1k1xfWsU4Yz/w7lMnN1ylgdF49JPwp9BQo/Cr0P4UifqD0FB+qr0P4U7J0SXNe5ulXg3oSyIz0t8b+xR"
                        + "VUO3579L0vt/DtaeKZMukmG15KW02M1Y+w3JKNJWlPF0gRx/UoqBe1S43NkbevrwREuE98Dz92QkfxVn5UpZ4ZJa2KOI4LnAZ6Z0ym9zn7Ckll"
                        + "xndaTjrgaKKtJWu36S0pDsNsx7GMjBXjBdWfrOK81Hn6YHdXRvuuLPpHTzt6vcosx2/hSlI4lurPRCE96j/7OBTabuIHRVV93r1FIu24AtXtFe"
                        + "621pKEIzyLi0hS1evNI+VbDtBIyzUIdG3o1o5f4AFgln2bN7uG7UOODlzjzOuvqSfmnffO1LrGRMUnTdsttsig/CZDfvLpHiSSEj0A+dffpXtT"
                        + "X1u4NsaztUSVDUrCpUBBadbHjwZKV+gwagCDAnXOe3Bt0N+ZKcOEMR2y4tR8kjnUtWnYs2q3t3zdnU0LRtsI4kxVqD058eCGhnB+8+VZlDcbhI"
                        + "/tGPJ69PXkFoty2b2Yo4RBPC0E8AMmQ+WMvJ9x1Vibxuzp+Hb4JsindRXO5oC7da7WC49JB6EgD4E9clQ5YPLlXBututliXE192k73Gdeay/Z9"
                        + "CwVB1ttXcVoz+cXyGSTwcuaj9URTI3v07oO2PWTY/SrdnLieB7UNzSHpz48QDkJHfzyPsioaud2ud8u710vFwkz5r6uJ2RJcLi1nzJ/CnVfeHV"
                        + "GA7BxyHDPxPw81D7P7EfRyZGAxg57xx2pHQYyIwRxxlx8BopK3f3z1PuzcUx5A+jNPx18UW0srykEcgt1XLjXjpyAT3DvMW5rHNL31DOeXnJWk"
                        + "UtHFSRCGBu60clo76mzTW1sDSW1sjdLcWAqQ0htK7bYlkpEhxZw0X+8JJIPAOfCCT4VHG3bNmf3X061qBbabYq4NCQXThHDxcgr7JOAfImrza/"
                        + "0jB15oSbpm4vOR0vlLiHmwCWnEnKVY7xnqO8E1K2i3fSWSSjVzeA8fFUPbrad9sqKai1bHIQXuHHczghpHPGckagYxxXE7LOuHrns3dXJzyDOF"
                        + "8fW8G0hCQFtNFASkckpwkgAcgE4qYrpc413ssy0zFqMWYw5Ge4evAtJQrHnhRqpug9D7p7P3a5i32iBqa2zkJCm4s9MdQWgnhcw4ORwpQI59ev"
                        + "KnsrXe5ShhO0s1JH85eI4H31HutdXvd6J2fIlWun2ktDox2NTGGgaDeaMehII9lVPX232odutTu2i+RVhniIiz0pPsJjY6LbX0OR1HVJyCARX3"
                        + "7XbYX7c7Wca22+M63a0OJNxuakkMxWs/ESroVkZCUDmSR3ZIs3+U28k2MphrR2m4DKzktXK5GSgnxLaE4JoLe8M9hEafuDZ7Oyk5DFmtQJTn9X"
                        + "2hwPkKVZY6940jIHjp8cJOXa60RnHbgno3Lv8AaCrOt6hYbYShr4GGkhCBnkhIGAM+QAqunaR3P0y5M0pbY97hzJLMiWp9mG6JDjIU0gJ4koyR"
                        + "xEYHoaxibEXbU4Dt+maw1QlRyfpOcpiP+4CgY++ox3X1XD2V1orRWi9P6YYubDCVzn47ftTHcVzDRIxlYTwk5JxxAdQac0dM23VDKh8zQ5hBAH"
                        + "eOnUDA/ckJ7tNcYnQ01I9zXDGXYjGvPvZd+1YQPy6vqc2LSEmOwRn329L90aA8eHmsj5U171YNs7TfJF43D1ib9dnFAuWvTreG8hIHCpzJx0He"
                        + "k1x5N+1HrRlt+8X6dcGF/EGVL4Gge8ezRhPLpzBrpRtKW+4QPcprALB5gI+AoPik9x/+NaHVW2vvMPaPcHc2g/Z9m4HqS5VqOmdROzLJ2Y4ER/"
                        + "ax+d2T/SGL4pW98m0QXLXtnpq26QhqGFSGkB6W4PFTihyP3nzqMbhcrjd7i5Puk6TNlufXfkOFxavmedSQ/stJcd4rdqKKWz0TLZWlQ8soyD/C"
                        + "nXpHZiwW+e1N1JON3KCFCI2gtsE/aOeJY8uQ8c1S37NXqeTs5YiAOuA0eWNPYKYiu9itUbpafVx46OL3ebnan1OFBciBOhtRnZkORHRKa9uwp1"
                        + "spDzeSONBP1k5BGRy5VpFXh1PY9MbhaNTpy/sJaSyMwpbCAHISsYBQOnDgAFHQgdxAIqBrTRd50Lqlyy3hCFHh9oxJaOWpLZJAWg+HIgg8wQQa"
                        + "YXayT2131gy08042b2sgvIMbm7ko+6TnI6g6Z8eY8k3qypKWogK1laPlT/05vRuRpiE3BgaicfiNDhRHnNpkJQPBJV8QHkDTAHI0tdimkiO9G4"
                        + "g+Gib1lBTVrOzqo2vb0cAfipi/xltxSjCmLEo/rGIof+dPnQW/FvvyUwdaXJuzTuIhL7bITGeHcOI5LZ9eR8R0qswIrYgt5+JJ++n7bpWHTtj7"
                        + "qJbstZ4zltKz+kL0Mssa0z2kSm3jcW1fVWl/2iD6cBwafEGbZrDDMl1MG3Mo5qfd4WQPMqVj8a8z7dcWoCssLmMePu8hTX4V0XrnapxBnpuEnH"
                        + "T28pTmP3s16MD6rWWcepJ+SfRRwUYxBDuj8IA+GFcPdftc2bT1qfs+28lu831YKPpIDjiRPtJJ/lVjuA+EHmSelUemvTLlcpFwnyHpMuS4p559"
                        + "5RUtxajlSlE9SSSSad0F7RCSDMg3BQ7w26kfiKcjM/ZsR8PWLUCnMdUy2wP+ynMVkjxntm+//SbVF4fGcCB58gP+UwNPXuTYpfNsvxFnLjGcZP"
                        + "6yT3H8e+pXst/tNzbSYMxsuYyWHDwOJ9Unr8s00Z0vbwqJt9tuiB3B15CvwFcoz9MpPK2OrHmurdY7rNaB2PbsdH0OdPIgafqPBRVbTsuA3+yc"
                        + "13kP1GfmpoalFPUEfKvqVeI8NovSpLTCB1U6sIH3moPVqS0tt8LMCUj0lrH8K4025xZK+L3U58VrKz/GrDWbbUzGfVhpd+Z39gUOzZR0ru+SB5"
                        + "D+5S5qHeiDbWFx9PJ+kJeMB5QIZQfHxX6DA86he73i5368O3O7zXZcp0/E44e7uAHQAdwHKtDjrajkIxWlShmsvvN6qbm/Mzu6OAGgH+dSrdab"
                        + "JSW0EwM7x4k8f/PAIpRWOaAahFMpaKKKEIooooQiiiihCOfiaMnxP30UUIRz8TR8zRRQhFFFFCEUUUUIRRRRQhf/2Q==";
    }
}