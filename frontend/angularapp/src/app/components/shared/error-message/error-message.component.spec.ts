import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ErrorMessage } from './error-message.component';

describe('ErrorMessage', () => {
  let component: ErrorMessage;
  let fixture: ComponentFixture<ErrorMessage>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ErrorMessage],
    }).compileComponents();

    fixture = TestBed.createComponent(ErrorMessage);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
