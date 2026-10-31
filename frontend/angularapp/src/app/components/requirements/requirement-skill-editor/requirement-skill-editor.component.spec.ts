import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RequirementSkillEditor } from './requirement-skill-editor.component';

describe('RequirementSkillEditor', () => {
  let component: RequirementSkillEditor;
  let fixture: ComponentFixture<RequirementSkillEditor>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RequirementSkillEditor],
    }).compileComponents();

    fixture = TestBed.createComponent(RequirementSkillEditor);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
