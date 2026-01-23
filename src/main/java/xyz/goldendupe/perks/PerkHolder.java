package xyz.goldendupe.perks;

import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import xyz.goldendupe.perks.action.Action;
import xyz.goldendupe.perks.action.ActionType;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Getter
public class PerkHolder {
    private final UUID uniqueId;
    private final Map<Action, Integer> tiers = new HashMap<>();
    private final Map<ActionType, Map<Action, Integer>> slotByAction = new HashMap<>();
    private final Map<ActionType, Map<Integer, Action>> actionBySlot = new HashMap<>();

    public PerkHolder(UUID uniqueId) {
        this.uniqueId = uniqueId;
    }

    public void setPerkTier(Action action, int tier) {
        this.tiers.put(action, tier);
    }

    public void reset(Action action) {
        this.tiers.remove(action);
    }

    public int getTier(@NotNull Action action) {
        return tiers.get(action) != null ? tiers.get(action) : 0;
    }

    public void setPerkSlot(@NotNull Action action, int slot) {
        ActionType type = action.getActionManager().getType(action);

        slotByAction.putIfAbsent(type, new HashMap<>());
        actionBySlot.putIfAbsent(type, new HashMap<>());

        Map<Action, Integer> actionSlots = slotByAction.get(type);
        Map<Integer, Action> slotActions = actionBySlot.get(type);

        // Remove action from old slot
        Integer oldSlot = actionSlots.remove(action);
        if (oldSlot != null) {
            slotActions.remove(oldSlot);
        }

        // Remove previous actions
        Action previousAction = slotActions.remove(slot);
        if (previousAction != null) {
            actionSlots.remove(previousAction);
        }

        actionSlots.put(action, slot);
        slotActions.put(slot, action);
    }

    public int getPerkSlot(@NotNull Action action) {
        ActionType type = action.getActionManager().getType(action);
        Map<Action, Integer> map = slotByAction.get(type);
        return map != null ? map.getOrDefault(action, -1) : -1;
    }

    public Action getActionFromSlot(@NotNull ActionType action, int slot) {
        Map<Integer, Action> map = actionBySlot.get(action);
        return map != null ? map.get(slot) :  null;
    }
}
