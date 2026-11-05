import { TestBed } from '@angular/core/testing';
import { StatusHistory } from './status-history.service';

describe('StatusHistory', () => {
  let service: StatusHistory;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(StatusHistory);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
