import { Component, OnInit } from '@angular/core';
import { Subscription, SubscriptionService } from '../../Services/subscription/subscription.service';

@Component({
  selector: 'app-subscriptions',
  templateUrl: './subscriptions.component.html',
  styleUrls: ['./subscriptions.component.css']
})
export class SubscriptionsComponent implements OnInit {
  subscriptions: Subscription[] = [];
  loading = false;
  error = '';
  newSubject = '';
  submitting = false;

  constructor(private subService: SubscriptionService) {}

  ngOnInit() { this.load(); }

  load() {
    this.loading = true;
    this.error = '';
    this.subService.getMySubscriptions().subscribe({
      next: (subs) => { this.subscriptions = subs; this.loading = false; },
      error: () => { this.error = 'Failed to load subscriptions'; this.loading = false; }
    });
  }

  subscribe() {
    const subject = this.newSubject.trim();
    if (!subject) return;
    this.submitting = true;
    this.subService.subscribe(subject).subscribe({
      next: () => { this.newSubject = ''; this.submitting = false; this.load(); },
      error: () => { this.error = 'Failed to subscribe'; this.submitting = false; }
    });
  }

  unsubscribe(id: string) {
    this.subService.unsubscribe(id).subscribe({
      next: () => this.load(),
      error: () => this.error = 'Failed to unsubscribe'
    });
  }
}
