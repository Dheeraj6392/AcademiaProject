import { Injectable } from '@angular/core';
import { BehaviorSubject } from 'rxjs';

export interface Toast {
  id: number;
  message: string;
  type: 'success' | 'error';
}

@Injectable({ providedIn: 'root' })
export class ToastService {
  private counter = 0;
  private subject = new BehaviorSubject<Toast[]>([]);
  toasts$ = this.subject.asObservable();

  show(message: string, type: 'success' | 'error' = 'success', duration = 4000) {
    const id = ++this.counter;
    const current = this.subject.value;
    this.subject.next([...current, { id, message, type }]);
    if (duration > 0) {
      setTimeout(() => this.clear(id), duration);
    }
  }

  clear(id: number) {
    this.subject.next(this.subject.value.filter(t => t.id !== id));
  }
}