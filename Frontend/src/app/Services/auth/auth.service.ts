import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable, tap } from 'rxjs';

interface Course {
  id: number;
  title: string;
  weeks: number;
  lessons: number;
  students: number;
  rating: number;
  price: number;
  imageUrl: string;
  description: string;
}

interface AuthResponse {
  token: string;
  role: 'USER' | 'ADMIN';
  username: string;
}

@Injectable({ providedIn: 'root' })
export class AuthService {
  private baseUrl = 'http://localhost:8080/api/auth';
  // Add explicit courses API base to avoid relying on dev-server proxy
  private coursesUrl = 'http://localhost:8080/api/courses';

  constructor(private http: HttpClient) {}

  login(username: string, password: string): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/login`, { username, password }).pipe(
      tap((resp) => this.storeAuth(resp))
    );
  }

  register(username: string, password: string, role: 'USER' | 'ADMIN'): Observable<AuthResponse> {
    return this.http.post<AuthResponse>(`${this.baseUrl}/register`, { username, password, role }).pipe(
      tap((resp) => this.storeAuth(resp))
    );
  }

  storeAuth(resp: AuthResponse) {
    localStorage.setItem('token', resp.token);
    localStorage.setItem('role', resp.role);
    localStorage.setItem('username', resp.username);
  }

  logout() {
    localStorage.removeItem('token');
    localStorage.removeItem('role');
    localStorage.removeItem('username');
  }

  get token() {
    return localStorage.getItem('token');
  }

  get role() {
    return localStorage.getItem('role');
  }

  isLoggedIn() {
    return !!this.token;
  }

  isAdmin() {
    return this.role === 'ADMIN';
  }

  getMyCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.coursesUrl}/me`);
  }

  getAllCourses(): Observable<Course[]> {
    return this.http.get<Course[]>(`${this.coursesUrl}`);
  }

  enroll(courseId: number): Observable<any> {
    return this.http.post(`${this.coursesUrl}/${courseId}/enroll`, {});
  }

  unenroll(courseId: number): Observable<any> {
    return this.http.delete(`${this.coursesUrl}/${courseId}/enroll`);
  }
}