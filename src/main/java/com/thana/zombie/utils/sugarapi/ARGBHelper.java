package com.thana.skyblockdeluxe.utils.sugarapi;

import com.mojang.blaze3d.systems.RenderSystem;

public class ARGBHelper {

    public static int alpha(int alpha) {
        return alpha >>> 24;
    }

    public static int red(int red) {
        return red >> 16 & 255;
    }

    public static int green(int green) {
        return green >> 8 & 255;
    }

    public static int blue(int blue) {
        return blue & 255;
    }

    public static int to32BitColor(int a, int r, int g, int b) {
        return a << 24 | r << 16 | g << 8 | b;
    }

    public static int multiply(int i, int j) {
        return to32BitColor(alpha(i) * alpha(j) / 255, red(i) * red(j) / 255, green(i) * green(j) / 255, blue(i) * blue(j) / 255);
    }

    public static int toChatColor(int a, int r, int g, int b) {
        int rgb = ARGBHelper.toChatColor(r, g, b);
        int alpha = a << 24 & -16777216;
        return rgb | alpha;
    }

    public static int toChatColor(int r, int g, int b) {
        return r * 65536 + g * 256 + b;
    }

    public static String rgbToHex(int r, int g, int b) {
        return String.format("#%02x%02x%02x", r, g, b);
    }

    public static String fastRgbToHex(int r, int g, int b) {
        return "#" + Integer.toHexString(toChatColor(r, g, b));
    }

    public static String fastArgbToHex(int a, int r, int g, int b) {
        return "#" + Integer.toHexString(toChatColor(a, r, g, b));
    }

    public static int mix(float saturation, int colorA, int colorB) {
        if (saturation > 1)
            saturation = 1.0F;
        else if (saturation < 0)
            saturation = 0.F;
        float j = 1.0F - saturation;

        int a1 = alpha(colorA);
        int r1 = red(colorA);
        int g1 = green(colorA);
        int b1 = blue(colorA);

        int a2 = alpha(colorB);
        int r2 = red(colorB);
        int g2 = green(colorB);
        int b2 = blue(colorB);

        int a = (int)((a1 * j) + (a2 * saturation));
        int r = (int)((r1 * j) + (r2 * saturation));
        int g = (int)((g1 * j) + (g2 * saturation));
        int b = (int)((b1 * j) + (b2 * saturation));
        return to32BitColor(a, r, g, b);
    }

    public static int increaseGreen(int colorFrom, int colorTo, float stepSize, float steps) {
        int red1 = red(colorFrom);
        int blue1 = blue(colorFrom);
        int red2 = red(colorTo);
        int blue2 = blue(colorTo);

        int minGreen = Math.min(green(colorFrom), green(colorTo));
        int maxGreen = Math.max(green(colorFrom), green(colorTo));

        float s = steps * stepSize;
        int i = 0;
        while (s > maxGreen) {
            s -= maxGreen;
            i++;
        }
        int green = i % 2 == 1 ? (int) (maxGreen - s) : (int) (minGreen + s);
        return toChatColor(red1 | red2, green, blue1 | blue2);
    }

    public static int increaseRed(int colorFrom, int colorTo, float stepSize, float steps) {
        int green1 = green(colorFrom);
        int blue1 = blue(colorFrom);
        int green2 = green(colorTo);
        int blue2 = blue(colorTo);

        int minRed = Math.min(red(colorFrom), red(colorTo));
        int maxRed = Math.max(red(colorFrom), red(colorTo));

        float s = steps * stepSize;
        int i = 0;
        while (s > maxRed) {
            s -= maxRed;
            i++;
        }
        int red = i % 2 == 1 ? (int) (maxRed - s) : (int) (minRed + s);
        return toChatColor(red, green1 | green2, blue1 | blue2);
    }

    public static int increaseBlue(int colorFrom, int colorTo, float stepSize, float steps) {
        int red1 = red(colorFrom);
        int green1 = green(colorFrom);
        int red2 = red(colorTo);
        int green2 = green(colorTo);

        int minBlue = Math.min(blue(colorFrom), blue(colorTo));
        int maxBlue = Math.max(blue(colorFrom), blue(colorTo));

        float s = steps * stepSize;
        int i = 0;
        while (s > maxBlue) {
            s -= maxBlue;
            i++;
        }
        int blue = i % 2 == 1 ? (int) (maxBlue - s) : (int) (minBlue + s);
        return toChatColor(red1 | red2, green1 | green2, blue);
    }

    public static int increase(int colorFrom, int colorTo, float stepSize, float steps) {
        int red = red(increaseRed(colorFrom, colorTo, stepSize, steps));
        int green = green(increaseGreen(colorFrom, colorTo, stepSize, steps));
        int blue = blue(increaseBlue(colorFrom, colorTo, stepSize, steps));
        return toChatColor(red, green, blue);
    }

    public static int gradient(int colorFrom, int colorTo, float scale) {
        int r = (int) (red(colorFrom) * scale + red(colorTo) * (1.0F - scale));
        int g = (int) (green(colorFrom) * scale + green(colorTo) * (1.0F - scale));
        int b = (int) (blue(colorFrom) * scale + blue(colorTo) * (1.0F - scale));
        return toChatColor(r, g, b);
    }

    public static float[] rgbArray(int r, int g, int b) {
        return new float[]{r / 255.0F, g / 255.0F, b / 255.0F};
    }

    public static float[] rgbaArray(int r, int g, int b, int a) {
        return new float[]{r / 255.0F, g / 255.0F, b / 255.0F, a / 255.0F};
    }

    public static float[] decimalArray(int color) {
        int r = red(color);
        int g = green(color);
        int b = blue(color);
        return rgbArray(r, g, b);
    }

    public static int rgbStringTo32Bit(String color) {
        try {
            String[] colorArray = color.split(",");
            int red = Integer.parseInt(colorArray[0]);
            int green = Integer.parseInt(colorArray[1]);
            int blue = Integer.parseInt(colorArray[2]);
            boolean hasAlpha = colorArray.length == 4;
            if (hasAlpha) {
                int alpha = Integer.parseInt(colorArray[3]);
                return alpha << 24 | red << 16 | green << 8 | blue;
            }
            else {
                return blue + 256 * green + 65536 * red;
            }
        }
        catch (NumberFormatException | ArrayIndexOutOfBoundsException e) {
            return 0;
        }
    }

    public static void color4f(float a, float r, float g, float b) {
        RenderSystem.setShaderColor(r, g, b, a);
    }
}
