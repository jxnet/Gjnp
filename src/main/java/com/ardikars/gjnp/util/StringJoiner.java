package com.ardikars.gjnp.util;

import java.util.Iterator;

public class StringJoiner {

    private final String delimiter;

    public StringJoiner(String delimiter) {
        this.delimiter = delimiter;
    }

    public String join(Iterable<String> iterable) {
        return join(iterable.iterator());
    }

    public String join(Iterator<String> iterator) {
        StringBuilder sb = new StringBuilder();
        sb.append(iterator.next());
        while (iterator.hasNext()) {
            sb.append(delimiter);
            sb.append(iterator.next());
        }
        return sb.toString();
    }

}
