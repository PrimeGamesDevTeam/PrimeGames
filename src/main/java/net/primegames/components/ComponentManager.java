package net.primegames.components;

import lombok.Getter;
import net.primegames.components.vote.VoteComponent;

import java.util.HashMap;
import java.util.Map;

public class ComponentManager {
    public static ComponentManager instance;
    @Getter
    private final Map<String, Component> components = new HashMap<>();

    public static ComponentManager getInstance() {
        if (instance == null)
            instance = new ComponentManager();
        return instance;
    }

    public void register(VoteComponent component) {
        components.put(component.getIdentifier(), component);
    }

}
