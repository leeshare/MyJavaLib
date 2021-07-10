package org.lixl.pattern;

/**
 * 构建器模式
 * Created by lxl on 18/7/12.
 */
public class D_BuilderPattern {
    private final String color;
    private final int tyre;
    private final int glass;
    private final int light;

    public static class Builder {
        private final int tyre;
        private final int glass;
        private String color;
        private int light;

        public Builder(int tyre, int glass) {
            this.tyre = tyre;
            this.glass = glass;
        }

        public Builder color(String value) {
            this.color = value;
            return this;
        }

        public Builder light(int value) {
            this.light = value;
            return this;
        }

        //将当前这些属性 所在的对象 Builder 传入并生成一个新的 父对象
        public D_BuilderPattern build() {
            return new D_BuilderPattern(this);
        }
    }

    private D_BuilderPattern(Builder builder) {
        this.color = builder.color;
        this.tyre = builder.tyre;
        this.glass = builder.glass;
        this.light = builder.light;
    }

    public static void main(String[] args) {
        D_BuilderPattern b = new Builder(4, 8).color("red").light(4).build();
    }
}
