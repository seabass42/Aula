package com.tutorportal.help;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface HelpRequestRepository extends JpaRepository<HelpRequest, Long> {

    List<HelpRequest> findByStudentIdOrderByCreatedAtDesc(Long studentId);

    /**
     * Rows for the tutor's queue, selected as scalars rather than the entity so
     * the student's name comes along without ever loading the lazy `student`
     * association (see HelpRequestView).
     */
    @Query("""
            select h.id, h.student.displayName, h.message, h.reply, h.status
            from HelpRequest h
            where h.status = :status
            order by h.createdAt asc
            """)
    List<Object[]> findRowsByStatusOrderByCreatedAtAsc(@Param("status") HelpRequestStatus status);

    @Query("""
            select h.id, h.student.displayName, h.message, h.reply, h.status
            from HelpRequest h
            where h.status = :status
            order by h.createdAt desc
            """)
    List<Object[]> findRowsByStatusOrderByCreatedAtDesc(@Param("status") HelpRequestStatus status);
}
