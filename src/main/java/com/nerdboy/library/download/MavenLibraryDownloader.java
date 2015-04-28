package com.nerdboy.library.download;

import com.nerdboy.library.Library;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class MavenLibraryDownloader implements LibraryDownloader {
    private File libraryFolder;
    public MavenLibraryDownloader(File libraryFolder){
        this.libraryFolder = libraryFolder;
    }

    @Override
    public File downloadLibrary(Library library, DownloadUpdateHandler[] handlers) throws Exception {
        File libraryFile = new File(libraryFolder, library.getRelativePath());
        if(! libraryFile.exists()){
            libraryFile.getParentFile().mkdirs();
            downloadFromUrl(library.constructMavenUrl(), libraryFile.getAbsolutePath(), handlers);
        }
        return libraryFile;
    }

    void downloadFromUrl(URL url, String localFilename, DownloadUpdateHandler[] handlers) throws IOException {
        InputStream is = null;
        FileOutputStream fos = null;

        try {
            URLConnection urlConn = url.openConnection();
            int maxLength = urlConn.getContentLength();

            is = urlConn.getInputStream();               //get connection inputstream
            fos = new FileOutputStream(localFilename);   //open outputstream to local file

            byte[] buffer = new byte[4096];              //declare 4KB buffer
            int len;
            int progress = 0;

            //while we have availble data, continue downloading and storing to local file
            while ((len = is.read(buffer)) > 0) {
                fos.write(buffer, 0, len);
                progress += len;
                for(DownloadUpdateHandler handler : handlers){
                    handler.update(progress, maxLength);
                }
            }
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
            } finally {
                if (fos != null) {
                    fos.close();
                }
            }
        }
    }


}
