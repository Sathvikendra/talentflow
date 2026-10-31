import { ComponentFixture, TestBed } from '@angular/core/testing';
import { L1EvaluationList } from './l1-evaluation-list.component';

describe('L1EvaluationList', () => {
  let component: L1EvaluationList;
  let fixture: ComponentFixture<L1EvaluationList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [L1EvaluationList],
    }).compileComponents();

    fixture = TestBed.createComponent(L1EvaluationList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
