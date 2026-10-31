import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ClientInterviewForm } from './client-interview-form.component';

describe('ClientInterviewForm', () => {
  let component: ClientInterviewForm;
  let fixture: ComponentFixture<ClientInterviewForm>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ClientInterviewForm],
    }).compileComponents();

    fixture = TestBed.createComponent(ClientInterviewForm);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
