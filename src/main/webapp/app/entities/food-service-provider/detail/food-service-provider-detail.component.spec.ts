import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { FoodServiceProviderDetailComponent } from './food-service-provider-detail.component';

describe('FoodServiceProvider Management Detail Component', () => {
  let comp: FoodServiceProviderDetailComponent;
  let fixture: ComponentFixture<FoodServiceProviderDetailComponent>;

  beforeEach(() => {
    TestBed.configureTestingModule({
      declarations: [FoodServiceProviderDetailComponent],
      providers: [
        {
          provide: ActivatedRoute,
          useValue: { data: of({ foodServiceProvider: { id: 123 } }) },
        },
      ],
    })
      .overrideTemplate(FoodServiceProviderDetailComponent, '')
      .compileComponents();
    fixture = TestBed.createComponent(FoodServiceProviderDetailComponent);
    comp = fixture.componentInstance;
  });

  describe('OnInit', () => {
    it('Should load foodServiceProvider on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(comp.foodServiceProvider).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
