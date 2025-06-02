package com.gimral.streaming.core.model;

public class ErrorInputOutput<IN, OUT> {
    public enum ErrorInputOutputType {
        SUCCESS, ERROR
    }

    private final OUT output;
    private final IN input;
    private final ErrorInputOutputType type;

    public ErrorInputOutput(IN input, OUT output, ErrorInputOutputType type) {
        this.output = output;
        this.input = input;
        this.type = type;
    }

    public OUT getOutput() {
        return output;
    }

    public IN getInput() {
        return input;
    }

    public ErrorInputOutputType getType() {
        return type;
    }

    @Override
    public String toString() {
        return "MapOutput{" +
                "output=" + output +
                ", input=" + input +
                ", type=" + type +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (!(o instanceof ErrorInputOutput))
            return false;

        ErrorInputOutput<?, ?> that = (ErrorInputOutput<?, ?>) o;

        if (output != null ? !output.equals(that.output) : that.output != null)
            return false;
        if (input != null ? !input.equals(that.input) : that.input != null)
            return false;
        return type == that.type;
    }

    @Override
    public int hashCode() {
        int result = output != null ? output.hashCode() : 0;
        result = 31 * result + (input != null ? input.hashCode() : 0);
        result = 31 * result + (type != null ? type.hashCode() : 0);
        return result;
    }

    public static <IN, OUT> ErrorInputOutput<IN, OUT> success(OUT output) {
        return new ErrorInputOutput<>(null, output, ErrorInputOutputType.SUCCESS);
    }

    public static <IN, OUT> ErrorInputOutput<IN, OUT> error(IN input) {
        return new ErrorInputOutput<IN, OUT>(input, null, ErrorInputOutputType.ERROR);
    }
}
