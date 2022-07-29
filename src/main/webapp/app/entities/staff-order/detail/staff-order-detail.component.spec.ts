import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { StaffOrderDetailComponent } from './staff-order-detail.component';

describe('StaffOrder Management Detail Component', () => {
  let comp: StaffOrderDetailComponent;
  let fixture: ComponentFixture<StaffOrderDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [StaffOrderDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ staffOrder: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(StaffOrderDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(StaffOrderDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load staffOrder on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.staffOrder).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
