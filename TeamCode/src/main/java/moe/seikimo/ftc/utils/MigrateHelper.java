package moe.seikimo.ftc.utils;

/**
 * Autonomous migration state comes with one return code:
 * - The first 16 bits represent the opcode of the new state.
 * - THe last 16 bits represent flags for the new state.
 */
public interface MigrateHelper {
    int NEW_INSTANCE = 0x0001;

    /**
     * Unpacks the opcode from the return code.
     *
     * @param retcode The return code from the migration state.
     * @return The opcode for the next state.
     */
    static int opcode(int retcode) {
        return (retcode >> 16) & 0xFFFF;
    }

    /**
     * Unpacks the flags from the return code.
     *
     * @param retcode The return code from the migration state.
     * @return The flags for the next state.
     */
    static int flags(int retcode) {
        return retcode & 0xFFFF;
    }

    /**
     * Packs the opcode and flags into a single return code.
     *
     * @param opcode The opcode for the next state.
     * @param flags The flags for the next state.
     * @return The packed return code.
     */
    static int pack(int opcode, int flags) {
        return ((opcode & 0xFFFF) << 16) | (flags & 0xFFFF);
    }
}
