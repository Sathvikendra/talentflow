import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClientInterviewList } from './client-interview-list.component';

describe('ClientInterviewList', () => {
  let component: ClientInterviewList;
  let fixture: ComponentFixture<ClientInterviewList>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ClientInterviewList],
    }).compileComponents();

    fixture = TestBed.createComponent(ClientInterviewList);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
