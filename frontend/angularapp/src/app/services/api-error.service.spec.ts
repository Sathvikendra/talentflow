import { TestBed } from '@angular/core/testing';
import { ApiError } from './api-error.service';

describe('ApiError', () => {
  let service: ApiError;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ApiError);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
