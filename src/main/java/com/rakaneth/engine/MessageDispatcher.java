package com.rakaneth.engine;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MessageDispatcher {
    private static MessageDispatcher instance;
    private final GameState gameState;
    private static final Logger logger = LoggerFactory.getLogger(MessageDispatcher.class);

    private MessageDispatcher(GameState state) {
        gameState = state;
    }

    public static MessageDispatcher getInstance() {
        if (instance == null) {
            logger.error("Must create MessageDispatcher instance with MessageDispatcher.create(gameState) first.");
        }
        return instance;
    }

    public static MessageDispatcher create(GameState state) {
        instance = new MessageDispatcher(state);
        return instance;
    }

    public void gameMessage(String message) {
        gameState.addMessage(message);
    }
}
