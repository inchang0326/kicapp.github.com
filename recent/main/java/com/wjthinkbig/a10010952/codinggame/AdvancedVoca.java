package com.wjthinkbig.a10010952.codinggame;

public enum AdvancedVoca {
    breakfast {
        @Override
        public String toString() {
            return "breakfast";
        }

        @Override
        public String meaningOf() {
            return "아침식사";
        }
    },
    classmate {
        @Override
        public String toString() {
            return "classmate";
        }

        @Override
        public String meaningOf() {
            return "반 친구";
        }
    },
    chopstick {
        @Override
        public String toString() {
            return "chopstick";
        }

        @Override
        public String meaningOf() {
            return "젓가락";
        }
    },
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
