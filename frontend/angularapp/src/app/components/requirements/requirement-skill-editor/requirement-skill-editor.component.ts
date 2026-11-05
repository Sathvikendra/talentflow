import { Component, EventEmitter, Input, Output } from '@angular/core';
import { FormArray, FormBuilder, FormGroup, Validators } from '@angular/forms';

export interface RequirementSkillItem {
  skillName: string;
  category: string;
  importance: string;
  minimumYears: number | null;
}

@Component({
  selector: 'app-requirement-skill-editor',
  standalone: false,
  styleUrl: './requirement-skill-editor.component.scss',
  templateUrl: './requirement-skill-editor.component.html',
})
export class RequirementSkillEditor {
  @Input() initialSkills: RequirementSkillItem[] = [];

  @Output() skillsChanged =
    new EventEmitter<RequirementSkillItem[]>();

  skillForm: FormGroup;

  constructor(private readonly fb: FormBuilder) {
    this.skillForm = this.fb.group({
      skills: this.fb.array([])
    });
  }

  ngOnInit(): void {
    if (this.initialSkills.length > 0) {
      this.initialSkills.forEach(skill => {
        this.addSkill(skill);
      });
    } else {
      this.addSkill();
    }
  }

  get skills(): FormArray {
    return this.skillForm.get('skills') as FormArray;
  }

  createSkillGroup(skill?: RequirementSkillItem): FormGroup {
    return this.fb.group({
      skillName: [
        skill?.skillName || '',
        Validators.required
      ],

      category: [
        skill?.category || ''
      ],

      importance: [
        skill?.importance || 'REQUIRED',
        Validators.required
      ],

      minimumYears: [
        skill?.minimumYears ?? null,
        Validators.min(0)
      ]
    });
  }

  addSkill(skill?: RequirementSkillItem): void {
    this.skills.push(this.createSkillGroup(skill));
    this.emitSkills();
  }

  removeSkill(index: number): void {
    this.skills.removeAt(index);
    this.emitSkills();
  }

  emitSkills(): void {
    this.skillsChanged.emit(
      this.skills.getRawValue() as RequirementSkillItem[]
    );
  }

  onSkillChange(): void {
    this.emitSkills();
  }

  getSkillValue(index: number): RequirementSkillItem {
    return this.skills.at(index).getRawValue() as RequirementSkillItem;
  }
}
