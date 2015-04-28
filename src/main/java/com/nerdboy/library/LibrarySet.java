package com.nerdboy.library;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class LibrarySet implements Iterable<Library>{
    private List<Library> libraries;

    public LibrarySet() {
        this.libraries = new ArrayList<Library>();
    }

    public List<Library> getLibraries() {
        return libraries;
    }

    public void join(LibrarySet otherLibrarySet) {
        libraries.addAll(otherLibrarySet.getLibraries());
    }

    public int size() {
        return libraries.size();
    }

    @Override
    public Iterator<Library> iterator() {
        return libraries.listIterator();
    }
}
