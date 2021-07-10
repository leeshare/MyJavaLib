package org.lixl.hadoop.lixlsource.fs;

import java.io.InvalidObjectException;
import java.io.ObjectInputValidation;
import java.io.Serializable;
import java.net.URI;

public class Path implements Comparable, Serializable, ObjectInputValidation {

    private URI uri;

    private void checkPathArg(String path) throws IllegalArgumentException {
        if (path == null) {
            throw new IllegalArgumentException("Can not create a Path from a null string");
        }
        if (path.length() == 0) {
            throw new IllegalArgumentException("Can not create a Path from an empty string");
        }
    }

    public Path(String pathString) throws IllegalArgumentException {
        checkPathArg(pathString);
    }

    @Override
    public void validateObject() throws InvalidObjectException {
        if (uri == null)
            throw new InvalidObjectException("no URI in deserialized Path");
    }

    @Override
    public int compareTo(Object o) {
        return 0;
    }
}
