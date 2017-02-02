package com.cs499.app.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import java.util.Objects;

/**
 * A Father.
 */
@Entity
@Table(name = "father")
public class Father implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "age")
    private Integer age;

    @OneToMany(mappedBy = "father")
    @JsonIgnore
    private Set<Son> sons = new HashSet<>();

    @OneToMany(mappedBy = "father")
    @JsonIgnore
    private Set<Daughter> daughters = new HashSet<>();

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Father name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getAge() {
        return age;
    }

    public Father age(Integer age) {
        this.age = age;
        return this;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Set<Son> getSons() {
        return sons;
    }

    public Father sons(Set<Son> sons) {
        this.sons = sons;
        return this;
    }

    public Father addSon(Son son) {
        sons.add(son);
        son.setFather(this);
        return this;
    }

    public Father removeSon(Son son) {
        sons.remove(son);
        son.setFather(null);
        return this;
    }

    public void setSons(Set<Son> sons) {
        this.sons = sons;
    }

    public Set<Daughter> getDaughters() {
        return daughters;
    }

    public Father daughters(Set<Daughter> daughters) {
        this.daughters = daughters;
        return this;
    }

    public Father addDaughter(Daughter daughter) {
        daughters.add(daughter);
        daughter.setFather(this);
        return this;
    }

    public Father removeDaughter(Daughter daughter) {
        daughters.remove(daughter);
        daughter.setFather(null);
        return this;
    }

    public void setDaughters(Set<Daughter> daughters) {
        this.daughters = daughters;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Father father = (Father) o;
        if (father.id == null || id == null) {
            return false;
        }
        return Objects.equals(id, father.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "Father{" +
            "id=" + id +
            ", name='" + name + "'" +
            ", age='" + age + "'" +
            '}';
    }
}
