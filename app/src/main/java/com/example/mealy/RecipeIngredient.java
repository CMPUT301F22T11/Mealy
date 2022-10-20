public class RecipeIngredient {
    private String title;
    private String description;
    private int amount;
    private int unit;
    private String category;

    // Getters
    public String getTitle() {
        return this.title;
    }

    
    public String getDescription() {
        return this.description;
    }

    public int getAmount() {
        return this.amount;
    }

    public int getUnit() {
        return this.unit;
    }

    public String getCategory() {
        return this.category;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public void setUnit(int unit) {
        this.unit = unit;
    }

    public void setCategory(int category) {
        this.category = category;
    }

    // Constructor
    public RecipeIngredient(String title, String description, int amount, int unit, String category) {
        this.title = title;
        this.description = description;
        this.amount = amount;
        this.unit = unit;
        this.category = category;
    }

}