package com.rakaneth.engine;

public enum SpellState {
    BASE {
        @Override
        SpellState update(boolean skip) {
            if (skip) {
                return DONE;
            }
            return FIRST_MOD;
        }
    },
    FIRST_MOD {
        @Override
        SpellState update(boolean skip) {
            if (skip) {
                return DONE;
            }
            return SECOND_MOD;
        }
    },
    SECOND_MOD {
        @Override
        SpellState update(boolean skip) {
            return DONE;
        }
    },
    DONE {
        @Override
        SpellState update(boolean skip) {
            return DONE;
        }
    };


    abstract SpellState update(boolean skip);
}
