Sodium4j is yet another Java binding of the excellent libSodium, implementing an object oriented interface. 

Three interfaces exist for the user: A low level direct exposure of the native functions, and two high level
encapsulations of libsodium, that gives a familiar Java experience.

Misuse of the low level interface can cause your JVM to crash unexpectedly. Learn from our suffering, and use the OO
interfaces.

This Readme and other documentation will be updated as this library is completed. Contributors are welcome, please log
an issue if you want to join in.

# Why another libsodium Java binding?

https://xkcd.com/927/

I have used many Java bindings of libsodium extensively, but have never found one that does exactly what I want.

# Design philosophy

* Inherit the philosophy of NaCl and libsodium, sodium4j offers easy to use and safe cryptographic primitives, rather
than have a massive selection of ciphers, hashes, etc, many of which may be weak, and available through overly generic
interfaces.

* Keep up to date with libsodium releases, and be backwards compatible with some older releases. Allow the user to use
what versions they want to, but warn on serious issues.

* Be 100% compatible with all of libsodium's unit tests. Implement all of libsodium's tests where suitable.

* Behaviour of sodium4j must be exactly as expected from a user of libsodium. Differences must be clearly documented.

# Interfaces

Sodium4j implements 2 interfaces: One allows specific primitives to be used, the other exposes generic interface into 
default primitives.

In addition, sodium4j provides additional supporting functions - handling of certificates, for example.


## Encapsulated interface
The encapsulated interface exposes all primitives implemented by libsodium. This is the safest interface to use for
long term stability, but can introduce security issues if you don't know what you are doing.

All primitives in each category are used exactly the same as all others.

## Default interface
This is the easiest and best interface to use. 

# Default Primitives Changes

From time to time, libsodium's default primitives change. Sodium4j will mirror these changes. These changes are usually
backwards compatible, but aren't guaranteed to be.

# Versioning

The library version is based on the latest version of libsodium that we have tested against, appended with an 
incremental build number. That does not mean that an earlier version of sodium4j will work against a later version of 
libsodium.

Thus 1.0.16-2 is the second release built for libsodium 1.0.16.