package com.talentflow.api.service;

import com.talentflow.api.dto.CandidateDTO;
import com.talentflow.api.entity.Candidate;
import com.talentflow.api.entity.User;
import com.talentflow.api.exception.CandidateNotFoundException;
import com.talentflow.api.repository.CandidateRepository;
import com.talentflow.api.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;
    private final UserRepository userRepository;

    public CandidateService(
            CandidateRepository candidateRepository,
            UserRepository userRepository
    ) {
        this.candidateRepository = candidateRepository;
        this.userRepository = userRepository;
    }

    public Candidate createCandidate(
            CandidateDTO.CreateCandidateRequest request
    ) {

        if (
            candidateRepository.existsByCandidateReference(
                    request.candidateReference()
            )
        ) {
            throw new IllegalArgumentException(
                    "Candidate reference already exists"
            );
        }

        User user =
                userRepository.findById(
                        request.createdBy()
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        ));

        Candidate candidate = new Candidate();

        candidate.setCandidateReference(
                request.candidateReference()
        );

        candidate.setFirstName(
                request.firstName()
        );

        candidate.setLastName(
                request.lastName()
        );

        candidate.setEmail(
                request.email()
        );

        candidate.setPhone(
                request.phone()
        );

        candidate.setTotalExperienceYears(
                request.totalExperienceYears()
        );

        candidate.setCurrentTitle(
                request.currentTitle()
        );

        candidate.setCurrentCompany(
                request.currentCompany()
        );

        candidate.setCurrentLocation(
                request.currentLocation()
        );

        candidate.setPreferredLocation(
                request.preferredLocation()
        );

        candidate.setNoticePeriodDays(
                request.noticePeriodDays()
        );

        candidate.setProfileSummary(
                request.profileSummary()
        );

        candidate.setCreatedBy(user);

        return candidateRepository.save(candidate);
    }

    public List<Candidate> getAllCandidates() {
        return candidateRepository.findAll();
    }

    public Candidate getCandidateById(Long id) {
        return candidateRepository.findById(id)
                .orElseThrow(() ->
                        new CandidateNotFoundException(
                                "Candidate not found with id: " + id
                        ));
    }

    public void deleteCandidate(Long id) {
        Candidate candidate = getCandidateById(id);
        candidateRepository.delete(candidate);
    }
}