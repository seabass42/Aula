package com.tutorportal.problem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProblemRepository extends JpaRepository<Problem, Long> {

    List<Problem> findByTopic(String topic);

    @Query("select distinct p.topic from Problem p order by p.topic")
    List<String> findAllTopics();

    /**
     * Picks a problem this student has not yet answered correctly.
     * Native query because it needs a LEFT JOIN against attempts filtered on
     * two columns, which JPQL makes awkward.
     */
    @Query(value = """
            select p.* from problems p
            left join attempts a
              on a.problem_id = p.id
             and a.student_id = :studentId
             and a.correct = true
            where a.id is null
            order by random()
            limit 1
            """, nativeQuery = true)
    Optional<Problem> findNextUnmasteredFor(@Param("studentId") Long studentId);
}
