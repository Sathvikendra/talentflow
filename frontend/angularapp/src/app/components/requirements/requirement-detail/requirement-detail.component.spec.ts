import { ComponentFixture, TestBed } from '@angular/core/testing';
import { RequirementDetail } from './requirement-detail.component';

describe('RequirementDetail', () => {
  let component: RequirementDetail;
  let fixture: ComponentFixture<RequirementDetail>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [RequirementDetail],
    }).compileComponents();

    fixture = TestBed.createComponent(RequirementDetail);
    component = fixture.componentInstance;
    await fixture.whenStable();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
