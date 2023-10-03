package org.example;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.io.File;
import java.io.FileWriter;
import java.util.Arrays;
import java.io.IOException;
import java.util.List;
import java.util.Collections;

public class Ls {
    @Option(name = "-l", usage = "Enable long format listing")
    private boolean isLongFormat;

    @Option(name = "-h", usage = "Enable long format listing")
    private boolean isHumanFormat;

    @Option(name = "-r", usage = "Enable long format listing")
    private boolean isRFormat;

    @Option(name = "-o", usage = "Enable long format listing")
    private String outputFile;

    @Argument(usage = "Directory or file")
    private File directoryOrFile;

    public void execute(String[] args) throws IOException {
        CmdLineParser parser = new CmdLineParser(this);
        try {
            parser.parseArgument(args);  //метод разбирает аргументы и привязывает значения к полям класса Ls
        } catch (CmdLineException e) {
            System.err.println(e.getMessage());
            System.err.println("Usage: [-l] [-h] [-r] [-o output.file] directory_or_file");
            parser.printUsage(System.err);
            return;
        }
        //проверка существования указанного файла или директории:
        try {
            if (!directoryOrFile.exists()) throw new IOException();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.out.print("IOException");
            return;
        }
        //формируется список имен файлов, которые будут обработаны
        List<String> fileNames;
        if (directoryOrFile.isDirectory()) {
            fileNames = Arrays.asList(directoryOrFile.list());
            if (isRFormat) Collections.reverse(fileNames);
        } else {
            fileNames = Collections.singletonList(directoryOrFile.getName());
        }
        //построение строки вывода
        StringBuilder outputBuilder = new StringBuilder();
        for (String fileName : fileNames) {
            if (isLongFormat)
                outputBuilder.append(longFormat(fileName));
            else if (isHumanFormat)
                outputBuilder.append(humanReadable(fileName));
            else
                outputBuilder.append(String.format("%s\n", fileName));
        }

        if (outputFile != null) {
            FileWriter writer = new FileWriter(outputFile);
            writer.write(outputBuilder.toString());
            writer.close();
        } else {
            System.out.print(outputBuilder);
        }
    }


    public String longFormat(String fileName) {
        File file;
        if (directoryOrFile.isDirectory()) {
            file = new File(directoryOrFile, fileName);
        } else {
            file = directoryOrFile;
        }
        String permissions = String.format("%3s", Integer.toBinaryString(file.canExecute() ? 1 : 0)
                + Integer.toBinaryString(file.canRead() ? 1 : 0)
                + Integer.toBinaryString(file.canWrite() ? 1 : 0)).replace(' ', '0');
        String lastModified = getLastModifiedString(file);
        String size = getSizeString(file);
        return isRFormat ? String.format("%s %s %s %s\n", size, lastModified, permissions, fileName)
                : String.format("%s %s %s %s\n", fileName, permissions, lastModified, size);
    }


    public String humanReadable(String fileName) {
        File file;
        if (directoryOrFile.isDirectory()) {
            file = new File(directoryOrFile, fileName);
        } else {
            file = directoryOrFile;
        }
        String size = getHumanReadableSizeString(file);
        String permissions = getPermissionsString(file);
        return isRFormat ? String.format("%s %s %s\n", size, permissions, fileName)
                : String.format("%s %s %s\n", fileName, permissions, size);
    }


    private String getPermissionsString(File file) {
        return (file.canRead() ? "r" : "-") +
                (file.canWrite() ? "w" : "-") +
                (file.canExecute() ? "x" : "-");
    }


    private String getLastModifiedString(File file) {
        long lastModified = file.lastModified();
        return String.format("%tF %<tT", lastModified);
    }


    private String getSizeString(File file) {
        return String.format("%d", file.length());
    }

    private String getHumanReadableSizeString(File file) {
        long size = file.length();
        String[] units = {"B", "KB", "MB", "GB", "TB"};
        int index = 0;
        while (size >= 1024 && index < units.length - 1) {
            size /= 1024;
            index++;
        }
        return String.format("%d %s", size, units[index]);
    }
}
