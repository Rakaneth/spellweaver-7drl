package com.rakaneth.view;

import com.rakaneth.GameConfig;
import com.rakaneth.Swatch;
import com.rakaneth.engine.DamageTypes;
import com.rakaneth.engine.GameState;
import com.rakaneth.engine.action.*;
import com.rakaneth.engine.effect.Effect;
import com.rakaneth.entity.Entity;
import com.valkryst.VTerminal.component.VPanel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import squidpony.StringKit;
import squidpony.squidai.AOE;
import squidpony.squidgrid.Direction;
import squidpony.squidmath.Coord;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.util.Comparator;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.rakaneth.GameConfig.*;
import static com.rakaneth.Swatch.*;
import static com.rakaneth.view.UIUtils.displayBoundedStat;

public class PlayView extends GameView {
    private UIUtils.Console msgs;
    private UIUtils.Console skills;
    private UIUtils.Console info;
    private UIUtils.Console stats;
    private Coord target;
    private DrawMode curMode = DrawMode.NORMAL;

    public PlayView(GameState gameState) {
        super(gameState);
    }

    private final Logger logger = LoggerFactory.getLogger(this.getClass());
    private final Map<Character, Color> colorTiles = Map.of( //TODO: Add doors & stairs
            '#', WALL_BG,
            '.', FLOOR_BG,
            '~', DEEP_BG,
            ',', SHALLOW_BG,
            '+', DOOR_BG,
            '/', DOOR_BG);

    @Override
    void render(VPanel panel) {
        renderMap(panel);
        renderMessages(panel);
        renderAbilities(panel);
        renderStats(panel);
        renderInfo(panel);
        if (curMode == DrawMode.TARGETING) {
            final var pos = gameState.player.getPos();
            if (target == null) target = pos;
            final var spell = gameState.player.getSpell();
            spell.setTarget(target);
            spell.setOrigin(pos);
            final var aoe = gameState.player.getSpell().cast().aoe;
            renderTargeting(panel, aoe);
        }
        renderEntities(panel);
    }

    @Override
    boolean handle(KeyEvent key) {
        //TODO: handle keypresses
        final var player = gameState.player;
        final var moveUp =  player.getPos().translate(Direction.UP);
        final var moveDown = player.getPos().translate(Direction.DOWN);
        final var moveLeft = player.getPos().translate(Direction.LEFT);
        final var moveRight = player.getPos().translate(Direction.RIGHT);
        final var code = key.getKeyCode();
        boolean update = true;
        int cost = 0;
        if (curMode == DrawMode.TARGETING) {
            final var moveTargetUp = target.translate(Direction.UP);
            final var moveTargetDown = target.translate(Direction.DOWN);
            final var moveTargetLeft = target.translate(Direction.LEFT);
            final var moveTargetRight = target.translate(Direction.RIGHT);
            target = switch(code) {
                case KeyEvent.VK_W -> {
                    update = false;
                    yield moveTargetUp;
                }
                case KeyEvent.VK_S -> {
                    update = false;
                    yield moveTargetDown;
                }
                case KeyEvent.VK_A -> {
                    update = false;
                    yield moveTargetLeft;
                }
                case KeyEvent.VK_D -> {
                    update = false;
                    yield moveTargetRight;
                }
                case KeyEvent.VK_ENTER -> {
                    curMode = DrawMode.NORMAL;
                    final var action = new FinishSpellAction(player, player.getSpell().getActionCost());
                    cost = action.doAction(gameState) + 100;
                    yield target;
                }
                default -> {
                    update = false;
                    gameState.addMessage("(Press ENTER to choose your target, or a movement key to move the cursor.)");
                    yield target;
                }
            };
        } else {
            final GameAction action = switch (code) {
                case KeyEvent.VK_W -> new MoveAction(player, moveUp);
                case KeyEvent.VK_S -> new MoveAction(player, moveDown);
                case KeyEvent.VK_A -> new MoveAction(player, moveLeft);
                case KeyEvent.VK_D -> new MoveAction(player, moveRight);
                case KeyEvent.VK_1 -> new CastAction(player, player.getKnownElement(0));
                case KeyEvent.VK_2 -> new CastAction(player, player.getKnownElement(1));
                case KeyEvent.VK_3 -> new CastAction(player, player.getKnownElement(2));
                case KeyEvent.VK_4 -> new CastAction(player, player.getKnownElement(3));
                case KeyEvent.VK_5 -> {
                    if (player.getKnownElements().size() >= 5)
                        yield new CastAction(player, player.getKnownElement(4));
                    else {
                        update = false;
                        yield new NoAction(player);
                    }
                }
                case KeyEvent.VK_6 -> {
                    if (player.getKnownElements().size() >= 6)
                        yield new CastAction(player, player.getKnownElement(5));
                    else {
                        update = false;
                        yield new NoAction(player);
                    }
                }
                case KeyEvent.VK_7 -> {
                    if (player.getKnownElements().size() >= 7)
                        yield new CastAction(player, player.getKnownElement(6));
                    else {
                        update = false;
                        yield new NoAction(player);
                    }
                }
                case KeyEvent.VK_ENTER -> {
                    curMode = DrawMode.TARGETING;
                    target = player.getPos();
                    update = false;
                    yield new NoAction(player);
                }
                case KeyEvent.VK_PERIOD, KeyEvent.VK_COMMA -> {
                    if (key.isShiftDown()) {
                        if (gameState.getCurMap().isStairs(player.getPos())) {
                            gameState.addMessage(player.name + " takes the stairs.");
                            yield new TakeStairsAction(player);
                        } else {
                            gameState.addMessage("No stairs here.");
                            update = false;
                            yield new NoAction(player);
                        }
                    }
                    yield new NoAction(player);
                }
                default -> {
                    logger.info("Unhandled key: {} ({})", key.getKeyChar(), key.getKeyCode());
                    update = false;
                    yield new NoAction(player);
                }
            };
            cost = action.doAction(gameState) + 100;
        }

        if (update) {
            player.changeNrg(-cost);
            logger.info(
                    "Turn: {} Actor {} acted, spending {}, has {} energy",
                    gameState.getGameTurn(),
                    player.name,
                    cost,
                    player.getNrg());
            player.updateFOV(gameState.getCurMap(), player.getPos());
        }

        return update;
    }

    private void drawCharAdjusted(VPanel panel, Coord mapCoord, char c, Color fg, Color bg) {
        if (inView(mapCoord)) {
            final Coord screenPoint = gameState.getCurMap()
                    .mapToScreen(mapCoord, gameState.player.getPos(), MAP_SCREEN);
            final int x = screenPoint.x;
            final int y = screenPoint.y;
            panel.setCodePointAt(x, y, c);
            panel.setForegroundAt(x, y, fg);
            panel.setBackgroundAt(x, y, bg);
        }
    }

    private boolean inView(Coord c) {
        final var screenPos = gameState.getCurMap()
                .mapToScreen(
                        c,
                        gameState.player.getPos(),
                        MAP_SCREEN);
        return screenPos.x >= 0
                && screenPos.y >= 0
                && screenPos.x < GameConfig.MAP_W
                && screenPos.y < MAP_H;
    }

    private Coord centerPos() {
        return gameState.player.getPos();
    }

    //DONE: render functions
    private void renderMap(VPanel panel) {
        final var centerPoint = centerPos();
        final var curMap = gameState.getCurMap();
        final var camPoint = curMap.cam(centerPos(), MAP_SCREEN);
        for (int y = camPoint.y; y < MAP_H + camPoint.y; y++) {
            for (int x = camPoint.x; x < GameConfig.MAP_W + camPoint.x; x++) {
                var t = curMap.getTile(x, y);
                var curPoint = Coord.get(x, y);
                t.ifPresent(tile -> {
                    if (inView(curPoint)) {
                        final var screenPoint = curMap.mapToScreen(curPoint, centerPoint, MAP_SCREEN);
                        Color bg = Color.BLACK;
                        Color fg = Color.WHITE;
                        if (curMap.isLit() || gameState.player.isVisible(curPoint.x, curPoint.y)) {
                            bg = colorTiles.getOrDefault(tile, Color.BLACK);
                        } else if (curMap.isExplored(curPoint)) {
                            bg = EXPLORED_BG;
                            fg = Color.BLACK;
                        } else {
                            return;
                        }
                        panel.setCodePointAt(screenPoint.x, screenPoint.y, tile);
                        panel.setForegroundAt(screenPoint.x, screenPoint.y, fg);
                        panel.setBackgroundAt(screenPoint.x, screenPoint.y, bg);
                    }
                });
            }
        }
    }

    private void renderMessages(VPanel panel) {
        if (msgs == null) {
            msgs = new UIUtils.Console(0, MAP_H, MSG_W, MSG_H, "Messages", panel);
        }

        int i = 1;
        java.util.List<String> wrapt;
        String nextMsg;
        int wsz;
        final var iterator = gameState.messages.listIterator(gameState.messages.size());
        while (iterator.hasPrevious()) {
            nextMsg = iterator.previous();
            wrapt = StringKit.wrap(nextMsg, msgs.width-2);
            wsz = wrapt.size();
            if (i + wsz > 9) break;
            for (int w=0; w<wsz; w++) {
                msgs.writeString(1, i + w, wrapt.get(w));
            }
            i += wsz;
        }
        msgs.border();
        //DONE: rest of messages
    }

    private void renderAbilities(VPanel panel) {
        if (skills == null) {
            skills = new UIUtils.Console(MSG_W, MAP_H, SKIL_W, SKIL_H, "Magic", panel);
        }

        final var player = gameState.player;

        if (curMode == DrawMode.TARGETING) {
            skills.writeString(1, 1, "Targeting your spell");
        } else {
            for (int i=0; i<player.getKnownElements().size(); i++) {
                String toWrite = String.format("%d) %s", i+1, player.getKnownElement(i).toString());
                skills.writeString(1, i+1, toWrite);
            }
        }

        skills.border();
        //DONE: rest of skills
    }

    private void renderStats(VPanel panel) {
        final var player = gameState.player;
        if (stats == null) {
            stats = new UIUtils.Console(MSG_W + SKIL_W, 0, STAT_W, STAT_H, "Stats", panel);
        }

        stats.writeString(1, 1, player.name);
        stats.writeString(1, 2, player.desc);
        stats.writeString(1, 3, gameState.getCurMap().getName() + " " + player.getPos());
        stats.writeString(1, 4, "HP: " + displayBoundedStat(player.getHp(), player.getMaxHp()));
        stats.writeString (1, 5, "Power: " + displayBoundedStat(player.getPower(), player.getMaxPower()));
        stats.writeString(1, 7, "Atk: " + player.getAtk());
        stats.writeString(1, 8, "Dfp: " + player.getDfp());
        stats.writeString(1, 9, "Spd: " + player.getSpd());
        stats.writeString(1, 10, "Will: " + player.getWil());


        stats.border();
        //DONE: rest of Stats
    }

    private void renderEntities(VPanel panel) {
        final var m = gameState.getCurMap();
        final var centerPoint = centerPos();
        final var toDraw = gameState.getCurrentEntities().stream()
                .map(Entity::getPos)
                .map(c -> gameState.getEntitiesAt(c))
                .map(l -> l.stream().max(Comparator.comparing(e -> e.zLayer)))
                .filter(Optional::isPresent)
                .map(Optional::get)
                .collect(Collectors.toList());

        for (Entity e : toDraw) {
            if (inView(e.getPos()) && (m.isLit() || gameState.player.canSee(e))) {
                final Color color = e.color == Color.BLACK ? FLOOR_BG : e.color;
                drawCharAdjusted(panel, e.getPos(), e.glyph, Color.WHITE, color);
            }
        }
    }

    private void renderInfo(VPanel panel) {
        if (info == null) {
            info = new UIUtils.Console(MSG_W + SKIL_W, MAP_H, INFO_W, INFO_H, "Effects", panel);
        }

        int i = 1;
        for (Effect e : gameState.player.getEffects()) {
            info.writeString(1, i++, e.toString());
        }

        info.border();
    }

    private void renderTargeting(VPanel panel, AOE aoe) {
        final var toDraw = aoe.findArea();

        toDraw.keySet().forEach(c -> {
            if (gameState.player.isVisible(c.x, c.y)) {
                drawCharAdjusted(panel, c, 'X', Color.CYAN, Color.BLACK);
            }
        });
        drawCharAdjusted(panel, target, 'X', Color.YELLOW, Color.BLACK);
    }

    enum DrawMode {NORMAL, TARGETING}
}
