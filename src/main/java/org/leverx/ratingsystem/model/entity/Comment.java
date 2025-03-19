package org.leverx.ratingsystem.model.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.Instant;

@Data
@Entity
@Table(name = "comments")
@Builder
@AllArgsConstructor
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "message")
    private String message;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = true)
    private User author;

    @ManyToOne
    @JoinColumn(name = "seller_id", nullable = false)
    private User seller;

    @CreationTimestamp
    @Column(name = "created_at")
    private Instant createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private Instant updatedAt;

    @Column(name = "rating")
    @Min(1)
    @Max(5)
    private Integer rating;

    @Column(name = "verified_by_admin")
    private Boolean verifiedByAdmin;

    public Comment() {

    }

    @Override
    public String toString() {
        return "Comment{" +
                "id=" + id +
                ", message='" + message + '\'' +
                ", author=" + author.getId() +
                ", seller=" + seller.getId() +
                ", createdAt=" + createdAt +
                ", updatedAt=" + updatedAt +
                ", rating=" + rating +
                ", verifiedByAdmin=" + verifiedByAdmin +
                '}';
    }
}
