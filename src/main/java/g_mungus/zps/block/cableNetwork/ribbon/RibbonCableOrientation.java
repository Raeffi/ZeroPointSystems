package g_mungus.zps.block.cableNetwork.ribbon;

import net.minecraft.core.Direction;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

import static net.minecraft.core.Direction.*;

public enum RibbonCableOrientation implements StringRepresentable {

    NORTH_SOUTH(NORTH, SOUTH, false, false, false),
    NORTH_SOUTH_ALT(NORTH, SOUTH, false, false, true),
    EAST_WEST(EAST, WEST, false, false, false),
    EAST_WEST_ALT(EAST, WEST, true, false, false),
    UP_DOWN(UP, DOWN, false, false, false),
    UP_DOWN_ALT(UP, DOWN, false, true, false),

    UP_NORTH(UP, NORTH, false, false, false),
    UP_NORTH_ALT(UP, NORTH, false, true, true),
    UP_EAST(UP, EAST, false, true, false),
    UP_EAST_ALT(UP, EAST, true, false, false),
    UP_SOUTH(UP, SOUTH, false, false, false),
    UP_SOUTH_ALT(UP, SOUTH, false, true, true),
    UP_WEST(UP, WEST, false, true, false),
    UP_WEST_ALT(UP, WEST, true, false, false),

    DOWN_NORTH(DOWN, NORTH, false, false, false),
    DOWN_NORTH_ALT(DOWN, NORTH, false, true, true),
    DOWN_EAST(DOWN, EAST, false, true, false),
    DOWN_EAST_ALT(DOWN, EAST, true, false, false),
    DOWN_SOUTH(DOWN, SOUTH, false, false, false),
    DOWN_SOUTH_ALT(DOWN, SOUTH, false, true, true),
    DOWN_WEST(DOWN, WEST, false, true, false),
    DOWN_WEST_ALT(DOWN, WEST, true, false, false),

    NORTH_EAST(NORTH, EAST, false, false, false),
    NORTH_EAST_ALT(NORTH, EAST, true, false, true),
    EAST_SOUTH(EAST, SOUTH, false, false, false),
    EAST_SOUTH_ALT(EAST, SOUTH, true, false, true),
    SOUTH_WEST(SOUTH, WEST, false, false, false),
    SOUTH_WEST_ALT(SOUTH, WEST, true, false, true),
    WEST_NORTH(WEST, NORTH, false, false, false),
    WEST_NORTH_ALT(WEST, NORTH, true, false, true)
;

    public final Direction direction_a;
    public final Direction direction_b;
    public final boolean twisted_x;
    public final boolean twisted_y;
    public final boolean twisted_z;

    RibbonCableOrientation(Direction directionA, Direction directionB, boolean twistedX, boolean twistedY, boolean twistedZ) {
        direction_a = directionA;
        direction_b = directionB;
        twisted_x = twistedX;
        twisted_y = twistedY;
        twisted_z = twistedZ;
    }

    @Override
    public @NotNull String getSerializedName() {
        return this.name().toLowerCase();
    }
}
