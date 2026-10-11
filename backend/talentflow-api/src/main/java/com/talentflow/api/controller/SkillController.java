package com.talentflow.api.controller;

import com.talentflow.api.dto.SkillDTO;
import com.talentflow.api.entity.Skill;
import com.talentflow.api.service.SkillService;

import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/skills")
public class SkillController {

    private final SkillService skillService;

    public SkillController(
            SkillService skillService
    ) {
        this.skillService = skillService;
    }

    @PostMapping
    public ResponseEntity<Skill> createSkill(
            @Valid
            @RequestBody
            SkillDTO.CreateSkillRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        skillService.createSkill(request)
                );
    }

    @GetMapping
    public ResponseEntity<List<Skill>> getAllSkills() {

        return ResponseEntity.ok(
                skillService.getAllSkills()
        );
    }

    @GetMapping("/{id}")
    public ResponseEntity<Skill> getSkillById(
            @PathVariable Long id
    ) {

        return ResponseEntity.ok(
                skillService.getSkillById(id)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<Skill> updateSkill(
            @PathVariable Long id,
            @RequestBody SkillDTO.UpdateSkillRequest request
    ) {

        return ResponseEntity.ok(
                skillService.updateSkill(
                        id,
                        request
                )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteSkill(
            @PathVariable Long id
    ) {

        skillService.deleteSkill(id);

        return ResponseEntity.noContent().build();
    }
}