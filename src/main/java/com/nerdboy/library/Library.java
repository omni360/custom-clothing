package com.nerdboy.library;

import com.google.common.base.Joiner;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

public class Library {

    public class Libraries {
        private List<Library> libraries;

        public List<Library> getLibraries() {
            return libraries;
        }

        public void setLibraries(List<Library> libraries) {
            this.libraries = libraries;
        }
    }

    private String groupId;
    private String artifactId;
    private String version;

    public Library(String groupId, String artifactId, String version) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getArtifactId() {
        return artifactId;
    }

    public void setArtifactId(String artifactId) {
        this.artifactId = artifactId;
    }

    public String getVersion() {
        return version;
    }

    public void setVersion(String version) {
        this.version = version;
    }

    public String getRelativePath(){
        List<String> pathSections = new LinkedList<String>(Arrays.asList(groupId.split("\\.")));
        pathSections.addAll(Arrays.asList(artifactId.split("\\.")));
        String filename = String.format("%s-%s.jar", artifactId, version);

        Joiner joiner = Joiner.on(File.separator);
        return joiner.join(pathSections) + File.separator + version + File.separator + filename;
    }

    public URL constructMavenUrl() throws MalformedURLException {
        return new URL("http", "central.maven.org", "/maven2/"+getRelativePath());
    }

}
