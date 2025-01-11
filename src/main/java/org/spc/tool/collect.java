package org.spc.tool;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import java.io.Serializable;
import java.util.AbstractMap;
import java.util.Map;

public interface collect {


    /**
     * The empty map (immutable).  This map is serializable.
     */
    @NotNull @Unmodifiable Map EMPTY_MAP = new EmptyMap<>();


    class EmptyMap<K, V> extends AbstractMap<K, V> implements Serializable {
        //todo
    }
}
