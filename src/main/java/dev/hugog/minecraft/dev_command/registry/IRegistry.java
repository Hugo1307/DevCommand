package dev.hugog.minecraft.dev_command.registry;

import java.util.List;

public interface IRegistry<K, V> {

    void add(K key, V value);
    void remove(K key);
    List<V> getValues(K key);
    void setValues(K key, List<V> value);

}
