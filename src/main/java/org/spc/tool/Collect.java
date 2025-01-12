package org.spc.tool;


import org.spc.api.IHamamapEx;
import org.spc.impl.EmptyHamaMap;

public interface Collect<K, V> {


    /**
     * Empty Hamamap (immutable - Fake)
     */
    IHamamapEx EMPTY_MAP = new EmptyHamaMap<>();


}
