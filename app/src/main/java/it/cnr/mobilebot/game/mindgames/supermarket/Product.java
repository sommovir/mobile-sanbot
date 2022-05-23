package it.cnr.mobilebot.game.mindgames.supermarket;

import java.util.Date;
import java.util.Random;

public class Product {

    private long id;
    private String name;//carota:carote:carote:carote
    private Department department = null;
    private String alternatives;
    public final String separatore = ",";
    public final String UNKNOWN = "unknown";

    public Product() {
    }

    public Product(long id, String name) {
        this.id = id;
        setName(name);
    }

    public Product(long id, String name, Department dep, String alternatives) throws MindGameExceptions {
        this.id = id;
        setName(name);
        setDepartment(dep);
        this.alternatives = alternatives;

    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public Department getDepartment() {
        return department;
    }

    public String getAlternatives() {
        return alternatives;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setAlternatives(String alternatives) {
        this.alternatives = alternatives;
    }

    public final void setName(String name) {
        if (name == null || name.isEmpty()) {
            this.name = UNKNOWN;
        } else {
            this.name = name;
        }
    }

    public void setDepartment(Department department) throws MindGameExceptions {

        if (department == null) {
            throw new MindGameExceptions() {
                @Override
                public String errorMessage() {
                    return "NULL PRODUCT";
                }
            };
        }
        this.department = department;
    }

    public String get() {
        String[] alternative = this.alternatives.split(separatore);
        Random random = new Random(new Date().getTime());
        int rnd = random.nextInt(alternative.length);
        return alternative[rnd];
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (obj instanceof Product) {
            Product p = (Product) obj;
            if ((p.getDepartment() == null && this.department == null || p.getDepartment().equals(this.department))
                    && p.getName().equals(this.name)
                    && p.getId() == this.getId()) {
                return true;
            } else {
                return false;
            }
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return this.name + " " + this.alternatives;
    }

}