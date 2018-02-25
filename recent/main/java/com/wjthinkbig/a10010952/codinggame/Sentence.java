package com.wjthinkbig.a10010952.codinggame;

public enum Sentence {
    ilikebanana {
        @Override
        public String toString() {
            return "ilikebanana";
        }

        @Override
        public String meaningOf() {
            return "나는 바나나를 좋아해";
        }

        @Override
        public String expression() {
            return "I like banana";
        }
    },
    iplaysoccer {
        @Override
        public String toString() {
            return "iplaysoccer";
        }

        @Override
        public String meaningOf() {
            return "나는 축구한다";
        }

        @Override
        public String expression() {
            return "I play soccer";
        }
    },
    istayathome {
        @Override
        public String toString() {
            return "istayathome";
        }

        @Override
        public String meaningOf() {
            return "나는 집에 있는다.";
        }

        @Override
        public String expression() {
            return "I stay at home";
        }
    },
    rainbowisbeautiful {
        @Override
        public String toString() {
            return "rainbowisbeautiful";
        }

        @Override
        public String meaningOf() {
            return "무지개는 아름답다.";
        }

        @Override
        public String expression() {
            return "Rainbow is beautiful";
        }
    };

    public abstract String expression();

    public abstract String toString();

    public abstract String meaningOf();

    public static int sizeOf() {
        return Sentence.values().length;
    }

    public static Sentence valueOf(int n) {
        return Sentence.values()[n];
    }

    public static boolean contain(String voca) {
        for(int i=0; i<Sentence.sizeOf(); i++) {
            if(Sentence.valueOf(i).toString().equals(voca)) {
                return true;
            }
        }
        return false;
    }
}
