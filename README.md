Based off kalium, sodium4j is a Java binding of libSodium, implementing an object oriented interface. 

Three interfaces exist for the user: A low level direct exposure of the native functions, and two high level encapsulations of libsodium, that gives a familiar Java experience.

Misuse of the low level interface can cause your JVM to crash unexpectedly. 

This Readme and other documentation will be updated as this library is completed. Contributors are welcome, just log an issue. 

# Why another libsodium Java binding?

https://xkcd.com/927/

I have used many Java bindings of libsodium extensively, but have never found one that does exactly what I want.

# Design philosophy

* Inherit the philosophy of NaCl and libsodium, sodium4j offers easy to use and safe cryptographic primitives, rather than have a massive selection of ciphers, hashes, etc, many of which may be weak, and available through overly generic interfaces.

* Keep up to date with libsodium releases, and be backwards compatible with some older releases. Allow the user to use what versions they want to, but warn on serious issues.

* Be 100% compatible with all of libsodium's unit tests. 

# Interfaces

Sodium4j implements 3 interfaces: A low level, and 2 higher level interfaces. Of the latter, one allows specific primitives to be used, the other exposes generic interface into default primitives.

In addition, sodium4j provides additional supporting functions - handling of certificates, for example.

## Low level interface

This exposes libsodium's native interface to the user via JNR-FFI. ABI changes to libsodium will be reflected in this interface. *Backwards compatibility is/is not permitted here*

There is no error checking or handling here. This makes it very easy to corrupt or crash your JVM. Improper handling can expose you to security risks. There is no real reason to use this interface. We use it internally. 

Not all functions are implemented by sodium4j, and some signatures are different to their libsodium counterparts. 

## Encapsulated interface
The encapsulated interface exposes all primitives implemented by libsodium. This is the safest interface to use for long term stability, but can introduce security issues if you don't know what you are doing. 

All primitives in each category are used exactly the same as all others.

## Default interface
This is the easiest and best interface to use. 

# Default Primitives

From time to time, libsodium's default primitives change. Sodium4j will mirror these changes. These changes are usually backwards compatible.
