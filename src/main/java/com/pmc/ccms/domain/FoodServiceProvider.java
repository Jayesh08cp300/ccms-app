package com.pmc.ccms.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A FoodServiceProvider.
 */
@Entity
@Table(name = "food_service_provider")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class FoodServiceProvider implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "doc_proof_name")
    private String docProofName;

    @Column(name = "doc_proof_no")
    private String docProofNo;

    @Column(name = "address")
    private String address;

    @Column(name = "contact_no")
    private String contactNo;

    @Column(name = "email_address")
    private String emailAddress;

    @OneToMany(mappedBy = "foodServiceProvider")
    @Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
    @JsonIgnoreProperties(value = { "menus", "foodServiceProvider" }, allowSetters = true)
    private Set<FoodService> foodServices = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public FoodServiceProvider id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFullName() {
        return this.fullName;
    }

    public FoodServiceProvider fullName(String fullName) {
        this.setFullName(fullName);
        return this;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getDocProofName() {
        return this.docProofName;
    }

    public FoodServiceProvider docProofName(String docProofName) {
        this.setDocProofName(docProofName);
        return this;
    }

    public void setDocProofName(String docProofName) {
        this.docProofName = docProofName;
    }

    public String getDocProofNo() {
        return this.docProofNo;
    }

    public FoodServiceProvider docProofNo(String docProofNo) {
        this.setDocProofNo(docProofNo);
        return this;
    }

    public void setDocProofNo(String docProofNo) {
        this.docProofNo = docProofNo;
    }

    public String getAddress() {
        return this.address;
    }

    public FoodServiceProvider address(String address) {
        this.setAddress(address);
        return this;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getContactNo() {
        return this.contactNo;
    }

    public FoodServiceProvider contactNo(String contactNo) {
        this.setContactNo(contactNo);
        return this;
    }

    public void setContactNo(String contactNo) {
        this.contactNo = contactNo;
    }

    public String getEmailAddress() {
        return this.emailAddress;
    }

    public FoodServiceProvider emailAddress(String emailAddress) {
        this.setEmailAddress(emailAddress);
        return this;
    }

    public void setEmailAddress(String emailAddress) {
        this.emailAddress = emailAddress;
    }

    public Set<FoodService> getFoodServices() {
        return this.foodServices;
    }

    public void setFoodServices(Set<FoodService> foodServices) {
        if (this.foodServices != null) {
            this.foodServices.forEach(i -> i.setFoodServiceProvider(null));
        }
        if (foodServices != null) {
            foodServices.forEach(i -> i.setFoodServiceProvider(this));
        }
        this.foodServices = foodServices;
    }

    public FoodServiceProvider foodServices(Set<FoodService> foodServices) {
        this.setFoodServices(foodServices);
        return this;
    }

    public FoodServiceProvider addFoodService(FoodService foodService) {
        this.foodServices.add(foodService);
        foodService.setFoodServiceProvider(this);
        return this;
    }

    public FoodServiceProvider removeFoodService(FoodService foodService) {
        this.foodServices.remove(foodService);
        foodService.setFoodServiceProvider(null);
        return this;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof FoodServiceProvider)) {
            return false;
        }
        return id != null && id.equals(((FoodServiceProvider) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "FoodServiceProvider{" +
            "id=" + getId() +
            ", fullName='" + getFullName() + "'" +
            ", docProofName='" + getDocProofName() + "'" +
            ", docProofNo='" + getDocProofNo() + "'" +
            ", address='" + getAddress() + "'" +
            ", contactNo='" + getContactNo() + "'" +
            ", emailAddress='" + getEmailAddress() + "'" +
            "}";
    }
}
