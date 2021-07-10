package org.lixl.java.lang;

abstract class LAbstractStringBuilder implements Appendable, CharSequence {

    char[] value;

    int count;

    LAbstractStringBuilder() {
    }

    LAbstractStringBuilder(int capacity) {
        value = new char[capacity];
    }

}
