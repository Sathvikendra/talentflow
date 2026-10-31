import { ComponentFixture, TestBed } from '@angular/core/testing';
import { L1EvaluationDetail } from './l1-evaluation-detail.component';

describe('L1EvaluationDetail', () => {
  let component: L1EvaluationDetail;
  let fixture: ComponentFixture<L1EvaluationDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [L1EvaluationDetail],
    }).compileComponents();

    fixture = TestBed.createComponent(L1EvaluationDetail);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
