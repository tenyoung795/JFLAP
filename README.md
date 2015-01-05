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
*   Use proper file URI when saving a JFF.
*   Properly scope nested Turing-machines.
*   More thread-safe. For one, Swing stuff actually runs on the Event Dispatch Thread
    now. Also, you can now render a new L-system derivation while JFLAP is still
    rendering another deriviation without corrupting the new derivation.
    Additionally, you can print while JFLAP is still rendering without corrupting
    either the printed document or the rendered image.

    One root cause of thread-unsafety was the Matrix class's use of (mutable) globals
    to cache angles and transformations. Consequently, the Matrix class
    no longer caches anything. (Simply turning the static fields into instance fields
    causes stack overflow.) However, this hasn't caused noticeable performance issues;
    in fact, refactoring the Renderer class sped up the process by a lot.
*   You can now see a L-system render over time.

![animating L-systems](https://github.com/tenyoung795/JFLAP/raw/master/lsystem.gif)

See [the issues](https://github.com/tenyoung795/JFLAP/issues) for TODOs.
