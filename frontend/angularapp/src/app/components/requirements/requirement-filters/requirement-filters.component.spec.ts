import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RequirementFilters } from './requirement-filters.component';

describe('RequirementFilters', () => {
  let component: RequirementFilters;
  let fixture: ComponentFixture<RequirementFilters>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RequirementFilters],
    }).compileComponents();

    fixture = TestBed.createComponent(RequirementFilters);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
