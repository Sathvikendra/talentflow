import { TestBed } from '@angular/core/testing';
import { Requirement } from './requirement.service';

describe('Requirement', () => {
  let service: Requirement;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(Requirement);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
