function initializeCoreMod() {
    return {
        'ChunkRenderDispatcher$RenderChunk#setOrigin': {
            'target': {
                'type': 'METHOD',
                'class': 'net.minecraft.client.renderer.chunk.ChunkRenderDispatcher$RenderChunk',
                'methodName': 'm_112801_', // setOrigin
                'methodDesc': '(III)V'
            },
            'transformer': function (methodNode) {
                var Opcodes = Java.type("org.objectweb.asm.Opcodes");
                var ASMAPI = Java.type("net.minecraftforge.coremod.api.ASMAPI");

                var MethodInsnNode = Java.type("org.objectweb.asm.tree.MethodInsnNode");
                var VarInsnNode = Java.type("org.objectweb.asm.tree.VarInsnNode");

                var code = methodNode.instructions;
                var instr = code.toArray();

                var resetInstr = null;

                for (i in instr) {
                    var instruction = instr[i];

                    if (instruction instanceof MethodInsnNode &&
                        (instruction.name === "reset" || instruction.name === "m_112846_")) {
                        resetInstr = instruction;
                        break;
                    }
                }

                if (resetInstr === null) {
                    ASMAPI.log("ERROR", "Could not find `BlockPos.MutableBlockPos#set(int, int, int)` invokation in " +
                        "`ChunkRenderDispatcher.RenderChunk#setOrigin(int, int, int)`.");
                    return methodNode;
                }

                code.insertBefore(resetInstr, new VarInsnNode(Opcodes.ALOAD, 0));
                code.insertBefore(resetInstr, new VarInsnNode(Opcodes.ILOAD, 1));
                code.insertBefore(resetInstr, new VarInsnNode(Opcodes.ILOAD, 2));
                code.insertBefore(resetInstr, new VarInsnNode(Opcodes.ILOAD, 3));

                // Invoke AsmHandler#setOrigin with the ChunkRender, x, y, and z.
                code.insertBefore(resetInstr, new MethodInsnNode(
                    Opcodes.INVOKESTATIC,
                    "lumien/chunkanimator/handler/AsmHandler",
                    "setOrigin",
                    "(Lnet/minecraft/client/renderer/chunk/ChunkRenderDispatcher$RenderChunk;III)V",
                    false
                ));

                return methodNode;
            }
        }
    }
}