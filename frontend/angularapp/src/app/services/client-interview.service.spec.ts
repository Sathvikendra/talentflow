import { TestBed } from '@angular/core/testing';
import { ClientInterview } from './client-interview.service';

describe('ClientInterview', () => {
  let service: ClientInterview;

  beforeEach(() => {
    TestBed.configureTestingModule({});
    service = TestBed.inject(ClientInterview);
  });

  it('should be created', () => {
    expect(service).toBeTruthy();
  });
});
