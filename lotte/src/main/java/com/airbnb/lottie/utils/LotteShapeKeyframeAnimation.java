package com.airbnb.lottie.utils;

import android.graphics.Path;
import android.view.animation.Interpolator;

import com.airbnb.lottie.model.LotteShapeData;

import java.util.List;

public class LotteShapeKeyframeAnimation extends LotteKeyframeAnimation<Path> {
    private final Path tempPath = new Path();
    private final LotteShapeData tempShapeData = new LotteShapeData();
    private final List<LotteShapeData> shapeData;

    public LotteShapeKeyframeAnimation(long duration, long compDuration, List<Float> keyTimes, List<LotteShapeData> shapeData, List<Interpolator> interpolators) {
        super(duration, compDuration, keyTimes, interpolators);
        this.shapeData = shapeData;
    }

    @Override
    public Path getValue() {
        if (progress <= 0f) {
            MiscUtils.getPathFromData(shapeData.get(0), tempPath);
            return tempPath;
        } else if (progress >= 1f) {
            MiscUtils.getPathFromData(shapeData.get(shapeData.size() - 1), tempPath);
            return tempPath;
        }

        int keyframeIndex = getKeyframeIndex();

        float startKeytime = keyTimes.get(keyframeIndex);
        float endKeytime = keyTimes.get(keyframeIndex + 1);

        float percentageIntoFrame = 0;
        if (!isDiscrete) {
            percentageIntoFrame = (progress - startKeytime) / (endKeytime - startKeytime);
            if (interpolators != null) {
                percentageIntoFrame = interpolators.get(keyframeIndex).getInterpolation(percentageIntoFrame);
            }
        }

        LotteShapeData startShapeData = shapeData.get(keyframeIndex);
        LotteShapeData endShapeData = shapeData.get(keyframeIndex + 1);

        tempShapeData.interpolateBetween(startShapeData, endShapeData, percentageIntoFrame);
        MiscUtils.getPathFromData(tempShapeData, tempPath);
        return tempPath;
    }
}