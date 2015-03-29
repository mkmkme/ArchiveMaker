package ru.reverendhomer;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

/**
 *
 * @author ecko
 */
public class Main {

    public static void main(String[] args) {
        if (args.length == 0) {
            System.err.println("Usage: archive.jar -gui/list of files");
            return;
        }
        final String fileName = "output.zip";
        if (args[0].equals("-gui")) {
            SwingUtilities.invokeLater(() -> {
                ArchiveMaker archiveMaker = new ArchiveMaker(fileName);
                ArchiveFrame af = new ArchiveFrame(archiveMaker);
                af.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                af.pack();
                af.setSize(500, 300);
                af.setLocationRelativeTo(null);
                af.setResizable(false);
                af.setVisible(true);
            });
        } else {
            List<String> paths = new ArrayList<>();
            paths.addAll(Arrays.asList(args));
            ArchiveMaker archiveMaker = new ArchiveMaker(fileName, paths);
            try {
                archiveMaker.makeArchive();
            } catch (IOException ex) {
            }
        }
    }
}
