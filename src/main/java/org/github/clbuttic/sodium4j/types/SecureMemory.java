package org.github.clbuttic.sodium4j.types;

import com.sun.jna.Pointer;
import org.github.clbuttic.sodium4j.Sodium4J;
import org.github.clbuttic.sodium4j.library.SodiumLibrary;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * A secure memory region, and primary type for Sodium4J usage.
 *
 * Underlying data is guarded. It is zero'ed out when disposed. Memory access can be changed at any time. It is
 * suggested that long lived sensitive data is set to disallow access when not needed, and destroyed when not needed,
 * using dispose().
 *
 * References to instances of this class can be passed around any number of times. The underlying data will be copied
 * only when explicitly requested, and only when access is allowed.
 *
 * This memory region can be treated as an arbitrary length number, for nonces, etc.
 */
public class SecureMemory {
    private Pointer pointer;
    private long length;
    private Access access = Access.ACCESS_READWRITE;
    //Allows for shorter calls
    private static SodiumLibrary library;

    /**
     * Constructor without arguments.
     *
     * This is disallowed, as null pointers or null SecureMemory regions are not allowed right now.
     *
     * The only time an instance does not point to an allocated memory region is if after it has been disposed.
     */
    private SecureMemory() {
    }

    /**
     * Constructor with space allocation.
     *
     * Allocations len bytes immediately.
     * @param len The length to allocate internally.
     */
    public SecureMemory(long len) {
        if (library == null)
            library = Sodium4J.getLibrary();
        this.length = len;
        pointer = Sodium4J.getLibrary().sodium_malloc(len);
        zero();
    }

    /**
     * Constructor from an array.
     *
     * Allocates memory and copies over data from the array.
     * @param array the array to copy data from.
     */
    public SecureMemory(byte[] array) {
        if (library == null)
            library = Sodium4J.getLibrary();
        length = array.length;
        pointer = Sodium4J.getLibrary().sodium_malloc(length);
        pointer.write(0, array, 0, (int)length);
    }

    /**
     * A copy constructor. Construct a copy from another instance.
     * @param source A SecureMemory to copy.
     */
    public SecureMemory(SecureMemory source) {
        if (source.canRead())
            throw new IllegalStateException("Source instance is read protected.");
        SecureMemory tmp = source.copyOfRange(0, getLength());;
        length = tmp.getLength();
        pointer = tmp.getPointer();
        access = tmp.access;
    }

    /**
     * Destroy the object when GC'ed
     */
    @Override
    public void finalize() {
        dispose();
    }

    /**
     * Destroy the underlying data.
     *
     * Call this when you do not need the data anymore. This is sometimes called automatically on GC.
     */
    public void dispose() {
        if (pointer == Pointer.NULL)
            return;
        //To dispose, we need to change access. It would be nice if the following block happens atomically.
        setAccessReadWrite();
        library.sodium_free(pointer);
        length = 0;
        pointer = Pointer.NULL;
    }

    /**
     * Return a shorter copy of this instance.
     *
     * @param offset Where to start copying
     * @param length How many bytes to copy.
     * @throws IllegalStateException If read protected.
     * @return A new copy
     */
    public SecureMemory copyOfRange(long offset, long length) {
        if (pointer == Pointer.NULL)
            throw new NullPointerException("Instance has been disposed.");
        if (!canRead())
            throw new IllegalStateException("Memory is read protected.");
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
     * Copy the memory region to a buffer.
     *
     * Buffer must be correctly allocated.
     * @param buffer The buffer to copy the internal region to.
     */
    public void copyTo(byte[] buffer) {
        if (pointer == Pointer.NULL)
            throw new NullPointerException("Instance has been disposed.");
        if (!canRead())
            throw new IllegalStateException("Memory is read protected.");
        if (buffer == null || buffer.length != getLength())
            throw new IllegalArgumentException("Supplied buffer is not the correct length");
        getPointer().read(0, buffer, 0, (int)getLength());
    }

    /**
     * Convert the memory region to a string, using a specific Charset.
     *
     * @param charset The charset to encode the String to.
     * @return The memory region as a String.
     */
    public String toString(Charset charset) {
        byte[] tmp = new byte[(int)getLength()];
        copyTo(tmp);
        return new String(tmp, charset);
    }

    /**
     * Convert the memory region to a String, using ISO8859:1 Charset.
     * @return The memory region as a String.
     */
    public String toString() {
        return toString(StandardCharsets.ISO_8859_1);
    }

    /**
     * Get the length of the internal data.
     * @return the length in bytes.
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

    /**
     * Zero out the memory.
     */
    public void zero() {
        if (pointer == Pointer.NULL)
            throw new NullPointerException("Instance has been disposed.");
        if (!canWrite())
            throw new IllegalStateException("Memory is write protected.");
        library.sodium_memzero(getPointer(), getLength());
    }

    /**
     * Tests if the memory region is all zero.
     * @return true if n contains all zeros, false if otherwise.
     */
    public boolean isZero() {
        if (pointer == Pointer.NULL)
            throw new NullPointerException("Instance has been disposed.");
        if (!canRead())
            throw new IllegalStateException("Memory is read protected.");
        int ret = Sodium4J.getLibrary().sodium_is_zero(getPointer(), getLength());

        switch(ret) {
            case 1:
                return true;
            case 0:
                return false;
            default:
                throw new IllegalStateException("Unknown return from sodium_is_zero: " + ret);
        }
    }

    /**
     * Returns true if this instance equals another SecureMemory;
     *
     * This comparison is performed in constant time, independent of contents of this or b.
     * @param b An instance to compare against.
     * @return True if equals, false otherwise.
     */
    public boolean equals(SecureMemory b) {
        if (getPointer() == Pointer.NULL)
            throw new NullPointerException("Instance has been disposed.");
        if (b.getPointer() == Pointer.NULL)
            throw new NullPointerException("Compared instance has been disposed.");

        if (!canRead())
            throw new IllegalStateException("Memory is read protected.");
        if (!b.canRead())
            throw new IllegalStateException("Compared instance is read protected.");

        //If the lengths are different, then they definitely aren't equals
        if (getLength() != b.getLength())
            return false;

        int ret = Sodium4J.getLibrary().sodium_memcmp(getPointer(), b.getPointer(), getLength());

        switch (ret) {
            case -1:
                return false;
            case 0:
                return true;
            default:
                throw new IllegalStateException("Unknown return from sodium_memcmp: " + ret);
        }
    }

    /**
     * Increment the region, treating it as an arbitrary length little endian number.
     */
    public void Increment() {
        if (pointer == Pointer.NULL)
            throw new NullPointerException("Instance has been disposed.");
        if (!canRead())
            throw new IllegalStateException("Memory is read protected.");

        Sodium4J.getLibrary().sodium_increment(getPointer(), getLength());
    }

    /**
     * Add a regions to the instance, treating both as arbitrary length little endian numbers
     * @param b The value to add.
     * @throws IllegalArgumentException if the arrays are not the same length
     */
    public void Add(SecureMemory b) {
        if (getPointer() == Pointer.NULL)
            throw new NullPointerException("Instance has been disposed.");
        if (b.getPointer() == Pointer.NULL)
            throw new NullPointerException("Compared instance has been disposed.");

        if (!canRead())
            throw new IllegalStateException("Memory is read protected.");
        if (!b.canRead())
            throw new IllegalStateException("Compared instance is read protected.");

        if (getLength() != b.getLength())
            throw new IllegalArgumentException("Memory regions are not the same length");

        Sodium4J.getLibrary().sodium_add(getPointer(), b.getPointer(), getLength());
    }

    /**
     * Subtracts a region from this instance, treating both as arbitrary length little endian numbers.
     * @param b The region to subtract.
     * @throws IllegalArgumentException if the arrays are not the same length
     */
    public  void Subtract(SecureMemory b) {
        if (getPointer() == Pointer.NULL)
            throw new NullPointerException("Instance has been disposed.");
        if (b.getPointer() == Pointer.NULL)
            throw new NullPointerException("Compared instance has been disposed.");

        if (!canRead())
            throw new IllegalStateException("Memory is read protected.");
        if (!b.canRead())
            throw new IllegalStateException("Compared instance is read protected.");

        if (getLength() != b.getLength())
            throw new IllegalArgumentException("Memory regions are not the same length");

        Sodium4J.getLibrary().sodium_sub(getPointer(), b.getPointer(), getLength());

    }

    /**
     * Compare this region with another, treating both as arbitrary length little endian numbers.
     * @param b The region to compare
     * @return -1 if this < b, 0 if this == b, 1 if this > b
     */
    public int Compare(SecureMemory b) {
        if (getPointer() == Pointer.NULL)
            throw new NullPointerException("Instance has been disposed.");
        if (b.getPointer() == Pointer.NULL)
            throw new NullPointerException("Compared instance has been disposed.");

        if (!canRead())
            throw new IllegalStateException("Memory is read protected.");
        if (!b.canRead())
            throw new IllegalStateException("Compared instance is read protected.");

        if (getLength() != b.getLength())
            throw new IllegalArgumentException("Arrays are not the same length");

        return Sodium4J.getLibrary().sodium_compare(getPointer(), b.getPointer(), getLength());
    }

    /**
     * Lock the memory region.
     *
     * We use sodim_malloc, so locking is done automatically, and we don't call this internally. The class caller is
     * free to lock and unlock as they see fit.
     */
    public void lock() {
        //TODO: Locking can fail, test this.
        library.sodium_mlock(getPointer(), getLength());
    }

    /**
     * Unlock the memory region.
     *
     * We don't internally lock or unlock, but the class caller is free to do this.
     */
    public void unlock() {
        //TODO: Unlocking can fail, test this.
        library.sodium_munlock(getPointer(), getLength());
    }

    /**
     * Fill the region with random, unpredictable data.
     */
    public void fillRandom() {
        if (getPointer() == Pointer.NULL)
            throw new NullPointerException("Instance has been disposed.");

        if (!canWrite())
            throw new IllegalStateException("Memory is write protected.");

        Sodium4J.getLibrary().randombytes_buf(getPointer(), getLength());
    }

    /**
     * Sets this memory region as being inaccessible.
     *
     * Any reads or writes to this will result in an exception being thrown, or if not done via this class, program
     * termination.
     */
    public void setAccessNone() {
        if (access == Access.ACCESS_NONE)
            return;
        library.sodium_mprotect_noaccess(getPointer());
        access = Access.ACCESS_NONE;
    }

    /**
     * Set this memory region to being read only.
     *
     * Any writes to this will result in an exception being thrown, or if not done via this class, program
     * termination.
     */
    public void setAccessReadOnly() {
        if (access == Access.ACCESS_READONLY)
            return;
        library.sodium_mprotect_readonly(getPointer());
        access = Access.ACCESS_READONLY;
    }

    /**
     * Allows read or write access to this memory region.
     */
    public void setAccessReadWrite() {
        if (access == Access.ACCESS_READWRITE)
            return;
        library.sodium_mprotect_readwrite(getPointer());
        access = Access.ACCESS_READWRITE;
    }

    /**
     * Check if we are allowed to read from the memory region.
     * @return True if an read operation is allowed.
     */
    public boolean canRead() {
        switch (access) {
            case ACCESS_NONE:
                return false;
            case ACCESS_READONLY:
            case ACCESS_READWRITE:
                return true;
        }
        //The fallthrough is assumed to be false
        return false;
    }

    /**
     * Check if we are allowed to write to the memory region.
     * @return True if an write operation is allowed.
     */
    public boolean canWrite() {
        switch (access) {
            case ACCESS_NONE:
            case ACCESS_READONLY:
                return false;
            case ACCESS_READWRITE:
                return true;
        }
        //The fallthrough is assumed to be false
        return false;
    }

    /**
     * Used internally to track the locking state.
     */
    private enum Access {
        ACCESS_READWRITE,
        ACCESS_READONLY,
        ACCESS_NONE;
    };
}
