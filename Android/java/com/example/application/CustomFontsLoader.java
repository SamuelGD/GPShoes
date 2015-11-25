package com.example.application;

import android.content.Context;
import android.graphics.Typeface;

public class CustomFontsLoader {
    public static final int PWPerspective = 0;
    public static final int CaviarDreams = 1;
    public static final int CaviarDreams_Bold = 2;
    public static final int fff = 3;
    public static final int HalloSansBlack = 4;
    public static final int HalloSansLight = 5;
    public static final int HalloSans = 6;

    private static final int NUM_OF_CUSTOM_FONTS = 7;

    private static boolean fontsLoaded = false;

    private static Typeface[] fonts = new Typeface[7];

    private static String[] fontPath =
            {

                    "fonts/PWPerspective.ttf",
                    "fonts/CaviarDreams.ttf",
                    "fonts/CaviarDreams_Bold.ttf",
                    "fonts/fff.ttf",
                    "fonts/HalloSansBlack.otf",
                    "fonts/HalloSansLight.otf",
                    "fonts/HalloSans.otf"


            };


    /**
     * Returns a loaded custom font based on it's identifier.
     *
     * @param context        - the current context
     * @param fontIdentifier = the identifier of the requested font
     * @return Typeface object of the requested font.
     */
    public static Typeface getTypeface(Context context, int fontIdentifier) {
        if (!fontsLoaded) {
            loadFonts(context);
        }
        return fonts[fontIdentifier];
    }




    private static void loadFonts(Context context) {
    for (int i = 0; i < NUM_OF_CUSTOM_FONTS; i++) {
        fonts[i] = Typeface.createFromAsset(context.getAssets(), fontPath[i]);
    }
    fontsLoaded = true;

}
}