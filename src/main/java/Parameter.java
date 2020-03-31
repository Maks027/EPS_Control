public class Parameter {
    private int id;
    private int value;
    private String name;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Parameter(int id, int value, String name) {
        this.id = id;
        this.value = value;
        this.name = name;
    }
}
