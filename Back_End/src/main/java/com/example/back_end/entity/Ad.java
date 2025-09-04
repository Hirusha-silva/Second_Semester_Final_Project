package com.example.back_end.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;
import java.util.List;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "ads")
public class Ad {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long adId;

    private String title;
    private String description;
    private Double price;
    private String location;

    @Enumerated(EnumType.STRING)
    private AdStatus status; // ACTIVE,PENDING

    @Temporal(TemporalType.TIMESTAMP)
    private Date postedDate;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne
    @JoinColumn(name = "category_id")
    private Category category;

    @ManyToOne
    @JoinColumn(name = "model_id")
    private VehicleModel vehicleModel;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL)
    private List<AdPhoto> adPhotos;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL)
    private List<Favorite> favorites;

    @OneToMany(mappedBy = "ad", cascade = CascadeType.ALL)
    private List<Message> messages;

}
