import { ComponentFixture, TestBed } from '@angular/core/testing';
import { SubmissionTimeline } from './submission-timeline.component';

describe('SubmissionTimeline', () => {
  let component: SubmissionTimeline;
  let fixture: ComponentFixture<SubmissionTimeline>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [SubmissionTimeline],
    }).compileComponents();

    fixture = TestBed.createComponent(SubmissionTimeline);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
