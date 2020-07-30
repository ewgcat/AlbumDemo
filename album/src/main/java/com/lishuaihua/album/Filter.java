package com.lishuaihua.album;


public interface Filter<T> {

    /**
     * Filter the file.
     *
     * @param attributes attributes of file.
     * @return Filter returns true, otherwise false.
     */
    boolean filter(T attributes);

}
