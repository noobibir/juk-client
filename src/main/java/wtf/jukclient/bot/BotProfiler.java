package wtf.jukclient.bot;

import net.minecraft.profiler.Profiler;

import java.util.ArrayList;
import java.util.List;

public class BotProfiler extends Profiler {
    @Override
    public void clearProfiling() {}
    @Override
    public void startSection(String name) {}
    @Override
    public void endSection() {}
    @Override
    public List<Result> getProfilingData(String profilerName) {return new ArrayList<>();}
    @Override
    public void endStartSection(String name) {}
    @Override
    public String getNameOfLastSection() {return "JUK";}
    @Override
    public void startSection(Class<?> p_startSection_1_) {}
}
