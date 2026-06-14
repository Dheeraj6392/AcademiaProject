import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

export interface AuthResponse {
  token: string;
  userId: string;
  email: string;
  role: 'STUDENT' | 'UPLOADER' | 'ADMIN';
}

export interface UserInfo {
  id: string;
  email: string;
  name: string;
  role: 'STUDENT' | 'UPLOADER' | 'ADMIN';
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private base = 'http://localhost:8080/api/v1';

  constructor(private http: HttpClient) {}

  login(email: string, name: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.base}/auth/login`, {
      email, name, googleToken: 'dev-token'
    }).pipe(tap(resp => this.storeAuth(resp)));
  }

  register(email: string, name: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.base}/auth/register`, {
      email, name, googleToken: 'dev-token'
    }).pipe(tap(resp => this.storeAuth(resp)));
  }

  me(): Observable<UserInfo> {
    return this.http.get<UserInfo>(`${this.base}/auth/me`);
  }

  private storeAuth(resp: AuthResponse) {
    localStorage.setItem('token', resp.token);
    localStorage.setItem('role', resp.role);
    localStorage.setItem('userId', resp.userId);
    localStorage.setItem('email', resp.email);
  }

  logout() {
    localStorage.clear();
  }

  get token() { return localStorage.getItem('token'); }
  get role()  { return localStorage.getItem('role') as 'STUDENT' | 'UPLOADER' | 'ADMIN' | null; }
  get email() { return localStorage.getItem('email'); }
  get userId(){ return localStorage.getItem('userId'); }

  isLoggedIn()  { return !!this.token; }
  isAdmin()     { return this.role === 'ADMIN'; }
  isUploader()  { return this.role === 'UPLOADER' || this.role === 'ADMIN'; }
}
