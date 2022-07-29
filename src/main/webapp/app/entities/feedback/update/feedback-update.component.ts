import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IFeedback, Feedback } from '../feedback.model';
import { FeedbackService } from '../service/feedback.service';
import { IMenu } from 'app/entities/menu/menu.model';
import { MenuService } from 'app/entities/menu/service/menu.service';

@Component({
  selector: 'jhi-feedback-update',
  templateUrl: './feedback-update.component.html',
})
export class FeedbackUpdateComponent implements OnInit {
  isSaving = false;

  menusSharedCollection: IMenu[] = [];

  editForm = this.fb.group({
    id: [],
    comment: [],
    dateTime: [],
    menu: [],
  });

  constructor(
    protected feedbackService: FeedbackService,
    protected menuService: MenuService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ feedback }) => {
      this.updateForm(feedback);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const feedback = this.createFromForm();
    if (feedback.id !== undefined) {
      this.subscribeToSaveResponse(this.feedbackService.update(feedback));
    } else {
      this.subscribeToSaveResponse(this.feedbackService.create(feedback));
    }
  }

  trackMenuById(_index: number, item: IMenu): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IFeedback>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe({
      next: () => this.onSaveSuccess(),
      error: () => this.onSaveError(),
    });
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(feedback: IFeedback): void {
    this.editForm.patchValue({
      id: feedback.id,
      comment: feedback.comment,
      dateTime: feedback.dateTime,
      menu: feedback.menu,
    });

    this.menusSharedCollection = this.menuService.addMenuToCollectionIfMissing(this.menusSharedCollection, feedback.menu);
  }

  protected loadRelationshipsOptions(): void {
    this.menuService
      .query()
      .pipe(map((res: HttpResponse<IMenu[]>) => res.body ?? []))
      .pipe(map((menus: IMenu[]) => this.menuService.addMenuToCollectionIfMissing(menus, this.editForm.get('menu')!.value)))
      .subscribe((menus: IMenu[]) => (this.menusSharedCollection = menus));
  }

  protected createFromForm(): IFeedback {
    return {
      ...new Feedback(),
      id: this.editForm.get(['id'])!.value,
      comment: this.editForm.get(['comment'])!.value,
      dateTime: this.editForm.get(['dateTime'])!.value,
      menu: this.editForm.get(['menu'])!.value,
    };
  }
}
