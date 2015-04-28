package com.nerdboy.library;

import com.google.gson.Gson;
import com.nerdboy.library.download.LibraryDownloader;
import com.nerdboy.library.download.MavenLibraryDownloader;
import net.minecraft.launchwrapper.LaunchClassLoader;
import net.minecraftforge.fml.relauncher.IFMLCallHook;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

public class LibraryLoader implements IFMLCallHook{
    private LibraryDownloader libraryDownloader;
    private LibrarySet requiredLibraries;

    private LaunchClassLoader classLoader;
    private File mcLocation;
    private File modFolder;

    private JProgressBar fileProgressBar;
    private JProgressBar globalProgressBar;
    private JLabel label;

    @Override
    public void injectData(Map<String, Object> data) {
        mcLocation = (File) data.get("mcLocation");
        modFolder = ((File) data.get("coremodLocation")).getParentFile();
        classLoader = (LaunchClassLoader) data.get("classLoader");

        requiredLibraries = extractLibrarySet();

        File libraryFolder = new File(mcLocation, "libraries");
        libraryDownloader = new MavenLibraryDownloader(libraryFolder);
    }

    private LibrarySet extractLibrarySet() {
        LibrarySet libraries = new LibrarySet();

        for (File modFile : modFolder.listFiles()) {
        if (modFile.getName().endsWith(".jar")) {
            try {
                libraries.join(getLibrariesFromZipEntry(modFile));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
        return libraries;
    }

    private LibrarySet getLibrariesFromZipEntry(File modFile) throws IOException {
        Gson gson = new Gson();
        ZipFile zipFile = new ZipFile(modFile);
        ZipEntry entry = zipFile.getEntry("libraries.json");
        if(entry == null) {
            return new LibrarySet();
        } else {
            InputStream stream = zipFile.getInputStream(entry);
            return gson.fromJson(new InputStreamReader(stream), LibrarySet.class);
        }
    }

    @Override
    public Void call() throws Exception {
        JFrame jFrame = setupWindow();
        try
        {
            List<File> requiredJarFiles = downloadLibraries();
            addLibrariesToClassloader(requiredJarFiles, classLoader);
        }finally {
            jFrame.dispose();
        }

        return null;
    }

    private JFrame setupWindow() {
        JFrame jFrame = new JFrame("Downloading required libraries");
        jFrame.setSize(300, 100);
        jFrame.setLocationRelativeTo(null);
        label = new JLabel("Downloading required libraries");
        fileProgressBar = new JProgressBar();
        fileProgressBar.setMaximum(requiredLibraries.size());
        jFrame.setLayout(new BorderLayout());
        jFrame.add(label, BorderLayout.NORTH);
        jFrame.add(fileProgressBar, BorderLayout.CENTER);
        jFrame.setVisible(true);
        return jFrame;
    }

    private List<File> downloadLibraries() throws Exception {
        List<File> requiredJarFiles = new LinkedList<File>();
        LibraryDownloader.DownloadUpdateHandler handler = new LibraryDownloader.DownloadUpdateHandler() {
            @Override
            public void update(int progress, int maxProgress) {
                fileProgressBar.setMaximum(maxProgress);
                fileProgressBar.setValue(progress);
            }
        };

        Iterator<Library> it = requiredLibraries.iterator();


        for(int i = 0; it.hasNext(); i++) {
            Library library = it.next();
            requiredJarFiles.add(libraryDownloader.downloadLibrary(library, new LibraryDownloader.DownloadUpdateHandler[] {handler}));
        }
        return requiredJarFiles;
    }

    private void addLibrariesToClassloader(List<File> requiredJarFiles, LaunchClassLoader classLoader) throws MalformedURLException {
        for(File file : requiredJarFiles){
            classLoader.addURL(file.toURI().toURL());
        }
    }
}
