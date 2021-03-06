# Spellweaver #

This is my 2021 7DRL entry. This is a classic roguelike with ASCII graphics.

# Libraries #

This uses VTerminal, a Swing ASCII console frontend, and SquidLib, which
provides various utilities suited for roguelikes.

# Game Premise #

You are an apprentice sorcerer sent into a musty dungeon to retrieve your
master's spellbook. You will encounter various monsters along the way,
using combinations of spells to defeat them.

# DevLog #

3.6 - Started with some scaffolding to ensure VTerminal would work how
I wanted. Implemented the scaffolding for `GameMap` and `MapBuilder` classes,
which encapsulate the state of the game map and build it, respectively.
I come to my first major design decision - while it is easier to ask the
maps to hold the state of Entities (which is what I have now), I think I would
rather store them in the `GameState` class for easier serialization. I'm
going to sleep on that decision for now. Also, I need to try to build a
JAR now so I can release dev builds to my testers who have graciously
volunteered to test for me.