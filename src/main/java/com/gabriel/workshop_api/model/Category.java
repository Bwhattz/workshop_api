package com.gabriel.workshop_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "categories")
@Getter
@Builder
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Category {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false, unique = true, length = 100)
    private String name;

    @OneToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(name = "events_category", joinColumns = @JoinColumn(name = "events_id"), inverseJoinColumns = @JoinColumn(name = "category_id"))
    private List<Event> events;

    public void updateCategory(Category category) {

        if(category.getName() != null) {
            this.name = category.getName();
        }
    }
}
