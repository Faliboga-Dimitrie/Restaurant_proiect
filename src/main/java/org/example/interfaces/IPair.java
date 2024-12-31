package org.example.interfaces;

public interface IPair<T,U> {
    T getFirst();
    U getSecond();
    void setFirst(T first);
    void setSecond(U second);
}
