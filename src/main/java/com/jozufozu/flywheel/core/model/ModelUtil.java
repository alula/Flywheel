package com.jozufozu.flywheel.core.model;

import java.util.Collection;
import java.util.function.Supplier;

import com.jozufozu.flywheel.backend.model.BufferBuilderExtension;
import com.jozufozu.flywheel.util.Pair;
import com.jozufozu.flywheel.util.transform.TransformStack;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.BufferBuilder.RenderedBuffer;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexFormat;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.ModelBlockRenderer;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.BlockAndTintGetter;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.structure.templatesystem.StructureTemplate;

public class ModelUtil {
	private static final ThreadLocal<ThreadLocalObjects> THREAD_LOCAL_OBJECTS = ThreadLocal.withInitial(ThreadLocalObjects::new);

	public static Pair<RenderedBuffer, Integer> endShadeSeparated(BufferBuilder shadedBuilder, BufferBuilder unshadedBuilder) {
		int unshadedStartVertex = ((BufferBuilderExtension) shadedBuilder).flywheel$getVertices();
		RenderedBuffer unshadedBuffer = unshadedBuilder.endOrDiscardIfEmpty();
		if (unshadedBuffer != null) {
			// FIXME: Unshaded indices
			((BufferBuilderExtension) shadedBuilder).flywheel$appendBufferUnsafe(unshadedBuffer.vertexBuffer());
		}
		RenderedBuffer buffer = shadedBuilder.end();
		return Pair.of(buffer, unshadedStartVertex);
	}

	public static Pair<RenderedBuffer, Integer> getRenderedBuffer(Bufferable bufferable) {
		ModelBlockRenderer blockRenderer = Minecraft.getInstance().getBlockRenderer().getModelRenderer();
		ThreadLocalObjects objects = THREAD_LOCAL_OBJECTS.get();

		objects.begin();

		bufferable.bufferInto(blockRenderer, objects.shadeSeparatingWrapper, objects.random);

		return objects.end();
	}

	public static Pair<RenderedBuffer, Integer> getBufferBuilder(BakedModel model, BlockState referenceState, PoseStack poseStack) {
		return new BakedModelBuilder(model).withReferenceState(referenceState)
				.withPoseStack(poseStack)
				.build();
	}

	public static Pair<RenderedBuffer, Integer> getBufferBuilder(BlockAndTintGetter renderWorld, BakedModel model, BlockState referenceState, PoseStack poseStack) {
		return new BakedModelBuilder(model).withReferenceState(referenceState)
				.withPoseStack(poseStack)
				.withRenderWorld(renderWorld)
				.build();
	}

	public static Pair<RenderedBuffer, Integer> getBufferBuilderFromTemplate(BlockAndTintGetter renderWorld, RenderType layer, Collection<StructureTemplate.StructureBlockInfo> blocks) {
		return new WorldModelBuilder(layer).withRenderWorld(renderWorld)
				.withBlocks(blocks)
				.build();
	}

	public static Pair<RenderedBuffer, Integer> getBufferBuilderFromTemplate(BlockAndTintGetter renderWorld, RenderType layer, Collection<StructureTemplate.StructureBlockInfo> blocks, PoseStack poseStack) {
		return new WorldModelBuilder(layer).withRenderWorld(renderWorld)
				.withBlocks(blocks)
				.withPoseStack(poseStack)
				.build();
	}

	public static Supplier<PoseStack> rotateToFace(Direction facing) {
		return () -> {
			PoseStack stack = new PoseStack();
			TransformStack.cast(stack)
					.centre()
					.rotateToFace(facing.getOpposite())
					.unCentre();
			return stack;
		};
	}

	private static class ThreadLocalObjects {
		public final RandomSource random = RandomSource.create();
		public final ShadeSeparatingVertexConsumer shadeSeparatingWrapper = new ShadeSeparatingVertexConsumer();
		public final BufferBuilder shadedBuilder = new BufferBuilder(512);
		public final BufferBuilder unshadedBuilder = new BufferBuilder(512);

		private void begin() {
			this.shadedBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
			this.unshadedBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.BLOCK);
			this.shadeSeparatingWrapper.prepare(this.shadedBuilder, this.unshadedBuilder);
		}

		private Pair<RenderedBuffer, Integer> end() {
			this.shadeSeparatingWrapper.clear();
			return ModelUtil.endShadeSeparated(shadedBuilder, unshadedBuilder);
		}
	}
}
