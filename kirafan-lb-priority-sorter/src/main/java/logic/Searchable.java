package logic;

import java.util.List;

public interface Searchable<T> {
   public List<T> find(String jxQuery);
   public List<T> findAll();
}
