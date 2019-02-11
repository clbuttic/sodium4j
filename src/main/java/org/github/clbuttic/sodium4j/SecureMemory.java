package org.github.clbuttic.sodium4j;

import com.sun.jna.Memory;
import com.sun.jna.Pointer;

/**
 * A secure memory region that extends com.sun.jna.Memory
 *
 * Underlying data is locked, and not copied across the native interface except when requested.
 *
 * Underlying data is zero'ed out when not needed.
 */
public class SecureMemory {
    private Pointer pointer;
    private long length;

    /**
     * Constructor without arguments.
     *
     * Sets internal pointer to null.
     */
    public SecureMemory() {
        pointer = Pointer.NULL;
        length = 0;
    }

    /**
     * Constructor with space allocation.
     *
     * Allocations len bytes immediately.
     * @param len
     */
    public SecureMemory(long len) {
        this.length = len;
        pointer = new Memory(len);
    }

    /**
     * Constructor from an array.
     *
     * Allocates memory and copies over data from the array.
     * @param array the array to copy data from.
     */
    public SecureMemory(byte[] array) {
        length = array.length;
        pointer = new Memory(length);
        pointer.write(0, array, 0, (int)length);
    }

    /**
     * Create a new copy of this instance.
     * @return A new copy.
     */
    public SecureMemory copyOf() {
        return copyOfRange(0, getLength());
    }

    /**
     * Return a shorter copy of this instance.
     *
     * @param offset Where to start copying
     * @param length How many bytes to copy.
     * @return A new copy
     */
    public SecureMemory copyOfRange(long offset, long length) {
        if (offset < 0 || offset > this.getLength()) {
            throw new IllegalArgumentException("offset must be a legal number");
        }
        if (offset + length > this.getLength())
            throw new IllegalArgumentException("Too many bytes requested: offset + length goes beyond the last byte.");

        SecureMemory tmp = new SecureMemory(length);
        Sodium4J.getCLibrary().memcpy(tmp.getPointer(), this.getPointer().getPointer(offset), length);
        return tmp;
    }

    /**
     * Get the length of the internal data.
     * @return the lenght in bytes.
     */
    public long getLength() {
        return length;
    }

    /**
     * Returns the internal JNA pointer.
     * @return the interal pointer.
     */
    public Pointer getPointer() {
        return pointer;
    }

}
