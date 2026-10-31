import { ComponentFixture, TestBed } from '@angular/core/testing';
import { L1EvaluationForm } from './l1-evaluation-form.component';

describe('L1EvaluationForm', () => {
  let component: L1EvaluationForm;
  let fixture: ComponentFixture<L1EvaluationForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [L1EvaluationForm],
    }).compileComponents();

    fixture = TestBed.createComponent(L1EvaluationForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
