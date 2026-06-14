import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';

export interface Subscription {
  id: string;
  subject: string;
  createdAt: string;
}

@Injectable({ providedIn: 'root' })
export class SubscriptionService {
  private base = 'http://localhost:8080/api/v1/subscriptions';

  constructor(private http: HttpClient) {}

  getMySubscriptions(): Observable<Subscription[]> {
    return this.http.get<Subscription[]>(this.base);
  }

  subscribe(subject: string): Observable<Subscription> {
    return this.http.post<Subscription>(this.base, { subject });
  }

  unsubscribe(id: string): Observable<void> {
    return this.http.delete<void>(`${this.base}/${id}`);
  }
}
