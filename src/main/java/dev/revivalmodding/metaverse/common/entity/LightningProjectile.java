package dev.revivalmodding.metaverse.common.entity;

import dev.revivalmodding.metaverse.init.MVEntities;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.MoverType;
import net.minecraft.entity.effect.LightningBoltEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.IPacket;
import net.minecraft.particles.IParticleData;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.util.math.*;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.fml.network.NetworkHooks;

public class LightningProjectile extends Entity {

    private static final double DRAG = 0.95;

    public LightningProjectile(World world) {
        this(MVEntities.LIGHTNING_PROJECTILE.get(), world);
    }

    public LightningProjectile(EntityType<?> type, World world) {
        super(type, world);
        noClip = true;
    }

    public static void shoot(World world, PlayerEntity source, int power) {
        LightningProjectile projectile = new LightningProjectile(world);
        Vec3d vec3d = projectile.getVectorForRotation(source.rotationPitch, source.rotationYaw);
        projectile.setPosition(source.getPosX(), source.getPosY() + source.getEyeHeight(), source.getPosZ());
        projectile.setMotion(power * vec3d.x, power * vec3d.y, power * vec3d.z);
        projectile.updateDirection();
        world.addEntity(projectile);
    }

    private void updateDirection() {
        Vec3d motion = this.getMotion();
        float f = MathHelper.sqrt(motion.x * motion.x + motion.z * motion.z);
        rotationYaw = (float) (MathHelper.atan2(motion.x, motion.z) * (180.0D / Math.PI));
        this.rotationPitch = (float) (MathHelper.atan2(motion.z, f) * (180D / Math.PI));
        prevRotationYaw = rotationYaw;
        prevRotationPitch = rotationPitch;
    }

    @Override
    public void remove() {
        super.remove();
    }

    @Override
    public void tick() {
        updateDirection();
        Vec3d motion = getMotion();
        setMotion(motion.x, motion.y - 0.05, motion.z);
        super.tick();
        Vec3d position = getPositionVec();
        BlockRayTraceResult traceResult = world.rayTraceBlocks(new RayTraceContext(position, position.add(motion), RayTraceContext.BlockMode.COLLIDER, RayTraceContext.FluidMode.NONE, this));
        if(traceResult != null && traceResult.getType() == RayTraceResult.Type.BLOCK) {
            if(!world.isRemote) {
                Vec3d hitVec = traceResult.getHitVec();
                ((ServerWorld) world).addLightningBolt(new LightningBoltEntity(world, hitVec.x, hitVec.y, hitVec.z, false));
                remove();
            }
        }
        if(world.isRemote) {
            for(int i = 0; i < 5; i++) {
                spawnParticleRandomly(world, ParticleTypes.SMOKE, getPosX(), getPosY(), getPosZ(), 5);
                spawnParticleRandomly(world, ParticleTypes.FLAME, getPosX(), getPosY(), getPosZ(), 20);
            }
        }
        move(MoverType.SELF, getMotion());
    }

    @Override
    public boolean isInRangeToRenderDist(double distance) {
        return false;
    }

    private void spawnParticleRandomly(World world, IParticleData data, double x, double y, double z, int modifier) {
        double dx = (rand.nextDouble() - rand.nextDouble()) / modifier;
        double dy = (rand.nextDouble() - rand.nextDouble()) / modifier;
        double dz = (rand.nextDouble() - rand.nextDouble()) / modifier;
        world.addParticle(data, true, x, y, z, dx, dy, dz);
    }

    @Override
    public IPacket<?> createSpawnPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    protected void registerData() {

    }

    @Override
    protected void writeAdditional(CompoundNBT compound) {
    }

    @Override
    protected void readAdditional(CompoundNBT compound) {
    }
}
