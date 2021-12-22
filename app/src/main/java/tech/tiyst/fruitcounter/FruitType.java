package tech.tiyst.fruitcounter;

public enum FruitType {
    BANANA(R.drawable.banana, R.string.fruitNameBanana);

    private int fruitImageID;
    private int fruitNameID;

    FruitType(int fruitImageID, int fruitName) {
        this.fruitImageID = fruitImageID;
        this.fruitNameID = fruitName;
    }

    public FruitType fromString(String name) {
        return FruitType.valueOf(name.toUpperCase());
    }

    public int getFruitImageID() {
        return fruitImageID;
    }

    public int getFruitNameID() {
        return fruitNameID;
    }

}
