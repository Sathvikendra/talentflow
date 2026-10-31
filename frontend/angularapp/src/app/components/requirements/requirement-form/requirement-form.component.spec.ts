import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RequirementForm } from './requirement-form.component';

describe('RequirementForm', () => {
  let component: RequirementForm;
  let fixture: ComponentFixture<RequirementForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RequirementForm],
    }).compileComponents();

    fixture = TestBed.createComponent(RequirementForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
