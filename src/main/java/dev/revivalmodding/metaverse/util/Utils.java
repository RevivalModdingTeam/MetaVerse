package dev.revivalmodding.metaverse.util;

import net.minecraft.entity.Entity;
import net.minecraft.util.math.Vec3d;

import java.util.Collection;
import java.util.function.BiPredicate;

public class Utils {

    public static double getMovementSpeed(Entity entity) {
        Vec3d motion = entity.getMotion();
        return Math.sqrt(sqr(motion.x) + sqr(motion.y) + sqr(motion.z));
    }

    public static double sqr(double n) {
        return n * n;
    }

    public static void clear(Object[] array) {
        for(int i = 0; i < array.length; i++) {
            array[i] = null;
        }
    }

    public static void swapElements(int e1, int e2, Object[] array) {
        Object tmp = array[e1];
        array[e1] = array[e2];
        array[e2] = tmp;
    }

    public static int firstNull(Object[] objects) {
        for(int i = 0; i < objects.length; i++) {
            if(objects[i] == null) {
                return i;
            }
        }
        return -1;
    }

    public static <T, U> boolean contains(T object, U[] group, BiPredicate<T, U> comparator) {
        for(U u : group) {
            if(comparator.test(object, u)) {
                return true;
            }
        }
        return false;
    }

    public static <T, U> boolean contains(T object, Collection<U> group, BiPredicate<T, U> comparator) {
        for(U u : group) {
            if(comparator.test(object, u)) {
                return true;
            }
        }
        return false;
    }
}
