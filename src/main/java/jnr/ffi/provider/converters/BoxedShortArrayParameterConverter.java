package jnr.ffi.provider.converters;

import jnr.ffi.mapper.ToNativeContext;
import jnr.ffi.mapper.ToNativeConverter;
import jnr.ffi.provider.ParameterFlags;

/**
 * Converts a Short[] array to a primitive short[] array parameter
 */
@ToNativeConverter.NoContext
@ToNativeConverter.Cacheable
public class BoxedShortArrayParameterConverter implements ToNativeConverter<Short[], short[]> {
    private static final ToNativeConverter<Short[], short[]> IN = new BoxedShortArrayParameterConverter(ParameterFlags.IN);
    private static final ToNativeConverter<Short[], short[]> OUT = new BoxedShortArrayParameterConverter.Out(ParameterFlags.OUT);
    private static final ToNativeConverter<Short[], short[]> INOUT = new BoxedShortArrayParameterConverter.Out(ParameterFlags.IN | ParameterFlags.OUT);
    private final int parameterFlags;

    public static ToNativeConverter<Short[], short[]> getInstance(ToNativeContext toNativeContext) {
        int parameterFlags = ParameterFlags.parse(toNativeContext.getAnnotations());
        return ParameterFlags.isOut(parameterFlags) ? ParameterFlags.isIn(parameterFlags) ? INOUT : OUT : IN;
    }

    public BoxedShortArrayParameterConverter(int parameterFlags) {
        this.parameterFlags = parameterFlags;
    }

    @Override
    public short[] toNative(Short[] array, ToNativeContext context) {
        if (array == null) {
            return null;
        }
        short[] primitive = new short[array.length];
        if (ParameterFlags.isIn(parameterFlags)) {
            for (int i = 0; i < array.length; i++) {
                primitive[i] = array[i] != null ? array[i] : 0;
            }
        }

        return primitive;
    }

    public static final class Out extends BoxedShortArrayParameterConverter implements PostInvocation<Short[], short[]> {
        Out(int parameterFlags) {
            super(parameterFlags);
        }

        @Override
        public void postInvoke(Short[] array, short[] primitive, ToNativeContext context) {
            if (array != null && primitive != null) {
                for (int i = 0; i < array.length; i++) {
                    array[i] = primitive[i];
                }
            }
        }
    }

    @Override
    public Class<short[]> nativeType() {
        return short[].class;
    }
}
