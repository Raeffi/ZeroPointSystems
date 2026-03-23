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
        RibbonCableOrientation newOrientation = current;

        List<Direction> selected = new ArrayList<>();

        List<Direction> available = new ArrayList<>();
        for (Direction dir : Direction.values()) {
            if (canConnect(pos, pos.relative(dir), level)) {
                available.add(dir);
            }
        }

        // Keep existing connections if still valid
        if (available.contains(current.direction_a)) {
            selected.add(current.direction_a);
        }
        if (available.contains(current.direction_b)) {
            selected.add(current.direction_b);
        }

        List<Direction> preservedDirections = new ArrayList<>(selected);

        // Add new connections if there is space
        for (Direction dir : available) {
            if (!selected.contains(dir) && selected.size() < 2) {
                selected.add(dir);
            }
        }

        // Needs new orientation if we have a connection in a new direction
        boolean needNewOrientation = selected.stream().anyMatch(it ->
                it != current.direction_a && it != current.direction_b
        );

        // Find new orientation, preserving existing twisted-ness
        if (needNewOrientation) {
            Map<Direction.Axis, RibbonCableOrientation.TwistState> twistRequirements = new HashMap<>();

            for (var axis: Direction.Axis.values()) {
                twistRequirements.put(axis, RibbonCableOrientation.TwistState.EITHER);
            }
            for (var dir : preservedDirections) {
                Direction.Axis axis = dir.getAxis();
                twistRequirements.put(axis, current.getTwistState(axis));
            }

            for (var orientation : RibbonCableOrientation.values()) {
                if (new HashSet<>(List.of(orientation.direction_a, orientation.direction_b)).containsAll(selected)
                        && twistRequirements.entrySet().stream().allMatch(entry ->
                            entry.getValue().match(orientation.getTwistState(entry.getKey())))
                ) {
                    newOrientation = orientation;
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
        //TODO
        return 0;
    }

    @Override
    public List<BlockPos> getConnectingNeighbors(NetworkNode self, Level level) {
        //TODO
        return List.of();
    }

}