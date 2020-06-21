package com.theprogrammingturkey.happyhearts.block;

import com.theprogrammingturkey.happyhearts.HappyHeartsCore;
import com.theprogrammingturkey.happyhearts.client.ClientProxy;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.HorizontalBlock;
import net.minecraft.block.material.Material;
import net.minecraft.block.material.MaterialColor;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.BlockItemUseContext;
import net.minecraft.particles.ParticleTypes;
import net.minecraft.state.BooleanProperty;
import net.minecraft.state.DirectionProperty;
import net.minecraft.state.StateContainer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.Direction;
import net.minecraft.util.Hand;
import net.minecraft.util.Mirror;
import net.minecraft.util.Rotation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.BlockRayTraceResult;
import net.minecraft.util.math.shapes.IBooleanFunction;
import net.minecraft.util.math.shapes.ISelectionContext;
import net.minecraft.util.math.shapes.VoxelShape;
import net.minecraft.util.math.shapes.VoxelShapes;
import net.minecraft.world.IBlockReader;
import net.minecraft.world.World;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;

import java.util.ArrayList;
import java.util.List;

public class HeartBlock extends Block
{
	public static final DirectionProperty FACING = HorizontalBlock.HORIZONTAL_FACING;
	//This works. I wish I didn't have too...
	public static final BooleanProperty TOGGLE = BooleanProperty.create("toggle");

	public final VoxelShape NS_SHAPE;
	public final VoxelShape WE_SHAPE;

	public HeartBlock()
	{
		super(Properties.create(Material.EARTH, MaterialColor.PINK).hardnessAndResistance(0.5f));
		this.setRegistryName(HappyHeartsCore.MODID, "heart");
		this.setDefaultState(this.stateContainer.getBaseState().with(FACING, Direction.NORTH).with(TOGGLE, false));
		NS_SHAPE = this.generateShape(true);
		WE_SHAPE = this.generateShape(false);
	}

	private VoxelShape generateShape(boolean ns)
	{
		List<VoxelShape> shapes = new ArrayList<>();
		shapes.add(VoxelShapes.create(ns ? 0.438 : 0.375, 0.000, !ns ? 0.438 : 0.375, ns ? 0.562 : 0.625, 0.062, !ns ? 0.562 : 0.625));
		shapes.add(VoxelShapes.create(ns ? 0.375 : 0.375, 0.062, !ns ? 0.375 : 0.375, ns ? 0.625 : 0.625, 0.125, !ns ? 0.625 : 0.625));
		shapes.add(VoxelShapes.create(ns ? 0.312 : 0.375, 0.125, !ns ? 0.312 : 0.375, ns ? 0.688 : 0.625, 0.188, !ns ? 0.688 : 0.625));
		shapes.add(VoxelShapes.create(ns ? 0.250 : 0.375, 0.188, !ns ? 0.250 : 0.375, ns ? 0.750 : 0.625, 0.250, !ns ? 0.750 : 0.625));
		shapes.add(VoxelShapes.create(ns ? 0.188 : 0.375, 0.250, !ns ? 0.188 : 0.375, ns ? 0.812 : 0.625, 0.312, !ns ? 0.812 : 0.625));
		shapes.add(VoxelShapes.create(ns ? 0.125 : 0.375, 0.312, !ns ? 0.125 : 0.375, ns ? 0.875 : 0.625, 0.375, !ns ? 0.875 : 0.625));
		shapes.add(VoxelShapes.create(ns ? 0.062 : 0.375, 0.375, !ns ? 0.062 : 0.375, ns ? 0.938 : 0.625, 0.438, !ns ? 0.938 : 0.625));
		shapes.add(VoxelShapes.create(ns ? 0.000 : 0.375, 0.438, !ns ? 0.000 : 0.375, ns ? 1.000 : 0.625, 0.500, !ns ? 1.000 : 0.625));
		shapes.add(VoxelShapes.create(ns ? 0.000 : 0.375, 0.500, !ns ? 0.000 : 0.375, ns ? 1.000 : 0.625, 0.562, !ns ? 1.000 : 0.625));
		shapes.add(VoxelShapes.create(ns ? 0.062 : 0.375, 0.562, !ns ? 0.062 : 0.375, ns ? 0.500 : 0.625, 0.625, !ns ? 0.500 : 0.625));
		shapes.add(VoxelShapes.create(ns ? 0.500 : 0.375, 0.562, !ns ? 0.500 : 0.375, ns ? 0.938 : 0.625, 0.625, !ns ? 0.938 : 0.625));
		shapes.add(VoxelShapes.create(ns ? 0.562 : 0.375, 0.625, !ns ? 0.562 : 0.375, ns ? 0.875 : 0.625, 0.688, !ns ? 0.875 : 0.625));
		shapes.add(VoxelShapes.create(ns ? 0.125 : 0.375, 0.625, !ns ? 0.125 : 0.375, ns ? 0.438 : 0.625, 0.688, !ns ? 0.438 : 0.625));
		shapes.add(VoxelShapes.create(ns ? 0.188 : 0.375, 0.688, !ns ? 0.188 : 0.375, ns ? 0.375 : 0.625, 0.750, !ns ? 0.375 : 0.625));
		shapes.add(VoxelShapes.create(ns ? 0.625 : 0.375, 0.688, !ns ? 0.625 : 0.375, ns ? 0.812 : 0.625, 0.750, !ns ? 0.812 : 0.625));

		VoxelShape result = VoxelShapes.empty();
		for(VoxelShape shape : shapes)
			result = VoxelShapes.combine(result, shape, IBooleanFunction.OR);

		return result;
	}

	@Override
	public VoxelShape getShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		if(state.get(FACING) == Direction.NORTH || state.get(FACING) == Direction.SOUTH)
			return NS_SHAPE;
		return WE_SHAPE;
	}

	@Override
	public VoxelShape getCollisionShape(BlockState state, IBlockReader worldIn, BlockPos pos, ISelectionContext context)
	{
		if(state.get(FACING) == Direction.NORTH || state.get(FACING) == Direction.SOUTH)
			return NS_SHAPE;
		return WE_SHAPE;
	}

	@Override
	public boolean hasTileEntity(BlockState state)
	{
		return true;
	}

	@Override
	public TileEntity createTileEntity(BlockState state, IBlockReader world)
	{
		return new HeartTE();
	}

	@Override
	public BlockRenderLayer getRenderLayer()
	{
		return BlockRenderLayer.CUTOUT_MIPPED;
	}

	@Override
	public boolean onBlockActivated(BlockState state, World worldIn, BlockPos pos, PlayerEntity player, Hand handIn, BlockRayTraceResult hit)
	{
		TileEntity te = worldIn.getTileEntity(pos);

		if(worldIn.isRemote() && te instanceof HeartTE && player.isSneaking())
		{
			DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientProxy.openNameInputGUI((HeartTE) te));
		}
		else
		{
			worldIn.addParticle(ParticleTypes.HEART, pos.getX() + Math.random(), pos.getY() + Math.random(), pos.getZ() + Math.random(), 0, 1, 0);
		}
		return true;
	}

	@Override
	public BlockState getStateForPlacement(BlockItemUseContext context)
	{
		return this.getDefaultState().with(FACING, context.getPlacementHorizontalFacing().getOpposite());
	}

	@Override
	public boolean func_220074_n(BlockState state) {
		return true;
	}

	@Override
	public BlockState rotate(BlockState state, Rotation rot)
	{
		return state.with(FACING, rot.rotate(state.get(FACING)));
	}

	@Override
	public BlockState mirror(BlockState state, Mirror mirrorIn)
	{
		return state.rotate(mirrorIn.toRotation(state.get(FACING)));
	}

	@Override
	protected void fillStateContainer(StateContainer.Builder<Block, BlockState> builder)
	{
		builder.add(FACING).add(TOGGLE);
	}

}
