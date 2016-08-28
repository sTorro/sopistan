package com.torrosoft.sopistan.utils;

import java.util.Iterator;

public class IterUtil {

    public static Iterable<Integer> iter(int to) {
        return new IntIterable(0, to);
    }

    public static Iterable<Integer> iter(int from, int to) {
        return new IntIterable(from, to);
    }


    private static class IntIterable implements Iterable<Integer> {

        private int start;
        private int end;

        private IntIterable(int start, int end) {
            this.start = start;
            this.end = end;
        }

        @Override
        public Iterator<Integer> iterator() {
            return new Iterator<Integer>() {
                private int actual = start;

                @Override
                public boolean hasNext() {
                    return actual != end;
                }

                @Override
                public Integer next() {
                    int value = actual;

                    if (actual < end) {
                        actual++;
                    } else if (actual > end) {
                        actual--;
                    }

                    return value;
                }

                @Override
                public void remove() {
                    // do nothing
                }
            };
        }
    }
}
