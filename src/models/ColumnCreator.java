package models;

public abstract class ColumnCreator<T, E> {
    public abstract E call(T t);
    public abstract double getWidth();
}
