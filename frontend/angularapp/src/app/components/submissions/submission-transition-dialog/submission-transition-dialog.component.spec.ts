import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SubmissionTransitionDialog } from './submission-transition-dialog.component';

describe('SubmissionTransitionDialog', () => {
  let component: SubmissionTransitionDialog;
  let fixture: ComponentFixture<SubmissionTransitionDialog>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SubmissionTransitionDialog],
    }).compileComponents();

    fixture = TestBed.createComponent(SubmissionTransitionDialog);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
