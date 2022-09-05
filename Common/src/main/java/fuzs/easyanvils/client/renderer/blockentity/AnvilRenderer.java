package fuzs.easyanvils.client.renderer.blockentity;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.math.Vector3f;
import fuzs.easyanvils.EasyAnvils;
import fuzs.easyanvils.config.ClientConfig;
import fuzs.easyanvils.world.level.block.entity.AnvilBlockEntity;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.block.model.ItemTransforms;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.entity.ItemRenderer;
import net.minecraft.core.Direction;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.AnvilBlock;

public class AnvilRenderer implements BlockEntityRenderer<AnvilBlockEntity> {
    private final ItemRenderer itemRenderer = Minecraft.getInstance().getItemRenderer();

    public AnvilRenderer(BlockEntityRendererProvider.Context context) {

    }

    @Override
    public void render(AnvilBlockEntity blockEntity, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay) {
        if (!EasyAnvils.CONFIG.get(ClientConfig.class).renderAnvilContents) return;
        Direction direction = blockEntity.getBlockState().getValue(AnvilBlock.FACING);
        int posData = (int) blockEntity.getBlockPos().asLong();
        this.renderFlatItem(0, blockEntity.getItem(0), direction, poseStack, bufferSource, packedLight, packedOverlay, posData);
        this.renderFlatItem(1, blockEntity.getItem(1), direction, poseStack, bufferSource, packedLight, packedOverlay, posData);
    }

    private void renderFlatItem(int index, ItemStack stack, Direction direction, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight, int packedOverlay, int posData) {
        if (stack.isEmpty()) return;
        boolean positiveDirection = direction.getAxisDirection().getStep() == 1;
        boolean mirrored = (positiveDirection ? 1 : 0) != index % 2;
        poseStack.pushPose();
        poseStack.translate(0.0,1.0375, 0.0);
        poseStack.mulPose(Vector3f.XN.rotationDegrees(90.0F));
        if (direction.getAxis() == Direction.Axis.X) {
            if (mirrored) {
                poseStack.translate(0.25, -0.5, 0.0);
            } else {
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
                poseStack.translate(-0.75, 0.5, 0.0);
            }
        } else if (direction.getAxis() == Direction.Axis.Z) {
            if (mirrored) {
                if (positiveDirection) {
                    poseStack.mulPose(Vector3f.ZN.rotationDegrees(90.0F));
                    poseStack.translate(0.25, 0.5, 0.0);
                } else {
                    poseStack.mulPose(Vector3f.ZP.rotationDegrees(180.0F));
                    poseStack.translate(-0.5, 0.25, 0.0);
                }
            } else {
                poseStack.mulPose(Vector3f.ZP.rotationDegrees(90.0F));
                poseStack.translate(-0.75, -0.5, 0.0);
            }
        }
        poseStack.scale(0.375F, 0.375F, 0.375F);
        this.itemRenderer.renderStatic(stack, ItemTransforms.TransformType.FIXED, packedLight, packedOverlay, poseStack, bufferSource, posData + index);
        poseStack.popPose();
    }
}