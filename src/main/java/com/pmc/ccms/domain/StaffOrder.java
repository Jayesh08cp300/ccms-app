package com.pmc.ccms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A StaffOrder.
 */
@Entity
@Table(name = "staff_order")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class StaffOrder implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @OneToMany(mappedBy = "staffOrder")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menuItems", "feedbacks", "foodService", "staffOrder" }, allowSetters = true)
    private Set<Menu> menus = new HashSet<>();

    @ManyToMany
    @JoinTable(
        name = "rel_staff_order__user",
        joinColumns = @JoinColumn(name = "staff_order_id"),
        inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    private Set<User> users = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public StaffOrder id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Set<Menu> getMenus() {
        return this.menus;
    }

    public void setMenus(Set<Menu> menus) {
        if (this.menus != null) {
            this.menus.forEach(i -> i.setStaffOrder(null));
        }
        if (menus != null) {
            menus.forEach(i -> i.setStaffOrder(this));
        }
        this.menus = menus;
    }

    public StaffOrder menus(Set<Menu> menus) {
        this.setMenus(menus);
        return this;
    }

    public StaffOrder addMenu(Menu menu) {
        this.menus.add(menu);
        menu.setStaffOrder(this);
        return this;
    }

    public StaffOrder removeMenu(Menu menu) {
        this.menus.remove(menu);
        menu.setStaffOrder(null);
        return this;
    }

    public Set<User> getUsers() {
        return this.users;
    }

    public void setUsers(Set<User> users) {
        this.users = users;
    }

    public StaffOrder users(Set<User> users) {
        this.setUsers(users);
        return this;
    }

    public StaffOrder addUser(User user) {
        this.users.add(user);
        return this;
    }

    public StaffOrder removeUser(User user) {
        this.users.remove(user);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof StaffOrder)) {
            return false;
        }
        return id != null && id.equals(((StaffOrder) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "StaffOrder{" +
            "id=" + getId() +
            "}";
    }
}
