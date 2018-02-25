package com.wjthinkbig.a10010952.codinggame;

public enum BasicVoca {
    blue {
        @Override
        public String toString() {
            return "blue";
        }

        @Override
        public String meaningOf() {
            return "파란색";
        }
    },
    red {
        @Override
        public String toString() {
            return "red";
        }

        @Override
        public String meaningOf() {
            return "빨간색";
        }
    },
    black {
        @Override
        public String toString() {
            return "black";
        }

        @Override
        public String meaningOf() {
            return "검정색";
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
    love {
        @Override
        public String toString() {
            return "love";
        }

        @Override
        public String meaningOf() {
            return "사랑";
        }

    },
    cloud {
        @Override
        public String toString() {
            return "cloud";
        }

        @Override
        public String meaningOf() {
            return "구름";
        }
    },
    sun {
        @Override
        public String toString() {
            return "sun";
        }

        @Override
        public String meaningOf() {
            return "태양";
        }
    },
    dog {
        @Override
        public String toString() {
            return "dog";
        }

        @Override
        public String meaningOf() {
            return "강아지";
        }
    },
    cat {
        @Override
        public String toString() {
            return "cat";
        }

        @Override
        public String meaningOf() {
            return "고양이";
        }
    };

    public abstract String toString();

    public abstract String meaningOf();

    public static int sizeOf() {
        return BasicVoca.values().length;
    }

    public static BasicVoca valueOf(int n) {
        return BasicVoca.values()[n];
    }

    public static boolean contain(String voca) {
        for(int i=0; i<BasicVoca.sizeOf(); i++) {
            if(BasicVoca.valueOf(i).toString().equals(voca)) {
                return true;
            }
        }
        return false;
    }
}
