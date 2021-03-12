package com.rakaneth.view;

import com.valkryst.VTerminal.component.VPanel;

public final class UIUtils {
    //naive writeString implementation
    public static void writeString(int x, int y, String s, VPanel panel) {
        for (int i = 0; i < s.length(); i++) {
            panel.setCodePointAt(x, y, s.codePointAt(i));
        }
    }

     public static String displayBoundedStat(int min, int max) {
        return String.format("%d/%d", min, max);
     }

    public static class Console {
        public final int width;
        public final int height;
        public final int x;
        public final int y;
        public final String caption;
        private VPanel panel;

        public Console(int x, int y, int width, int height, String caption, VPanel panel) {
            this.x = x;
            this.y = y;
            this.width = width;
            this.height = height;
            this.caption = caption;
            this.panel = panel;
        }

        public Console(int x, int y, int width, int height, VPanel panel) {
            this(x, y, width, height, "", panel);
        }

        private void putChar(int x, int y, char c) {
            int rx = this.x + x;
            int ry = this.y + y;
            panel.setCodePointAt(rx, ry, c);
        }

        public void writeString(int x, int y, String s) {
            for (int i = 0; i < s.length(); i++) {
                putChar(x + i, y, s.charAt(i));
            }
        }

        public void border() {
            final char ul = '\u2554';
            final char ll = '\u255A';
            final char ur = '\u2557';
            final char lr = '\u255D';
            final char horz = '\u2550';
            final char vert = '\u2551';
            int xEnd = width - 1;
            int yEnd = height - 1;

            putChar(0, 0, ul);
            putChar(0, yEnd, ll);
            putChar(xEnd, 0, ur);
            putChar(xEnd, yEnd, lr);

            for (int xs = 1; xs < xEnd; xs++) {
                putChar(xs, 0, horz);
                putChar(xs, yEnd, horz);
            }

            for (int ys = 1; ys < yEnd; ys++) {
                putChar(0, ys, vert);
                putChar(xEnd, ys, vert);
            }

            if (!caption.isEmpty()) {
                writeString(1, 0, caption);
            }
        }


    }
}
