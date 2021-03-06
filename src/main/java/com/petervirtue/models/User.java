package com.petervirtue.models;

/*

User Model:
- id (int)
- name (varchar(128))
- created_at (timestamp)
- updated_at (timestamp)

*/

public class User {

    private String name;
    private int id;

    public User(String name) {
        this.name = name;
    }

    public User(int id, String name) {
        this.name = name;
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        // Work with the database to set the name and then save
    }

    public int getID() {
        return id;
    }
    
}
