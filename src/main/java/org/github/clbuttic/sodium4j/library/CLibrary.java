package org.github.clbuttic.sodium4j.library;

import com.sun.jna.Library;
import com.sun.jna.Pointer;

public interface CLibrary extends Library {

    public void memcpy(Pointer a, Pointer b, long length);
}
