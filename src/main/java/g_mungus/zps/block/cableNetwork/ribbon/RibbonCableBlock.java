package g_mungus.zps.block.cableNetwork.ribbon;

import g_mungus.zps.block.cableNetwork.core.BuiltinCableStandards;
import g_mungus.zps.block.cableNetwork.core.CableComponentBlock;
import g_mungus.zps.block.cableNetwork.core.Channels;
import g_mungus.zps.block.cableNetwork.core.NetworkNode;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.EnumProperty;

import java.util.List;

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
        //TODO
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