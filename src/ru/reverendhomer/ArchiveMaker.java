package ru.reverendhomer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

public class ArchiveMaker {
	private static final int BUFFER_SIZE = 1024;
	static ZipOutputStream zos;

	public static void makeArchive(String name, List<File> list) throws IOException {
		zos = new ZipOutputStream(new FileOutputStream(name));
		System.out.println("Имя ожидаемого архива: " + name);
		List<File> fl = ArchiveFrame.getFiles();
		FileInputStream fis;
		byte[] buffer = new byte[BUFFER_SIZE];
		try {
			for (File i : fl) {
				fis = new FileInputStream(i);
				ZipEntry ze = new ZipEntry(i.getName());
				System.out.println(i.getName() + " добавлен в архив");
				zos.putNextEntry(ze);
				while (fis.read(buffer) > 0)
					zos.write(buffer);
				zos.closeEntry();
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				zos.flush();
				zos.close();
				System.out.println("Архивация прошла успешно!");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
}
