package com.courseselling.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

/**
 * Course entity representing available courses for sale.
 */
@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Course {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long courseId;

    @Column(nullable = false)
    private String courseName;

    @Column(columnDefinition = "TEXT")
    private String courseDescription;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false)
    private String instructor;

    @Column(name = "course_duration")
    private String duration; // e.g., "6 weeks", "12 hours"

    @Column(nullable = false)
    private String category;

    @Column(name = "skill_level")
    private String skillLevel; // Beginner, Intermediate, Advanced

    @Column(name = "total_students")
    private Integer totalStudents = 0;

    @Column(name = "rating")
    private Double rating = 0.0;

    @Column(name = "is_available")
    private Boolean isAvailable = true;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    @Column(columnDefinition = "LONGTEXT")
    private String courseContent; // Course materials and resources

    @OneToMany(mappedBy = "course", cascade = CascadeType.ALL)
    private Set<CourseEnrollment> enrollments = new HashSet<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
