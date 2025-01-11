package org.spc.tool;


import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;
import org.spc.api.IHamamapEx;
import org.spc.impl.EmptyHamaMap;

public interface collect<K, V> {


    /**
     * Empty Hamamap (immutable - Fake)
     */
    @NotNull
    @Unmodifiable
    IHamamapEx EMPTY_MAP = new EmptyHamaMap();


}
