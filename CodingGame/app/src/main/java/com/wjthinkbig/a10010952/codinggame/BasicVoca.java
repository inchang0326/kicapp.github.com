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
    green {
        @Override
        public String toString() {
            return "green";
        }

        @Override
        public String meaningOf() {
            return "초록색";
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
    yellow {
        @Override
        public String toString() {
            return "yellow";
        }

        @Override
        public String meaningOf() {
            return "노란색";
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
    apple {
        @Override
        public String toString() {
            return "apple";
        }

        @Override
        public String meaningOf() {
            return "사과";
        }
    },
    kiwi {
        @Override
        public String toString() {
            return "kiwi";
        }

        @Override
        public String meaningOf() {
            return "키위";
        }
    },
    cherry {
        @Override
        public String toString() {
            return "cherry";
        }

        @Override
        public String meaningOf() {
            return "체리";
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
    tree {
        @Override
        public String toString() {
            return "tree";
        }

        @Override
        public String meaningOf() {
            return "나무";
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
