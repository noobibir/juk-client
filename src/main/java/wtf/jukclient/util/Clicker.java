package wtf.jukclient.util;

import java.util.Random;

public class Clicker {
    public int clicksThisTick;
    private float thisSecondCps = -1;
    private int ticksPast;
    private float graphSlope = -1;
    private int prevClicks;
    private final Random rnd;

    public Clicker() {
        rnd = new Random();
    }

    public void tick(float minCPS, float maxCPS) {
        ticksPast ++;
        if (ticksPast >= 20) {
            thisSecondCps = -1;
            ticksPast = 0;
        }
        if (thisSecondCps == -1) {
            float randomValue = rnd.nextFloat();
            thisSecondCps = minCPS + randomValue * (maxCPS - minCPS);
            graphSlope = thisSecondCps / 20;
            prevClicks = 0;
        }

        int curClicks = Math.round(ticksPast * graphSlope);

        clicksThisTick = 0;
        if (curClicks > prevClicks) {
            clicksThisTick = curClicks - prevClicks;
        }
        prevClicks = curClicks;
    }
}
