
package hmod.solvers.hh.models.oldattr;

import optefx.util.output.OutputManager;

public final class StrategicOscillationHandler {
    private final double oscillationModifier;
    private final double growProportion;
    private final int amplificationScore;
    private double piMultiplier;
    private double oscillationRate;
    private boolean oscillationEnabled;

    public StrategicOscillationHandler(double oscillationModifier, double growProportion, int amplificationScore) {
        this.growProportion = growProportion;
        this.oscillationModifier = oscillationModifier;
        this.amplificationScore = amplificationScore;
    }

    public double getCurrentOscillationRate() {
        return this.oscillationRate;
    }

    public double getGrowProportion() {
        return this.growProportion;
    }

    public int getAmplificationScore() {
        return this.amplificationScore;
    }

    public double getPIMultiplier() {
        return this.piMultiplier;
    }

    public boolean isOscillationEnabled() {
        return this.oscillationEnabled;
    }

    public void enableOscillation() {
        this.oscillationEnabled = true;
        this.restartPiMultiplier();
    }

    public void disableOscillation() {
        OutputManager.println((String)"selHyp-so-events", (Object)"Oscillation complete, returning to base strategy...");
        this.oscillationEnabled = false;
    }

    public void updateOscillationRate() {
        double sign = this.piMultiplier < 3.141592653589793 ? 1.0 : -1.0;
        double innerValue = 1.0 + Math.pow(this.oscillationModifier * Math.tan(this.piMultiplier + 1.5707963267948966), 2.0);
        this.oscillationRate = (1.0 + sign / Math.sqrt(innerValue)) / 2.0;
    }

    public void updatePIMultiplier(int globalNoImproveIterations) {
        this.piMultiplier = 1.5707963267948966 + Math.log10(1.0 + this.growProportion * (double)globalNoImproveIterations);
    }

    public boolean checkPIMultiplier() {
        return this.piMultiplier <= 4.71238898038469;
    }

    public void restartPiMultiplier() {
        OutputManager.println((String)"selHyp-so-events", (Object)"Setting strategy to intensification...");
        this.piMultiplier = 1.5707963267948966;
    }
}
