package g_mungus.zps.block.cableNetwork.ribbon;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.core.Direction.*;

public enum RibbonCableOrientation implements StringRepresentable {

    NORTH_SOUTH(NORTH, SOUTH, false),
    NORTH_SOUTH_ALT(NORTH, SOUTH, true),
    EAST_WEST(EAST, WEST, false),
    EAST_WEST_ALT(EAST, WEST, true),
    UP_DOWN(UP, DOWN, false),
    UP_DOWN_ALT(UP, DOWN, true),

    UP_NORTH(UP, NORTH, false),
    UP_NORTH_ALT(UP, NORTH, true),
    UP_EAST(UP, EAST, false),
    UP_EAST_ALT(UP, EAST, true),
    UP_SOUTH(UP, SOUTH, false),
    UP_SOUTH_ALT(UP, SOUTH, true),
    UP_WEST(UP, WEST, false),
    UP_WEST_ALT(UP, WEST, true),

    DOWN_NORTH(DOWN, NORTH, false),
    DOWN_NORTH_ALT(DOWN, NORTH, true),
    DOWN_EAST(DOWN, EAST, false),
    DOWN_EAST_ALT(DOWN, EAST, true),
    DOWN_SOUTH(DOWN, SOUTH, false),
    DOWN_SOUTH_ALT(DOWN, SOUTH, true),
    DOWN_WEST(DOWN, WEST, false),
    DOWN_WEST_ALT(DOWN, WEST, true),

    NORTH_EAST(NORTH, EAST, false),
    NORTH_EAST_ALT(NORTH, EAST, true),
    EAST_SOUTH(EAST, SOUTH, false),
    EAST_SOUTH_ALT(EAST, SOUTH, true),
    SOUTH_WEST(SOUTH, WEST, false),
    SOUTH_WEST_ALT(SOUTH, WEST, true),
    WEST_NORTH(WEST, NORTH, false),
    WEST_NORTH_ALT(WEST, NORTH, true)
;

    public final Direction direction_a;
    public final Direction direction_b;
    public final boolean twisted_a;
    public final boolean twisted_b;

    RibbonCableOrientation(Direction directionA, Direction directionB, boolean alt) {
        direction_a = directionA;
        direction_b = directionB;

        if (directionA.getNormal().getY() != 0 && directionB.getNormal().getY() == 0) {
            boolean out = alt;
            if (directionB.getAxis() == Axis.Z) {
                out = !out;
            }
            twisted_a = out;
        } else {
            twisted_a = alt;
        }

        if (directionB.getNormal().getY() != 0 && directionA.getNormal().getY() == 0) {
            boolean out = alt;
            if (directionA.getAxis() == Axis.Z) {
                out = !out;
            }
            twisted_b = out;
        } else {
            twisted_b = alt;
        }
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name().toLowerCase();
    }

    public TwistState getTwistState(Axis axis) {
        boolean aOnAxis = direction_a.getAxis() == axis;
        boolean bOnAxis = direction_b.getAxis() == axis;
        if (aOnAxis) return twisted_a ? TwistState.TWISTED : TwistState.NORMAL;
        if (bOnAxis) return twisted_b ? TwistState.TWISTED : TwistState.NORMAL;
        return TwistState.EITHER;
    }

    public enum TwistState {
        NORMAL, TWISTED, EITHER;

        public boolean match(TwistState other) {
            if (this == EITHER || other == EITHER) return true;
            return this == other;
        }
    }
}
