package com.wjthinkbig.a10010952.codinggame;

import com.wjthinkbig.a10010952.R;

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

        @Override
        public int getDrawable() {
            return R.drawable.codinggame_up;
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

        @Override
        public int getDrawable() {
            return R.drawable.codinggame_down;
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

        @Override
        public int getDrawable() {
            return R.drawable.codinggame_left;
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

        @Override
        public int getDrawable() {
            return R.drawable.codinggame_right;
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

        @Override
        public int getDrawable() {
            return R.drawable.repeat;
        }
    },
    get {
        @Override
        public int getRow() {
            return 0;
        }

        @Override
        public int getCol() {
            return 0;
        }

        @Override
        public int getDrawable() {
            return R.drawable.get;
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

        @Override
        public int getDrawable() {
            return 0;
        }
    };

    public abstract int getDrawable();

    public abstract int getRow();

    public abstract int getCol();

    public static Arrow valueOf(int n) {
        return Arrow.values()[n];
    }
}
