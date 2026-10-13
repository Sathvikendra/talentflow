package com.talentflow.api.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.talentflow.api.entity.ClientInterview;
import com.talentflow.api.entity.ClientInterview.Result;

@Repository
public interface ClientInterviewRepository extends JpaRepository<ClientInterview, Long> {
    List<ClientInterview> findBySubmissionId(Long submissionId);

    List<ClientInterview> findByResult(Result result);
}
