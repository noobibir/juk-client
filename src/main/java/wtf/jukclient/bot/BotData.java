package wtf.jukclient.bot;

import com.google.common.collect.Queues;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import com.google.common.util.concurrent.ListenableFutureTask;
import net.minecraft.client.multiplayer.ServerData;
import net.minecraft.network.INetHandler;
import net.minecraft.network.NetworkManager;
import net.minecraft.util.IThreadListener;
import net.minecraft.util.Session;
import org.apache.commons.lang3.Validate;

import java.util.Queue;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class BotData implements IThreadListener {
    public NetworkManager netManager;
    public INetHandler netHandler;
    public BotWorld world;
    public BotController controller;
    public BotEntity player;
    public Session session;
    public BotSettings settings;

    private final Queue<FutureTask<? >> scheduledTasks = Queues.newArrayDeque();

    public <V> ListenableFuture<V> addScheduledTask(Callable<V> callableToSchedule)
    {
        Validate.notNull(callableToSchedule);

        if (!this.isCallingFromMinecraftThread())
        {
            ListenableFutureTask<V> listenablefuturetask = ListenableFutureTask.<V>create(callableToSchedule);

            synchronized (this.scheduledTasks)
            {
                this.scheduledTasks.add(listenablefuturetask);
                return listenablefuturetask;
            }
        }
        else
        {
            try
            {
                return Futures.immediateFuture(callableToSchedule.call());
            }
            catch (Exception exception)
            {
                return Futures.immediateFailedCheckedFuture(exception);
            }
        }
    }

    @Override
    public ListenableFuture<Object> addScheduledTask(Runnable runnableToSchedule)
    {
        Validate.notNull(runnableToSchedule);
        return this.addScheduledTask(Executors.callable(runnableToSchedule));
    }

    @Override
    public boolean isCallingFromMinecraftThread() {
        return false;
    }

    public BotNetHandlerPlay getNetHandler()
    {
        return this.player != null ? this.player.sendQueue : null;
    }

    public void loadWorld(BotWorld worldClientIn)
    {
        if (worldClientIn == null) {
            BotNetHandlerPlay nethandlerplayclient = this.getNetHandler();

            if (nethandlerplayclient != null) {
                nethandlerplayclient.cleanup();
            }
        }

        this.netManager = null;

        this.world = worldClientIn;

        if (worldClientIn != null)
        {
            if (this.player == null)
            {
                this.player = this.controller.createPlayer(worldClientIn);
                this.controller.flipPlayer(this.player);
            }

            this.player.preparePlayerToSpawn();
            worldClientIn.spawnEntityInWorld(this.player);
            this.player.movementInput = new BotMovementInput(this.settings);
            this.controller.setPlayerCapabilities(this.player);
        }
        else
        {
            //this.saveLoader.flushCache();
            this.player = null;
        }

        //System.gc();
        //this.systemTime = 0L;
    }

    public void setDimensionAndSpawnPlayer(int dimension)
    {
        this.world.setInitialSpawnLocation();
        this.world.removeAllEntities();
        int i = 0;
        String s = null;

        if (this.player != null)
        {
            i = this.player.getEntityId();
            this.world.removeEntity(this.player);
            s = this.player.getClientBrand();
        }

        BotEntity entityplayersp = this.player;
        this.player = this.controller.createPlayer(this.world);
        this.player.getDataWatcher().updateWatchedObjectsFromList(entityplayersp.getDataWatcher().getAllWatched());
        this.player.dimension = dimension;
        this.player.preparePlayerToSpawn();
        this.player.setClientBrand(s);
        this.world.spawnEntityInWorld(this.player);
        this.controller.flipPlayer(this.player);
        this.player.movementInput = new BotMovementInput(this.settings);
        this.player.setEntityId(i);
        this.controller.setPlayerCapabilities(this.player);
        this.player.setReducedDebug(entityplayersp.hasReducedDebug());

//        if (this.currentScreen instanceof GuiGameOver) {
//            this.displayGuiScreen((GuiScreen)null);
//        }
    }
}
