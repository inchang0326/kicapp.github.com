package com.wjthinkbig.a10010952.codinggame;

public enum AdvancedVoca {
    computer {
        @Override
        public String toString() {
            return "computer";
        }

        @Override
        public String meaningOf() {
            return "컴퓨터";
        }
    },
    birthday {
        @Override
        public String toString() {
            return "birthday";
        }

        @Override
        public String meaningOf() {
            return "생일";
        }
    };

    public abstract String toString();

    public abstract String meaningOf();

    public static int sizeOf() {
        return AdvancedVoca.values().length;
    }

    public static AdvancedVoca valueOf(int n) {
        return AdvancedVoca.values()[n];
    }

    public static boolean contain(String voca) {
        for(int i=0; i<AdvancedVoca.sizeOf(); i++) {
            if(AdvancedVoca.valueOf(i).toString().equals(voca)) {
                return true;
            }
        }
        return false;
    }
}
