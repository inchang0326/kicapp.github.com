package com.example.me.myapplication;

public enum Arrow {
    up {
        @Override
        public int getRow() {
            return -1;
        }

        @Override
        public int getCol() {
            return 0;
        }
    },
    down {
        @Override
        public int getRow() {
            return 1;
        }

        @Override
        public int getCol() {
            return 0;
        }
    },
    left {
        @Override
        public int getRow() {
            return 0;
        }

        @Override
        public int getCol() {
            return -1;
        }
    },
    right {
        @Override
        public int getRow() {
            return 0;
        }

        @Override
        public int getCol() {
            return 1;
        }
    },
    repeat {
        @Override
        public int getRow() {
            return 0;
        }

        @Override
        public int getCol() {
            return 0;
        }
    },
    dummy {
        @Override
        public int getRow() {
            return 0;
        }

        @Override
        public int getCol() {
            return 0;
        }
    };

    public abstract int getRow();

    public abstract int getCol();

    public static Arrow valueOf(int n) {
        return Arrow.values()[n];
    }
}
