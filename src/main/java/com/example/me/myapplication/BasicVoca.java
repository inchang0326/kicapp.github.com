package com.example.me.myapplication;

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
