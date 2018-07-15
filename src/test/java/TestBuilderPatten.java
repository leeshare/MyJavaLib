/**
 * 构建器模式
 * Created by lxl on 18/7/12.
 */
public class TestBuilderPatten {
    private final String color;
    private final int tyre;
    private final int glass;
    private final int light;

    public static class Builder{
        private final int tyre;
        private final int glass;
        private String color;
        private int light;
        public Builder(int tyre, int glass){
            this.tyre = tyre;
            this.glass = glass;
        }
        public Builder color(String value){
            this.color = value;
            return this;
        }
        public Builder light(int value){
            this.light = value;
            return this;
        }
        public TestBuilderPatten build(){
            return new TestBuilderPatten(this);
        }
    }
    private TestBuilderPatten(Builder builder){
        this.color = builder.color;
        this.tyre = builder.tyre;
        this.glass = builder.glass;
        this.light = builder.light;
    }

    public static void main(String[] args){
        TestBuilderPatten b = new Builder(4, 8).color("red").light(4).build();
    }
}
