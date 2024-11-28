-injars       build/libs/app-all.jar
-outjars      build/libs/app-all-out.jar
-printmapping build/libs/app-all.map

-keep class kotlin.Metadata

-dontwarn **

-dontobfuscate

-keep public class com.github.huymaster.App {
    public static void main(java.lang.String[]);
}

-keepclasseswithmembers class com.formdev.flatlaf.FlatLaf {
    public boolean isSupportedLookAndFeel();
    public boolean isNativeLookAndFeel();
}