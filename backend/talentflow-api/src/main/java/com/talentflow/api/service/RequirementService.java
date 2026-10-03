package com.talentflow.api.service;

import com.talentflow.api.dto.RequirementDTO;
import com.talentflow.api.entity.Client;
import com.talentflow.api.entity.Requirement;
import com.talentflow.api.entity.User;
import com.talentflow.api.exception.RequirementNotFoundException;
import com.talentflow.api.repository.ClientRepository;
import com.talentflow.api.repository.RequirementRepository;
import com.talentflow.api.repository.UserRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RequirementService {

    private final RequirementRepository requirementRepository;
    private final ClientRepository clientRepository;
    private final UserRepository userRepository;

    public RequirementService(
            RequirementRepository requirementRepository,
            ClientRepository clientRepository,
            UserRepository userRepository
    ) {
        this.requirementRepository = requirementRepository;
        this.clientRepository = clientRepository;
        this.userRepository = userRepository;
    }

    public Requirement createRequirement(
            RequirementDTO.CreateRequirementRequest request
    ) {

        if (requirementRepository.existsByRrNumber(
                request.rrNumber()
        )) {

            throw new IllegalArgumentException(
                    "RR Number already exists"
            );
        }

        Client client =
                clientRepository.findById(
                        request.clientId()
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "Client not found"
                        ));

        User user =
                userRepository.findById(
                        request.createdBy()
                )
                .orElseThrow(() ->
                        new IllegalArgumentException(
                                "User not found"
                        ));

        Requirement requirement =
                new Requirement();

        requirement.setRrNumber(
                request.rrNumber()
        );

        requirement.setClient(client);

        requirement.setTitle(
                request.title()
        );

        requirement.setDescription(
                request.description()
        );

        requirement.setEmploymentType(
                request.employmentType()
        );

        requirement.setLocation(
                request.location()
        );

        requirement.setWorkMode(
                request.workMode()
        );

        requirement.setOnshoreOrOffshore(
                request.onshoreOrOffshore()
        );

        requirement.setMinExperienceYears(
                request.minExperienceYears()
        );

        requirement.setMaxExperienceYears(
                request.maxExperienceYears()
        );

        requirement.setPositionsCount(
                request.positionsCount()
        );

        requirement.setPriority(
                request.priority()
        );

        requirement.setStatus("OPEN");

        requirement.setCreatedBy(user);

        requirement.setTargetFillDate(
                request.targetFillDate()
        );

        return requirementRepository.save(
                requirement
        );
    }

    public List<Requirement> getAllRequirements() {
        return requirementRepository.findAll();
    }

    public Requirement getRequirementById(
            Long id
    ) {

        return requirementRepository
                .findById(id)
                .orElseThrow(() ->
                        new RequirementNotFoundException(
                                "Requirement not found with id: " + id
                        ));
 }
}