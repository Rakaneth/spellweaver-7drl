# Spellweaver #
This is my 2021 7DRL entry. This is a classic roguelike with ASCII graphics.

## Libraries ##
This uses VTerminal, a Swing ASCII console frontend, and SquidLib, which
provides various utilities suited for roguelikes.

## Game Premise ##
You are an apprentice sorcerer sent into a musty dungeon to retrieve your
master's spellbook. You will encounter various monsters along the way,
using combinations of spells to defeat them.

## How to Build ##
You will need JDK 15 or higher to build this. I personally used the OpenJDK.

```
git clone https://github.com/Rakaneth/spellweaver-7drl
cd spellweaver-7drl
gradlew clean build
```

The resultant executable JAR file will be in 
`./build/libs/spellweaver-7drl-<version number>.jar`.

Jars will also be available under this repo's Releases.

## How to Play ##
Coming Soon!

## DevLog ##

### 3.6 ###
Started with some scaffolding to ensure VTerminal would work how
I wanted. Implemented the scaffolding for `GameMap` and `MapBuilder` classes,
which encapsulate the state of the game map and build it, respectively.
I come to my first major design decision - while it is easier to ask the
maps to hold the state of Entities (which is what I have now), I think I would
rather store them in the `GameState` class for easier serialization. I'm
going to sleep on that decision for now. Also, I need to try to build a
JAR now so I can release dev builds to my testers who have graciously
volunteered to test for me.

Moved `Entity` storage to the `GameState`. Ideally, I should only need to
serialize one object when I get to that point.

Also, basic map generation and a properly scrolling map are in. It will need
some color and checks against the player moving into walls.

Furthermore, `gradlew build` creates a running jar now. Why
IntelliJ's `Build Artifacts` does not baffles me.

Added some color to map tiles and `Entity`

This might be a case of premature optimization, but I appear to be
thrashing the GC somehow. I'll keep going for now, but I will keep
an eye on this.

Also added FOV. SquidLib makes putting in a lot of the plumbing
very simple.

### 3.7 ###
Added basic saving and loading, the main game view, and file reading.
Java generics are a mess.

### 3.8 ###
Working on the game's main mechanic - spellcasting. The general idea is
for the player to use 1 to 3 different magical elements, with the first
element providing a base effect and the other two modifying the spell,
giving it properties like AOE and increased power, at the expense of
`Power`. 

### 3.9 ###
In working on the code behind this mechanic, I discovered I had a lot of 
plumbing that still needed to be done, so now `Combatant` entities
have proper stats, can receive damage, be healed, and can have
`Effects` on them - buffs, debuffs, crowd-control, damage-over-time, etc.

`Combatant`s have the following stats by default:

* `atk`: attack power, how hard it hits in melee
* `dfp`: defense power, how well it absorbs damage
* `will`: mental fortitude, the ability to resist things like `Fear` and `Stun`.
  `Will` also affects the hero's spellcasting.
* `spd`: General speed, how fast it moves
*  `hp`: Hit points, dead at 0

Added a `MessageDispatcher` class to send messages to the `GameState`
without needing to pass it around like a football. 

Also added a `Poison` effect and changed some of the plumbing on `Effects`
to make messaging regarding them easier.

### 3.10 ###
More hacking on creatures - the `MapBuilder` now can seed its own 
creatures. There is an argument for leaving that to the caller,
but I like the idea of having everything the map needs to function
in the builder.

Some new creatures got added to the creatures.yml file.

### 3.11 ###
Scheduling system is in. It isn't perfect, but here we are at day 6 and
it seems to work. 

Bump combat is finally in, as are `Weapon` and `Armor` effects.

* `Weapon` - empowers bump attacks, granting them power and an element
* `Armor` - strengthens defense and grants resistance to an element

For the 7DRL, these are the closest things to equipment I have time to 
implement. I think it's more thematic this way, anyway.

Tweaked the appearance of effects to show modifications, like the number
of poison stacks or the element an `Armor` or `Weapon` buff is keyed to.

### 3.12 ###
Cutting it very close. Spellcasting is in, but is likely to have a lot of
bugs, as it took me the whole week to figure out how to get this in.

Implemented a `DiceRoller` singleton class with access to one of the game's RNGs
(two are used, one for generating the map, the other for combat - 
the idea is to allow for seeded runs without also fixing combat luck).
This is done to avoid having to pass the whole `GameState` around if I
need randomness in a part of the engine with no direct access to the
`GameState`.

Saving broke at the last moment after implementing spells, and I don't have
time to fix it before the deadline, so I will leave it out of the 7drl 
build.