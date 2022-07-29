package com.pmc.ccms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A Menu.
 */
@Entity
@Table(name = "menu")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class Menu implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "serve_date")
    private LocalDate serveDate;

    @Column(name = "book_before_date")
    private LocalDate bookBeforeDate;

    @Column(name = "cancel_before_date")
    private LocalDate cancelBeforeDate;

    @OneToMany(mappedBy = "menu")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "item", "menu" }, allowSetters = true)
    private Set<MenuItem> menuItems = new HashSet<>();

    @OneToMany(mappedBy = "menu")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menu" }, allowSetters = true)
    private Set<Feedback> feedbacks = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "menus", "foodServiceProvider" }, allowSetters = true)
    private FoodService foodService;

    @ManyToOne
    @JsonIgnoreProperties(value = { "menus", "users" }, allowSetters = true)
    private StaffOrder staffOrder;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public Menu id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public Menu name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public LocalDate getServeDate() {
        return this.serveDate;
    }

    public Menu serveDate(LocalDate serveDate) {
        this.setServeDate(serveDate);
        return this;
    }

    public void setServeDate(LocalDate serveDate) {
        this.serveDate = serveDate;
    }

    public LocalDate getBookBeforeDate() {
        return this.bookBeforeDate;
    }

    public Menu bookBeforeDate(LocalDate bookBeforeDate) {
        this.setBookBeforeDate(bookBeforeDate);
        return this;
    }

    public void setBookBeforeDate(LocalDate bookBeforeDate) {
        this.bookBeforeDate = bookBeforeDate;
    }

    public LocalDate getCancelBeforeDate() {
        return this.cancelBeforeDate;
    }

    public Menu cancelBeforeDate(LocalDate cancelBeforeDate) {
        this.setCancelBeforeDate(cancelBeforeDate);
        return this;
    }

    public void setCancelBeforeDate(LocalDate cancelBeforeDate) {
        this.cancelBeforeDate = cancelBeforeDate;
    }

    public Set<MenuItem> getMenuItems() {
        return this.menuItems;
    }

    public void setMenuItems(Set<MenuItem> menuItems) {
        if (this.menuItems != null) {
            this.menuItems.forEach(i -> i.setMenu(null));
        }
        if (menuItems != null) {
            menuItems.forEach(i -> i.setMenu(this));
        }
        this.menuItems = menuItems;
    }

    public Menu menuItems(Set<MenuItem> menuItems) {
        this.setMenuItems(menuItems);
        return this;
    }

    public Menu addMenuItem(MenuItem menuItem) {
        this.menuItems.add(menuItem);
        menuItem.setMenu(this);
        return this;
    }

    public Menu removeMenuItem(MenuItem menuItem) {
        this.menuItems.remove(menuItem);
        menuItem.setMenu(null);
        return this;
    }

    public Set<Feedback> getFeedbacks() {
        return this.feedbacks;
    }

    public void setFeedbacks(Set<Feedback> feedbacks) {
        if (this.feedbacks != null) {
            this.feedbacks.forEach(i -> i.setMenu(null));
        }
        if (feedbacks != null) {
            feedbacks.forEach(i -> i.setMenu(this));
        }
        this.feedbacks = feedbacks;
    }

    public Menu feedbacks(Set<Feedback> feedbacks) {
        this.setFeedbacks(feedbacks);
        return this;
    }

    public Menu addFeedback(Feedback feedback) {
        this.feedbacks.add(feedback);
        feedback.setMenu(this);
        return this;
    }

    public Menu removeFeedback(Feedback feedback) {
        this.feedbacks.remove(feedback);
        feedback.setMenu(null);
        return this;
    }

    public FoodService getFoodService() {
        return this.foodService;
    }

    public void setFoodService(FoodService foodService) {
        this.foodService = foodService;
    }

    public Menu foodService(FoodService foodService) {
        this.setFoodService(foodService);
        return this;
    }

    public StaffOrder getStaffOrder() {
        return this.staffOrder;
    }

    public void setStaffOrder(StaffOrder staffOrder) {
        this.staffOrder = staffOrder;
    }

    public Menu staffOrder(StaffOrder staffOrder) {
        this.setStaffOrder(staffOrder);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Menu)) {
            return false;
        }
        return id != null && id.equals(((Menu) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Menu{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", serveDate='" + getServeDate() + "'" +
            ", bookBeforeDate='" + getBookBeforeDate() + "'" +
            ", cancelBeforeDate='" + getCancelBeforeDate() + "'" +
            "}";
    }
}
