public class Recipe {
    private String title;
    private int time;
    private int numServings;
    private String category;
    private String comments;
    private List<RecipeIngredient> recipeIngredientList;


    // Getters
    public String getTitle() {
        return this.title;
    }
    
    public int getTime() {
        return this.time;
    }
    
    public int getNumServings() {
        return this.numServings;
    }

    public String getCategory() {
        return this.category;
    }

    public String getComments() {
        return this.comments;
    }

    public List<RecipeIngredient> getRicipeIngredientList() {
        return this.recipeIngredientList;
    }

    // Setters
    public void setTitle(String title) {
        this.title = title;
    }
    
    public void setTime(int time) {
        this.time = time;
    }

    public void setNumServings(int numServings) {
        this.numServings = numServings;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public void setRecipeIngredientList(List<RecipeIngredient> recipeIngredientList) {
        this.recipeIngredientList = new ArrayList<>();

        for (RecipeIngredient x : recipeIngredientList) {
            this.recipeIngredientList.add(x);
        }
    }

    // Constructor.
    public Recipe(String title, int time, int numServings, String category, String comments, List<RecipeIngredient> recipeIngredientList) {
        this.title = title;
        this.time = time;
        this.numServings = numServings;
        this.category = category;
        this.comments = comments;
        this.recipeIngredientList = new ArrayList<>();

        for (RecipeIngredient x : recipeIngredientList) {
            this.recipeIngredientList.add(x);
        }

    }

    // Add ingredient to the recipe.
    public recipeIngredientAdd(RecipeIngredient ingredient) {
        this.recipeIngredientList.add(ingredient);

    }

    // Delete given ingredient from the recipe.
    public recipeIngredientDelete(RecipeIngredient ingredient) {
        this.recipeIngredientList.remove(ingredient);
    }


}