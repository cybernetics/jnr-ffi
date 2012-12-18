package jnr.ffi.provider.converters;

import jnr.ffi.mapper.ToNativeContext;
import jnr.ffi.mapper.ToNativeConverter;
import jnr.ffi.provider.ParameterFlags;

/**
 * Converts a Double[] array to a double[] array parameter
 */
@ToNativeConverter.NoContext
@ToNativeConverter.Cacheable
public class BoxedDoubleArrayParameterConverter implements ToNativeConverter<Double[], double[]> {
    private static final ToNativeConverter<Double[], double[]> IN = new BoxedDoubleArrayParameterConverter(ParameterFlags.IN);
    private static final ToNativeConverter<Double[], double[]> OUT = new BoxedDoubleArrayParameterConverter.Out(ParameterFlags.OUT);
    private static final ToNativeConverter<Double[], double[]> INOUT = new BoxedDoubleArrayParameterConverter.Out(ParameterFlags.IN | ParameterFlags.OUT);

    private final int parameterFlags;

    public static ToNativeConverter<Double[], double[]> getInstance(ToNativeContext toNativeContext) {
        int parameterFlags = ParameterFlags.parse(toNativeContext.getAnnotations());
        return ParameterFlags.isOut(parameterFlags) ? ParameterFlags.isIn(parameterFlags) ? INOUT : OUT : IN;
    }

    BoxedDoubleArrayParameterConverter(int parameterFlags) {
        this.parameterFlags = parameterFlags;
    }

    @Override
    public double[] toNative(Double[] array, ToNativeContext context) {
        if (array == null) {
            return null;
        }
        double[] primitive = new double[array.length];
        if (ParameterFlags.isIn(parameterFlags)) {
            for (int i = 0; i < array.length; i++) {
                primitive[i] = array[i] != null ? array[i] : 0;
            }
        }

        return primitive;
    }

    public static final class Out extends BoxedDoubleArrayParameterConverter implements PostInvocation<Double[], double[]> {
        Out(int parameterFlags) {
            super(parameterFlags);
        }

        @Override
        public void postInvoke(Double[] array, double[] primitive, ToNativeContext context) {
            if (array != null && primitive != null) {
                for (int i = 0; i < array.length; i++) {
                    array[i] = primitive[i];
                }
            }
        }
    }

    @Override
    public Class<double[]> nativeType() {
        return double[].class;
    }
}
