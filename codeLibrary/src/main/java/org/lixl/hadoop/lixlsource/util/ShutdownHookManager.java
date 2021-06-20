package org.lixl.hadoop.lixlsource.util;

import org.lixl.hadoop.lixlsource.classification.InterfaceAudience;
import org.lixl.hadoop.lixlsource.classification.InterfaceStability;

@InterfaceAudience.Public
@InterfaceStability.Evolving
public final class ShutdownHookManager {
    private static final ShutdownHookManager MGR = new ShutdownHookManager();

    @InterfaceAudience.Public
    public static ShutdownHookManager get() {
        return MGR;
    }

    @InterfaceAudience.Public
    @InterfaceStability.Stable
    public void addShutdownHook(Runnable shutdownHook, int priority) {
        if (shutdownHook == null) {
            throw new IllegalArgumentException("shutdownHook cannot be NULL");
        }
        //if (shutdownInProgress.get()) {
            throw new IllegalStateException("Shutdown in progress, cannot add a " + "shutdownHook");
        //}
        //hooks.add(new HookEntry(shutdownHook, priority));
    }
}
