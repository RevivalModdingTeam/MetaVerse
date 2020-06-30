package dev.revivalmodding.metaverse.ability;

public interface LevelableAbility {

    int getMaxLevel();

    int getCurrentLevel();

    void setLevel(int level);
}
