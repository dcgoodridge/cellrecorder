package net.dcgoodridge.cellrecorder;


public class CircularArray<T> {

    private Object[] array;
    private int size = 0;
    private int capacity = 16;

    private int insertIndex = 0;

    public CircularArray(int capacity) {
        array = new Object[capacity];
        this.size = 0;
        this.capacity = capacity;
    }

    @SuppressWarnings("unchecked")
    public T get(int index) {
        final T item = (T) array[index];
        return item;
    }

    public void add(T item) {
        array[insertIndex] = item;

        insertIndex++;
        insertIndex = insertIndex % capacity;

        if (size < capacity) {
            size++;
        }

    }

    public int size() {
        return this.size;
    }

    public int capacity() {
        return this.capacity;
    }

    public void clear() {
        size = 0;
        insertIndex = 0;
    }


}
