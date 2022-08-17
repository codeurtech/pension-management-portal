import { ComponentFixture, TestBed } from '@angular/core/testing';

import { FetchdetailsComponent } from './fetchdetails.component';

describe('FetchdetailsComponent', () => {
  let component: FetchdetailsComponent;
  let fixture: ComponentFixture<FetchdetailsComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ FetchdetailsComponent ]
    })
    .compileComponents();

    fixture = TestBed.createComponent(FetchdetailsComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
