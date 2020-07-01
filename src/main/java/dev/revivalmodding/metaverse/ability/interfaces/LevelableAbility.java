package dev.revivalmodding.metaverse.ability.interfaces;

public interface LevelableAbility {

    int getMaxLevel();

    int getCurrentLevel();

    void setLevel(int level);
}
