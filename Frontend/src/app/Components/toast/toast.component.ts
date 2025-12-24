import { Component } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ToastService } from '../../Services/toast/toast.service';

@Component({
  selector: 'app-toast',
  standalone: true,
  imports: [CommonModule], // <-- ADDED
  template: `
    <div class="toast-container">
      <div
        *ngFor="let t of toastService.toasts$ | async"
        class="toast"
        [class.toast-success]="t.type === 'success'"
        [class.toast-error]="t.type === 'error'"
      >
        {{ t.message }}
      </div>
    </div>
  `,
  styles: [`
    .toast-container { position: fixed; top: 20px; left: 50%; transform: translateX(-50%); z-index: 9999; }
    .toast { display: flex; align-items: center; gap: 12px; min-width: 280px; padding: 12px 16px; margin-bottom: 8px; border-radius: 8px; color: #fff; font-weight: 500; box-shadow: 0 4px 12px rgba(0,0,0,.15); animation: slide .3s ease; }
    .toast-success { background: #10b981; }
    .toast-error   { background: #ef4444; }
    .btn-close { margin-left: auto; background: none; border: none; color: inherit; font-size: 18px; cursor: pointer; }
    @keyframes slide { from { opacity: 0; transform: translateY(-20px); } to { opacity: 1; transform: translateY(0); } }
  `]
})
export class ToastComponent {
  constructor(public toastService: ToastService) {}
}