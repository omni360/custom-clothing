package com.nerdboy.library.download;

import com.nerdboy.library.Library;

import java.io.File;

public interface LibraryDownloader {
    public File downloadLibrary(Library library, DownloadUpdateHandler[] handlers) throws Exception;

    public interface DownloadUpdateHandler {
        public void update(int progress, int maxProgress);
    }
}
