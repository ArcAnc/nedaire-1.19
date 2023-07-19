/**
 * @author ArcAnc
 * Created at: 06.05.2023
 * Copyright (c) 2023
 * <p>
 * This code is licensed under "Ancient's License of Common Sense"
 * Details can be found in the license file in the root folder of this project
 */

package com.arcanc.nedaire.util.helpers;

import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class MathHelper
{

    public static @NotNull Vec3 lerp (Vec3 a, Vec3 b, double scale)
    {
        return new Vec3(checkedLerp(a.x(), b.x(), scale), checkedLerp(a.y(), b.y(), scale), checkedLerp(a.z(), b.z(), scale));
    }

    public static double checkedLerp(double a, double b, double scale)
    {
        return a > b ? lerp(b, a, scale) : lerp(a, b, scale);
    }

    public static float checkedLerp(float a, float b, float scale)
    {
        return a > b ? lerp(b, a, scale): lerp(a, b, scale);
    }

    public static double lerp(double start, double finish, double scale)
    {
        return start + scale * (finish-start);
    }

    public static float lerp(float start, float finish, float scale)
    {
     return start + scale * (finish-start);
    }
}
