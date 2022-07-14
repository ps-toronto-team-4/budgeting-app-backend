package com.sapient.model.beans;
import javax.persistence.*;

@Entity
@Table(name = "default_categories")
public class DefaultCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String colourHex;
    private String name;
    private String description;

    public Integer getId() {
        return id;
    }

    public String getColourHex() {
        return colourHex;
    }

    public void setColourHex(String colourHex) {
        this.colourHex = colourHex;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
