package org.example;

import java.util.ArrayList;
import java.util.List;

public class ListCopyWrapper {
  private final ArrayList<Integer> list;

  public ListCopyWrapper(List<Integer> list) {
    this.list = (ArrayList<Integer>) list;
  }

  public List<Integer> getList() {
    return this.list;
  }

  public int size() {
    return this.list.size();
  }

  public int get(int index) {
    if (index >= this.list.size()) {
      throw new RuntimeException();
    }
    return this.list.get(index);
  }

  public void set(int index, int value) {
    if (index >= this.list.size()) {
      throw new RuntimeException();
    }
    this.list.set(index, value);
  }
}
