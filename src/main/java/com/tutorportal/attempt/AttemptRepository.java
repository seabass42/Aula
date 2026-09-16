package com.tutorportal.attempt;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AttemptRepository extends JpaRepository<Attempt, Long> {

    long countByStudentId(Long studentId);

    long countByStudentIdAndCorrectTrue(Long studentId);

    List<Attempt> findTop10ByStudentIdOrderByCreatedAtDesc(Long studentId);

    /**
     * Per-topic accuracy for one student. Returns rows of
     * [topic, totalAttempts, correctAttempts] -- see TopicStat for the mapping.
     */
    @Query(value = """
            select p.topic                             as topic,
                   count(*)                            as attempts,
                   sum(case when a.correct then 1 else 0 end) as correct
            from attempts a
            join problems p on p.id = a.problem_id
            where a.student_id = :studentId
            group by p.topic
            order by p.topic
            """, nativeQuery = true)
    List<Object[]> topicBreakdownFor(@Param("studentId") Long studentId);
}
