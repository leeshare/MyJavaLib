package resume;

public class BuilderCar {

    private final int driver;
    private final int tyres;
    private boolean glass;
    private String color;

    public static class Builder{
        private final int driver;
        private final int tyres;
        private boolean glass;
        private String color;

        public Builder(int driver, int tyres){
            this.driver = driver;
            this.tyres = tyres;
        }

        public Builder Glass(boolean glass){
            this.glass = glass;
            return this;
        }
        public Builder Color(String color){
            this.color = color;
            return this;
        }
        public BuilderCar build(){
            return new BuilderCar(this);
        }

    }

    private BuilderCar(Builder builder){
        this.driver = builder.driver;
        this.tyres = builder.tyres;
        this.glass = builder.glass;
        this.color = builder.color;
    }

    public static void main(String[] args){
        BuilderCar b = new Builder(1, 4).Glass(true).Color("black").build();
    }
}
