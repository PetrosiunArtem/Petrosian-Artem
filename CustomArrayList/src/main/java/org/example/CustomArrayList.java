package org.example;

public class CustomArrayList<T> implements ArrayList<T> {
  private Object[] arrayList;
  private int capacity = 0;
  private int currentSize;

  @SafeVarargs
  public CustomArrayList(T... values) {
    int length = values.length;
    allocateMemory(length);
    this.arrayList = values;
    this.currentSize = arrayList.length;
  }

  private void allocateMemory(int length) {
    if (length <= this.capacity) return;
    while (capacity < length) {
      this.capacity = Math.max(1, 2 * this.capacity);
    }
    Object[] newArray = new Object[this.capacity];
    assert this.arrayList != null;
    System.arraycopy(this.arrayList, 0, newArray, 0, this.currentSize);
    this.arrayList = newArray.clone();
  }

  public void add(T object) {
    if (this.currentSize + 1 >= this.capacity) {
      allocateMemory(this.currentSize + 1);
    }
    this.arrayList[this.currentSize] = object;
    this.currentSize++;
  }

  @Override
  public T get(int index) throws IndexOutOfBoundsException {
    if (index < 0 || index >= this.currentSize) {
      throw new IndexOutOfBoundsException(index);
    }
    return (T) this.arrayList[index];
  }

  @Override
  public void remove(int index) {
    if (index < 0 || index >= this.currentSize) {
      throw new IndexOutOfBoundsException(index);
    }
    for (int i = index; i + 1 < this.currentSize; i++) {
      this.arrayList[i] = arrayList[i + 1];
    }
    this.currentSize -= 1;
  }

  public int size() {
    return this.currentSize;
  }
}
