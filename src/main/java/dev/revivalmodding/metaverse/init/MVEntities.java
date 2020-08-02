package dev.revivalmodding.metaverse.init;

import dev.revivalmodding.metaverse.MetaVerse;
import dev.revivalmodding.metaverse.common.entity.LightningProjectile;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityClassification;
import net.minecraft.entity.EntityType;
import net.minecraftforge.fml.RegistryObject;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;

public class MVEntities {

    public static final DeferredRegister<EntityType<?>> TYPES = DeferredRegister.create(ForgeRegistries.ENTITIES, MetaVerse.MODID);
    public static final RegistryObject<EntityType<LightningProjectile>> LIGHTNING_PROJECTILE = register("lightning_projectile", EntityType.Builder.<LightningProjectile>create(LightningProjectile::new, EntityClassification.MISC).setTrackingRange(128).setUpdateInterval(1).setShouldReceiveVelocityUpdates(true));

    private static <E extends Entity>RegistryObject<EntityType<E>> register(String name, EntityType.Builder<E> builder) {
        return TYPES.register(name, () -> builder.build(MetaVerse.getResource(name).toString()));
    }
}
