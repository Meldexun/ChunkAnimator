package lumien.chunkanimator.config;

import com.mojang.datafixers.util.Function4;
import com.mojang.datafixers.util.Function5;
import penner.easing.*;

/**
 * @author Harley O'Connor
 */
public enum EasingFunction {
    LINEAR(Linear::easeOut),
    QUAD(Quad::easeOut),
    CUBIC(Cubic::easeOut),
    QUART(Quart::easeOut),
    QUINT(Quint::easeOut),
    EXPO(Expo::easeOut),
    SINE(Sine::easeOut),
    CIRC(Circ::easeOut),
    BACK(Back::easeOut),
    BOUNCE(Bounce::easeOut),
    ELASTIC(Elastic::easeOut);

    private final Function4<Float, Float, Float, Float, Float> easeOutFunc;

    EasingFunction(Function4<Float, Float, Float, Float, Float> easeOutFunc) {
        this.easeOutFunc = easeOutFunc;
    }

    public Function4<Float, Float, Float, Float, Float> easeOutFunc() {
        return easeOutFunc;
    }
}
