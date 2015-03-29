package ru.reverendhomer;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
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
	JFileChooser fchooser; 
	/* Панели для центральной части окна, корневого окна, flowLayout и верхней части окна соответственно */
	JPanel center, panel, centerPanel, top; 
	JLabel name; // "Имя архива: "
	JTextField output; // Путь к конечному архиву будет здесь
	JButton act, browse; // Кнопка "Zip it!" и кнопка вызова fchooser соответственно
	JScrollPane sbar;
	static List<FileLine> files = new ArrayList<>();

	/* Преобразование списка панелей в список файлов */
	public static ArrayList<File> getFiles() {
		List<File> paths = new ArrayList<>();
                files.stream().forEach((i) -> {
                    paths.add(new File(i.getPath()));
                });
		return (ArrayList<File>) paths;
	}

	static int index; // Присваеваемый индекс для каждого нового элемента списка панелей
	static JLabel copyr = new JLabel("© Преподобный Гомер",
			SwingConstants.RIGHT);

	/* Внутренний класс для панели файла */
	final class FileLine extends JComponent {
		JTextField file;
		JButton rmButton;
		JPanel fileP; // Панель для flowLayout. Без неё строки "размажет" по размеру центра
		int id;
		
		void setID(int nid) {
			this.id = nid;
		}

		public FileLine(String path, final int id) {
			fileP = new JPanel();
			fileP.setLayout(new BoxLayout(fileP, BoxLayout.X_AXIS));
			file = new JTextField(path);
			file.setEditable(false);
			ImageIcon removePic = new ImageIcon(this.getClass().getResource(
					"/res/remove.png"));
			rmButton = new JButton(removePic);
			rmButton.setPreferredSize(new Dimension(29, 29)); // Размеры кнопки и изображения должны совпадать.
			file.setPreferredSize(new Dimension(200, 20));
			fileP.add(file);
			fileP.add(rmButton);
			center.add(fileP);
			rmButton.addActionListener((ActionEvent e) -> {
                            System.out.println("№ удаляемого элемента: " + id);
                            System.out.println(id + " " + files.get(id).getPath() + " удалён из очереди");
                            center.setVisible(false);
                            center.remove(id);
                            files.remove(id);
                            index = refreshID(files);
                            System.out.println("**********************************************************");
                            files.stream().forEach((i) -> {
                                System.out.println("№" + i.id + ": " + i.file.getText());
                            });
                            System.out.println("**********************************************************");
                            System.out.println("Количество файлов в списке: " + index);
                            sbar.revalidate();
                            center.setVisible(true);
                        });

		}
		
		public int refreshID(List<FileLine> list) {
			int tmp = 0;
			System.out.println("_____________________________________________________________");
			for (FileLine i: list) {
				i.setID(tmp);
				System.out.println("№ элемента " + i.file.getText() + ": " + i.id);
				tmp++;
			}
			System.out.println("_____________________________________________________________");
			return tmp;
		}

		public String getPath() {
			return file.getText();
		}

	}

	public ArchiveFrame() {
		super("Adolbe Zipler");
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
		fchooser = new JFileChooser();
		center = new JPanel();
		panel = new JPanel(new BorderLayout());
		centerPanel = new JPanel(new FlowLayout());
		center.setLayout(new BoxLayout(center, BoxLayout.Y_AXIS));
		sbar = new JScrollPane(center);
		add(sbar, BorderLayout.LINE_END);
		centerPanel.add(center);

		Border border = BorderFactory.createEmptyBorder(20, 20, 20, 20);
		panel.setBorder(border);

		top = new JPanel();
		name = new JLabel("Name of archive:");
		output = new JTextField();
		act = new JButton("Zip file!");
		browse = new JButton("Browse");
		top.setLayout(new BoxLayout(top, BoxLayout.X_AXIS));
		top.add(name);
		top.add(output);
		top.add(browse);
		top.add(act);

		ImageIcon addPic = new ImageIcon(this.getClass().getResource(
				"/res/add.png"));
		JButton addButton = new JButton(addPic);
		JPanel panAdd = new JPanel(new FlowLayout());
		panAdd.add(addButton);
		addButton.setPreferredSize(new Dimension(29, 29));
		addButton.addActionListener((ActionEvent arg0) -> {
                    int ret = fchooser.showDialog(null, "Open file");
                    if (ret == JFileChooser.APPROVE_OPTION) {
                        
                        FileLine fl = new FileLine(fchooser.getSelectedFile()
                                .getAbsolutePath(), index);
                        files.add(fl);
                        index++;
                        System.out.println(fchooser.getSelectedFile().getName() + " добавлен в очередь архивации");
                        System.out.println("Количество файлов в очереди: " + index);
                        center.setVisible(false);
                        center.add(fl);
                        center.revalidate();
                        sbar.revalidate();
                        center.setVisible(true);
                    }
                });
		panel.add(top, BorderLayout.PAGE_START);
		panel.add(centerPanel, BorderLayout.CENTER);
		panel.add(panAdd, BorderLayout.LINE_END);
		panel.add(copyr, BorderLayout.PAGE_END);

		act.addActionListener((ActionEvent arg0) -> {
                    try {
                        ArchiveMaker.makeArchive(output.getText(), getFiles());
                        center.setVisible(false);
                        output.setText("");
                        center.removeAll();
                        center.revalidate();
                        center.setVisible(true);
                    } catch (IOException e) {
                    }
                });

		browse.addActionListener((ActionEvent arg0) -> {
                    int ret = fchooser.showDialog(null, "Save ZIP");
                    if (ret == JFileChooser.APPROVE_OPTION)
                        output.setText(fchooser.getSelectedFile().getAbsolutePath()
                                + ".zip");
                });

		return panel;
	}

	public static void main(String[] args) throws IOException {
		if (args.length == 0) System.err.println("Usage: archive.jar -gui/list of files");
		if (args[0].equals("-gui")) {
			SwingUtilities.invokeLater(() -> {
                            ArchiveFrame af = new ArchiveFrame();
                            af.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                            af.pack();
                            af.setSize(500, 300);
                            af.setLocationRelativeTo(null);
                            af.setResizable(false);
                            af.setVisible(true);
                        });
		} else {
			List<File> fl = new ArrayList<>();
                        for (String arg : args) {
                            fl.add(new File(arg));
                        }
			ArchiveMaker.makeArchive("output.zip", fl);
		}
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
						+ "Ещё больше пасхальных яиц и глупых шуток в одной программе!");
		note.setEditable(false);
		ap.add(note, BorderLayout.CENTER);
		ap.add(ArchiveFrame.copyr, BorderLayout.PAGE_END);
		af.setContentPane(ap);
		af.pack();
		af.setLocationRelativeTo(null);
		af.setResizable(false);
		af.setVisible(true);
	}
}
