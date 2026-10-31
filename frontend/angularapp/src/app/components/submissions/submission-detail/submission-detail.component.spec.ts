import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SubmissionDetail } from './submission-detail.component';

describe('SubmissionDetail', () => {
  let component: SubmissionDetail;
  let fixture: ComponentFixture<SubmissionDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SubmissionDetail],
    }).compileComponents();

    fixture = TestBed.createComponent(SubmissionDetail);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
