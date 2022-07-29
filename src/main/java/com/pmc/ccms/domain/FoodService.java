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
 * A FoodService.
 */
@Entity
@Table(name = "food_service")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FoodService implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "rate")
    private Float rate;

    @Column(name = "start_date")
    private LocalDate startDate;

    @Column(name = "end_date")
    private LocalDate endDate;

    @OneToMany(mappedBy = "foodService")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menuItems", "feedbacks", "foodService", "staffOrder" }, allowSetters = true)
    private Set<Menu> menus = new HashSet<>();

    @ManyToOne
    @JsonIgnoreProperties(value = { "foodServices" }, allowSetters = true)
    private FoodServiceProvider foodServiceProvider;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FoodService id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public FoodService name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Float getRate() {
        return this.rate;
    }

    public FoodService rate(Float rate) {
        this.setRate(rate);
        return this;
    }

    public void setRate(Float rate) {
        this.rate = rate;
    }

    public LocalDate getStartDate() {
        return this.startDate;
    }

    public FoodService startDate(LocalDate startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(LocalDate startDate) {
        this.startDate = startDate;
    }

    public LocalDate getEndDate() {
        return this.endDate;
    }

    public FoodService endDate(LocalDate endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(LocalDate endDate) {
        this.endDate = endDate;
    }

    public Set<Menu> getMenus() {
        return this.menus;
    }

    public void setMenus(Set<Menu> menus) {
        if (this.menus != null) {
            this.menus.forEach(i -> i.setFoodService(null));
        }
        if (menus != null) {
            menus.forEach(i -> i.setFoodService(this));
        }
        this.menus = menus;
    }

    public FoodService menus(Set<Menu> menus) {
        this.setMenus(menus);
        return this;
    }

    public FoodService addMenu(Menu menu) {
        this.menus.add(menu);
        menu.setFoodService(this);
        return this;
    }

    public FoodService removeMenu(Menu menu) {
        this.menus.remove(menu);
        menu.setFoodService(null);
        return this;
    }

    public FoodServiceProvider getFoodServiceProvider() {
        return this.foodServiceProvider;
    }

    public void setFoodServiceProvider(FoodServiceProvider foodServiceProvider) {
        this.foodServiceProvider = foodServiceProvider;
    }

    public FoodService foodServiceProvider(FoodServiceProvider foodServiceProvider) {
        this.setFoodServiceProvider(foodServiceProvider);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodService)) {
            return false;
        }
        return id != null && id.equals(((FoodService) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoodService{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", rate=" + getRate() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
