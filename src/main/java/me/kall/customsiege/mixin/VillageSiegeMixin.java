package me.kall.customsiege.mixin;

import me.kall.customsiege.CustomSiege;
import me.kall.customsiege.config.SiegeConfig;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.DifficultyInstance;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.ai.village.VillageSiege;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.Constant;
import org.spongepowered.asm.mixin.injection.ModifyConstant;

import javax.annotation.Nullable;

@Mixin(value = VillageSiege.class, priority = 2000)
public abstract class VillageSiegeMixin {
    @Shadow @Nullable protected abstract Vec3 findRandomSpawnPos(ServerLevel level, BlockPos pos);
    @Shadow private int spawnX,spawnY, spawnZ;

    @Unique private DifficultyInstance difficulty_cache = null;

    @ModifyConstant(method = "tryToSetupSiege", constant = @Constant(intValue = 20))
    private int setup(int constant) {
        return SiegeConfig.MAX_SPAWNABLE_ENTITIES;
    }

    /**
     * @author Kall
     * @reason Weight-based entities spawning.
     */
    @Overwrite
    private void trySpawn(ServerLevel level) {
        Vec3 vec3 = this.findRandomSpawnPos(level, new BlockPos(this.spawnX, this.spawnY, this.spawnZ));
        if (vec3 == null) return;

        ResourceLocation chosenId = ResourceLocation.parse(SiegeConfig.SPAWNABLE_ENTITIES.shuffle().stream().findFirst().orElse(""));

        EntityType<?> type = BuiltInRegistries.ENTITY_TYPE.get(chosenId);

        Entity entity = type.create(level);
        if (entity instanceof Mob mob) {

            if (difficulty_cache == null) difficulty_cache = level.getCurrentDifficultyAt(mob.blockPosition());

            mob.finalizeSpawn(level, difficulty_cache, MobSpawnType.EVENT, null);
            mob.moveTo(vec3.x, vec3.y, vec3.z, level.random.nextFloat() * 360.0F, 0.0F);
            level.addFreshEntityWithPassengers(mob);
        } else {
            CustomSiege.LOGGER.error("Entity {} is not a Mob, skipping spawn", chosenId);
        }
    }
}
