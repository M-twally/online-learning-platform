package Db.com;
import org.bson.codecs.pojo.annotations.BsonId;
import org.bson.types.ObjectId;

import com.mongodb.client.MongoCollection;
import com.mongodb.client.model.Filters;
import org.bson.Document;

public class User {
    @BsonId
    private ObjectId id;
    private String name;
    private String email;
    private String password;
    private String affiliation;
    private int yearsOfExperience;
    private String bio;
    private String role;

    // Constructors, getters, and setters

    public User() {
    }

    public User(String name, String email, String password, String affiliation, int yearsOfExperience, String bio, String role) {
        this.name = name;
        this.email = email;
        this.password = password;
        this.affiliation = affiliation;
        this.yearsOfExperience = yearsOfExperience;
        this.bio = bio;
        this.role=role; // Ensure role is valid
    }

    // Getters and setters


    public void setRole(String role) {
        this.role = role;
    }

    public ObjectId getId() {
        return id;
    }

    public void setId(ObjectId id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAffiliation() {
        return affiliation;
    }

    public void setAffiliation(String affiliation) {
        this.affiliation = affiliation;
    }

    public int getYearsOfExperience() {
        return yearsOfExperience;
    }

    public void setYearsOfExperience(int yearsOfExperience) {
        this.yearsOfExperience = yearsOfExperience;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public String getRole() {
        return role;
    }



    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", affiliation='" + affiliation + '\'' +
                ", yearsOfExperience=" + yearsOfExperience +
                ", bio='" + bio + '\'' +
                ", role='" + role + '\'' +
                '}';
    }

    // Method to check if username is unique
    public boolean isUsernameUnique(MongoCollection<Document> collection) {
        return collection.find(Filters.eq("name", this.name)).first() == null;
    }

    // Method to check if email is unique
    public boolean isEmailUnique(MongoCollection<Document> collection) {
        return collection.find(Filters.eq("email", this.email)).first() == null;
    }

    // Method to check if user is valid (i.e., username and email are unique)
    public boolean isValid(MongoCollection<Document> collection) {
        return isUsernameUnique(collection) && isEmailUnique(collection);
    }

}

