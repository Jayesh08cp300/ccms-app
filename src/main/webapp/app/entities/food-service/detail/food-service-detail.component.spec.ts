import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FoodServiceDetailComponent } from './food-service-detail.component';

describe('FoodService Management Detail Component', () => {
  let comp: FoodServiceDetailComponent;
  let fixture: ComponentFixture<FoodServiceDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FoodServiceDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ foodService: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FoodServiceDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FoodServiceDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load foodService on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.foodService).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
