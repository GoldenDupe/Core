package xyz.goldendupe.pets;

import bet.astral.cloudplusplus.minecraft.paper.bootstrap.InitAfterBootstrap;
import bet.astral.messenger.v2.Messenger;
import bet.astral.messenger.v2.translation.TranslationKey;
import io.github.classgraph.ClassGraph;
import io.github.classgraph.ClassInfoList;
import io.github.classgraph.ScanResult;
import org.bukkit.entity.Entity;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import xyz.goldendupe.GoldenDupe;
import xyz.goldendupe.perks.PerkHolder;
import xyz.goldendupe.perks.PerkHolderListener;
import xyz.goldendupe.perks.PerkHolderManager;
import xyz.goldendupe.perks.action.*;
import xyz.goldendupe.perks.PetOwner;
import xyz.goldendupe.perks.gui.ActionsMenu;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

public class GDActionManager implements ActionManager, InitAfterBootstrap {
    private final GoldenDupe goldenDupe;
    private final ActionsMenu actionsMenu;
    private final Map<ActionType, Set<Action>> actions = new HashMap<>();
    private final Map<Action, ActionType> actionTypes = new HashMap<>();
    private final Map<Action, Map<Integer, PerkTier>> tiers = new HashMap<>();
    private final Map<String, Action> actionByKey = new HashMap<>();
    private PerkHolderManager perkHolderManager;

    public GDActionManager(GoldenDupe goldenDupe) {
        this.goldenDupe = goldenDupe;
        this.actionsMenu = new ActionsMenu(this);
        perkHolderManager = new PerkHolderManager(this);
        for (ActionType actionType : ActionType.values()) {
            Set<Action> set = new HashSet<>();
            actions.put(actionType, set);
        }

        try (ScanResult scanResult = new ClassGraph()
                .enableAllInfo().acceptPackages("xyz.goldendupe.perks").scan()) {
            ClassInfoList classInfo = scanResult.getClassesWithAnnotation(AutoRegister.class);
            List<String> classes = classInfo.getNames();
            for (String clazzName : classes) {
                Class<?> clazz = Class.forName(clazzName);
                AutoRegister autoRegister = clazz.getAnnotation(AutoRegister.class);
                ActionType actionType = autoRegister.value();
                Constructor<?> constructor = clazz.getDeclaredConstructor(ActionManager.class);
                constructor.setAccessible(true);
                Action action = (Action) constructor.newInstance(this);

                Set<Action> set = actions.get(actionType);
                set.add(action);
                actionByKey.put(action.getName().getKey().toLowerCase(), action);
                actionTypes.put(action, actionType);

                if (autoRegister.tiers().length > 0) {
                    tiers.putIfAbsent(action, new HashMap<>());
                    for (PerkTier perkTier : autoRegister.tiers()) {
                        tiers.get(action).put(perkTier.tier(), perkTier);
                    }
                }
            }
        } catch (ClassNotFoundException | InvocationTargetException | InstantiationException | IllegalAccessException |
                 NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public double random() {
        return GoldenDupe.RANDOM.nextDouble();
    }

    @Override
    public void applyDamage(Player killer, Entity victim) {

    }

    @Override
    public void applyDeath(Player killer, Entity victim) {
        applyIfSet(killer, victim, getPlayerPerkSlot(killer, ActionType.KILL, 0));
        applyIfSet(killer, victim, getPlayerPerkSlot(killer, ActionType.KILL, 1));
        applyIfSet(killer, victim, getPlayerPerkSlot(killer, ActionType.KILL, 2));
        applyIfSet(killer, victim, getPlayerPerkSlot(killer, ActionType.DEATH, 0));
        applyIfSet(killer, victim, getPlayerPerkSlot(killer, ActionType.DEATH, 1));
        applyIfSet(killer, victim, getPlayerPerkSlot(killer, ActionType.DEATH, 2));
    }
    public void applyIfSet(Player killer, Entity victim, Action action) {
        if (action != null) {
            action.applyDeath(killer, victim);
        }
    }

    @Override
    public int getActionTier(Player player, Action action) {
        return perkHolderManager.getPerkHolder(player).getTier(action);
    }

    @Override
    public JavaPlugin getPlugin() {
        return goldenDupe;
    }

    @Override
    public PetOwner getPetOwner(UUID player) {
        return null;
    }

    @Override
    public Set<Action> getKillActions() {
        return actions.get(ActionType.KILL);
    }

    @Override
    public Set<Action> getDeathActions() {
        return actions.get(ActionType.DEATH);
    }

    @Override
    public Set<Action> getDamageActions() {
        return actions.get(ActionType.DAMAGE);
    }

    @Override
    @Deprecated
    public Set<Action> getMovementActions() {
        return actions.get(ActionType.MOVEMENT);
    }

    @Override
    public ActionsMenu getMenu() {
        return actionsMenu;
    }

    @Override
    public @NotNull Messenger getMessenger() {
        return goldenDupe.messenger();
    }

    @Override
    public @Nullable PerkTier getPerkTier(Action action, int tier) {
        return tiers.get(action).get(tier);
    }

    @Override
    public int getPlayerPerkTier(Player player, Action action) {
        return getActionTier(player, action);
    }

    @Override
    public void setPlayerPerkTier(@NotNull Player player, Action action, int i) {
        PerkHolder perkHolder = perkHolderManager.getPerkHolder(player);
        perkHolder.setPerkTier(action, i);
        perkHolderManager.save(player);
    }

    @Override
    public Action getAction(@NotNull TranslationKey id) {
        return actionByKey.get(id.getKey().toLowerCase());
    }

    @Override
    public Collection<? extends Action> getActions() {
        return actionByKey.values();
    }

    @Override
    public void loadPlayer(Player player) {
        perkHolderManager.load(player);
    }

    @Override
    public void unloadPlayer(Player player) {
        perkHolderManager.unload(player);
    }

    @Override
    public boolean isMaxTier(Action action, int currentTier) {
        Map<Integer, PerkTier> tiersByLevel = tiers.get(action);
        if (tiersByLevel == null) {
            throw new IllegalStateException("No tier for action " + action.getName().getKey());
        }

        boolean isHighest = true;
        for (PerkTier perkTier : tiersByLevel.values()) {
            if (currentTier < perkTier.tier()) {
                isHighest = false;
                break;
            }
        }
        return isHighest;
    }

    @Override
    public int getPerkSlot(Player player, Action action) {
        return perkHolderManager.getPerkHolder(player).getPerkSlot(action);
    }

    @Override
    public void setPerkSlot(Player player, Action action, int slot) {
        perkHolderManager.getPerkHolder(player).setPerkSlot(action, slot);
        perkHolderManager.save(player);
    }

    @Override
    public ActionType getType(Action action) {
        return actionTypes.get(action);
    }

    @Override
    public int getMaxTier(Action action) {
        Map<Integer, PerkTier> tiersByLevel = tiers.get(action);
        if (tiersByLevel == null) {
            throw new IllegalStateException("No tier for action " + action.getName().getKey());
        }

        int highest = 0;
        for (PerkTier perkTier : tiersByLevel.values()) {
            if (highest < perkTier.tier()) {
                highest = perkTier.tier();
            }
        }
        return highest;
    }

    @Override
    public int getPlayerUsedPerkTier(Player player, Action action) {
        return getPlayerPerkTier(player, action);
    }

    @Override
    public Action getPlayerPerkSlot(Player player, ActionType actionType, int slot) {
        return perkHolderManager.getPerkHolder(player).getActionFromSlot(actionType, slot);
    }

    @Override
    public void init() {
        this.goldenDupe.registerListener(new ActionListener(this));
        this.goldenDupe.registerListener(new PerkHolderListener(this));
        perkHolderManager.init();
    }
}
