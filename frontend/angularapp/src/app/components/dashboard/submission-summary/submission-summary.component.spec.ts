import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SubmissionSummary } from './submission-summary.component';

describe('SubmissionSummary', () => {
  let component: SubmissionSummary;
  let fixture: ComponentFixture<SubmissionSummary>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SubmissionSummary],
    }).compileComponents();

    fixture = TestBed.createComponent(SubmissionSummary);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
