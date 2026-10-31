import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClientInterviewDetail } from './client-interview-detail.component';

describe('ClientInterviewDetail', () => {
  let component: ClientInterviewDetail;
  let fixture: ComponentFixture<ClientInterviewDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ClientInterviewDetail],
    }).compileComponents();

    fixture = TestBed.createComponent(ClientInterviewDetail);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
