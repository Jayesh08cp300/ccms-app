package com.pmc.ccms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Item.
 */
@Entity
@Table(name = "item")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Item implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "veg")
    private Boolean veg;

    @OneToMany(mappedBy = "item")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "item", "menu" }, allowSetters = true)
    private Set<MenuItem> menuItems = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Item id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Item name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Boolean getVeg() {
        return this.veg;
    }

    public Item veg(Boolean veg) {
        this.setVeg(veg);
        return this;
    }

    public void setVeg(Boolean veg) {
        this.veg = veg;
    }

    public Set<MenuItem> getMenuItems() {
        return this.menuItems;
    }

    public void setMenuItems(Set<MenuItem> menuItems) {
        if (this.menuItems != null) {
            this.menuItems.forEach(i -> i.setItem(null));
        }
        if (menuItems != null) {
            menuItems.forEach(i -> i.setItem(this));
        }
        this.menuItems = menuItems;
    }

    public Item menuItems(Set<MenuItem> menuItems) {
        this.setMenuItems(menuItems);
        return this;
    }

    public Item addMenuItem(MenuItem menuItem) {
        this.menuItems.add(menuItem);
        menuItem.setItem(this);
        return this;
    }

    public Item removeMenuItem(MenuItem menuItem) {
        this.menuItems.remove(menuItem);
        menuItem.setItem(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Item)) {
            return false;
        }
        return id != null && id.equals(((Item) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Item{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", veg='" + getVeg() + "'" +
            "}";
    }
}
