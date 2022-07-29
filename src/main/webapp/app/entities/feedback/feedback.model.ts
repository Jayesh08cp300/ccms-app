import dayjs from 'dayjs/esm';
import { IMenu } from 'app/entities/menu/menu.model';

export interface IFeedback {
  id?: number;
  comment?: string | null;
  dateTime?: dayjs.Dayjs | null;
  menu?: IMenu | null;
}

export class Feedback implements IFeedback {
  constructor(public id?: number, public comment?: string | null, public dateTime?: dayjs.Dayjs | null, public menu?: IMenu | null) {}
}

export function getFeedbackIdentifier(feedback: IFeedback): number | undefined {
  return feedback.id;
}
