import { TestBed } from '@angular/core/testing';
import { L1Evaluation } from './l1-evaluation.service';

describe('L1Evaluation', () => {
  let service: L1Evaluation;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(L1Evaluation);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
