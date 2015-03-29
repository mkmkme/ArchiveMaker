package ru.reverendhomer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.border.Border;

@SuppressWarnings("serial")
public class ArchiveFrame extends JFrame {

    private final JFileChooser afFileChooser = new JFileChooser();
    /* Панели для центральной части окна, корневого окна, flowLayout и верхней части окна соответственно */
    private final JPanel afCenterPanel = new JPanel();
    private final JPanel afRootPanel = new JPanel(new BorderLayout());
    private final JPanel afFlowPanel = new JPanel(new FlowLayout());
    private final JPanel afTopPanel = new JPanel();
    // "Имя архива: "
    private final JLabel afNameLabel = new JLabel("Name of archive:");
    // Путь к конечному архиву будет здесь
    private final JTextField afPath = new JTextField();
    // Кнопка "Zip it!"
    private final JButton afActionButton = new JButton("Zip file!");
    //кнопка вызова fchooser
    private final JButton afBrowseButton = new JButton("Browse");
    private final JScrollPane afScrollBar = new JScrollPane(afCenterPanel);
    private final ArchiveMaker afArchiveMaker;
//	static List<FileLine> files = new ArrayList<>();

    /* Преобразование списка панелей в список файлов */
//	public List<File> getFiles() {
//		List<File> paths = new ArrayList<>();
//                files.stream().forEach((i) -> {
//                    paths.add(new File(i.getPath()));
//                });
//		return paths;
//	}
    int index; // Присваеваемый индекс для каждого нового элемента списка панелей
    private final JLabel copyr = new JLabel("© Преподобный Гомер", SwingConstants.RIGHT);

    /* Внутренний класс для панели файла */
    final class FileLine extends JComponent {

        private final JTextField flPath;
        private final JButton flRemoveButton;
        // Панель для flowLayout. Без неё строки "размажет" по размеру центра
        private final JPanel flFlowLayout;
        int id;

        void setID(int nid) {
            this.id = nid;
        }

        public FileLine(String path, final int id) {
            flFlowLayout = new JPanel();
            flFlowLayout.setLayout(new BoxLayout(flFlowLayout, BoxLayout.X_AXIS));
            flPath = new JTextField(path);
            flPath.setEditable(false);
            final ImageIcon removePic = new ImageIcon(
                    this.getClass().getResource("/res/remove.png")
            );
            flRemoveButton = new JButton(removePic);
            // Размеры кнопки и изображения должны совпадать.
            flRemoveButton.setPreferredSize(new Dimension(29, 29));
            flPath.setPreferredSize(new Dimension(200, 20));
            flFlowLayout.add(flPath);
            flFlowLayout.add(flRemoveButton);
            afCenterPanel.add(flFlowLayout);
            flRemoveButton.addActionListener((ActionEvent e) -> {
                System.out.println("№ удаляемого элемента: " + id);
                System.out.println(id + " " + flPath.getText() + " удалён из очереди");
//                            afCenterPanel.setVisible(false);
                afArchiveMaker.removeFile(this.getPath());
                afCenterPanel.remove(flFlowLayout);
                afCenterPanel.revalidate();
                repaint();
//                            files.remove(id);
//                            index = refreshID(files);
//                            System.out.println("**********************************************************");
//                            files.stream().forEach((i) -> {
//                                System.out.println("№" + i.id + ": " + i.file.getText());
//                            });
//                            System.out.println("**********************************************************");
//                            System.out.println("Количество файлов в списке: " + index);
                afScrollBar.revalidate();
//                            afCenterPanel.setVisible(true);
            });

        }

        public int refreshID(List<FileLine> list) {
            int tmp = 0;
            System.out.println("_____________________________________________________________");
            for (FileLine i : list) {
                i.setID(tmp);
                System.out.println("№ элемента " + i.flPath.getText() + ": " + i.id);
                tmp++;
            }
            System.out.println("_____________________________________________________________");
            return tmp;
        }

        public String getPath() {
            return flPath.getText();
        }

    }

    public ArchiveFrame(ArchiveMaker archiveMaker) {
        super("Adolbe Zipler");
        this.afArchiveMaker = archiveMaker;

        setLayout(new BorderLayout());
        add(addWidgets(), BorderLayout.CENTER);
        JMenuBar mb = new JMenuBar();

        JMenu filem = new JMenu("File");
        JMenu helpm = new JMenu("Help");

        JMenuItem set = new JMenuItem("Settings");
        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem about = new JMenuItem("About");

        ImageIcon icon = new ImageIcon(this.getClass().getResource(
                "/res/icon.jpg"));
        setIconImage(icon.getImage());

        filem.add(set);
        filem.add(exit);
        helpm.add(about);
        mb.add(filem);
        mb.add(helpm);
        setJMenuBar(mb);

        set.addActionListener((ActionEvent arg0) -> {
            SwingUtilities.invokeLater(new SettingsRun());
        });

        exit.addActionListener((ActionEvent arg0) -> {
            System.exit(0);
        });

        about.addActionListener((ActionEvent arg0) -> {
            SwingUtilities.invokeLater(new AboutRun());
        });

    }

    private JComponent addWidgets() {
        afCenterPanel.setLayout(new BoxLayout(afCenterPanel, BoxLayout.Y_AXIS));
        add(afScrollBar, BorderLayout.LINE_END);
        afFlowPanel.add(afCenterPanel);

        Border border = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        afRootPanel.setBorder(border);

        afTopPanel.setLayout(new BoxLayout(afTopPanel, BoxLayout.X_AXIS));
        afTopPanel.add(afNameLabel);
        afTopPanel.add(afPath);
        afTopPanel.add(afBrowseButton);
        afTopPanel.add(afActionButton);

        ImageIcon addPic = new ImageIcon(this.getClass().getResource(
                "/res/add.png"));
        JButton addButton = new JButton(addPic);
        JPanel panAdd = new JPanel(new FlowLayout());
        panAdd.add(addButton);
        addButton.setPreferredSize(new Dimension(29, 29));
        addButton.addActionListener((ActionEvent arg0) -> {
            int ret = afFileChooser.showDialog(null, "Open file");
            if (ret != JFileChooser.APPROVE_OPTION) {
                return;
            }
            String path = afFileChooser.getSelectedFile().getAbsolutePath();
            FileLine fl = new FileLine(path, index);
            afArchiveMaker.addFile(path);
//                    files.add(fl);
            index++;
            System.out.println(
                    afFileChooser.getSelectedFile().getName()
                    + " добавлен в очередь архивации"
            );
            System.out.println(
                    "Количество файлов в очереди: "
                    + index
            );
            afCenterPanel.setVisible(false);
            afCenterPanel.add(fl);
            afCenterPanel.revalidate();
            afScrollBar.revalidate();
            afCenterPanel.setVisible(true);
        });
        afRootPanel.add(afTopPanel, BorderLayout.PAGE_START);
        afRootPanel.add(afFlowPanel, BorderLayout.CENTER);
        afRootPanel.add(panAdd, BorderLayout.LINE_END);
        afRootPanel.add(copyr, BorderLayout.PAGE_END);

        afActionButton.addActionListener((ActionEvent arg0) -> {
            try {
                afArchiveMaker.makeArchive();
                afCenterPanel.setVisible(false);
                afPath.setText("");
                afCenterPanel.removeAll();
                afCenterPanel.revalidate();
                afCenterPanel.setVisible(true);
            } catch (IOException e) {
            }
        });

        afBrowseButton.addActionListener((ActionEvent arg0) -> {
            int ret = afFileChooser.showDialog(null, "Save ZIP");
            if (ret == JFileChooser.APPROVE_OPTION) {
                afPath.setText(
                        afFileChooser.getSelectedFile().getAbsolutePath()
                        + ".zip"
                );
            }
        });

        return afRootPanel;
    }
}

class SettingsRun implements Runnable {

    @Override
    public void run() {
        JFrame frm = new JFrame("Settings");
        JPanel pnl = new JPanel(new BorderLayout());
        JButton but = new JButton("Сделать всё круто");
        final JLabel label = new JLabel("Всё и так круто!");
        label.setVisible(false);

        pnl.add(but, BorderLayout.CENTER);
        pnl.add(label, BorderLayout.PAGE_END);
        Border border = BorderFactory.createEmptyBorder(20, 20, 20, 20);
        pnl.setBorder(border);

        but.addActionListener((ActionEvent arg0) -> {
            label.setVisible(true);
        });

        frm.setContentPane(pnl);
        frm.pack();
        frm.setSize(new Dimension(400, 200));
        frm.setLocationRelativeTo(null);
        frm.setResizable(false);
        frm.setVisible(true);
    }
}

class AboutRun implements Runnable {

    @Override
    public void run() {
        JFrame af = new JFrame("About");
        JPanel ap = new JPanel(new BorderLayout());
        JTextArea note = new JTextArea(
                "Adolbe Zipler.\n"
                + "Архиватор с умопомрачительным интерфейсом\n"
                + " и невероятной функциональностью!\n"
                + "За такой не жалко отдать и 600 рублей в год!\n"
                + "Кстати, не забудьте приобрести Adolbe Zipler DeLuxe Edition!\n"
                + "Ещё больше пасхальных яиц и глупых шуток в одной программе!"
        );
        note.setEditable(false);
        ap.add(note, BorderLayout.CENTER);
        ap.add(new JLabel(
                "© Преподобный Гомер",
                SwingConstants.RIGHT), BorderLayout.PAGE_END);
        af.setContentPane(ap);
        af.pack();
        af.setLocationRelativeTo(null);
        af.setResizable(false);
        af.setVisible(true);
    }
}
