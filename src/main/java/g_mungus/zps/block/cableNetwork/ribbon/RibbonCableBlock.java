package g_mungus.zps.block.cableNetwork.ribbon;

import g_mungus.zps.block.cableNetwork.core.BuiltinCableStandards;
import g_mungus.zps.block.cableNetwork.core.CableComponentBlock;
import g_mungus.zps.block.cableNetwork.core.Channels;
import g_mungus.zps.block.cableNetwork.core.NetworkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.*;

public class RibbonCableBlock extends CableComponentBlock {

    public static final BooleanProperty CONNECTED_A = BooleanProperty.create("connected_a");
    public static final BooleanProperty CONNECTED_B = BooleanProperty.create("connected_b");

    public static final EnumProperty<RibbonCableOrientation> ORIENTATION = EnumProperty.create("orientation", RibbonCableOrientation.class);


    public RibbonCableBlock(Properties arg) {
        super(arg);
        this.registerDefaultState(this.stateDefinition.any()
                .setValue(ORIENTATION, RibbonCableOrientation.NORTH_SOUTH)
                .setValue(CONNECTED_A, false)
                .setValue(CONNECTED_B, false)
        );
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(CONNECTED_A, CONNECTED_B, ORIENTATION);
    }

    @Override
    public String getCableStandard() {
        return BuiltinCableStandards.RIBBON;
    }

    @Override
    public boolean isTerminal() {
        return false;
    }

    @Override
    public int getTotalChannelCount() {
        return 1;
    }

    @Override
    public int getNewChannel(BlockPos self, NetworkNode input, Level level) {
        return Channels.MAIN;
    }

    @Override
    public void updateConnections(BlockState state, Level level, BlockPos pos) {
        BlockState newState = getNewBlockState(state, level, pos);

        if (!state.equals(newState)) {
            level.setBlock(pos, newState, 3);
            updateNetwork(pos, level);
        }
    }

    private BlockState getNewBlockState(BlockState state, Level level, BlockPos pos) {
        RibbonCableOrientation current = state.getValue(ORIENTATION);

        // Collect connectable neighbors
        List<Direction> available = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            if (canConnect(pos, pos.relative(dir), level)) available.add(dir);
        }

        // Keep existing connections if still valid, then fill from available
        List<Direction> selected = new ArrayList<>();
        if (available.contains(current.direction_a)) selected.add(current.direction_a);
        if (available.contains(current.direction_b)) selected.add(current.direction_b);
        for (Direction dir : available) {
            if (!selected.contains(dir) && selected.size() < 2) selected.add(dir);
        }

        // Find new orientation if connections changed, preserving twist for kept directions
        RibbonCableOrientation newOrientation = current;
        boolean needsNewOrientation = selected.stream().anyMatch(d -> d != current.direction_a && d != current.direction_b);
        if (needsNewOrientation) {
            List<Direction> preserved = selected.stream()
                    .filter(d -> d == current.direction_a || d == current.direction_b)
                    .toList();
            for (RibbonCableOrientation o : RibbonCableOrientation.values()) {
                if (selected.stream().allMatch(d -> d == o.direction_a || d == o.direction_b)
                        && preserved.stream().allMatch(d -> current.getTwistState(d.getAxis()).match(o.getTwistState(d.getAxis())))) {
                    newOrientation = o;
                    break;
                }
            }
        }

        return state
                .setValue(ORIENTATION, newOrientation)
                .setValue(CONNECTED_A, available.contains(newOrientation.direction_a))
                .setValue(CONNECTED_B, available.contains(newOrientation.direction_b));
    }

    @Override
    public int getChannelCountForConnection(BlockPos self, BlockPos from, Level level) {
        BlockState fromState = level.getBlockState(from);
        if (!(fromState.getBlock() instanceof RibbonCableBlock)) return 1;

        Direction connecting = Direction.fromDelta(
                from.getX() - self.getX(),
                from.getY() - self.getY(),
                from.getZ() - self.getZ()
        );
        if (connecting == null) return 1;
        Direction.Axis axis = connecting.getAxis();

        RibbonCableOrientation selfOrientation = level.getBlockState(self).getValue(ORIENTATION);
        RibbonCableOrientation fromOrientation = fromState.getValue(ORIENTATION);

        return selfOrientation.getTwistState(axis).match(fromOrientation.getTwistState(axis)) ? 1 : 0;
    }

    @Override
    public List<BlockPos> getConnectingNeighbors(NetworkNode self, Level level) {
        BlockPos origin = self.pos();
        return new ArrayList<>(List.of(
                origin.above(),
                origin.below(),
                origin.north(),
                origin.east(),
                origin.south(),
                origin.west()
        ));
    }

}