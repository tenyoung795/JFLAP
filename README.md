JFLAP
=====

Some modifications to the last version of JFLAP 7.0.

So far, the only modifications are

*   Converting to Gradle projects "thin" and "thick."
*   Refactoring how JFLAP decides whether to support SVG exporting.

    Previously, JFLAP checked for hardcoded JFLAP, JFLAP_Thin,
    and svg jars, and even a made-up foo.txt file in the org directory.
    This is rather fragile and doesn't even work with the thick
    JFLAP.jar download provided by jflap.org.

    Instead, only the thick project compiles the only class dealing with SVG.
    Then JFLAP just checks whether that class exists before providing the option
    to export a machine as SVG.

See [the issues](https://github.com/tenyoung795/JFLAP/issues) for TODOs.
