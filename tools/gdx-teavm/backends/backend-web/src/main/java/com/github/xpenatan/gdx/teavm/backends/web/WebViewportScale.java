package com.github.xpenatan.gdx.teavm.backends.web;

final class WebViewportScale {

    static final String PROPERTY = "girafferun.web.viewportScale";

    private WebViewportScale() {
    }

    static int scale(int size) {
        String value = System.getProperty(PROPERTY);
        if (value == null || value.isEmpty()) {
            return size;
        }
        float scale = Float.parseFloat(value);
        return Math.max(1, (int) (size * scale));
    }
}
