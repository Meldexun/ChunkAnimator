function initializeCoreMod() {
    return {
        'LevelRenderer#renderChunkLayer': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.LevelRenderer',
                'methodName': 'm_172993_', // renderChunkLayer
                'methodDesc': '(Lnet/minecraft/client/renderer/RenderType;Lcom/mojang/blaze3d/vertex/PoseStack;DDDLcom/mojang/math/Matrix4f;)V'
            },
            'transformer': function (methodNode) {
                const Opcodes = Java.type("org.objectweb.asm.Opcodes");
                const ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI");

                const MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");
                const VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");

                const code = methodNode.instructions;
                const instr = code.toArray();

                let setInstr = null;

                for (i in instr) {
                    const instruction = instr[i];

                    if (instruction instanceof MethodInsnNode &&
                        (instruction.name === "set" || instruction.name === "m_5889_") &&
                        instruction.desc === "(FFF)V") {
                        setInstr = instruction;
                        break;
                    }
                }

                if (setInstr === null) {
                    ASMAPI.log("ERROR", "Could not find `Uniform#set(float, float, float)` invokation in " +
                        "`LevelRenderer#renderChunkLayer(RenderType, PoseStack, double, double, double, Matrix4f)`.");
                    return methodNode;
                }

                code.insertBefore(setInstr, new VarInsnNode(Opcodes.ALOAD, 17));
                // code.insertBefore(setInstr, new VarInsnNode(Opcodes.ALOAD, 14));
                code.set(setInstr, new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "lumien/chunkanimator/handler/AsmHandler",
                    "preRenderChunk",
                    "(Lcom/mojang/blaze3d/shaders/Uniform;FFF" +
                    "Lnet/minecraft/client/renderer/chunk/ChunkRenderDispatcher$RenderChunk;" +
                    ")V",
                    false
                ));

                return methodNode;
            }
        }
    }
}
