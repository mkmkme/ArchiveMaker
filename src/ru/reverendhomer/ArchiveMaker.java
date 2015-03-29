package ru.reverendhomer;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ArchiveMaker {
	private static final int BUFFER_SIZE = 1024;
        private final List<String> filePaths;
        private final String archName;
	private ZipOutputStream zos;

        public ArchiveMaker(String name) {
            this.filePaths = new ArrayList<>();
            this.archName = name;
            try {
                this.zos = new ZipOutputStream(new FileOutputStream(name));
            } catch (FileNotFoundException ex) {
            }
        }

        public ArchiveMaker(String name, List<String> files) {
            this.filePaths = files;
            this.archName = name;
            try {
                this.zos = new ZipOutputStream(new FileOutputStream(name));
            } 
            catch (FileNotFoundException ex) {
            }
        }

        public void addFile(String name) {
            this.filePaths.add(name);
        }
        
        public void removeFile(String name) {
            this.filePaths.removeIf(n -> n.compareTo(name) == 0);
        }

	public void makeArchive(/*String name, List<File> list*/) throws IOException {
		System.out.println("Имя ожидаемого архива: " + this.archName);
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			for (String fileName : filePaths) {
				FileInputStream fis = new FileInputStream(fileName);
				ZipEntry ze = new ZipEntry(fileName);
				System.out.println(fileName + " добавлен в архив");
				zos.putNextEntry(ze);
				while (fis.read(buffer) > 0) {
					zos.write(buffer);
                                }
				zos.closeEntry();
			}
		} 
                catch (IOException e) {
		} 
                finally {
			try {
				zos.flush();
				zos.close();
				System.out.println("Архивация прошла успешно!");
			} 
                        catch (IOException e) {
			}
		}

	}
}
