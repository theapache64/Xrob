package com.theah64.xrob.utils;

import java.io.*;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Created by theapache64 on 13/10/16.
 */
public class Zipper {

    private final File inputPath;
    private final File outputPath;
    private final byte[] buffer = new byte[1024];

    public Zipper(File inputPath, File outputPath) {
        this.inputPath = inputPath;
        this.outputPath = outputPath;
    }

    public Zipper(String inputPath, String outputPath) {
        this(new File(inputPath), new File(outputPath));
    }


    public void startZipping() throws IOException {

        final FileOutputStream fos = new FileOutputStream(outputPath);
        final ZipOutputStream zos = new ZipOutputStream(fos);

        zipIt(zos, inputPath);

        zos.closeEntry();
        zos.close();

    }

    private void zipIt(ZipOutputStream zos, final File file) throws IOException {

        if (file.isDirectory()) {

            for (final File f : file.listFiles()) {
                //recursion
                zipIt(zos, f);
            }

        } else {

            //Getting real path by keeping the directory structure
            final String filePath = inputPath.getName() + file.getAbsolutePath().split(inputPath.getName())[1];
            final ZipEntry zipEntry = new ZipEntry(filePath);

            zos.putNextEntry(zipEntry);
            final FileInputStream fis = new FileInputStream(file.getAbsolutePath());
            int len;
            while ((len = fis.read(buffer)) != -1) {
                zos.write(buffer, 0, len);
            }
            fis.close();

        }

    }


}
