package com.example.me.thinkbig;

public enum BasicVoca {
    think {
        @Override
        public String toString() {
            return "think";
        }

        @Override
        public String meaningOf() {
            return "생각하다";
        }
    },
    happy {
        @Override
        public String toString() {
            return "happy";
        }

        @Override
        public String meaningOf() {
            return "행복한";
        }
    };

    public abstract String toString();

    public abstract String meaningOf();

    public static int numberOf() {
        return BasicVoca.values().length;
    }

    public static BasicVoca valueOf(int n) {
        return BasicVoca.values()[n];
    }
}