package com.example.me.codinggame;

/**
 * Created by Me on 2018-01-20.
 */

public enum BasicVoca {
    dream {
        @Override
        public String toString() {
            return "dream";
        }

        @Override
        public String meaningOf() {
            return "꿈(목표)";
        }
    },
    sheep {
        @Override
        public String toString() {
            return "sheep";
        }

        @Override
        public String meaningOf() {
            return "양";
        }
    },
    sleep {
        @Override
        public String toString() {
            return "sleep";
        }

        @Override
        public String meaningOf() {
            return "잠";
        }
    };

    // for debugging
    /*
    culture {
        @Override
        public String toString() {
            return "culture";
        }

        @Override
        public String meaningOf() {
            return "문화";
        }
    };
    */
    public abstract String toString();
    public abstract String meaningOf();
    public static int numberOf() {
        return BasicVoca.values().length;
    }
    public static BasicVoca valueOf(int n) {
        return BasicVoca.values()[n];
    }
}
