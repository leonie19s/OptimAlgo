package project.problems;

public class PackingRectangle {
    public int width;
    public int height;

    public PackingRectangle(int width, int height) {
        this.width = width;
        this.height = height;

    }
    public int getWidth(){
        return width;
    }

    public int getHeight(){
        return height;
    }

    public PackingRectangle copy(){
        return new PackingRectangle(this.width, this.height);
    }
}


