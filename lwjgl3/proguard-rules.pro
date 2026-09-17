
-dontwarn com.badlogic.gdx.**
-dontwarn org.lwjgl.**


-keep class com.badlogic.gdx.** { *; }
-keep class org.lwjgl.** { *; }


-keepclasseswithmembers public class * {
    public static void main(java.lang.String[]);
}

