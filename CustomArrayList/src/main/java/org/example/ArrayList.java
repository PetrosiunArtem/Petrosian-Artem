package org.example;

/**
 * Интерфейс минимальных методов для создания собственного ArrayList
 *
 * @param <T> тип элемента
 */
public interface ArrayList<T> {
  /**
   * Добавление элемента в конец массива
   *
   * @param value значение добавляемого элемента
   */
  public void add(T value);

  /**
   * Взятие элемента по данному индексу
   *
   * @param index индекс элемента
   * @return значение элемента с индексом index
   */
  public T get(int index);

  /**
   * Удаление элемента по данному индексу
   *
   * @param index индекс элемента
   */
  public void remove(int index);
}
