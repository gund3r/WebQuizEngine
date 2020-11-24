package main.java;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CompletedQuizzesRepository extends PagingAndSortingRepository<CompletedQuiz, Long> {

    @Query(value = "SELECT cq FROM CompletedQuiz cq WHERE cq.userId = ?1")
    Page<CompletedQuiz> findAllCompletedQuizzesForUser(Long userId, Pageable paging);

}
