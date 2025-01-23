package ru.t1.java.demo.service.impl;

import java.io.IOException;
import java.util.List;

public interface ImplService<E, D> {
    void registerEvents(List<E> events);

    List<D> parseJson() throws IOException;
}
