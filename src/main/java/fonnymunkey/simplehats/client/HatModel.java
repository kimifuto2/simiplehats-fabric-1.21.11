package fonnymunkey.simplehats.client;

import fonnymunkey.simplehats.SimpleHats;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeDeformation;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.entity.state.HumanoidRenderState;
import net.minecraft.resources.Identifier;

public class HatModel extends EntityModel<HumanoidRenderState> {
    public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(
        Identifier.fromNamespaceAndPath(SimpleHats.modId, "hat"), "main");

    private final ModelPart hat;

    public HatModel(ModelPart root) {
        super(root);
        this.hat = root.getChild("hat");
    }

    public static LayerDefinition createLayer() {
        MeshDefinition mesh = new MeshDefinition();
        PartDefinition part = mesh.getRoot();
        part.addOrReplaceChild("hat", CubeListBuilder.create()
            .texOffs(0, 0).addBox(-4.5F, -1.0F, -4.5F, 9.0F, 2.0F, 9.0F, new CubeDeformation(0.25F))
            .texOffs(0, 11).addBox(-4.0F, 1.0F, -4.0F, 8.0F, 6.0F, 8.0F, new CubeDeformation(0.5F)),
            PartPose.ZERO);
        return LayerDefinition.create(mesh, 32, 16);
    }

    @Override
    public void setupAnim(HumanoidRenderState state) {
    }
}
