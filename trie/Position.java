package trie;

public interface Position<E> {
  E getElement() throws IllegalStateException;
}
