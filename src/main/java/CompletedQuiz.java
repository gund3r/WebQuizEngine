package main.java;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@EqualsAndHashCode
@Component
@Entity
@Table
public class CompletedQuiz {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Long cq_id;

    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Long userId;

    Long id;

    LocalDateTime completedAt;

    public CompletedQuiz() {};

    public CompletedQuiz(Long quizId, Long userId) {
        this.id = quizId;
        this.userId = userId;
        this.completedAt = java.time.LocalDateTime.now();
    }

    @Override
    public String toString(){
        return "{id=" + getId() + ", completedAt=" + getCompletedAt() + "}";
    }

}
