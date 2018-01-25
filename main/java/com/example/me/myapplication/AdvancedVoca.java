package com.example.me.myapplication;

/**
 * Created by Me on 2018-01-20.
 */

public enum AdvancedVoca {
    important {
        @Override
        public String toString() {
            return "important";
        }

        @Override
        public String meaningOf() {
            return "중요한";
        }
    },
    experience {
        @Override
        public String toString() {
            return "experience";
        }

        @Override
        public String meaningOf() {
            return "경험";
        }
    },
    treasure {
        @Override
        public String toString() {
            return "treasure";
        }

        @Override
        public String meaningOf() {
            return "보물";
        }
    };

    public abstract String toString();
    public abstract String meaningOf();
    public static int numberOf() {
        return AdvancedVoca.values().length;
    }
    public static AdvancedVoca valueOf(int n) {
        return AdvancedVoca.values()[n];
    }
}
