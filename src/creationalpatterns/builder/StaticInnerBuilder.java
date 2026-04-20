package creationalpatterns.builder;

//most commonly used builder pattern
//cleaner code, simple workflow
//Tight coupling of builder with product provides encapsulation as builder tied to product to create object and avoids misuse of builder class

//Product class
class Laptop {
    //optional and recommended to use final
    //makes object immutable and thread-safe
    private final String brand;
    private final String processor;
    private final int ram;
    private final int storage;

    //private constructor (only builder can create object)
    private Laptop(Builder builder) {
        this.brand = builder.brand;
        this.processor = builder.processor;
        this.ram = builder.ram;
        this.storage = builder.storage;
    }

    //static inner builder class, compiled as Laptop.Builder so no conflict with Builder interface in DirectorBuilder pattern
    public static class Builder {
        private String brand;
        private String processor;
        private int ram;
        private int storage;

        public Builder setBrand(String brand) {
            this.brand = brand;
            return this;
        }

        public Builder setProcessor(String processor) {
            this.processor = processor;
            return this;
        }

        public Builder setRam(int ram) {
            this.ram = ram;
            return this;
        }

        public Builder setStorage(int storage) {
            this.storage = storage;
            return this;
        }

        public Laptop build() {
            
            //optional : if need validations on some of the fields
            if (brand == null || processor == null) {
                throw new IllegalStateException("Brand and Processor are required");
            }

            return new Laptop(this);
        }
    }

    @Override
    public String toString() {
        return "Laptop [brand=" + brand + ", processor=" + processor +
                ", ram=" + ram + "GB, storage=" + storage + "GB]";
    }
}

class StaticInnerBuilderMain {
    public static void main(String[] args) {
        Laptop laptop = new Laptop.Builder()
                .setBrand("Dell")
                .setProcessor("Intel i7")
                .setRam(16)
                .setStorage(512)
                .build();

        System.out.println(laptop);

    }
}
